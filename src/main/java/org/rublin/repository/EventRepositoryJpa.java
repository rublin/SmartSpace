package org.rublin.repository;

import org.rublin.model.Trigger;
import org.rublin.model.event.Event;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryJpa {
    /*extends
} PagingAndSortingRepository<Event, Integer> {
    List<Event> findByTrigger(Trigger trigger);

    List<Event> findAllByAlarmTrue();

    List<Event> findAllByTimeBetween(LocalDateTime from, LocalDateTime to);*/
}
