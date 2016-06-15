import model.DigitTrigger;
import repository.mock.InMemoryTriggerRepository;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class Main {
    public static void main(String[] args) {
        InMemoryTriggerRepository repository = new InMemoryTriggerRepository();
        repository.save(new DigitTrigger("Move 1 floor"));
        repository.save(new DigitTrigger("Door 1 floor"));
        repository.getAll().forEach(System.out::println);
    }
}
