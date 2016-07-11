package org.rublin.main;

import org.rublin.model.ControlledObject;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.service.*;
import org.rublin.web.CurrentObject;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.rublin.web.rest.StateRestController;

import java.util.Arrays;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")){
            ControlledObjectService objectService = (ControlledObjectServiceImpl) springContext.getBean(ControlledObjectService.class);
            ControlledObject obj = objectService.get(CurrentObject.getId());
            System.out.println(Arrays.toString(springContext.getBeanDefinitionNames()));
            TriggerService triggerService = (TriggerServiceImpl)springContext.getBean(TriggerService.class);
            triggerService.save(new Trigger("Move 2 floor", Type.DIGITAL), obj);
//            triggerService.save(new Trigger("Door 2 floor", Type.DIGITAL));
            triggerService.getAll().forEach(System.out::println);
            StateService stateService = (StateServiceImpl) springContext.getBean(StateService.class);
            Trigger trigger1000 = triggerService.get(1000);
            Event event = new DigitEvent(trigger1000, true);
            System.out.println(event);
            stateService.save(trigger1000, event);
            stateService.save(triggerService.get(1000), new DigitEvent(triggerService.get(1000), false));
            stateService.get(triggerService.get(1000)).forEach(System.out::println);
            StateRestController stateController = (StateRestController)springContext.getBean(StateRestController.class);
            stateController.get(triggerService.get(1000)).forEach(System.out::println);
        }
    }
}
