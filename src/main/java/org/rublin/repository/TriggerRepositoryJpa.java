package org.rublin.repository;

import org.rublin.model.Trigger;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TriggerRepositoryJpa extends PagingAndSortingRepository<Trigger, Integer>  {
    List<Trigger> findByState(boolean state);
}
