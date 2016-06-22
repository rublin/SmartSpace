package main;

import model.DigitTrigger;
import model.DigitEvent;
import repository.mock.InMemoryStateRepository;
import repository.mock.InMemoryTriggerRepository;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        InMemoryTriggerRepository triggerRepository = new InMemoryTriggerRepository();
        triggerRepository.save(new DigitTrigger("main Move 1 floor"));
        triggerRepository.save(new DigitTrigger("main Door 1 floor"));
        triggerRepository.getAll().forEach(System.out::println);
        InMemoryStateRepository stateRepository = new InMemoryStateRepository();
        stateRepository.save(triggerRepository.get(1), new DigitEvent(true));
        //hread.sleep(1000);
        /*stateRepository.save(triggerRepository.get(2), new DigitEvent(false));
        Thread.sleep(1000);*/
        stateRepository.save(triggerRepository.get(2), new DigitEvent(true));
        //Thread.sleep(5000);
        stateRepository.save(triggerRepository.get(1), new DigitEvent(false));
        stateRepository.getAll(triggerRepository.get(1)).forEach(System.out::println);
    }
}
