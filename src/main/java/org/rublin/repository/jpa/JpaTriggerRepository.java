package org.rublin.repository.jpa;

import org.rublin.model.Zone;
import org.rublin.model.Trigger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.rublin.repository.TriggerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;

import static org.rublin.model.Trigger.DELETE_BY_ZONE;

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
    public boolean deleteAllByZone(List<Zone> list) {
        int result = em.createNamedQuery(DELETE_BY_ZONE)
                .setParameter("zone", list)
                .executeUpdate();
        System.out.println(result);
        return false;
    }

    @Override
    public Trigger get(int id) {
        return em.find(Trigger.class, id);
    }

    @Override
    public List<Trigger> getByState(boolean state) {
        return em.createNamedQuery(Trigger.GET_BY_STATE, Trigger.class).setParameter("state", state).getResultList();
    }

    @Override
    public Collection<Trigger> getAll() {
        return em.createNamedQuery(Trigger.GET_ALL_SORTED, Trigger.class).getResultList();
    }
}
