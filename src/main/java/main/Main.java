package main;

import model.DigitTrigger;
import repository.mock.InMemoryStateRepository;
import repository.mock.InMemoryTriggerRepository;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        InMemoryTriggerRepository repository = new InMemoryTriggerRepository();
        repository.save(new DigitTrigger("Move 1 floor"));
        repository.save(new DigitTrigger("Door 1 floor"));
        repository.getAll().forEach(System.out::println);
        InMemoryStateRepository stateRepository = new InMemoryStateRepository();
        stateRepository.save(repository.get(1), true);
        Thread.sleep(1000);
        stateRepository.save(repository.get(2), true);
        Thread.sleep(1000);
        stateRepository.save(repository.get(2), false);
        Thread.sleep(5000);
        stateRepository.save(repository.get(1), false);
        stateRepository.getAll(repository.get(1)).forEach((k, v) -> System.out.println(k+" "+v));
    }
}
