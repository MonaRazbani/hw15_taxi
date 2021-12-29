package Serivces;

import dataAccess.PassengerDataAccess;
import enumeration.TripStatus;
import models.Car;
import models.Driver;
import models.Passenger;

import java.util.List;

public class PassengerService {
    private PassengerDataAccess passengerDao = new PassengerDataAccess();
    public void showListOfPassenger() {
        List<Passenger> passengers = passengerDao.getListOfPassengers();
        for (Passenger passenger : passengers) {
            System.out.println(passenger);
        }
    }
    public void addPassengerByAdmin(String driverInfo) {
        String[] splitInfo = driverInfo.split(",");
        String passengerName = splitInfo[0];
        String passengerFamily = splitInfo[1];
        String username = splitInfo[2];
        String phoneNumber = splitInfo[3];
        String nationalCode = splitInfo[4];
        Passenger passenger = new Passenger();
        passenger.setName(passengerName);
        passenger.setFamily(passengerFamily);
        passenger.setUsername(username);
        passenger.setPhoneNumber(phoneNumber);
        passenger.setNationalCode(nationalCode);
        passenger.setStatus(TripStatus.STOP);
        passengerDao.saveNewPassenger(passenger);
        System.out.println("New passenger saved successfully.");
    }

    public Passenger findPassengerNationalCode(String nationalCode) {
        return passengerDao.findByNationalCode(nationalCode);
    }

    public void increasePassengerBalance(Passenger passenger , int amountIncreaseBalance){
        passenger.setBalance(passenger.getBalance()+amountIncreaseBalance);
        passengerDao.updatePassenger(passenger);
    }
    public void decreasePassengerBalance(Passenger passenger , int amountDecreaseBalance){
        passenger.setBalance(passenger.getBalance()-amountDecreaseBalance);
        passengerDao.updatePassenger(passenger);
    }
    public Passenger getAllPassengerInfo (Passenger passenger){
        return passengerDao.getAllPassengerInfo(passenger);
    }
}

