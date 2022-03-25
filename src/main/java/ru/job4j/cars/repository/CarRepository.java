package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.cars.model.Car;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {

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
            Session session = Repository.SESSION_FACTORY.openSession();
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
        try (Session session = Repository.SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            rsl = session.createQuery("select c from Car c", Car.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            Repository.SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
        }
        return rsl;
    }

    public Car findCarByName(String name) {

        try {
            Session session = Repository.SESSION_FACTORY.openSession();
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
            Session session = Repository.SESSION_FACTORY.openSession();
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
