import java.io.Serializable;

public class Passenger implements Serializable {
    private String firstName;
    private String lastName;
    private int age;
    private String airline;

    public Passenger(String firstName, String lastName, int age, String airline) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.airline = airline;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public int getAge() {
        return this.age;
    }

    public String toString() {
        return firstName.charAt(0) + ". " + this.lastName + ", " + this.age;
    }

    public String getAirline() {
        return this.airline;
    }

}


