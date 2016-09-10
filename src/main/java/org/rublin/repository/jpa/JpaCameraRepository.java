package org.rublin.repository.jpa;

import org.rublin.model.Camera;
import org.rublin.model.Zone;
import org.rublin.repository.CameraRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

/**
 * Created by Ruslan Sheremet (rublin) on 10.09.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JpaCameraRepository implements CameraRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Camera save(Camera camera, Zone zone) {
        if (camera.isNew()) {
            camera.setZone(zone);
            em.persist(camera);
            return camera;
        } else {
            return em.merge(camera);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Camera.DELETE).setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public Camera get(int id) {
        return em.find(Camera.class, id);
    }

    @Override
    public Collection<Camera> getAll() {
        return em.createNamedQuery(Camera.GET_ALL_SORTED, Camera.class).getResultList();
    }

    @Override
    public Collection<Camera> getByZone(Zone zone) {
        return em.createNamedQuery(Camera.GET_BY_ZONE, Camera.class).setParameter("zone", zone).getResultList();
    }
}
