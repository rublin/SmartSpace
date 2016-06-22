package web;

import model.*;
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
                AbstractTrigger trigger = triggerController.get(Integer.parseInt(triggerId));
                Event event;
                if (trigger instanceof DigitTrigger) {
                    LOG.info("new digital event {} from trigger {}", state, trigger.getName());
                    event = new DigitEvent(Boolean.parseBoolean(state));
                } else {
                    LOG.info("new analog event {} from trigger {}", state, trigger.getName());
                    event = new AnalogEvent(Double.parseDouble(state));
                }
                stateController.save(trigger, event);
                trigger.setEvent(event);
            }
        }

        LOG.info("trigger id is {}", triggerId);
        if (triggerId.isEmpty())
            triggerId="1";
        AbstractTrigger trigger = triggerController.get(Integer.parseInt(triggerId));
        req.setAttribute("eventList", stateController.getAll(trigger));
        req.setAttribute("trigger", trigger);
        req.setAttribute("triggerList", triggerController.getAll());
        LOG.info("get event {} trigger", trigger.getName());
        req.getRequestDispatcher("/triggerStates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
        context.close();
    }
}
