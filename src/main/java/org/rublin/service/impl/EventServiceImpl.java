package org.rublin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.model.Trigger;
import org.rublin.model.event.Event;
import org.rublin.repository.EventRepository;
import org.rublin.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public List<Event> get(Trigger trigger) {
        return eventRepository.get(trigger);
    }

    @Override
    public List<Event> get(Trigger trigger, int numberLatestEvents) {
        List<Event> events = eventRepository.get(trigger);
        return events.stream()
                .limit(numberLatestEvents)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.getAll();
    }

    @Override
    public List<Event> getBetween(LocalDateTime from, LocalDateTime to) {
        return eventRepository.getBetween(from, to);
    }

    @Override
    public List<Event> getAlarmed() {
        return eventRepository.getAlarmed();
    }
}
