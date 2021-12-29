
import Serivces.DriverService;
import Serivces.PassengerService;
import Serivces.TripService;
import enumeration.*;
import models.Driver;
import models.Passenger;

import java.sql.SQLException;
import java.util.*;
import java.sql.Date;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static DriverService driverService = new DriverService();
    private static PassengerService passengerService = new PassengerService();
    private static TripService tripService = new TripService();

    public static void main(String[] args) throws Exception {
        while (true) {
            MainMenu.showMainMenu();
            String choice = getChoiceNumber();

            int choiceNumber = Integer.parseInt(choice);

            switch (choiceNumber) {
                case 1:
                    addGroupOfDriversByAdmin();
                    break;
                case 2:
                    addGroupOfPassengersByAdmin();
                    break;
                case 3:
                    DriverSignUpOrLogin();
                    break;
                case 4:
                    passengerSignUpOrLogin();
                    break;
                case 5:
                    tripService.showOngoingTrips();
                    break;
                case 6:
                    driverService.showListOfDrivers();
                    break;
                case 7:
                    passengerService.showListOfPassenger();
                    break;
                default:
                    System.out.println("Invalid input!");

            }
        }
    }


    private static void addGroupOfDriversByAdmin() {
        String numberOfDrivers;
        System.out.println("Enter number of drivers");
        numberOfDrivers = scanner.next();
        int driverNumbers = Integer.parseInt(numberOfDrivers);
        for (int i = 0; i < driverNumbers; i++) {
            driverRegister();
            System.out.println("driver Saved");
        }
            System.out.println("drivers are saved successfully ");
    }

    private static void addGroupOfPassengersByAdmin() {
        String numberOfPassengers;
        System.out.println("Enter number of passenger");
        numberOfPassengers = scanner.next();
        int passengerNumber = Integer.parseInt(numberOfPassengers);
        for (int i = 0; i < passengerNumber; i++) {
           passengerRegister();
        }
        System.out.println("drivers are saved successfully ");
    }

    private static void DriverSignUpOrLogin() {
        System.out.println("nationalCode:");
        String nationalCode = scanner.next();
            Driver driver = driverService.findDriverNationalCode(nationalCode);
            if (driver != null)
                try {
                    if (driver.getStatus().equals(TripStatus.WAIT)) {
                        if (!driverService.isLocationUpdate(driver)) { //true = null
                            Double[] point = getDriverLocation();
                            driverService.updateDriverLocation(driver, point);
                        }
                        int choiceNumber;
                        do {
                            System.out.println("You are waiting for a trip request.");
                            System.out.println("1. Exit");
                            String choice = getChoiceNumber();
                            choiceNumber = Integer.parseInt(choice);
                        } while (choiceNumber != 1);
                    } else if (driver.getStatus() == TripStatus.ONGOING) {
                        System.out.println(driver);
                        int choiceNumber;
                        do {
                            DriverLoginMenu.showDriverLoginMenu();
                            String choice = getChoiceNumber();
                            choiceNumber = Integer.parseInt(choice);
                            switch (choiceNumber) {
                                case 1:
                                    tripService.confirmCashReceiptByDriver(driver);
                                    break;
                                case 2:
                                    tripService.TravelFinishByDriver(driver);
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Invalid number!");
                            }
                        }
                        while (choiceNumber != 3);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            else {
                int choiceNumber;
                do {
                    SignupMenu.showSignupMenu();
                    String choice = getChoiceNumber();
                    choiceNumber = Integer.parseInt(choice);
                    switch (choiceNumber) {
                        case 1:
                            driverRegister();
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("Invalid number!");
                    }
                } while (choiceNumber != 2);
            }
        }


    private static void driverRegister(){
        System.out.println("Enter your information");
            outer:
            while (true) {
                System.out.println("Enter drivers information like this : name,family,username,phoneNumber,nationalCode,plaque");
                String driverInfo = scanner.next();
                System.out.println("Enter car information like this : model,car");
                String carInfo = scanner.next();
                if (!ValidationUtil.checkingPersonInfo(driverInfo) && ValidationUtil.checkingCarInfo(carInfo)) {
                    driverService.addDriverByAdmin(driverInfo, carInfo);
                    break outer;
                } else {
                    System.out.println("wrong info try again ");
                }
            }
        }

    private static void passengerSignUpOrLogin() throws Exception {
        System.out.println("nationalCode:");
        String nationalCode = scanner.next();
        Passenger passenger = passengerService.findPassengerNationalCode(nationalCode);
        if (passenger != null) {
          //  System.out.println(passengerService.getAllPassengerInfo(passenger));
            int choiceNumber;
            do {
                PassengerLoginMenu.showPassengerLoginMenu();
                String choice = getChoiceNumber();
                choiceNumber = Integer.parseInt(choice);
                switch (choiceNumber) {
                    case 1:
                        if (passenger.getStatus() == TripStatus.STOP) {
                            Double[] point = getOriginDestination();
                            double originLat = point[0];
                            double originLong = point[1];
                            double destinationLat = point[2];
                            double destinationLong = point[3];
                            tripService.createTrip(passenger,originLat,originLong,destinationLat,destinationLong,PayStatus.CASH);
                        }
                        break;
                    case 2:
                        if (passenger.getStatus() == TripStatus.STOP) {
                            Double[] point = getOriginDestination();
                            double originLat = point[0];
                            double originLong = point[1];
                            double destinationLat = point[2];
                            double destinationLong = point[3];
                            int tripPrice = tripService.calculateTripPrice(originLat, originLong, destinationLat, destinationLong);
                            if (passenger.getBalance() >tripPrice) {
                                tripService.createTrip(passenger, originLat, originLong, destinationLat, destinationLong, PayStatus.ACCOUNT);
                                passengerService.decreasePassengerBalance(passenger,tripPrice);
                            }else {
                                System.out.println("Your balance is not enough!");
                                do {
                                    System.out.println("1. Increase account balance");
                                    System.out.println("2. Exit");
                                    choice = getChoiceNumber();
                                    choiceNumber = Integer.parseInt(choice);
                                } while (choiceNumber == 2);
                                switch (choiceNumber) {
                                    case 1:
                                        increasePassengerBalance(passenger);
                                        break;
                                    case 2:
                                        break;
                                    default:
                                        System.out.println("Invalid value");
                                }
                            }
                        }
                        break;
                    case 3:
                        increasePassengerBalance(passenger);
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid number!");
                }
            } while (choiceNumber != 4);
        } else {
            System.out.println("Username " + nationalCode + " doesn't exist, Register or Exit");
            int choiceNumber;
            do {
                SignupMenu.showSignupMenu();
                String choice = getChoiceNumber();
                choiceNumber = Integer.parseInt(choice);
                switch (choiceNumber) {
                    case 1:
                        passengerRegister();
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Invalid number!");
                }
            } while (choiceNumber != 2);
        }
    }

    private static Double[] getDriverLocation() {
        Double[] point = new Double[2];
        String latitude;
        do {
            System.out.println("Enter your location latitude:");
            latitude = scanner.next();
        } while (!ValidationUtil.isDouble(latitude));
        point[0] = Double.parseDouble(latitude);
        String longitude;
        do {
            System.out.println("Enter your location longitude:");
            longitude = scanner.next();
        } while (!ValidationUtil.isDouble(longitude));
        point[1] = Double.parseDouble(longitude);
        return point;
    }

    private static Double[] getOriginDestination() throws SQLException, ClassNotFoundException {
        System.out.println("Enter the origin and destination of your travel:");
        Double[] point = new Double[4];
        String originLat;
        do {
            System.out.println("Origin latitude:");
            originLat = scanner.next();
        } while (!ValidationUtil.isDouble(originLat));
        point[0] = Double.parseDouble(originLat);
        String originLong;
        do {
            System.out.println("Origin longitude:");
            originLong = scanner.next();
        } while (!ValidationUtil.isDouble(originLong));
        point[1] = Double.parseDouble(originLong);
        String destinationLat;
        do {
            System.out.println("Destination latitude:");
            destinationLat = scanner.next();
        } while (!ValidationUtil.isDouble(destinationLat));
        point[2] = Double.parseDouble(destinationLat);
        String destinationLong;
        do {
            System.out.println("Destination longitude:");
            destinationLong = scanner.next();
        } while (!ValidationUtil.isDouble(destinationLong));
        point[3] = Double.parseDouble(destinationLong);
        return point;
    }

    private static void increasePassengerBalance(Passenger passenger) {
        String amount;
        do {
            System.out.println("Enter amount in RIAL:");
            amount = scanner.next();
        } while (!ValidationUtil.isNumeric(amount));
        int amountNumber = Integer.parseInt(amount);
        passengerService.increasePassengerBalance(passenger,amountNumber);
        System.out.println("Your balance increased.");
    }

    private static void passengerRegister(){
        outer:
        while (true) {
            System.out.println("Enter passenger information like this : name,family,username,phoneNumber,nationalCode");
            String passengerinfo = scanner.next();

            if (!ValidationUtil.checkingPersonInfo(passengerinfo)) {
                passengerService.addPassengerByAdmin(passengerinfo);
                break outer;
            } else {
                System.out.println("wrong info try again ");
            }
        }
        System.out.println("Your information was successfully registered.");
    }

    private static String getChoiceNumber() {
        String choice;
        do {
            choice = scanner.next();
        } while (!ValidationUtil.isNumeric(choice));
        return choice;
    }

    private static String getCarPlaqueFromInput() {
        String plaque;
        do {
            System.out.println("Enter driver car plate number:");
            plaque = scanner.next();
        } while (!ValidationUtil.isAlphabetic(plaque));
        return plaque;
    }

    private static Date getBirthDateFromInput() {
        String date;
        do {
            System.out.println("Enter birth date like 1370-02-12:");
            date = scanner.next();
        } while (!ValidationUtil.isValidFormatDate(date));
        Date birthDate = Date.valueOf(date);//converting string into sql date
        return birthDate;
    }

    private static Date getTripDateFromInput() {
        String date;
        do {
            System.out.println("Enter trip date like 1400-07-01:");
            date = scanner.next();
        } while (!ValidationUtil.isValidFormatDate(date));
        Date TripDate = Date.valueOf(date);//converting string into sql date
        return TripDate;
    }

    private static String getNationalCodeFromInput() {
        String nationalCode;
        do {
            System.out.println("Enter national code:");
            nationalCode = scanner.next();
        } while (!ValidationUtil.isNumeric(nationalCode) && !ValidationUtil.isIranianNationalCode(nationalCode));
        return nationalCode;
    }

    private static String getPhoneNumberFromInput() {
        String phoneNumber;
        do {
            System.out.println("Enter phone number:");
            phoneNumber = scanner.next();
        } while (!ValidationUtil.isValidPhoneNumber(phoneNumber));
        return phoneNumber;
    }

    private static String getUsernameFromInput() {
        String username;
        do {
            System.out.println("Enter username:\nUsername must be longer than 4 character.");
            username = scanner.next();
        } while (!ValidationUtil.isValidUsername(username));
        return username;
    }

    private static String getFamilyFromInput() {
        String family;
        do {
            System.out.println("Enter family:");
            family = scanner.next();
        } while (!ValidationUtil.isLetter(family));
        return family;
    }

    private static String getNameFromInput() {
        String name;
        do {
            System.out.println("Enter name:");
            name = scanner.next();
        } while (!ValidationUtil.isLetter(name));
        return name;
    }

    private static void chooseVehicleByPassenger() {
        Vehicle.showVehicleMenu();
        String choice = getChoiceNumber();
        int choiceNumber = Integer.parseInt(choice);
        switch (choiceNumber) {
            case 1:
                System.out.println("Car");
                //TODO
                break;
            case 2:
                System.out.println("Motor cycle");
                //TODO
                break;
            case 3:
                System.out.println("Van");
                //TODO
                break;
            case 4:
                System.out.println("RV");
                //TODO
                break;
            default:
                System.out.println("Invalid value!");
        }
    }

}
