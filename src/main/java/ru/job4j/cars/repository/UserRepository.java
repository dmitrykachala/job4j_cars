package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

public class UserRepository {

    private UserRepository() {

    }

    private static class Holder {
        private static final UserRepository INST = new UserRepository();
    }

    public static UserRepository getInstance() {
        return Holder.INST;
    }

    public User add(User user) {
        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findUserByEmail(String email) {

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            session.beginTransaction();
            Query query = session
                    .createQuery("from ru.job4j.cars.model.User where email = :email");
            query.setParameter("email", email);

            session.getTransaction().commit();
            User rsl = (User) query.uniqueResult();
            session.close();
            return rsl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
