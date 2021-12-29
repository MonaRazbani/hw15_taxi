package dataAccess;
import models.Driver;
import models.Passenger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDataAccess extends DataBaseAccess {


    public void saveNewPassenger(Passenger passenger) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(passenger);
        transaction.commit();
        session.close();
    }
    public void updatePassenger(Passenger passenger) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(passenger);
        transaction.commit();
        session.close();
    }

    public Passenger findByNationalCode(String nationalCode) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Passenger> query = session.createQuery("FROM Passenger passenger WHERE passenger.nationalCode=:nationalCode ");
        query.setParameter("nationalCode", nationalCode);
        Passenger result = query.getSingleResult();
        transaction.commit();
        session.close();
        return result;
    }

    public List<Passenger> getListOfPassengers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Passenger> query = session.createQuery("from Passenger fetch all properties ");
        List<Passenger> resultList = query.getResultList();
        transaction.commit();
        session.close();
        return resultList;
    }
    public Passenger getAllPassengerInfo(Passenger passenger){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<Passenger> query = session.createQuery("from Passenger passenger fetch all properties where passenger.nationalCode=:nationalCode");
        query.setParameter("nationalCode", passenger.getNationalCode());
        Passenger result = query.getSingleResult();
        transaction.commit();
        session.close();
        return result;

    }
}
