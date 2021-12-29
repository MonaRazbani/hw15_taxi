package models;

import enumeration.TripStatus;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)// because we might have one person that is driver and passenger either
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String name;
    private String family;
    private String username;
    private String phoneNumber;
    private String nationalCode;
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && nationalCode == person.nationalCode && Objects.equals(username, person.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, nationalCode);
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                ", status=" + status +
                '}';
    }
}