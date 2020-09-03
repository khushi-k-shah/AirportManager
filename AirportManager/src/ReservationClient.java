import javax.naming.InvalidNameException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ReservationClient {
    //creating the client socket
    //same thing as countdown client. creating a socket out of the server port
    //UI, main method
    //1.create socket, 2.get response from client or response listener 3. write to client socket
    private static Passenger passenger;

    public static boolean validNumber(String number) {
        boolean b = true;
        for (int i = 0; i < number.length(); i++) {
            if (Character.isDigit(number.charAt(i))) {
                int characterNum = Integer.valueOf(number.charAt(i));
                if (characterNum < 0) {
                    b = false;
                }
            } else {
                b = false;
            }
        }
        return b;
    }

    public static void main(String[] args) {
        String toPort = "";
        String hostname = "";
        String portString;
        int port;
        Socket socket;
        BufferedWriter socketWriter = null;
        BufferedReader socketReader = null;
        try {
            hostname = JOptionPane.showInputDialog(null, "What is the host you would like " +
                            "to connect to?",
                    "Host?", JOptionPane.QUESTION_MESSAGE);
            if (hostname == null) {
                throw new InvalidNameException();
            }
            toPort = JOptionPane.showInputDialog(null, "What is the port " +
                    "number you would like to connect to?", "Port?", JOptionPane.QUESTION_MESSAGE);
            if (toPort == null || !validNumber(toPort)) { // checks if the port is a non negative number
                throw new InvalidNameException();
            }
            port = Integer.valueOf(toPort);
            socket = new Socket(hostname, port);
            ResponseListener rl = new ResponseListener(socket);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    rl.stageTwo();
                }
            });
        } catch (InvalidNameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ResponseListener extends JFrame {
        private static ArrayList<String> airlines;
        private static int readPassengers = 0;
        private static int debugcount = 0;
        private static Passenger passengerObject;
        private static Airline airlineObject;
        private static String titleForFrame = "Purdue University Flight Reservation System";
        private static Gate gate;
        private static String[] passengers = new String[0];
        private static String firstName;
        private static String lastName;
        private static String age;
        private static String airlineString;
        private static Socket socket;
        private static BufferedWriter socketWriter = null;
        private static BufferedReader socketReader = null;
        private static JScrollPane scrollPane = new JScrollPane();
        private static int maxCapacityNumber = 0;

        public ResponseListener(Socket socket) {
            this.socket = socket;

            try {
                socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String s = "";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public static void stageTwo() {
            JFrame frame = new JFrame(titleForFrame);
            //frame.setSize(new Dimension(580, 580));
            frame.setSize(580, 580);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel jp1 = new JPanel();
            JPanel jp2 = new JPanel();
            jp2.setPreferredSize(new Dimension(500, 500));
            JPanel jp3 = new JPanel();
            JLabel label = new JLabel();
            JLabel imageLabel = new JLabel();
            label.setText("Welcome to the Purdue University Airline Reservation Management System!");
            Font f = label.getFont();
            label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            jp1.add(label);
            imageLabel.setIcon(new ImageIcon("purduePic.png"));
            jp2.add(imageLabel);
            JButton b1 = new JButton("Exit");
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    thankYouScreen();
                    frame.dispose();
                }
            });

            JButton b2 = new JButton("Book a Flight");
            b2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    frame.dispose();
                    stageThree();
                }
            });

            jp3.setLayout(new FlowLayout(FlowLayout.CENTER));
            jp3.add(b1);
            jp3.add(b2);
            frame.add(jp1, BorderLayout.NORTH);
            frame.add(jp2, BorderLayout.CENTER);
            frame.add(jp3, BorderLayout.SOUTH);
            frame.setVisible(true);
        }

        public static void stageThree() {
            JFrame frame = new JFrame(titleForFrame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(580, 580));
            JPanel panelOne = new JPanel();
            JPanel panelTwo = new JPanel();
            JButton exitButton = new JButton("Exit");
            JButton confirmButton = new JButton("Yes, I want to book a flight");
            JLabel heading = new JLabel();
            heading.setText("Do you want to book a flight today?");
            Font f = heading.getFont();
            heading.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            panelOne.add(heading);
            panelTwo.setLayout(new FlowLayout(FlowLayout.CENTER));
            panelTwo.add(exitButton);
            panelTwo.add(confirmButton);
            frame.add(panelOne, BorderLayout.NORTH);
            frame.add(panelTwo, BorderLayout.SOUTH);
            frame.setVisible(true);

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    thankYouScreen();
                    System.out.println("going to dispose");
                    frame.dispose();
                }
            });
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    frame.dispose();
                    stageFour();
                }
            });
        }

        public static void stageFour() {
            JFrame flightSelectionFrame = new JFrame(titleForFrame);
            flightSelectionFrame.setSize(new Dimension(650, 650));
            flightSelectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panelOne = new JPanel();
            JPanel panelTwo = new JPanel();
            JPanel panelThreeForDescription = new JPanel();
            JPanel panelForComboBox = new JPanel();
            JLabel instructions = new JLabel();
            instructions.setText("Choose a flight from the drop the drop down menu");
            Font f = instructions.getFont();
            instructions.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            JButton exitButton = new JButton("Exit");
            JButton chooseButton = new JButton("Choose this flight");

            JComboBox<String> airlineComboBox = new JComboBox<>(availableAirlines());
            airlineComboBox.setSelectedIndex(1);
            JLabel description = new JLabel();
            airlineObject = new Delta();
            description.setText("<html><center><font size=”6”>Delta Airlines is proud to be one of the five " +
                    "premier Airlines atPurdue University. <p>We are extremely offer exceptional services, with " +
                    "free limited WiFi for all customers. <p>Passengers who use T-Mobile as a cell phone carrier " +
                    "get additional benefits. <p>We are also happy to offer power outlets in each seat for " +
                    "passenger use. <p>We hope you choose to fly Delta as your next Airline. </font></center></html>");
            flightSelectionFrame.validate();
            airlineComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (airlineComboBox.getSelectedItem().equals("Alaska")) {
                        airlineObject = new Alaska();
                        description.setText("<html><center><font size=”6”>Alaska Airlines is proud to serve the " +
                                "strong and knowledgeable Boilermakers from Purdue University. <p>We primarily fly " +
                                "westward, and often have stops in Alaska and California. <p>We have first class " +
                                "amenities, even in coach class. <p> We provide fun snacks, such as pretzels and " +
                                "goldfish. <p>We also have comfortable seats, and free WiFi. <p>We hope you choose " +
                                "Alaska Airlines for your next itinerary. </font></center></html>");
                    } else if (airlineComboBox.getSelectedItem().equals("Delta")) {
                        airlineObject = new Delta();
                        description.setText("<html><center><font size=”6”>Delta Airlines is proud to be one of the " +
                                "five premier Airlines atPurdue University. <p>We are extremely offer exceptional " +
                                "services, with free limited WiFi for all customers. <p> Passengers who use " +
                                "T-Mobile as a cell phone carrier get additional benefits. <p>We are also happy " +
                                "to offer power outlets in each seat for passenger use. <p>We hope you choose to " +
                                "fly Delta as your next Airline. </font></center></html>");
                    } else {
                        airlineObject = new Southwest();
                        description.setText("<html><center><font size=”6”>Southwest Airlines is proud of offer " +
                                "flights to Purdue University. <p>We are happy to offer free in flight wifi, as " +
                                "well as our amazing snacks. <p>In addition we offer flights for much cheaper than " +
                                "other airlines, and offer two free checked bags. <p>We hope you choose " +
                                "Southwest for your next flight.</font></center></html>");
                    }
                    flightSelectionFrame.validate();
                }
            });
            panelForComboBox.setLayout(new BoxLayout(panelForComboBox, BoxLayout.Y_AXIS));
            panelForComboBox.add(airlineComboBox);
            panelForComboBox.add(panelThreeForDescription);
            panelThreeForDescription.add(description);
            panelOne.add(instructions, BorderLayout.NORTH);
            panelTwo.add(exitButton);
            panelTwo.add(chooseButton);
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flightSelectionFrame.setVisible(false);
                    thankYouScreen();
                    flightSelectionFrame.dispose();
                }
            });
            flightSelectionFrame.add(panelOne, BorderLayout.PAGE_START);
            flightSelectionFrame.add(panelForComboBox);
            flightSelectionFrame.add(panelTwo, BorderLayout.PAGE_END);
            flightSelectionFrame.setVisible(true);
            chooseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    flightSelectionFrame.setVisible(false);
                    flightSelectionFrame.dispose();
                    stageFive();
                }
            });
            airlineComboBox.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
                        passengerList(airlineObject);
                    }
                }
            });
        }

        public static String[] availableAirlines() { // use this method for DropDownMenu in ClientSideGUI
            ArrayList<String> arry = new ArrayList<String>();
            if (readPassengers == 0) {
                try {
                    int n = 0;
                    while (n < 3) {
                        String line = socketReader.readLine(); //should read in order: alaska, delta, southwest
                        arry.add(line);
                        n++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                airlines = new ArrayList<>();

                if (Integer.parseInt(arry.get(0)) < 100) { //alaska
                    airlines.add("Alaska");
                }
                if (Integer.parseInt(arry.get(1)) < 200) { //delta
                    airlines.add("Delta");
                }
                if (Integer.parseInt(arry.get(2)) < 100) { //southwest
                    airlines.add("Southwest");
                }
                readPassengers++;
            }
            Object[] array = airlines.toArray();
            return Arrays.copyOf(array, array.length, String[].class);
        }


        public static void passengerList(Airline airline) {
            if (airline != null) {
                try {
                    socketWriter.write(airline.getAirline());
                    System.out.println("written to socket");
                    socketWriter.newLine();
                    socketWriter.flush();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            System.out.println(airline + "in passenger list");
            ArrayList<String> arry = new ArrayList<String>();
            int maxPassengers = 0;
            try {
                maxPassengers = Integer.parseInt(socketReader.readLine());
                int count = 0;
                while (count < maxPassengers) {
                    String line = socketReader.readLine();
                    System.out.println(line + "passenger list method");
                    if (line == null) {
                        break;
                    }
                    arry.add(line);
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(300, 300));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //ArrayList<String> passengerList = new ArrayList<String>();
            //passengers = new String[0];
            JPanel header = new JPanel();
            JLabel airlineName = new JLabel();
            Font f = airlineName.getFont();
            airlineName.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            JLabel passengersLeft = new JLabel();

            if (airline instanceof Alaska) { //read from file instead of passenger list
                String s = String.format("%d : 100", maxPassengers);
                passengersLeft.setText(s);
                airlineName.setText("Alaska Airlines.");
                passengers = new String[arry.size()]; //current passengers
                int count = 0;
                for (String pass : arry) {
                    passengers[count] = pass;
                    count++;
                }
            } else if (airline instanceof Delta) { //read from file instead of passenger list
                String s = String.format("%d : 200", maxPassengers);
                passengersLeft.setText(s);
                airlineName.setText("Delta Airlines.");
                passengers = new String[arry.size()]; //current passengers
                int count = 0;
                for (String pass : arry) {
                    passengers[count] = pass;
                    count++;
                }
            } else if (airline instanceof Southwest) { //read from file instead of passenger list
                String s = String.format("%d : 100", maxPassengers);
                passengersLeft.setText(s);
                airlineName.setText("Southwest Airlines.");
                passengers = new String[arry.size()]; //current passengers
                int count = 0;
                for (String pass : arry) {
                    passengers[count] = pass;
                    count++;
                }
            }
            header.add(airlineName, BorderLayout.EAST);
            header.add(passengersLeft, BorderLayout.WEST);
            JPanel panelForScroll = new JPanel();
            JPanel panelForButton = new JPanel();
            JPanel panelForHeader = new JPanel();
            panelForHeader.add(header, BorderLayout.NORTH);
            JButton exitButton = new JButton("Exit");
            JList listOfPassengers = new JList<String>(passengers);
            JScrollPane scrollPaneForFrame = new JScrollPane(listOfPassengers);
            scrollPaneForFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPaneForFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            panelForButton.add(exitButton, BorderLayout.CENTER);
            panelForScroll.add(scrollPaneForFrame);

            frame.pack();
            frame.add(panelForHeader, BorderLayout.NORTH);
            frame.getContentPane().add(panelForScroll, BorderLayout.CENTER);
            frame.add(panelForButton, BorderLayout.SOUTH);
            frame.setVisible(true);

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                }
            });

            KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
            Action escapeAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            };
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    escapeKeyStroke, "ESCAPE");
            frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);

        }

        public static void stageFive() {
            JFrame frame = new JFrame(titleForFrame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(580, 580));
            JPanel panelForLabelNorth = new JPanel();
            JPanel panelForButtonsSouth = new JPanel();

            JLabel label = new JLabel();
            label.setText("Are you sure that you want to book a flight on " + airlineObject.getAirline() +
                    " Airlines ?");
            Font f = label.getFont();
            label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            JButton exitButton = new JButton("Exit");
            JButton noButton = new JButton("No, I want a different flight.");
            JButton yesButton = new JButton("Yes, I want this flight.");

            panelForLabelNorth.add(label);
            panelForButtonsSouth.setLayout(new FlowLayout(FlowLayout.CENTER));
            panelForButtonsSouth.add(exitButton);
            panelForButtonsSouth.add(noButton);
            panelForButtonsSouth.add(yesButton);

            frame.add(panelForLabelNorth, BorderLayout.NORTH);
            frame.add(panelForButtonsSouth, BorderLayout.SOUTH);
            frame.setVisible(true);

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    thankYouScreen();
                    frame.dispose();
                }
            });

            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // send info to server
                    gate = new Gate(airlineObject);
                    frame.setVisible(false);
                    frame.dispose();
                    stageSix();
                    //stageEight();
                }
            });

            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    stageFour();
                }
            });

            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        //cursor needs to go to next textfield
                    }
                }
            });
        }

        public static void stageSix() {
            JFrame frame = new JFrame(titleForFrame);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(580, 580));
            JPanel panelForInstructions = new JPanel();
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel instructions = new JLabel();
            instructions.setText("Please input your information below.");
            panelForInstructions.add(instructions);
            frame.add(panelForInstructions, BorderLayout.NORTH);

            JLabel labelFirstName = new JLabel();
            labelFirstName.setText("What is your first name?");
            JTextField textFirstName = new JTextField();
            textFirstName.setSize(new Dimension(580, 125));
            panel.add(labelFirstName);
            panel.add(textFirstName);

            JLabel labelLastName = new JLabel();
            labelLastName.setText("What is your last name?");
            JTextField textLastName = new JTextField();
            textLastName.setSize(new Dimension(580, 125));
            panel.add(labelLastName);
            panel.add(textLastName);

            JLabel labelAge = new JLabel();
            labelAge.setText("What is your age?");
            JTextField textAge = new JTextField();
            textAge.setSize(new Dimension(580, 125));
            panel.add(labelAge);
            panel.add(textAge);

            JPanel panelButtons = new JPanel();
            JButton exitButton = new JButton("Exit");
            JButton nextButton = new JButton("Next");
            panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
            panelButtons.add(exitButton);
            panelButtons.add(nextButton);
            frame.add(panelButtons, BorderLayout.SOUTH);

            frame.add(panel);
            frame.setVisible(true);

            //take all information and store it
            //when presses enter
            textFirstName.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        firstName = textFirstName.getText();
                        textLastName.requestFocus();
                    }
                }
            });

            textLastName.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        lastName = textLastName.getText();
                        textAge.requestFocus();
                    }
                }
            });

            textAge.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        age = textAge.getText();
                        textAge.requestFocus();
                    }
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    thankYouScreen();
                    frame.dispose();
                }
            });

            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        //if (firstName == null || lastName == null || age == null){
                        firstName = textFirstName.getText();
                        lastName = textLastName.getText();
                        age = textAge.getText();
                        //}
                        if (checkName(firstName) && checkName(lastName) && checkAge(age)) {
                            passengerObject = new Passenger(firstName, lastName, Integer.parseInt(age),
                                    airlineObject.getAirline());
                            stageSeven();
                        } else {
                            if (firstName == null || !checkName(firstName)) {
                                //error message for name
                                JOptionPane.showMessageDialog(null,
                                        "Please enter a valid name without special characters " +
                                                "(aside from dashes).",
                                        titleForFrame, JOptionPane.ERROR_MESSAGE, null);
                                frame.setVisible(false);
                                firstName = null;
                                stageSix();
                            } else if (lastName == null || !checkName(lastName)) {
                                JOptionPane.showMessageDialog(null,
                                        "Please enter a valid name without special characters " +
                                                "(aside from dashes).",
                                        titleForFrame, JOptionPane.ERROR_MESSAGE, null);
                                lastName = null;
                                stageSix();
                            } else if (age == null || !checkAge(age)) {
                                JOptionPane.showMessageDialog(null,
                                        "Please enter a valid age without special characters.",
                                        titleForFrame, JOptionPane.ERROR_MESSAGE, null);
                                age = null;
                                stageSix();
                            }
                        }
                        frame.setVisible(false);
                        frame.dispose();
                    } catch (NullPointerException excep) {
                        excep.printStackTrace();
                    }
                }
            });
        }


        public static boolean checkName(String string) {
            try {
                if (string == null || string.equals("")) {
                    return false;
                } else {
                    for (int i = 0; i < string.length(); i++) {
                        if (!(string.charAt(i) >= 65 && string.charAt(i) <= 90) &&
                                !(string.charAt(i) >= 97 && string.charAt(i) <= 122)
                                && (string.charAt(i) != 45)
                                && (string.charAt(i) != 32)) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public static boolean checkAge(String string) {
            try {
                if (string == null || string.equals("")) {
                    return false;
                } else {
                    for (int i = 0; i < string.length(); i++) {
                        if (string.charAt(i) != '0' && string.charAt(i) != '1' && string.charAt(i) != '2' &&
                                string.charAt(i) != '3' && string.charAt(i) != '4' && string.charAt(i) != '5' &&
                                string.charAt(i) != '6' && string.charAt(i) != '7' && string.charAt(i) != '8' &&
                                string.charAt(i) != '9' && string.charAt(i) != ' ') {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public static void stageSeven() {
            String message = "Are all the details you entered correct? \n The passenger's name is " +
                    passengerObject.getFirstName() + " " + passengerObject.getLastName() + " and their age is " +
                    passengerObject.getAge() + ". \n If all the information shown is correct, select the Yes button " +
                    "below, otherwise, select the No button";
            int selection = JOptionPane.showConfirmDialog(null, message, "Confirm Info",
                    JOptionPane.YES_NO_OPTION);
            if (selection == 0) {
                stageEight();
            } else {
                stageSix();
            }
        }

        public static void stageEight() {
            if (airlineObject.getAirline().equals("Alaska")) {
                maxCapacityNumber = 100;
            } else if (airlineObject.getAirline().equals("Southwest")) {
                maxCapacityNumber = 100;
            } else if (airlineObject.getAirline().equals("Delta")) {
                maxCapacityNumber = 200;
            }
            //implement JScroll, passenger list, updated passenger list, and return the new passenger list
            String[] passengerArray = new String[0];
            if (airlineObject != null) {
                try {
                    socketWriter.write(airlineObject.getAirline());
                    socketWriter.newLine();
                    socketWriter.flush();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            int maxPassengers = 0;
            try {
                maxPassengers = Integer.parseInt(socketReader.readLine());
                passengerArray = new String[maxPassengers];
                int count = 0;
                while (count < maxPassengers) {
                    String line = socketReader.readLine();
                    System.out.println(line + "passenger list method");
                    passengerArray[count] = line;
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            BoardingPass newPass = new BoardingPass(passengerObject, gate, airlineObject);
            newPass.addPassenger(passengerObject);
            JFrame frame = new JFrame(titleForFrame);
            //frame.setLayout(new BoxLayout());
            frame.setSize(new Dimension(580, 580));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());
            JPanel infoPanel = new JPanel();
            JLabel infoLabel = new JLabel();
            String passengerRatio = String.format("%d:%d", maxPassengers, maxCapacityNumber);
            infoLabel.setText("<html><center><font size=”20”> Flight data displaying for " +
                    airlineObject.getAirline() +
                    " Airlines <p> Enjoy your flight! <p> Flight is now boarding " +
                    "at Gate " + gate.toString() + " <p> " + passengerRatio);
            Font f = infoLabel.getFont();
            infoLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            infoPanel.add(infoLabel);
            JPanel scrollPanel = new JPanel();
            scrollPanel.setLayout(new BorderLayout());
            scrollPanel.setSize(200, 200);
            JList<String> listOfPassengers = new JList<String>(passengerArray);
            scrollPane = new JScrollPane(listOfPassengers);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPanel.add(scrollPane, BorderLayout.NORTH);
            JPanel boardingPassPanel = new JPanel();
            JLabel boardingPassLabel = new JLabel();
            boardingPassLabel.setText(newPass.toString());
            boardingPassPanel.add(boardingPassLabel);
            scrollPanel.add(boardingPassPanel, BorderLayout.SOUTH);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JButton exitButton = new JButton("Exit");
            JButton refreshButton = new JButton("Refresh Flight Status");
            buttonPanel.add(exitButton);
            buttonPanel.add(refreshButton);
            frame.add(infoPanel, BorderLayout.NORTH);
            frame.add(scrollPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    thankYouScreen();
                    frame.dispose();
                }
            });

            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (debugcount == 0) {
                        String[] newpassengerArray = new String[0];
                        int maxPassengers = 0;
                        if (passengerObject != null) {
                            try {
                                socketWriter.write(passengerObject.toString());
                                socketWriter.newLine();
                                System.out.println(passengerObject.toString() + "in refresh button");
                                socketWriter.write(airlineObject.getAirline());
                                System.out.println(airlineObject.getAirline() + "in refresh button");
                                socketWriter.newLine();
                                socketWriter.flush();
                                maxPassengers = Integer.parseInt(socketReader.readLine());
                                newpassengerArray = new String[maxPassengers];
                                int count = 0;
                                while (count < maxPassengers) {
                                    String line = socketReader.readLine();
                                    System.out.println(line + " new passenger list");
                                    newpassengerArray[count] = line;
                                    count++;
                                }

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            String passengerRatio = String.format("%d:%d", maxPassengers, maxCapacityNumber);
                            infoLabel.setText("<html><center><font size=”20”> Flight data displaying for " +
                                    airlineObject.getAirline() +
                                    " Airlines <p> Enjoy your flight! <p> Flight is now boarding " +
                                    "at Gate " + gate.toString() + " <p> " + passengerRatio);
                            scrollPanel.removeAll();
                            JList<String> newlistOfPassengers = new JList<String>(newpassengerArray);
                            scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
                            scrollPanel.setSize(200, 200);
                            JScrollPane newScroll = new JScrollPane(newlistOfPassengers);
                            newScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                            newScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                            scrollPanel.add(newScroll);
                            scrollPanel.add(boardingPassPanel);
                            scrollPanel.revalidate();
                            scrollPanel.repaint();
                            frame.validate();
                        }
                    }
                    debugcount++;
                }
            });
        }


        public static void thankYouScreen() {
            JOptionPane.showMessageDialog(null,
                    "Thank you for using the Purdue University Airline Management System!",
                    "Thank you!", JOptionPane.INFORMATION_MESSAGE, null);
            try {
                socketWriter.close();
                socketReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
