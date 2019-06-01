package org.rublin.repository;

import org.rublin.model.Zone;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ZoneRepositoryJpa extends PagingAndSortingRepository<Zone, Integer> {
    void deleteById(Integer id);
}
