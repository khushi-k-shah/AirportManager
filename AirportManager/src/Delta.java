import java.util.ArrayList;

public class Delta implements Airline {
    private static ArrayList<String> passengers = new ArrayList<>();
    private static Gate gate;

    private static String deltaDescription = "Delta Airlines is proud to be one of the five premier Airlines " +
            "at Purdue " +
            "University. We are extremely offer exceptional services, with free limited WiFi for all customers. " +
            "Passengers who use T-Mobile as a cell phone carrier get additional benefits. We are also happy to " +
            "offer " +
            "power outlets in each seat for passenger use. We hope you choose to fly Delta as your next Airline.";

    public String getAirline() {
        return "Delta";
    }

    public static ArrayList<String> getPassengers() {
        return Delta.passengers;
    }

    public static int getNumOfPassengers() {
        return Delta.passengers.size();
    }

    public static void setGate(Gate gate) {
        Delta.gate = gate;
    }

    public static Gate getGate() {
        return Delta.gate;
    }

    public static String getDeltaDescription() {
        return Delta.deltaDescription;
    }
}
