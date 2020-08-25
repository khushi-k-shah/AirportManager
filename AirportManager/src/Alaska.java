import java.util.ArrayList;

/**
 * Project 5 - Airport Manager
 *
 * @author Khushi Shah, Natalie Renee Evoniuk, Lab section g20
 * @version 12/3/19
 */
public class Alaska implements Airline {
    private static ArrayList<String> passengers = new ArrayList<>();
    private static Gate gate;
    private static String alaskaDescription = "Alaska Airlines is proud to serve the strong and knowledgeable " +
            "Boilermakers " +
            "from Purdue University. We primarily fly westward, and often have stops in Alaska and California. " +
            "We have first class amenities, even in coach class. We provide fun snacks, such as pretzels and " +
            "goldfish. We also have comfortable seats, and free WiFi. We hope you choose Alaska Airlines for your" +
            " next itinerary.";


    public String getAirline() {
        return "Alaska";
    }

    public static ArrayList<String> getPassengers() {
        return Alaska.passengers;
    }

    public static int getNumOfPassengers() {
        return Alaska.passengers.size();
    }

    public static void setGate(Gate gate) {
        Alaska.gate = gate;
    }

    public static Gate getGate() {
        return Alaska.gate;
    }

    public static String getAlaskaDescription() {
        return Alaska.alaskaDescription;
    }

}