package models;

import enumeration.PayStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Data
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Passenger passenger;
    @ManyToOne(cascade = CascadeType.ALL)
    private Driver driver;
    private double originLat;
    private double originLong;
    private double destinationLat;
    private double destinationLong;
    private int price;
    private Date tripDate;
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Double.compare(trip.originLat, originLat) == 0 && Double.compare(trip.originLong, originLong) == 0 && Double.compare(trip.destinationLat, destinationLat) == 0 && Double.compare(trip.destinationLong, destinationLong) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originLat, originLong, destinationLat, destinationLong);
    }
}
