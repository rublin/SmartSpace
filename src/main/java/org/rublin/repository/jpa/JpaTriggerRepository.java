package org.rublin.repository.jpa;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.rublin.repository.TriggerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

/**
 * Created by Sheremet on 28.06.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JpaTriggerRepository implements TriggerRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Trigger save(Trigger trigger, Zone obj) {
        if (trigger.isNew()){
            trigger.setZone(obj);
            em.persist(trigger);
            return trigger;
        } else {
            return em.merge(trigger);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Trigger.DELETE).setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public Trigger get(int id) {
        return em.find(Trigger.class, id);
    }

    @Override
    public Collection<Trigger> getAll() {
        return em.createNamedQuery(Trigger.GET_ALL_SORTED, Trigger.class).getResultList();
    }
}
