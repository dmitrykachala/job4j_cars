package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

public class UserRepository {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

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
            Session session = sf.openSession();
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
            Session session = sf.openSession();
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
