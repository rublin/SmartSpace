package web;

import model.*;
import model.event.AnalogEvent;
import model.event.DigitEvent;
import model.event.Event;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import web.rest.StateRestController;
import web.rest.TriggerRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 16.06.2016.
 */
public class StateServlet extends HttpServlet {
    private static final Logger LOG = getLogger(StateServlet.class);
    private ConfigurableApplicationContext context;
    private StateRestController stateController;
    private TriggerRestController triggerController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        stateController = context.getBean(StateRestController.class);
        triggerController = context.getBean(TriggerRestController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String triggerId = req.getParameter("triggerId");
        if (action!=null) {
            if (action.equals("addEvent")) {
                String state = req.getParameter("state");
                Trigger trigger = triggerController.get(Integer.parseInt(triggerId));
                Event event;
                if (trigger instanceof DigitTrigger) {
                    LOG.info("new digital event {} from trigger {}", state, trigger.getName());
                    event = new DigitEvent(trigger, Boolean.parseBoolean(state));
                } else {
                    LOG.info("new analog event {} from trigger {}", state, trigger.getName());
                    event = new AnalogEvent(trigger, Double.parseDouble(state));
                }
                stateController.save(trigger, event);
                trigger.setEvent(event);
            }
        }

        LOG.info("trigger id is {}", triggerId);
        if (triggerId==null)
            triggerId="1";
        Trigger trigger = triggerController.get(Integer.parseInt(triggerId));
        req.setAttribute("eventList", stateController.getAll(trigger));
        req.setAttribute("trigger", trigger);
        req.setAttribute("triggerList", triggerController.getAll());
        LOG.info("get event {} trigger", trigger.getName());
        req.getRequestDispatcher("/triggerStates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LOG.info("post", req);
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        String name = req.getParameter("name");
        if (id==null) {
            if (type.equals("digital")) {
                LOG.info("Create digital trigger {}", name);
                triggerController.create(new DigitTrigger(name));
            } else if (type.equals("analog")){
                LOG.info("Create analog trigger {}", name);
                triggerController.create(new AnalogTrigger(name));
            }
        } else {
            Trigger trigger = triggerController.get(Integer.parseInt(id));
            LOG.info("Update trigger {}. New name is {}", trigger, name);
            trigger.setName(name);
            triggerController.update(trigger);
        }
        resp.sendRedirect("states");
    }

    @Override
    public void destroy() {
        super.destroy();
        context.close();
    }
}
