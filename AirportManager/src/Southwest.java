import java.util.ArrayList;

/**
 * Project 5 - Airport Manager
 *
 * @author Khushi Shah, Natalie Renee Evoniuk, Lab section g20
 * @version 12/3/19
 */
public class Southwest implements Airline {
    private static ArrayList<String> passengers = new ArrayList<>();
    private static Gate gate;

    public static String southwestDescription = "Southwest Airlines is proud of offer flights to Purdue " +
            "University. We are " +
            "happy to offer free in flight wifi, as well as our amazing snacks. In addition we offer flights for " +
            "much cheaper than other airlines, and offer two free checked bags. We hope you choose Southwest for " +
            "your next flight.";

    public String getAirline() {
        return "Southwest";
    }

    public static ArrayList<String> getPassengers() {
        return Southwest.passengers;
    }

    public static int getNumOfPassengers() {
        return Southwest.passengers.size();
    }

    public static void setGate(Gate gate) {
        Southwest.gate = gate;
    }

    public static Gate getGate() {
        return Southwest.gate;
    }

    public static String getSouthwestDescription() {
        return Southwest.southwestDescription;
    }
}



