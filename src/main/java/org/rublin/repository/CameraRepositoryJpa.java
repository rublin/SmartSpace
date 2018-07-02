package org.rublin.repository;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CameraRepositoryJpa extends PagingAndSortingRepository<Camera, Integer> {
    List<Camera> findAllByZone(Zone zone);
}
