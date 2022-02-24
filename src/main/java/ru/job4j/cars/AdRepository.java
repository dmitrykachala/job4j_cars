package ru.job4j.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Ad;

import java.sql.Timestamp;
import java.util.Collection;

public class AdRepository {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf;

    public AdRepository() {
        sf = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }
    
    public Collection<Ad> findLastAds() {

        Collection<Ad> rsl = null;

        try {
            Session session = sf.openSession();
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
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        return rsl;
    }

    public Collection<Ad> findAdWithPic() {
        Collection<Ad> rsl = null;

        try {
            Session session = sf.openSession();
            rsl = session.createQuery(
                            "select distinct a from Ad a "
                                    + "join fetch a.car c "
                                    + "where a.pictureLink is not null", Ad.class
                    ).list();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        return rsl;
    }

    public Collection<Ad> findCar(String name) {
        Collection<Ad> rsl = null;

        try {
            Session session = sf.openSession();
            rsl = session.createQuery(
                    "select distinct a from Ad a "
                            + "join fetch a.car c "
                            + "where c.name = :name", Ad.class
            )
                    .setParameter("name", name).list();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        return rsl;
    }

    public static void main(String[] args) {

        AdRepository ar = new AdRepository();

        System.out.println("LAST");
        for (Ad str : ar.findLastAds()) {
            System.out.println(str);
        }

        System.out.println("PIC");
        for (Ad str : ar.findAdWithPic()) {
            System.out.println(str);
        }

        System.out.println("CAR");
        System.out.println(ar.findCar("s"));

    }
}
