package web;

import model.AbstractTrigger;
import model.DigitTrigger;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AbstractTrigger trigger = triggerController.get(1);
        req.setAttribute("stateMap", stateController.getAll(trigger));
        req.setAttribute("trigger", trigger);
        LOG.info("get state {} trigger", trigger.getName());
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        stateController = context.getBean(StateRestController.class);
        triggerController = context.getBean(TriggerRestController.class);
    }
}
