package org.rublin.web;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.rublin.web.rest.TriggerRestController;

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
    protected static TriggerRestController controller;
    private ConfigurableApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = context.getBean(TriggerRestController.class);
    }
/*

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LOG.info("post", req);
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        String name = req.getParameter("name");
        if (id.isEmpty()) {
            if (type.equals("digital")) {
                LOG.info("Create digital trigger {}", name);
                controller.create(new Trigger(name));
            } else if (type.equals("analog")){
                LOG.info("Create analog trigger {}", name);
                controller.create(new Trigger(name, Type.DIGITAL));
            }
        } else {
            Trigger trigger = controller.get(Integer.parseInt(id));
            LOG.info("Update trigger {}. New name is {}", trigger, name);
            trigger.setName(name);
            controller.update(trigger);
        }
        resp.sendRedirect("triggers");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action==null){
            req.setAttribute("triggerList", controller.getAll());
            LOG.info("get");
        }
        req.getRequestDispatcher("/triggerList.jsp").forward(req, resp);
    }
*/

    @Override
    public void destroy() {
        super.destroy();
        context.close();
    }
}
