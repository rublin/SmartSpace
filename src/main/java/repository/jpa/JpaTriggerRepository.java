package repository.jpa;

import model.Trigger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repository.TriggerRepository;

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
    public Trigger save(Trigger trigger) {
        if (trigger.isNew()){
            em.persist(trigger);
            return trigger;
        } else {
            return em.merge(trigger);
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
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
