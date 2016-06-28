package main;

import model.Trigger;
import model.Type;
import model.event.DigitEvent;
import model.event.Event;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.StateService;
import service.StateServiceImpl;
import service.TriggerService;
import service.TriggerServiceImpl;
import web.rest.StateRestController;

import java.util.Arrays;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")){
            System.out.println(Arrays.toString(springContext.getBeanDefinitionNames()));
            TriggerService triggerService = (TriggerServiceImpl)springContext.getBean(TriggerService.class);
            triggerService.save(new Trigger("Move 2 floor", Type.DIGITAL));
//            triggerService.save(new Trigger("Door 2 floor", Type.DIGITAL));
            triggerService.getAll().forEach(System.out::println);
            StateService stateService = (StateServiceImpl) springContext.getBean(StateService.class);
            Trigger trigger1000 = triggerService.get(1000);
            Event event = new DigitEvent(trigger1000, true);
            System.out.println(event);
            stateService.save(trigger1000, event);
            stateService.save(triggerService.get(1000), new DigitEvent(triggerService.get(1000), false));
            stateService.getAll(triggerService.get(1000)).forEach(System.out::println);
            StateRestController stateController = (StateRestController)springContext.getBean(StateRestController.class);
            stateController.getAll(triggerService.get(1000)).forEach(System.out::println);
        }
    }
}
