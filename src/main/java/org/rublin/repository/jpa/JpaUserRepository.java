package org.rublin.repository.jpa;

import org.rublin.model.user.User;
import org.rublin.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
@Repository
@Transactional(readOnly = true)
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public User save(User user) {
        if (user.isNew()) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(User.DELETE).setParameter("id", id).executeUpdate() != 0;
    }

    @Override
    public User get(int id) {
        return em.find(User.class, id);
    }

    @Override
    public User getByEmail(String email) {
        return em.createNamedQuery(User.BY_EMAIL, User.class).setParameter("email", email).getSingleResult();
    }

    @Override
    public User getByTelegramId(int telegramId) {
        return em.createNamedQuery(User.BY_TELEGRAMID, User.class).setParameter("telegramId", telegramId).getSingleResult();
    }

    @Override
    public User getByTelegramName(String name) {
        return em.createNamedQuery(User.BY_TELEGRAMNAME, User.class).setParameter("telegramName", name).getSingleResult();
    }

    @Override
    public User getByMobile(String mobile) {
        return em.createNamedQuery(User.BY_MOBILE, User.class).setParameter("mobile", mobile).getSingleResult();
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.ALL_SORTED, User.class).getResultList();
    }
}
