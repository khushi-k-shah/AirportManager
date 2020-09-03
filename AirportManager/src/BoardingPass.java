import java.io.Serializable;

public class BoardingPass implements Serializable {
    private Passenger passengerForBoardingPass;
    private Gate gate;
    private Airline airline;

    public BoardingPass(Passenger passenger, Gate gate, Airline airline) {
        this.passengerForBoardingPass = passenger;
        this.gate = gate;
        this.airline = airline;
    }

    public String toString() {
        return "<html><center><font size=”6”>-------------------------------------------------------------<p>" +
                "BOARDING PASS FOR FLIGHT 18000 WITH " + this.airline.getAirline() + " Airlines <p>PASSENGER " +
                "FIRST NAME: " +
                this.passengerForBoardingPass.getFirstName() + "<p>PASSENGER LAST NAME: " +
                this.passengerForBoardingPass.getLastName() +
                "<p>PASSENGER AGE: " + this.passengerForBoardingPass.getAge() + "<p>You can now begin boarding at " +
                "gate " + this.gate.toString() + "<p>-------------------------------------------------------------" +
                "</font></center></html>";
    }


    public void addPassenger(Passenger passenger) {
        String passengerString = passenger.toString();
        if (this.airline instanceof Alaska) {
            Alaska.getPassengers().add(passengerString);
        } else if (this.airline instanceof Southwest) {
            Southwest.getPassengers().add(passengerString);
        } else {
            Delta.getPassengers().add(passengerString);
        }
    }
}
