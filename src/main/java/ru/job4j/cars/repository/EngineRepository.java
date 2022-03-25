package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.cars.model.Engine;

import java.util.ArrayList;
import java.util.List;

public class EngineRepository {

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
            Session session = Repository.SESSION_FACTORY.openSession();
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
        try (Session session = Repository.SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            rsl = session.createQuery("select e from Engine e", Engine.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            Repository.SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
        }
        return rsl;
    }

    public Engine findEngineById(int id) {

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
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
