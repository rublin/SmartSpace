package org.rublin.repository.jpa;

import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.event.AbstractEvent;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.repository.EventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sheremet on 27.06.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JpaEventRepository implements EventRepository {

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
            events = new ArrayList<>(em.createNamedQuery(DigitEvent.GET, DigitEvent.class).setParameter("trigger_id", trigger.getId()).getResultList());
        } else {
            events = new ArrayList<>(em.createNamedQuery(AnalogEvent.GET, AnalogEvent.class).setParameter("trigger_id", trigger.getId()).getResultList());
        }
        return events;
    }

    @Override
    public List<Event> getAll() {
        List<DigitEvent> digitalEvents = em.createNamedQuery(DigitEvent.GET_ALL, DigitEvent.class).getResultList();
        List<AnalogEvent> analogEvents = em.createNamedQuery(AnalogEvent.GET_ALL, AnalogEvent.class).getResultList();
        return mergeListsWithSort(digitalEvents, analogEvents);
    }

    @Override
    public List<Event> getBetween(LocalDateTime from, LocalDateTime to) {
        List<AnalogEvent> analogEvents = em.createNamedQuery(AnalogEvent.GET_BETWEEN, AnalogEvent.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
        List<DigitEvent> digitEvents = em.createNamedQuery(DigitEvent.GET_BETWEEN, DigitEvent.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
        return mergeListsWithSort(digitEvents, analogEvents);
    }

    @Override
    public List<Event> getAlarmed() {
        List<DigitEvent> digitEvents = em.createNamedQuery(DigitEvent.GET_ALARMED, DigitEvent.class).getResultList();
        List<AnalogEvent> analogEvents = em.createNamedQuery(AnalogEvent.GET_ALARMED, AnalogEvent.class).getResultList();
        return mergeListsWithSort(digitEvents, analogEvents);
    }

    private List<Event> mergeListsWithSort (List<DigitEvent> digitEvents, List<AnalogEvent> analogEvents) {
        List<Event> allEvents = new ArrayList<>(digitEvents);
        allEvents.addAll(analogEvents);
        return allEvents.stream()
                .sorted((e1, e2) -> e2.getTime().compareTo(e1.getTime()))
                .collect(Collectors.toList());
    }
}
