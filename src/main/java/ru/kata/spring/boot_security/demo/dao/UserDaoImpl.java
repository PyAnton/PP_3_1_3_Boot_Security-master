package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
    @Override
    public void add(User user) {
        getEntityManager().persist(user);
    }

    @Override
    public void dell(long id) {
        getEntityManager()
                .createQuery("delete from User where id=: id")
                .setParameter("id", id)
                .executeUpdate();
    }

//    @Override
//    public void update(long id, String firstName, String lastName, String email) {
//        getEntityManager()
//                .createQuery("update User SET firstName =: firstName, lastName =: lastName," +
//                        " email =: email where id=: id")
//
//                .setParameter("firstName", firstName)
//                .setParameter("lastName", lastName)
//                .setParameter("email", email)
//                .setParameter("id", id)
//                .executeUpdate();
//    }
    @Override
    public User findUserById(long id) {
        List<User> users = getEntityManager()
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<User> listUsers() {
        return getEntityManager().createQuery("From User").getResultList();
    }
}
