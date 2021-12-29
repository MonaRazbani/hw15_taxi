package Serivces;

import models.Car;

public class CarService {
    public Car createNewCar (String info ){
      String[] splitInfo = info.split(",");
        String model = splitInfo[0];
        String carColor = splitInfo[1];
        Car car = new Car() ;
        car.setCarColor(carColor);
        car.setModel(model);
        return car;
    }
}
