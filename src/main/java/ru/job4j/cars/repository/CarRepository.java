package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.cars.model.Car;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private CarRepository() {

    }

    private static class Holder {
        private static final CarRepository INST = new CarRepository();
    }

    public static CarRepository getInstance() {
        return CarRepository.Holder.INST;
    }

    public Car add(Car car) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(car);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return car;
    }

    public List<Car> findAll() {
        List<Car> rsl = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();

            rsl = session.createQuery("select c from Car c", Car.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            sf.getCurrentSession().getTransaction().rollback();
        }
        return rsl;
    }

    public Car findCarByName(String name) {

        try {
            Session session = sf.openSession();
            session.beginTransaction();
            Query query = session
                    .createQuery("from ru.job4j.cars.model.Car where name = :name");
            query.setParameter("name", name);

            session.getTransaction().commit();
            Car rsl = (Car) query.uniqueResult();
            session.close();
            return rsl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Car findCarById(int id) {

        try {
            Session session = sf.openSession();
            session.beginTransaction();
            Query query = session
                    .createQuery("from ru.job4j.cars.model.Car where id = :id");
            query.setParameter("id", id);

            session.getTransaction().commit();
            Car rsl = (Car) query.uniqueResult();
            session.close();
            return rsl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        CarRepository ar = new CarRepository();

        System.out.println("Cars");

        for (Car str : ar.findAll()) {
            System.out.println(str);
        }

        System.out.println(ar.findCarById(2));

/*        System.out.println("PIC");
        for (Ad str : ar.findAdWithPic()) {
            System.out.println(str);
        }

        System.out.println("CAR");
        System.out.println(ar.findCar("s"));*/

    }
}
