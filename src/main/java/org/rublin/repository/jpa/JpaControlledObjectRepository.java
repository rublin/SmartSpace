package org.rublin.repository.jpa;

import org.rublin.model.ControlledObject;
import org.rublin.repository.ControlledObjectRepository;
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
public class JpaControlledObjectRepository implements ControlledObjectRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public ControlledObject save(ControlledObject object) {
        if (object.isNew()) {
            em.persist(object);
            return object;
        } else {
            return em.merge(object);
        }
    }

    @Override
    public ControlledObject get(int id) {
        return em.find(ControlledObject.class, id);
    }

    @Override
    public Collection<ControlledObject> getAll() {
        return em.createNamedQuery(ControlledObject.GET_All_SORTED, ControlledObject.class).getResultList();
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(ControlledObject.DELETE).setParameter("id", id).executeUpdate() != 0;
    }
}
