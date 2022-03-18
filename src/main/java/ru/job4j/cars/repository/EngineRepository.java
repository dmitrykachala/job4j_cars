package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.cars.model.Engine;

import java.util.ArrayList;
import java.util.List;

public class EngineRepository {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private EngineRepository() {

    }

    private static class Holder {
        private static final EngineRepository INST = new EngineRepository();
    }

    public static EngineRepository getInstance() {
        return EngineRepository.Holder.INST;
    }

    public Engine add(Engine engine) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(engine);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return engine;
    }

    public List<Engine> findAll() {
        List<Engine> rsl = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();

            rsl = session.createQuery("select e from Engine e", Engine.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
        }
        return rsl;
    }

    public Engine findEngineById(int id) {

        try {
            Session session = sf.openSession();
            session.beginTransaction();
            Query query = session
                    .createQuery("from ru.job4j.cars.model.Engine where id = :id");
            query.setParameter("id", id);

            session.getTransaction().commit();
            Engine rsl = (Engine) query.uniqueResult();
            session.close();
            return rsl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
