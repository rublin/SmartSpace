package org.rublin.repository.jpa;

import org.rublin.model.Zone;
import org.rublin.repository.ZoneRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

/**
 * Created by Sheremet on 11.07.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JpaZoneRepository implements ZoneRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Zone save(Zone object) {
        if (object.isNew()) {
            em.persist(object);
            return object;
        } else {
            return em.merge(object);
        }
    }

    @Override
    public Zone get(int id) {
        return em.find(Zone.class, id);
    }

    @Override
    public Collection<Zone> getAll() {
        return em.createNamedQuery(Zone.GET_All_SORTED, Zone.class).getResultList();
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Zone.DELETE).setParameter("id", id).executeUpdate() != 0;
    }
}
