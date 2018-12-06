package org.rublin.main;

import org.rublin.ZoneTestData;
import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.service.*;
import org.rublin.service.impl.EventServiceImpl;
import org.rublin.service.impl.TriggerServiceImpl;
import org.rublin.service.impl.ZoneServiceImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class SpringMain {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext springContext = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml")){
            ZoneService zoneService = (ZoneServiceImpl) springContext.getBean(ZoneService.class);
            UserService userService = (UserService) springContext.getBean(UserService.class);
//            EventService eventService = (EventService) springContext.getBean(EventService.class);
            Zone zone = zoneService.get(ZoneTestData.ZONE_ID);

            System.out.println(Arrays.toString(springContext.getBeanDefinitionNames()));
            TriggerService triggerService = (TriggerServiceImpl)springContext.getBean(TriggerService.class);
            triggerService.save(new Trigger("Move 2 floor 2", Type.DIGITAL), zone);
//            triggerService.save(new Trigger("Door 2 floor", Type.DIGITAL));
            triggerService.getAll().forEach(System.out::println);
            EventService eventService = (EventServiceImpl) springContext.getBean(EventService.class);
            eventService.getAlarmed().forEach(System.out::println);
//            Trigger trigger103 = triggerService.get(103);
//            Event event = new DigitEvent(trigger103, true);
//            System.out.println(event);
//            eventService.save(trigger103, event);
//            eventService.save(triggerService.get(103), new DigitEvent(triggerService.get(103), false));
//            eventService.get(triggerService.get(103)).forEach(System.out::println);
//            userService.getAll().forEach(System.out::println);
        }
    }
}
