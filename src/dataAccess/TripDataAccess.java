package dataAccess;

import enumeration.TripStatus;
import models.Driver;
import models.Trip;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TripDataAccess extends DataBaseAccess {


    public Trip findOnGoingTripByDriver(Driver driver) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Trip> query = session.createQuery("FROM Trip trip WHERE trip.driver=:driver and trip.driver.tripStatus =:tripStatus");
        query.setParameter("driver", driver);
        query.setParameter("tripStatus",TripStatus.ONGOING);
        Trip result = query.getSingleResult();
        transaction.commit();
        session.close();
        return result;
    }
    public List<Trip> findOnGoingTrip() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Trip> query = session.createQuery("FROM Trip trip WHERE trip.driver.status=:ongoing ");
        query.setParameter("ongoing", TripStatus.ONGOING);
        List<Trip> results = query.getResultList();
        transaction.commit();
        session.close();
        return results;
    }

    public void updateTrip(Trip trip) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(trip);
        transaction.commit();
        session.close();
    }


    public void saveTrip(Trip trip) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(trip);
        transaction.commit();
        session.close();
    }

    public List<Trip> getOngoingTravels() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Trip> query = session.createQuery("FROM Trip trip WHERE trip.driver.tripStstus=:ongoing ");
        query.setParameter("ongoing", TripStatus.ONGOING);
        List<Trip> results = query.getResultList();
        transaction.commit();
        session.close();
        return results;
    }
}
