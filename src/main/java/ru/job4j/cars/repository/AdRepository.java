package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.job4j.cars.model.Ad;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Category;
import ru.job4j.cars.model.Engine;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AdRepository {

    public AdRepository() {

    }

    private static class Holder {
        private static final AdRepository INST = new AdRepository();
    }

    public static AdRepository getInstance() {
        return Holder.INST;
    }

    public List<Ad> findAll() {

        return this.tx(
                session -> session
                        .createQuery("select distinct a from Ad a join fetch a.categories").list()
        );

    }

    public void update(int id) {

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            session.beginTransaction();

            String hql = "update ru.job4j.cars.model.Ad SET sold = :sold where id = :id";
            Query query = session.createQuery(hql);

            query.setParameter("sold", true);
            query.setParameter("id", id);
            query.executeUpdate();

            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Collection<Ad> findLastAds() {

        Collection<Ad> rsl = null;

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            rsl = session.createQuery(
                    "select distinct a from Ad a "
                            + "join fetch a.car c "
                            + "where a.created between :stDate and :endDate", Ad.class
            ).setParameter("stDate",
                            new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000))
                    .setParameter("endDate", new Timestamp(System.currentTimeMillis())).list();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return rsl;
    }

    public Ad findAdById(int id) {
        Ad rsl = null;

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            rsl = session.createQuery(
                    "select distinct a from Ad a "
                            + "where a.id =:id ", Ad.class
            ).setParameter("id", id).uniqueResult();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return rsl;
    }

    public Collection<Ad> findAdWithPic() {
        Collection<Ad> rsl = null;

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            rsl = session.createQuery(
                            "select distinct a from Ad a "
                                    + "join fetch a.car c "
                                    + "where a.pictureLink is not null", Ad.class
                    ).list();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return rsl;
    }

    public Collection<Ad> findCar(String name) {
        Collection<Ad> rsl = null;

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
            rsl = session.createQuery(
                    "select distinct a from Ad a "
                            + "join fetch a.car c "
                            + "where c.name = :name", Ad.class
            )
                    .setParameter("name", name).list();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return rsl;
    }

    public void addNewAd(Ad ad, String[] ids, int carId) {
        try (Session session = Repository.SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            for (String id : ids) {
                Category category = session.find(Category.class, Integer.parseInt(id));
                ad.addCategory(category);

                ad.setCar(CarRepository.getInstance().findCarById(carId));
            }
            session.save(ad);

            session.getTransaction().commit();
        } catch (Exception e) {
            Repository.SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
        }
    }

    public List<Category> allCategories() {
        List<Category> rsl = new ArrayList<>();
        try (Session session = Repository.SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            rsl = session.createQuery("select c from Category c", Category.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            Repository.SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
        }
        return rsl;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = Repository.SESSION_FACTORY.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {

        AdRepository ar = AdRepository.getInstance();
        CarRepository cr = CarRepository.getInstance();
        EngineRepository er = EngineRepository.getInstance();

        Engine engine = er.findEngineById(3);

        System.out.println(er.findAll());

        Car car = new Car();
        car.setBody("wagon");
        car.setEngine(engine);
        car.setName("audi");
        cr.add(car);

        System.out.println(cr.findCarById(4));
    }
}
