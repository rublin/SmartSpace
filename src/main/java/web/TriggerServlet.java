package web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import web.rest.TriggerRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class TriggerServlet extends HttpServlet {
    private static final Logger LOG = getLogger(TriggerServlet.class);
    private TriggerRestController controller;
    private ConfigurableApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = context.getBean(TriggerRestController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action==null){
            req.setAttribute("triggerList", controller.getAll());
            LOG.info("getAll");
        }
        req.getRequestDispatcher("/trigger.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
        context.close();
    }
}
