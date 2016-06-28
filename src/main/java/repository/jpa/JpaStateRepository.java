package repository.jpa;

import model.Trigger;
import model.event.Event;
import repository.StateRepository;

import java.util.List;

/**
 * Created by Sheremet on 27.06.2016.
 */
public class JpaStateRepository implements StateRepository {
    @Override
    public void save(Trigger trigger, Event event) {

    }

    @Override
    public List<Event> get(Trigger trigger) {
        return null;
    }

    @Override
    public List<Event> getAll() {
        return null;
    }
}
