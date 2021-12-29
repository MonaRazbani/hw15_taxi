package dataAccess;

import enumeration.TripStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;

public class DriverDataAccess extends DataBaseAccess {


    public models.Driver findByNationalCode(String nationalCode) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<models.Driver> query = session.createQuery("FROM Driver driver WHERE driver.nationalCode=:nationalCode ");
        query.setParameter("nationalCode", nationalCode);
        models.Driver result = query.getSingleResult();
        transaction.commit();
        session.close();
        return result;
    }

    public void updateDriver(models.Driver driver) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(driver);
        transaction.commit();
        session.close();
    }

    public List<models.Driver> findDriverByWaitStatus() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<models.Driver> query = session.createQuery("FROM Driver driver WHERE driver.status=:status ");
        query.setParameter("status", TripStatus.WAIT);
        List<models.Driver> results = query.getResultList();
        transaction.commit();
        session.close();
        return results;
    }

    public void saveNewDriver(models.Driver driver) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
            session.save(driver);
        transaction.commit();
        session.close();
    }
    public List<models.Driver> getListOfDrivers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query<models.Driver> query = session.createQuery("from Driver fetch all properties ");
        List<models.Driver> resultList = query.getResultList();
        transaction.commit();
        session.close();
        return resultList;

    }
}