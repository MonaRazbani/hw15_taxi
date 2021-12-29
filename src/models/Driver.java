package models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Driver extends Person{

    private String plaque;
    @ManyToOne(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    private Car car;
    private double currentLocationLat;
    private double currentLocationLong;
    @OneToMany (cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    private List<Trip> trips = new ArrayList<>();

    @Override
    public String toString() {
        return "Driver{" +super.toString()+
                "plaque='" + plaque + '\'' +
                ", car=" + car ;
    }
}
