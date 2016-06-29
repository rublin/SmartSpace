package repository.jpa;

import model.Trigger;
import model.Type;
import model.event.AnalogEvent;
import model.event.DigitEvent;
import model.event.Event;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repository.StateRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sheremet on 27.06.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JpaStateRepository implements StateRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Trigger trigger, Event event) {
        //Trigger ref = em.getReference(Trigger.class, int tr)
        if (event.isNew()) {
            event.setTrigger(trigger);
            em.persist(event);
        }
    }

    @Override
    public List<Event> get(Trigger trigger) {
        List<Event> events;
        if (trigger.getType().equals(Type.DIGITAL)) {
            events = new ArrayList<>(em.createNamedQuery(DigitEvent.GET_ALL_SORTED, DigitEvent.class).setParameter("trigger_id", trigger.getId()).getResultList());
        } else {
            events = new ArrayList<>(em.createNamedQuery(AnalogEvent.GET_ALL_SORTED, AnalogEvent.class).setParameter("trigger_id", trigger.getId()).getResultList());
        }
        return events;
    }

    @Override
    public List<Event> getAll() {
        return null;
    }
}
