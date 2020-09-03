import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReservationServer {
    //1. connect to client 2.run client handler
    private ServerSocket serverSocket;
    private static String hostName = "";
    private static InetAddress ip;

    public ReservationServer() throws IOException {
        this.serverSocket = new ServerSocket(0);
    }

    //serve the client side
    public void serveClients() {
        try {
            ip = InetAddress.getLocalHost();
            // hostName = ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Socket clientSocket;
        ClientHandler handler;
        Thread handlerThread;
        int portNumber = this.serverSocket.getLocalPort();
        System.out.printf("serving clients on port #%d", portNumber);
        try {
            clientSocket = this.serverSocket.accept();
            handler = new ClientHandler(clientSocket);
            handlerThread = new Thread(handler);
            handlerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReservationServer server;

        try {
            server = new ReservationServer();
        } catch (IOException e) {
            e.printStackTrace();

            return;
        } //end try catch

        server.serveClients();
    } //main

    
    public static class ClientHandler implements Runnable {
        private int count = 1;
        private Socket clientSocket;
        private static String person;
        private BufferedWriter socketWriter = null;
        private BufferedReader socketReader = null;
        private FileReader fr = null; //FILE IO
        private BufferedReader bfr = null;
        private FileWriter fw = null;
        private BufferedWriter bfw = null;
        private File f;
        private static String numOfPassOnPlane;

        public ClientHandler(Socket clientSocket) throws NullPointerException {
            if (clientSocket == null) {
                throw new NullPointerException();

            } else {
                this.clientSocket = clientSocket;
            }
        }

        public void run() {
            try {
                socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                f = new File("Reservations.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Airline plane = new Alaska();
                passengerListSize(plane);
                plane = new Delta();
                passengerListSize(plane);
                plane = new Southwest();
                passengerListSize(plane); //necessary for the drop down menu: need to check if each
                // airline has full passengers
                String value = " ";
                while (value != null) { //stage 4
                    value = socketReader.readLine();
                    System.out.println(value);
                    if (value != null) {
                        if (value.equals("Alaska")) {
                            plane = new Alaska();
                            passengerListSize(plane); //reads amount of passengers
                            readFile(plane); //reads passenger list
                        } else if (value.equals("Delta")) {
                            plane = new Delta();
                            passengerListSize(plane); //reads amount of passengers
                            readFile(plane); //reads passenger list
                        } else if (value.equals("Southwest")) {
                            plane = new Southwest();
                            passengerListSize(plane); //reads amount of passengers
                            readFile(plane); //reads passenger list
                        } else { //stage 8
                            if (count == 2) {
                                int doit = 0;
                            }
                            count++;
                            String airlinename = socketReader.readLine();
                            System.out.println(airlinename);
                            writeToFile(value, airlinename);
                            if (airlinename.equals("Alaska")) {
                                plane = new Alaska();
                            } else if (airlinename.equals("Delta")) {
                                plane = new Delta();
                            } else if (airlinename.equals("Southwest")) {
                                plane = new Southwest();
                            }
                            passengerListSize(plane);
                            readFile(plane);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void writeToFile(String p, String airlineName) {
            ArrayList<String> listOfPassengers = new ArrayList<String>();
            String passenger = p;
            fr = null;
            bfr = null;
            try {
                fr = new FileReader(f);
                bfr = new BufferedReader(fr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    String line = bfr.readLine();
                    if (line == null) {
                        break;
                    } else {
                        if (line.contains("ALASKA") && airlineName.equals("Alaska")) {
                            String lineNumberOfPassengers = bfr.readLine();
                            lineNumberOfPassengers = newNumberOfPassengers(lineNumberOfPassengers);
                            listOfPassengers.add(line);
                            listOfPassengers.add(lineNumberOfPassengers);
                            listOfPassengers.add(passenger);
                        } else if (line.contains("DELTA") && airlineName.equals("Delta")) {
                            String lineNumberOfPassengers = bfr.readLine();
                            lineNumberOfPassengers = newNumberOfPassengers(lineNumberOfPassengers);
                            listOfPassengers.add(line);
                            listOfPassengers.add(lineNumberOfPassengers);
                            listOfPassengers.add(passenger);
                        } else if (line.contains("SOUTHWEST") && airlineName.equals("Southwest")) {
                            String lineNumberOfPassengers = bfr.readLine();
                            lineNumberOfPassengers = newNumberOfPassengers(lineNumberOfPassengers);
                            listOfPassengers.add(line);
                            listOfPassengers.add(lineNumberOfPassengers);
                            listOfPassengers.add(passenger);
                        } else {
                            listOfPassengers.add(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fw = new FileWriter(f);
                bfw = new BufferedWriter(fw);
                for (String e : listOfPassengers) {
                    bfw.write(e);
                    bfw.newLine();
                    bfw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String newNumberOfPassengers(String line) {
            String s = "";
            String modify = "";
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '/') {
                    int m = Integer.valueOf(s) + 1;
                    modify = modify + line.substring(i, line.length());
                    s = String.valueOf(m) + modify;
                    break;
                } else if (Character.isDigit(c)) {
                    s = s + c;
                }
            }
            return s;
        }

        public void readFile(Airline airline) {
            ArrayList<String> passengers = new ArrayList<>();
            bfr = null;
            fr = null;
            try {
                fr = new FileReader(f);
                bfr = new BufferedReader(fr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String s = "";
                if (airline instanceof Alaska) {
                    while (true) {
                        s = bfr.readLine(); //airline name
                        if (s.equals("ALASKA")) {
                            break;
                        }
                    }
                    s = bfr.readLine(); //passenger size
                    s = bfr.readLine(); //first passenger
                    while (s != null && !s.equals("DELTA") && !s.equals("")) {
                        passengers.add(s);
                        s = bfr.readLine();
                    }

                } else if (airline instanceof Delta) {

                    while (true) {
                        s = bfr.readLine();
                        if (s.equals("DELTA")) {
                            break;
                        }
                    }
                    s = bfr.readLine();
                    s = bfr.readLine();
                    while (s != null && !s.equals("SOUTHWEST") && !s.equals("")) { //blows through this conidtion
                        passengers.add(s);
                        s = bfr.readLine();
                    }

                } else if (airline instanceof Southwest) {

                    while (true) {
                        s = bfr.readLine();
                        if (s.equals("SOUTHWEST")) {
                            break;
                        }
                    }
                    s = bfr.readLine();
                    s = bfr.readLine();
                    while (s != null && !s.equals("")) {
                        passengers.add(s);
                        s = bfr.readLine();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Object[] passengersArray = passengers.toArray();
            String[] passengersStringArray = Arrays.copyOf(passengersArray, passengersArray.length, String[].class);
            try {
                for (String s : passengersStringArray) {
                    socketWriter.write(s);
                    System.out.println(s + " in server");
                    socketWriter.newLine();
                    socketWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void passengerListSize(Airline airline) { //purpose: given an airline,
            // find the size and return it to the client socket
            try {
                fr = null;
                bfr = null;
                fr = new FileReader(f);
                bfr = new BufferedReader(fr);
                //bfr = new BufferedReader(new FileReader(new File("Reservations.txt")));
                String s = "";
                if (airline instanceof Alaska) {

                    while (true) {
                        s = bfr.readLine();
                        if (s.equals("ALASKA")) {
                            break;
                        }
                    }
                    s = bfr.readLine();

                } else if (airline instanceof Delta) {

                    while (true) {
                        s = bfr.readLine();
                        if (s.equals("DELTA")) {
                            break;
                        }
                    }
                    s = bfr.readLine();

                } else if (airline instanceof Southwest) {

                    while (true) {
                        s = bfr.readLine();
                        if (s.equals("SOUTHWEST")) {
                            break;
                        }
                    }
                    s = bfr.readLine();

                }
                String size = s.substring(0, s.indexOf("/"));
                System.out.println(size + airline.getAirline());
                socketWriter.write(size);
                socketWriter.newLine();
                socketWriter.flush();

                //flush the size to clients side
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

