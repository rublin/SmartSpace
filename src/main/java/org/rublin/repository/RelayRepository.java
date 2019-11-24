package org.rublin.repository;

import org.rublin.model.Relay;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RelayRepository extends PagingAndSortingRepository<Relay, Integer> {

    Optional<Relay> findByName(String name);
}
