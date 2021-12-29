package Serivces;

import dataAccess.DriverDataAccess;
import dataAccess.PassengerDataAccess;
import dataAccess.TripDataAccess;
import enumeration.PayStatus;
import enumeration.TripStatus;
import models.Driver;
import models.Passenger;
import models.Trip;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripService {
    private TripDataAccess tripDao = new TripDataAccess();
    private PassengerDataAccess passengerDao = new PassengerDataAccess();
    private DriverDataAccess driverDao = new DriverDataAccess();

    public List<Trip> showOngoingTravels() {
        return tripDao.findOnGoingTrip();
    }

    public void confirmCashReceiptByDriver(Driver driver) {
        Trip trip = tripDao.findOnGoingTripByDriver(driver);
        if (trip.getPayStatus() == PayStatus.CASH) {
            trip.setPayStatus(PayStatus.PAYED);
            tripDao.updateTrip(trip);
            System.out.println("Confirmed");
        }
    }

    public void TravelFinishByDriver(Driver driver) {
        Trip trip = tripDao.findOnGoingTripByDriver(driver);
        driver.setCurrentLocationLong(trip.getDestinationLong());
        driver.setCurrentLocationLat(trip.getDestinationLat());
        driver.setStatus(TripStatus.WAIT);
        driverDao.updateDriver(driver);
        trip.getPassenger().setStatus(TripStatus.STOP);
        passengerDao.updatePassenger(trip.getPassenger());
        System.out.println("Travel is finished and your location is updated.");
    }

    public void createTrip (Passenger passenger, double originLat, double originLong, double destinationLat, double destinationLong,PayStatus payStatus){
        int price = calculateTripPrice(originLat,originLong,destinationLat,destinationLong) ;
        Driver availableDriver = findAvailableDriver(originLat, originLong, destinationLat, destinationLong);
        Trip trip = new Trip();
        trip.setDriver(availableDriver);
        trip.setPassenger(passenger);
        trip.setPayStatus(payStatus);
        trip.setOriginLat(originLat);
        trip.setOriginLong(originLong);
        trip.setDestinationLat(destinationLat);
        trip.setDestinationLong(destinationLong);
        trip.setPrice(price);
        tripDao.saveTrip(trip);
        System.out.println("Your request accepted by " + availableDriver.getName() + "," +
                availableDriver.getFamily() + ", plaque number: " +availableDriver.getPlaque()+" price :"+price);
    }
    public Driver  findAvailableDriver( double originLat, double originLong, double destinationLat, double destinationLong) {
        List<Driver> foundDrivers = driverDao.findDriverByWaitStatus();
        List<Double> distances = new ArrayList<>();
        for (Driver item : foundDrivers) {
            double locLat = item.getCurrentLocationLat();
            double locLong = item.getCurrentLocationLong();
            double distance = Math.sqrt((Math.exp(locLat) - Math.exp(originLat)) + ((Math.exp(locLong)) - Math.exp(originLong)));
            distances.add(distance);
        }
        double minDistance = Collections.min(distances);
        int index = distances.indexOf(minDistance);
        Driver selectedDriver = foundDrivers.get(index);
        return selectedDriver;
    }

    public int calculateTripPrice(double origLat, double origLong, double destLat, double destLong) {
        double distance = Math.sqrt((Math.exp(origLat - destLat)) + (Math.exp(origLong - destLong)));
        return (int) (1000 * distance);
    }
    public void showOngoingTrips() {
        List<Trip> trips = tripDao.findOnGoingTrip();
        for (Trip trip : trips) {
            System.out.println(trip);
        }
    }

}


