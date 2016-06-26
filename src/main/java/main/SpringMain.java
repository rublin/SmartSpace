package main;

import model.event.DigitEvent;
import model.DigitTrigger;
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
        try (ConfigurableApplicationContext springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml")){
            System.out.println(Arrays.toString(springContext.getBeanDefinitionNames()));
            TriggerService triggerService = (TriggerServiceImpl)springContext.getBean(TriggerService.class);
            triggerService.save(new DigitTrigger("Move 1 floor"));
            triggerService.save(new DigitTrigger("Door 1 floor"));
            triggerService.getAll().forEach(System.out::println);
            StateService stateService = (StateServiceImpl) springContext.getBean(StateService.class);
            stateService.save(triggerService.get(1), new DigitEvent(triggerService.get(1), true));
            stateService.save(triggerService.get(1), new DigitEvent(triggerService.get(1), false));
            stateService.getAll(triggerService.get(1)).forEach(System.out::println);
            StateRestController stateController = (StateRestController)springContext.getBean(StateRestController.class);
            stateController.getAll(triggerService.get(1)).forEach(System.out::println);
        }
    }
}
