package org.rublin.web;

import org.rublin.model.*;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.service.ZoneService;
import org.rublin.util.Notification;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.rublin.web.rest.StateRestController;
import org.rublin.web.rest.TriggerRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 16.06.2016.
 */
public class StateServlet extends HttpServlet {
    private static final Logger LOG = getLogger(StateServlet.class);
    private ConfigurableApplicationContext context;
    private StateRestController stateController;
    private TriggerRestController triggerController;
    private ZoneService zoneService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        stateController = context.getBean(StateRestController.class);
        triggerController = context.getBean(TriggerRestController.class);
        zoneService = context.getBean(ZoneService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String triggerId = req.getParameter("triggerId");
        LOG.info("trigger id is {}", triggerId);
        if (action!=null) {
            if (action.equals("addEvent")) {
                String state = req.getParameter("state");
                Trigger trigger = triggerController.get(Integer.parseInt(triggerId));
                Event event;
                if (trigger.getType() == Type.DIGITAL) {
                    LOG.info("new digital event {} from trigger {}", state, trigger.getName());
                    event = new DigitEvent(trigger, Boolean.parseBoolean(state));
                } else {
                    LOG.info("new analog event {} from trigger {}", state, trigger.getName());
                    event = new AnalogEvent(trigger, Double.parseDouble(state));
                }
                Zone zone = trigger.getZone();
                if (zone.getSecure()) {
                    zone.setStatus(ZoneStatus.RED);
                    zoneService.save(zone);
                    Notification.sendMailWithAttach(String.format("Trigger %s activity", trigger.getName()),
                            String.format("<h2>Zone: <span style=\"color: blue;\">%s</span></h2>\n" +
                                            "<h2>Trigger: <span style=\"color: blue;\">%s</span></h2>\n" +
                                            "<h2>Status: <span style=\"color: %s;\">%s</span></h2>",
                                    zone.getName(),
                                    trigger.getName(),
                                    Boolean.parseBoolean(event.getState().toString()) ? "green" : "red",
                                    Boolean.parseBoolean(event.getState().toString()) ? "close | without move" : "open | with move"), "http://192.168.0.31/Streaming/channels/1/picture");
                    LOG.info("Security issue in zone {} form trigger {}", zone.getName(), trigger.getName());
                }
                stateController.save(trigger, event);
                //trigger.setEvent(event);
            } else if (action.equals("edit")) {
                Trigger trigger = triggerController.get(Integer.parseInt(triggerId));
                LOG.info("edit trigger {}  by {} id (get in form)", trigger.getName(), trigger.getId());
                req.setAttribute("eventList", stateController.get(trigger));
                req.setAttribute("trigger", trigger);
                req.setAttribute("triggerList", triggerController.getAll());
            } else if (action.equals("delete")) {
                Trigger trigger = triggerController.get(Integer.parseInt(triggerId));
                LOG.info("trigger {} was delete by {} id", trigger.getName(), trigger.getId());
                triggerController.delete(trigger.getId());
                req.setAttribute("triggerList", triggerController.getAll());
                req.setAttribute("eventList", stateController.getAll());
            }
        } else {
            if (triggerId==null) {
                Collection<Zone> zones = zoneService.getAll();
                req.setAttribute("zoneList", zones);
                req.setAttribute("eventList", stateController.getAll());
                req.setAttribute("triggerList", triggerController.getAll());
                LOG.info("get all events from all triggers");
            } else {
                Trigger trigger = triggerController.get(Integer.parseInt(triggerId));
                req.setAttribute("eventList", stateController.get(trigger));
                req.setAttribute("trigger", trigger);
                req.setAttribute("triggerList", triggerController.getAll());
                LOG.info("get event {} trigger", trigger.getName());
            }
        }
        req.getRequestDispatcher("/triggerStates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Zone zone = zoneService.get(CurrentZone.getId());
        req.setCharacterEncoding("UTF-8");
        LOG.info("post", req);
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        String name = req.getParameter("name");
        String secure = req.getParameter("secure");
        if (secure != null) {
            zone.setSecure(Boolean.valueOf(secure));
            zone.setStatus(ZoneStatus.GREEN);
            zoneService.save(zone);
            Notification.sendMail(String.format("Zone %s ", zone.getName()),
                    String.format("<h1>Zone: <span style=\"color: blue;\">%s</span></h1>\n" +
                                    "<h2>Status: <span style=\"color: %s;\">%s</span></h2>\n" +
                                    "<h2>Secure: <span style=\"color: %s;\">%s</span></h2>",
                            zone.getName(),
                            zone.getStatus(),
                            zone.getStatus(),
                            zone.getSecure() ? "GREEN" : "GREY",
                            zone.getSecure()));
            LOG.info("change Zone secure state to {}", zone.getSecure());
        } else {
            LOG.info("post trigger (edit or create) with id: ", id);
            if (id==null || id.isEmpty()) {
                if (type.equals("digital")) {
                    LOG.info("Create digital trigger {}", name);
                    triggerController.create(new Trigger(name, Type.DIGITAL), zone);
                } else if (type.equals("analog")){
                    LOG.info("Create analog trigger {}", name);
                    triggerController.create(new Trigger(name, Type.ANALOG), zone);
                }
            } else {
                Trigger trigger = triggerController.get(Integer.parseInt(id));
                LOG.info("Update trigger {}. New name is {}", trigger, name);
                trigger.setName(name);
                triggerController.update(trigger, zone);
            }
        }

        resp.sendRedirect("states");
    }

    @Override
    public void destroy() {
        super.destroy();
        context.close();
    }
}
