import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Gate implements Serializable {
    private String terminal;
    private int gateNum;
    private Airline airline;

    public Gate(Airline airline) {
        this.airline = airline;
        Random random = new Random();

        if (this.airline instanceof Alaska) {
            if (Alaska.getPassengers().size() == 0) { // checks if gate is full
                terminal = "A";
                gateNum = random.nextInt(18) + 1;
                Alaska.setGate(new Gate(terminal, gateNum));
            } else if (Alaska.getPassengers().size() == 100) {
                Alaska.setGate(null);
            } else { // if not full, using the same gate
                terminal = Alaska.getGate().getTerminal(); // returns gate
                gateNum = Alaska.getGate().getGateNum();
            }
        } else if (this.airline instanceof Southwest) {
            if (Southwest.getPassengers().size() == 0) {
                terminal = "B";
                gateNum = random.nextInt(18) + 1;
                Southwest.setGate(new Gate(terminal, gateNum));
            } else if (Southwest.getPassengers().size() == 100) {
                Southwest.setGate(null);
            } else {
                terminal = Southwest.getGate().getTerminal();
                gateNum = Southwest.getGate().getGateNum();
            }
        } else {
            if (Delta.getPassengers().size() == 0) { // checks if gate is full
                terminal = "C";
                gateNum = random.nextInt(18) + 1;
                Delta.setGate(new Gate(terminal, gateNum));
            } else if (Delta.getPassengers().size() == 200) {
                Delta.setGate(null);
            } else { // if not full, using the same gate
                terminal = Delta.getGate().getTerminal(); // returns gate
                gateNum = Delta.getGate().getGateNum();
            }
        }
    }

    public Gate() {
    }

    public Gate(String terminal, int gateNum) {
        this.terminal = terminal;
        this.gateNum = gateNum;
    }

    public String getTerminal() {
        return this.terminal;
    }

    public int getGateNum() {
        return this.gateNum;
    }

    public String toString() {
        return terminal + gateNum;
    }

}
