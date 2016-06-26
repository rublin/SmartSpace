package main;

import model.*;
import repository.mock.InMemoryStateRepository;
import repository.mock.InMemoryTriggerRepository;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        InMemoryTriggerRepository triggerRepository = new InMemoryTriggerRepository();
        InMemoryStateRepository stateRepository = new InMemoryStateRepository();
        //stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);
        triggerRepository.save(new DigitTrigger("main Move 1 floor"));
        triggerRepository.save(new DigitTrigger("main Door 1 floor"));
        triggerRepository.getAll().forEach(System.out::println);

        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),true));
        //hread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),false));
        Thread.sleep(1000);
        stateRepository.save(triggerRepository.get(2), new DigitEvent(triggerRepository.get(2),true));
        //Thread.sleep(5000);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(triggerRepository.get(1),false));
        stateRepository.get(triggerRepository.get(1)).forEach(System.out::println);

        Trigger analog = triggerRepository.save(new AnalogTrigger("FAT"));
        stateRepository.save(triggerRepository.get(analog.getId()), new AnalogEvent(analog, 12.0));
        stateRepository.get(triggerRepository.get(analog.getId())).forEach(System.out::println);
    }
}
