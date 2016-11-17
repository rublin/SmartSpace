package org.rublin.main;

import org.rublin.controller.ModemController;
import org.rublin.model.*;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.repository.mock.InMemoryObjectRepository;
import org.rublin.repository.mock.InMemoryEventRepository;
import org.rublin.repository.mock.InMemoryTriggerRepository;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    private static final int ZONE_ID = 10;
    public static void main(String[] args) throws InterruptedException {
        /*
        InMemoryObjectRepository objectRepository = new InMemoryObjectRepository();
        InMemoryTriggerRepository triggerRepository = new InMemoryTriggerRepository();
        InMemoryEventRepository stateRepository = new InMemoryEventRepository();
        Zone obj = objectRepository.get(ZONE_ID);
        //stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        triggerRepository.save(new Trigger("org.rublin.main Move 1 floor"), obj);
        triggerRepository.save(new Trigger("org.rublin.main Door 1 floor"), obj);
        triggerRepository.getAll().forEach(System.out::println);

        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),true));
        //hread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),false));
        Thread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),true));
        //Thread.sleep(5000);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);

        Trigger analog = triggerRepository.save(new Trigger("FAT"), obj);
        stateRepository.save(triggerRepository.get(analog.getId()), new AnalogEvent(analog, 12.0));
        stateRepository.get(triggerRepository.get(analog.getId())).forEach(System.out::println);
        */
//        ModemController modem = new ModemController();
//        modem.start();
//        modem.sendSms("+380950724287", "Zone Zone1 notification:\n" +
//                "Trigger: <b>Trigger1</b>; Status: <b>BAD</b>".replaceAll("\\<.*\\>", ""));
//        modem.stop();
        System.out.println("Zone Zone1 notification:\nTrigger: <b>Trigger1</b>; Status: <b>BAD</b>".replaceAll("<[^>]*>", ""));
    }
}
