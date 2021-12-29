package models;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity

public class Passenger extends Person {

    private int balance;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Trip> trips = new ArrayList<>();

    @Override
    public String toString() {
        return "Passenger{" +super.toString() +
                "balance=" + balance +
                '}';
    }
}
