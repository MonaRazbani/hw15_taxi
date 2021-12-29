package Serivces;

import dataAccess.DriverDataAccess;
import enumeration.TripStatus;
import lombok.Data;
import models.Car;
import models.Driver;

import java.util.List;

@Data
public class DriverService {
    private DriverDataAccess driverDao = new DriverDataAccess();

    public void showListOfDrivers() {
        List<Driver> drivers = driverDao.getListOfDrivers();
        for (Driver driver : drivers) {
            System.out.println(driver);
        }
    }

    public void addDriverByAdmin(String driverInfo,String carInfo) {
        Car car = createNewCar(carInfo);
         String[] splitInfo = driverInfo.split(",");
            String driverName = splitInfo[0];
            String driverFamily = splitInfo[1];
            String username = splitInfo[2];
            String phoneNumber = splitInfo[3];
            String nationalCode = splitInfo[4];
            String plaque = splitInfo[5];
            Driver driver = new Driver();
            driver.setName(driverName);
            driver.setFamily(driverFamily);
            driver.setUsername(username);
            driver.setPhoneNumber(phoneNumber);
            driver.setPlaque(plaque);
            driver.setNationalCode(nationalCode);
            driver.setStatus(TripStatus.WAIT);
            driver.setCar(car);
            driverDao.saveNewDriver(driver);
            System.out.println("New drivers saved successfully.");
        }
    public Car createNewCar (String info ){
        String[] splitInfo = info.split(",");
        String model = splitInfo[0];
        String carColor = splitInfo[1];
        Car car = new Car() ;
        car.setCarColor(carColor);
        car.setModel(model);
        return car;
    }

    public Driver findDriverNationalCode(String nationalCode) {
        return driverDao.findByNationalCode(nationalCode);
    }

    public boolean isLocationUpdate(Driver driver) {

        if (driver.getCurrentLocationLat()== 0.0 && driver.getCurrentLocationLong() == 0.0) {
            return false;
        }
        return true;
    }

    public void updateDriverLocation(Driver driver, Double[] point) {
        driver.setCurrentLocationLat(point[0]);
        driver.setCurrentLocationLong(point[1]);
        driverDao.updateDriver(driver);
    }
}

