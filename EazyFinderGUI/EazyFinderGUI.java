package EazyFinderGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EazyFinderGUI {
    JFrame frame = new JFrame();
    //    JPanel panel = new JPanel();
    JButton backButton, logoutButton = new JButton("Logout");
    JLabel msg; // Used to print corresponding messages
    Font timesNewRoman = new Font("Times New Roman", Font.BOLD, 15);
    short frameSize = 700;
    String dirname = System.getProperty("user.dir"); // Path upto src

    public static void main(String[] args) {
        new EazyFinderGUI().HomePage();
    }

    JButton loginButton = new JButton();
    JButton signupButton = new JButton();

    void HomePage() {
        frame.getContentPane().removeAll();
        frame.repaint();

        frame.setSize(350, 300);
        frame.setTitle("Home Page");
//        frame.add(panel);
        frame.setLayout(null);
//        panel.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        loginButton = new JButton("LogIn");
        signupButton = new JButton("SignUp");

        frame.add(loginButton);
        frame.add(signupButton);

        loginButton.setBounds(50, 103, 100, 30);
        loginButton.setBackground(Color.ORANGE);
        loginButton.setFont(timesNewRoman);
        loginButton.addActionListener(new LoginSignUpCommonCode("LogIn"));

        signupButton.setBounds(180, 103, 100, 30);
        signupButton.setBackground(Color.ORANGE);
        signupButton.setFont(timesNewRoman);
        signupButton.addActionListener(new LoginSignUpCommonCode("SignUp"));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class Back implements ActionListener {
        byte num;

        Back(byte num) {
            this.num = num;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (num == 1) {
                int result = JOptionPane.showConfirmDialog(frame, "Are You Sure?", "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    HomePage();
                }
            } else if (num == 2) {
                displayMenu();
            }
        }
    }

    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel rePasswordLabel = new JLabel("Re-Type Password:");
    JTextField userText = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JPasswordField rePasswordField = new JPasswordField();

    class LoginSignUpCommonCode implements ActionListener {
        String buttonName;

        LoginSignUpCommonCode(String buttonName) {
            this.buttonName = buttonName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();

            backButton = new JButton("Back");
            msg = new JLabel();

            frame.setTitle(buttonName);
//            frame.add(panel);
            frame.add(userLabel);
            frame.add(passwordLabel);
            frame.add(userText);
            frame.add(passwordField);
            frame.add(msg);
            frame.add(backButton);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            userLabel.setBounds(50, 50, 80, 25);
            userLabel.setFont(timesNewRoman);

            userText.setBounds(130, 50, 120, 25);
            userText.setFont(timesNewRoman);

            passwordLabel.setBounds(50, 80, 80, 25);
            passwordLabel.setFont(timesNewRoman);

            passwordField.setBounds(130, 80, 120, 25);

            userText.setText("");
            passwordField.setText("");
            rePasswordField.setText("");
            msg.setText("");

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 1));

            if (buttonName.equals("LogIn")) {
                loginButton = new JButton("LogIn");
                frame.add(loginButton);

                loginButton.setBounds(120, 120, 80, 25);
                loginButton.setBackground(Color.DARK_GRAY);
                loginButton.setForeground(Color.WHITE);
                loginButton.setFont(timesNewRoman);
                loginButton.addActionListener(new LogIn());

                msg.setBounds(0, 170, 350, 25);
                msg.setFont(timesNewRoman);
                msg.setHorizontalAlignment(0);
            } else {
                signupButton = new JButton("SignUp");
                frame.add(signupButton);

                frame.add(rePasswordLabel);
                frame.add(rePasswordField);

                rePasswordLabel.setBounds(0, 110, 130, 25);
                rePasswordLabel.setFont(timesNewRoman);
                rePasswordField.setBounds(130, 110, 120, 25);

                signupButton.setBounds(125, 150, 100, 25);
                signupButton.setBackground(Color.DARK_GRAY);
                signupButton.setForeground(Color.WHITE);
                signupButton.setFont(timesNewRoman);
                signupButton.addActionListener(new SignUp());

                msg.setBounds(0, 180, 350, 25);
                msg.setFont(timesNewRoman);
                msg.setHorizontalAlignment(JLabel.CENTER);
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    long encryptPassword(String password) {
        long encryptedPassword = 0, a = 1;
        short i, len = (short) password.length();
        for (i = 0; i < len; i++) {
            encryptedPassword += ((int) password.charAt(i)) * a;
            a *= 100;
        }
        return encryptedPassword;
    }

    String username;
    String password;
    File db = new File(dirname + "\\EazyFinderGUI\\LogInSignUpDatabase.txt");

    class LogIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            username = userText.getText();
            password = String.valueOf(passwordField.getPassword());
            if (username.equals("") || password.equals("")) {
                msg.setText("Please Fill all the Fields");
            } else {
                String str;
                String[] credentials;
                boolean found = false;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(db));
                    while ((str = reader.readLine()) != null) {
                        credentials = str.split(" ");
                        if (username.equals(credentials[0]) && String.valueOf(encryptPassword(password)).equals(credentials[1])) {
                            found = true;
                            break;
                        }
                    }
                    reader.close();
                } catch (Exception ex) {
                    msg.setText("Error in reading file");
                }
                if (!found) {
                    msg.setText("No User With Given Credentials");
                } else {
                    displayMenu();
                }
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    boolean isPasswordNotAccepted(String password) {
        boolean inRange = false, hasWhiteSpace = true, hasLowerCaseLetter = false, hasUpperCaseLetter = false, hasDigit = false,
                hasSpecialCharacter = false;
        byte i, len = (byte) password.length();
        byte maxPasswordLength = 16;
        if (len >= 8 && len <= maxPasswordLength) inRange = true;
        if (!password.contains(" ")) hasWhiteSpace = false;

        for (i = 0; i < len; i++) {
            if (Character.isAlphabetic(password.charAt(i))) {
                if (Character.isLowerCase(password.charAt(i))) hasLowerCaseLetter = true;
                else hasUpperCaseLetter = true;
            } else if (Character.isDigit(password.charAt(i))) hasDigit = true;
            else hasSpecialCharacter = true;
        }

        return !inRange || hasWhiteSpace || !hasLowerCaseLetter || !hasUpperCaseLetter || !hasDigit || !hasSpecialCharacter;
    }

    class SignUp implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            username = userText.getText();
            password = String.valueOf(passwordField.getPassword());
            String rePassword = String.valueOf(rePasswordField.getPassword());
            if (username.equals("") || password.equals("") || rePassword.equals("")) {
                msg.setText("Please Fill all the fields");
            } else if (!password.equals(rePassword)) {
                msg.setText("Passwords doesn't match");
            } else if (isPasswordNotAccepted(password)) {
                msg.setText("Password Doesn't Follow Rules");
            } else {
                String str;
                String[] credentials;
                boolean found = false;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(db));
                    while ((str = reader.readLine()) != null) {
                        credentials = str.split(" ");
                        if (username.equals(credentials[0])) {
                            msg.setText("Username Already Taken");
                            found = true;
                            break;
                        }
                    }
                    reader.close();
                    if (!found) {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(db, true));
                        writer.write(username + " " + encryptPassword(password) + "\n");
                        writer.flush();
                        writer.close();
                        File th = new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + username + ".txt");
                        File en = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + username + ".txt");
                        if (th.createNewFile() && en.createNewFile()) {
                            displayMenu();
                        } else {
                            msg.setText("Due to some Error we couldn't create your account");
                        }
                    }
                } catch (Exception ex) {
                    msg.setText("Error in reading file");
                }
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    class Verification implements ActionListener {
        char case_;

        Verification(char case_) {
            this.case_ = case_;
        }

        JFrame verificationFrame;

        @Override
        public void actionPerformed(ActionEvent e) {
            verificationFrame = new JFrame("Verification");
            verificationFrame.setLayout(null);
            verificationFrame.setVisible(true);
            verificationFrame.setSize(300, 300);
            verificationFrame.setLocationRelativeTo(frame);

            passwordLabel = new JLabel("Enter Password:");
            passwordField = new JPasswordField();
            JButton verifyButton = new JButton("Verify");

            verificationFrame.add(passwordLabel);
            verificationFrame.add(passwordField);
            verificationFrame.add(verifyButton);

            passwordLabel.setBounds(40, 100, 120, 25);
            passwordLabel.setFont(timesNewRoman);
            passwordField.setBounds(160, 100, 100, 25);

            verifyButton.setBounds(75, 150, 150, 25);
            verifyButton.setBackground(Color.BLUE);
            verifyButton.setForeground(Color.WHITE);
            if (case_ == 'B') {
                verifyButton.addActionListener(new BookingsUI());
            } else if(case_ == 'A'){
                verifyButton.setText("Delete Account");
                verifyButton.setBackground(Color.RED);
            }

            verifyButton.addActionListener(new Checking());

            verificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        class Checking implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                msg = new JLabel();
                msg.setOpaque(true);

                verificationFrame.add(msg);

                msg.setBounds(0, 200, 300, 50);
                msg.setFont(timesNewRoman);
                msg.setHorizontalAlignment(0);
                msg.setVerticalAlignment(0);

                if (String.valueOf(passwordField.getPassword()).equals("")) {
                    msg.setText("Please type Password");
                } else if (!password.equals(String.valueOf(passwordField.getPassword()))) {
                    msg.setForeground(Color.RED);
                    msg.setText("Password Incorrect");
                } else {
                    verificationFrame.dispose();
                    if (case_ == 'T') TransactionHistory();
                    else if (case_ == 'U') new UpdateUsernameUI().updateUsernameUI();
                    else if (case_ == 'A') AccountDeletion();
                    else if (case_ == 'P') new PasswordChangeUI().passwordChangeUI();
                    else if (case_ == 'S') new SwitchAccounts().switchAccountsUI();
                }
            }
        }
    }

    JButton menuBookingButton, menuTHButton, menuUpdateUsernameButton,
            menuEnquiryButton, menuPasswordChangeButton, menuAccountDeleteButton, menuSwitchAccountsButton;
    JLabel usernameLabel;
    JLabel finderImage = new JLabel(new ImageIcon(dirname + "\\EazyFinderGUI\\finder.png"));

    void displayMenu() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setSize(frameSize, frameSize);
        frame.setTitle("EazyFinder");
        frame.setLocationRelativeTo(null);
//        frame.add(panel);

        menuBookingButton = new JButton("Book for a Journey");
        menuTHButton = new JButton("See Transaction History");
        menuUpdateUsernameButton = new JButton("Update Username");
        menuEnquiryButton = new JButton("Enquire");
        menuPasswordChangeButton = new JButton("Change Password");
        menuAccountDeleteButton = new JButton("Delete my Account");
        menuSwitchAccountsButton = new JButton("Switch Accounts");
        usernameLabel = new JLabel("Username: " + username);

        frame.add(usernameLabel);
        frame.add(finderImage);
        frame.add(menuBookingButton);
        frame.add(menuTHButton);
        frame.add(menuUpdateUsernameButton);
        frame.add(menuEnquiryButton);
        frame.add(menuPasswordChangeButton);
        frame.add(menuAccountDeleteButton);
        frame.add(menuSwitchAccountsButton);
        frame.add(logoutButton);

        usernameLabel.setBounds(0, 0, frameSize, 25);
        usernameLabel.setBackground(Color.cyan);
        usernameLabel.setForeground(Color.DARK_GRAY);
        usernameLabel.setHorizontalAlignment(0);
        usernameLabel.setFont(timesNewRoman);
        usernameLabel.setOpaque(true);

        finderImage.setBounds(318, 33, 64, 64); // Change the values as per the image

        // change all ActionListeners to Verification Class
        menuBookingButton.setBounds(200, 130, 300, 30);
        menuBookingButton.setBackground(Color.DARK_GRAY);
        menuBookingButton.setForeground(Color.WHITE);
        menuBookingButton.setFont(timesNewRoman);
        menuBookingButton.addActionListener(new Verification('B'));

        menuTHButton.setBounds(200, 200, 300, 30);
        menuTHButton.setBackground(Color.DARK_GRAY);
        menuTHButton.setForeground(Color.WHITE);
        menuTHButton.setFont(timesNewRoman);
        menuTHButton.addActionListener(new Verification('T'));

        menuEnquiryButton.setBounds(200, 270, 300, 30);
        menuEnquiryButton.setBackground(Color.DARK_GRAY);
        menuEnquiryButton.setForeground(Color.WHITE);
        menuEnquiryButton.setFont(timesNewRoman);
        menuEnquiryButton.addActionListener(new EnquireUI());

        menuUpdateUsernameButton.setBounds(200, 340, 300, 30);
        menuUpdateUsernameButton.setBackground(Color.DARK_GRAY);
        menuUpdateUsernameButton.setForeground(Color.WHITE);
        menuUpdateUsernameButton.setFont(timesNewRoman);
        menuUpdateUsernameButton.addActionListener(new Verification('U'));

        menuPasswordChangeButton.setBounds(200, 410, 300, 30);
        menuPasswordChangeButton.setBackground(Color.DARK_GRAY);
        menuPasswordChangeButton.setForeground(Color.WHITE);
        menuPasswordChangeButton.setFont(timesNewRoman);
        menuPasswordChangeButton.addActionListener(new Verification('P'));

        menuAccountDeleteButton.setBounds(200, 480, 300, 30);
        menuAccountDeleteButton.setBackground(Color.DARK_GRAY);
        menuAccountDeleteButton.setForeground(Color.WHITE);
        menuAccountDeleteButton.setFont(timesNewRoman);
        menuAccountDeleteButton.addActionListener(new Verification('A'));

        menuSwitchAccountsButton.setBounds(200, 550, 300, 30);
        menuSwitchAccountsButton.setBackground(Color.DARK_GRAY);
        menuSwitchAccountsButton.setForeground(Color.WHITE);
        menuSwitchAccountsButton.setFont(timesNewRoman);
        menuSwitchAccountsButton.addActionListener(new Verification('S'));

        logoutButton.setBounds(300, 620, 100, 30);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(timesNewRoman);
        logoutButton.addActionListener(new Back((byte) 1));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    JLabel nameLabel, phoneLabel, adultLabel, childrenLabel, phoneMessage;
    JLabel cityLabel, sourceLabel, destinationLabel;
    JTextField nameText, phoneText;
    JComboBox<String> cityField, sourceField, destinationField, adultField, childrenField;
    String[] childArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] adultArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] citiesArray = {"--Select--", "Hyderabad", "Bengaluru", "Chennai"};
    String[] places, temp = {"--Select--"};
    String city, source, destination, name, phone;
    short noOfAdults, noOfChildren;
    float cost;

    void getPlaces() {
        short noOfPlaces = 0;
        File cityFile = new File(dirname + "\\EazyFinderGUI\\CitiesInfo\\" + city + ".txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cityFile));
            while (reader.readLine() != null) noOfPlaces++;
            reader.close();
        } catch (Exception ignored) {
        }

        places = new String[noOfPlaces + 1];
        places[0] = "--Select--";

        String str;
        short i;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cityFile));
            for (i = 1; (str = reader.readLine()) != null; i++) {
                places[i] = str.split(" ")[0];
            }
            reader.close();
        } catch (Exception ignored) {
        }
    }

    // Changes the Source and Destination Items according to City Selected
    class changingCombos implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            city = String.valueOf(cityField.getSelectedItem());
            getPlaces();
            sourceField.removeAllItems();
            destinationField.removeAllItems();
            short i, len = (short) places.length;
            sourceField.addItem("--Select--");
            destinationField.addItem("--Select--");
            for (i = 1; i < len; i++) {
                sourceField.addItem(places[i].toUpperCase());
                destinationField.addItem(places[i].toUpperCase());
            }
        }
    }


    class BookingsUI implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();

            backButton = new JButton("Back");
            JButton continueButton = new JButton("Continue");

            nameLabel = new JLabel("Name:");
            nameLabel.setFont(timesNewRoman);
            nameText = new JTextField();
            nameText.setFont(timesNewRoman);

            phoneLabel = new JLabel("Phone:");
            phoneLabel.setFont(timesNewRoman);
            phoneText = new JTextField();
            phoneText.setFont(timesNewRoman);

            cityLabel = new JLabel("City");
            cityLabel.setFont(timesNewRoman);
            cityField = new JComboBox<>(citiesArray);
            cityField.setFont(timesNewRoman);

            sourceLabel = new JLabel("Source:");
            sourceLabel.setFont(timesNewRoman);
            sourceField = new JComboBox<>(temp);
            sourceField.setFont(timesNewRoman);

            destinationLabel = new JLabel("Destination:");
            destinationLabel.setFont(timesNewRoman);
            destinationField = new JComboBox<>(temp);
            destinationField.setFont(timesNewRoman);

            adultLabel = new JLabel("Adults:");
            adultLabel.setFont(timesNewRoman);
            adultField = new JComboBox<>(adultArray);
            adultField.setFont(timesNewRoman);

            childrenLabel = new JLabel("Children:");
            childrenLabel.setFont(timesNewRoman);
            childrenField = new JComboBox<>(childArray);
            childrenField.setFont(timesNewRoman);

            msg = new JLabel();
            phoneMessage = new JLabel();

            frame.setTitle("Booking");
            frame.add(backButton);
            frame.add(nameLabel);
            frame.add(phoneLabel);
            frame.add(cityLabel);
            frame.add(adultLabel);
            frame.add(childrenLabel);
            frame.add(nameText);
            frame.add(phoneText);
            frame.add(cityField);
            frame.add(adultField);
            frame.add(childrenField);
            frame.add(continueButton);
            frame.add(msg);
            frame.add(phoneMessage);
            frame.add(logoutButton);

            frame.add(sourceLabel);
            frame.add(destinationLabel);
            frame.add(sourceField);
            frame.add(destinationField);

            cityField.addActionListener(new changingCombos());

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 2));

            nameLabel.setBounds(200, 50, 100, 25);
            phoneLabel.setBounds(200, 100, 100, 25);
            cityLabel.setBounds(200, 150, 100, 25);
            sourceLabel.setBounds(200, 200, 100, 25);
            destinationLabel.setBounds(200, 250, 100, 25);
            adultLabel.setBounds(200, 300, 100, 25);
            childrenLabel.setBounds(200, 350, 100, 25);

            nameText.setBounds(300, 50, 200, 25);
            nameText.setText(username);

            phoneText.setBounds(300, 100, 200, 25);
            cityField.setBounds(300, 150, 200, 25);
            sourceField.setBounds(300, 200, 200, 25);
            destinationField.setBounds(300, 250, 200, 25);
            adultField.setBounds(300, 300, 200, 25);
            childrenField.setBounds(300, 350, 200, 25);

            continueButton.setBounds(275, 400, 150, 25);
            continueButton.setBackground(Color.GREEN);
            continueButton.setForeground(Color.WHITE);
            continueButton.setFont(timesNewRoman);
            continueButton.addActionListener(new ContinueToModeOfTransportation());

            phoneMessage.setBounds(500, 100, 200, 25);
            phoneMessage.setFont(timesNewRoman);
            phoneMessage.setForeground(Color.RED);
            phoneMessage.setFont(timesNewRoman);
            phoneMessage.setHorizontalAlignment(JLabel.CENTER);

            logoutButton.setBounds(586, 0, 100, 30);
            logoutButton.setBackground(Color.RED);
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(timesNewRoman);
            logoutButton.addActionListener(new Back((byte) 1));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        String[] motOptions = {"Choose Mode Of Transportation Option", "One Transport for Whole Journey",
                "Mode Of Transportation Place to Place"};
        JComboBox<String> modeOfTransportationCB = new JComboBox<>(motOptions);

        class ContinueToModeOfTransportation implements ActionListener {
            BookingMainCode bookingObj;

            @Override
            public void actionPerformed(ActionEvent e) {
                msg.setText("");
                phoneMessage.setText("");

                msg.setFont(timesNewRoman);
                msg.setForeground(Color.RED);
                msg.setHorizontalAlignment(0);

                name = nameText.getText().trim();
                phone = phoneText.getText().trim();
                city = String.valueOf(cityField.getSelectedItem());
                source = String.valueOf(sourceField.getSelectedItem());
                destination = String.valueOf(destinationField.getSelectedItem());
                noOfAdults = Short.parseShort(String.valueOf(adultField.getSelectedItem()));
                noOfChildren = Short.parseShort(String.valueOf(childrenField.getSelectedItem()));

                if (name.equals("") || phone.equals("") || source.equals("--Select--") || destination.equals("--Select--")) {
                    msg.setBounds(225, 470, 250, 25);
                    msg.setText("Please Complete all the fields");
                } else if (source.equals(destination)) {
                    msg.setBounds(200, 470, 350, 25);
                    msg.setText("Source and Destination Cannot be the same");
                } else if (!phone.matches("^[6-9]\\d{9}")) {
                    phoneMessage.setText("Invalid Phone Number");
                } else {
                    BookingMainCode bookingObj = new BookingMainCode(city, source, destination, noOfAdults, noOfChildren);

                    bookingObj.new EnquireAndBookings().bookings();

                    String[] route = bookingObj.route;
                    cost = bookingObj.cost;
                    short i, len = bookingObj.routeLen;

                    StringBuilder routeCost = new StringBuilder("<html>\nRoute: ");
                    for (i = len; i >= 0; i--) {
                        routeCost.append(route[i]);
                        if (i != 0) routeCost.append(" -> ");
                    }
                    routeCost.append("\nCost: ").append(cost).append("</html>");

                    JLabel routeCostMessage = new JLabel();

                    frame.add(routeCostMessage);

                    routeCostMessage.setOpaque(true);
                    routeCostMessage.setBounds(0, 430, frameSize, 50);
                    routeCostMessage.setFont(timesNewRoman);
                    routeCostMessage.setHorizontalAlignment(0);
                    routeCostMessage.setText(String.valueOf(routeCost).replaceAll("\n", "<br>"));

                    frame.add(modeOfTransportationCB);

                    modeOfTransportationCB.setBounds(200, 500, 300, 25);
                    modeOfTransportationCB.setFont(timesNewRoman);

                    JButton modeOfTransportationButton = new JButton("GO");
                    frame.add(modeOfTransportationButton);
                    modeOfTransportationButton.setBounds(300, 540, 100, 25);
                    modeOfTransportationButton.setBackground(Color.GREEN);
                    modeOfTransportationButton.setForeground(Color.WHITE);
                    modeOfTransportationButton.setFont(timesNewRoman);
                    modeOfTransportationButton.addActionListener(new AfterMOT());
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }

            class AfterMOT implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    byte selectedOption = (byte) modeOfTransportationCB.getSelectedIndex();
                    msg = new JLabel();
                    frame.add(msg);
                    msg.setFont(timesNewRoman);
                    msg.setText("");

                    if (selectedOption == 0) {
                        msg.setBounds(0, 570, frameSize, 25);
                        msg.setHorizontalAlignment(0);
                        msg.setText("Please Select an Option for Mode of Transportation first");
                    } else if (selectedOption == 1) {
                        oneModeOfTransportation(bookingObj.noOfVehicles, bookingObj.vehicles, bookingObj.costPerKM);
                    } else {
                        placeToPlaceModeOfTransportation();
                    }
                }
            }

            void oneModeOfTransportation(short noOfVehicles, String[] vehicles, short[] costPerKM) {
                frame.getContentPane().removeAll();
                frame.repaint();

                JRadioButton[] motRB = new JRadioButton[noOfVehicles];
                motRB[0] = new JRadioButton("Bus");
                motRB[1] = new JRadioButton("Bike");
                motRB[2] = new JRadioButton("Auto");
                motRB[3] = new JRadioButton("Metro");
                motRB[4] = new JRadioButton("Cab");

//                motGroup.add(motRB[0]); motGroup.add(motRB[1]); motGroup.add(motRB[2]);
//                motGroup.add(motRB[3]); motGroup.add(motRB[4]);

                backButton = new JButton("Back to Menu");

                frame.add(backButton);
                frame.add(logoutButton);

                backButton.setBounds(0, 0, 150, 30);
                backButton.setBackground(Color.BLACK);
                backButton.setForeground(Color.WHITE);
                backButton.setFont(timesNewRoman);
                backButton.addActionListener(new Back((byte) 2));

                logoutButton.setBounds(586, 0, 100, 30);
                logoutButton.setBackground(Color.RED);
                logoutButton.setForeground(Color.WHITE);
                logoutButton.setFont(timesNewRoman);
                logoutButton.addActionListener(new Back((byte) 1));

                short i;
                Object[][] rows = new Object[noOfVehicles][3];
                for (i = 0; i < noOfVehicles; i++) {
                    rows[i][0] = i + 1;
                    rows[i][1] = motRB[i];
                    rows[i][2] = costPerKM[i];
                }

                Object[] columns = {"ID", "Vehicle", "Cost Per Kilometer"};

                DefaultTableModel dtm = new DefaultTableModel();
                dtm.setDataVector(rows, columns);

                ButtonGroup motGroup = new ButtonGroup();
                motGroup.add((JRadioButton) dtm.getValueAt(0, 1));
                motGroup.add((JRadioButton) dtm.getValueAt(1, 1));
                motGroup.add((JRadioButton) dtm.getValueAt(2, 1));
                motGroup.add((JRadioButton) dtm.getValueAt(3, 1));
                motGroup.add((JRadioButton) dtm.getValueAt(4, 1));

                JTable vehicleTable = new JTable(dtm);
                vehicleTable.setBounds(100, 100, 300, 300);
                vehicleTable.setFont(timesNewRoman);
                vehicleTable.setVisible(true);

                JScrollPane sp = new JScrollPane(vehicleTable);
                frame.add(sp);

                JComboBox<String> vehicleCB = new JComboBox<>(vehicles);
                frame.add(vehicleCB);
                vehicleCB.setFont(timesNewRoman);
                vehicleCB.setBounds(0, 500, 100, 25);

                JButton book = new JButton("Book");
                frame.add(book);
                book.setBackground(Color.GREEN);
                book.setForeground(Color.WHITE);
                book.setFont(timesNewRoman);
                book.setBounds(0, 600, 100, 25);

                book.addActionListener(book_E -> {
                    short motIndex = (short) vehicleCB.getSelectedIndex();
                    BookingMainCode bookingObj = new BookingMainCode(motIndex,
                            Short.parseShort(String.valueOf(adultField.getSelectedItem())),
                            Short.parseShort(String.valueOf(childrenField.getSelectedItem())));
                    float totalCost = bookingObj.calculateTotalCost();

                    JLabel costLabel = new JLabel("Total Cost: " + totalCost);
                    frame.add(costLabel);
                    costLabel.setBounds(0, 600, frameSize, 100);
                    costLabel.setFont(timesNewRoman);
                    costLabel.setHorizontalAlignment(0);
                    costLabel.setVerticalAlignment(0);

                    JButton proceedButton = new JButton("Proceed");
                    frame.add(proceedButton);
                    proceedButton.setBounds(600, 600, 100, 25);
                    proceedButton.setBackground(Color.GREEN);
                    proceedButton.setForeground(Color.WHITE);
                    proceedButton.setFont(timesNewRoman);
                    proceedButton.addActionListener(new LoadDetails());
                });
            }

            class LoadDetails implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.getContentPane().removeAll();
                    frame.repaint();

                    Date date = new Date();

                    BookingMainCode bookingObj = new BookingMainCode();

                    bookingObj.loadDetails(username, city, source, destination, bookingObj.calculateTotalCost(), nameText.getText(),
                            Long.parseLong(phoneText.getText()), noOfAdults, noOfChildren,
                            new SimpleDateFormat("dd:MM:yyyy").format(date),
                            new SimpleDateFormat("HH:mm:ss").format(date));

                    backButton = new JButton("Back");
                    msg = new JLabel("<html>Successfully Booked a Ticket from " +
                            source.toUpperCase() + " to " + destination.toUpperCase() + "<br" + "Total Cost: " + cost +
                            "<br>" + "</html>");

                    frame.add(backButton);
                    frame.add(msg);
                    frame.add(logoutButton);

                    backButton.setBounds(0, 0, 80, 30);
                    backButton.setBackground(Color.BLACK);
                    backButton.setForeground(Color.WHITE);
                    backButton.addActionListener(new Back((byte) 2));

                    msg.setBounds(0, 0, frameSize, frameSize);
                    msg.setHorizontalAlignment(0);
                    msg.setVerticalAlignment(0);

                    logoutButton.setBounds(586, 0, 100, 30);
                    logoutButton.setBackground(Color.RED);
                    logoutButton.setForeground(Color.WHITE);
                    logoutButton.setFont(timesNewRoman);
                    logoutButton.addActionListener(new Back((byte) 1));
                }
            }

            void placeToPlaceModeOfTransportation() {
                System.out.println("HI");
            }
        }
    }


    void TransactionHistory() {
        frame.getContentPane().removeAll();
        frame.repaint();

        msg = new JLabel();
        backButton = new JButton("Back");

        frame.add(msg);
        frame.add(backButton);
        frame.add(logoutButton);

        backButton.setBounds(0, 0, 80, 30);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new Back((byte) 2));

        logoutButton.setBounds(586, 0, 100, 30);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(timesNewRoman);
        logoutButton.addActionListener(new Back((byte) 1));

        JLabel thHeading = new JLabel("Your Transactions:");
        thHeading.setBounds(0, 0, frameSize, 30);
        thHeading.setHorizontalAlignment(0);
        thHeading.setFont(timesNewRoman);
        frame.add(thHeading);

        String msgText = String.valueOf(new TransactionHistoryMainCode().transactionHistory(username));

        if (msgText.equals("NO")) {
            JLabel noTransactionsImage = new JLabel(new ImageIcon(dirname + "\\EazyFinderGUI\\no_transactions.png"));
            frame.add(noTransactionsImage);
            noTransactionsImage.setBounds(115, 204, 466, 292);
        } else {
            msg.setBounds(0, 0, frameSize, frameSize);
            msg.setHorizontalAlignment(0);
            msg.setVerticalAlignment(0);
            msg.setFont(timesNewRoman);
            msg.setText(msgText);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class EnquireUI implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();

            backButton = new JButton("Back");

            cityLabel = new JLabel("City:");
            cityField = new JComboBox<>(citiesArray);

            sourceLabel = new JLabel("Source:");
            sourceField = new JComboBox<>(temp);

            destinationLabel = new JLabel("Destination:");
            destinationField = new JComboBox<>(temp);

            msg = new JLabel();

            JButton enquireButton = new JButton("Enquire");

            frame.add(backButton);
            frame.add(cityLabel);
            frame.add(cityField);
            frame.add(sourceLabel);
            frame.add(sourceField);
            frame.add(destinationLabel);
            frame.add(destinationField);
            frame.add(enquireButton);
            frame.add(logoutButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.addActionListener(new Back((byte) 2));

            cityLabel.setBounds(200, 200, 100, 25);
            cityLabel.setFont(timesNewRoman);
            sourceLabel.setBounds(200, 250, 100, 25);
            sourceLabel.setFont(timesNewRoman);
            destinationLabel.setBounds(200, 300, 100, 25);
            destinationLabel.setFont(timesNewRoman);

            cityField.setBounds(300, 200, 200, 25);
            cityField.setFont(timesNewRoman);
            sourceField.setBounds(300, 250, 200, 25);
            sourceField.setFont(timesNewRoman);
            destinationField.setBounds(300, 300, 200, 25);
            destinationField.setFont(timesNewRoman);

            cityField.addActionListener(new changingCombos());

            enquireButton.setBounds(300, 350, 100, 25);
            enquireButton.setBackground(Color.DARK_GRAY);
            enquireButton.setForeground(Color.WHITE);
            enquireButton.setFont(timesNewRoman);
            enquireButton.addActionListener(new EnquireMainCode());

            logoutButton.setBounds(586, 0, 100, 30);
            logoutButton.setBackground(Color.RED);
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(timesNewRoman);
            logoutButton.addActionListener(new Back((byte) 1));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        class EnquireMainCode implements ActionListener { // Save Enquiries for recommendations
            @Override
            public void actionPerformed(ActionEvent e) {
                msg = new JLabel();
                msg.setFont(timesNewRoman);
                msg.setOpaque(true);

                frame.add(msg);

                msg.setBounds(0, 400, frameSize, frameSize);
                msg.setFont(timesNewRoman);
                msg.setForeground(Color.RED);
                msg.setHorizontalAlignment(0);

                String city = String.valueOf(cityField.getSelectedItem());
                source = String.valueOf(sourceField.getSelectedItem());
                destination = String.valueOf(destinationField.getSelectedItem());

                if (city.equals("--Select--") || source.equals("--Select--") || destination.equals("--Select--")) {
                    msg.setText("Please Complete all the Fields");
                } else if (source.equals(destination)) {
                    msg.setText("Source and Destination Cannot be same");
                } else {
                    JFrame enquireFrame = new JFrame();
                    enquireFrame.setSize(500, 500);

                    enquireFrame.setLayout(null);
                    enquireFrame.setVisible(true);
                    enquireFrame.setLocationRelativeTo(frame);

                    JLabel routeCostMessage = new JLabel();
                    enquireFrame.add(routeCostMessage);

                    BookingMainCode enquiryObj = new BookingMainCode(city, source, destination);
                    enquiryObj.new EnquireAndBookings().bookings();

                    String[] route = enquiryObj.route;
                    short i, routeLen = enquiryObj.routeLen;
                    cost = enquiryObj.cost;

                    StringBuilder routeCost = new StringBuilder("<html>\nRoute: ");
                    for (i = routeLen; i >= 0; i--) {
                        routeCost.append(route[i].toUpperCase());
                        if (i != 0) routeCost.append(" -> ");
                    }
                    routeCost.append("\nCost: ").append(cost).append(" /-").append("</html>");

                    enquireFrame.setTitle(enquiryObj.source.toUpperCase() + " to " + enquiryObj.destination.toUpperCase());

                    routeCostMessage.setBounds(0, 0, 500, 500);
                    routeCostMessage.setHorizontalAlignment(0);
                    routeCostMessage.setVerticalAlignment(0);
                    routeCostMessage.setFont(timesNewRoman);
                    routeCostMessage.setText(String.valueOf(routeCost).replaceAll("\n", "<br>"));

                    enquireFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    // store the enquiries
                    File enquiryFile = new File(dirname + "\\Enquiries\\" + username + ".txt");
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(enquiryFile));
                        writer.write(city.toLowerCase() + "," + source.toLowerCase() + "," + destination.toLowerCase());
                        writer.flush();
                        writer.close();
                    } catch (Exception ex) {
                        msg.setText("Error Occurred");
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }


    class UpdateUsernameUI {
        JFrame updateUsernameFrame = new JFrame();
        JLabel newUsernameLabel = new JLabel("New Username:");
        JTextField newUsernameText = new JTextField();
        JButton changeUsernameButton = new JButton("Change Username");

        void updateUsernameUI() {
            updateUsernameFrame.setSize(400, 400);
            updateUsernameFrame.setLocationRelativeTo(frame);
            updateUsernameFrame.setVisible(true);
            updateUsernameFrame.setLayout(null);

            updateUsernameFrame.add(newUsernameLabel);
            updateUsernameFrame.add(newUsernameText);
            updateUsernameFrame.add(changeUsernameButton);

            newUsernameLabel.setBounds(90, 180, 120, 25);
            newUsernameLabel.setFont(timesNewRoman);

            newUsernameText.setBounds(210, 180, 100, 25);
            newUsernameText.setFont(timesNewRoman);

            changeUsernameButton.setBounds(50, 220, 200, 25);
            changeUsernameButton.setBackground(Color.RED);
            changeUsernameButton.setForeground(Color.WHITE);
            changeUsernameButton.setFont(timesNewRoman);
            changeUsernameButton.addActionListener(new UpdateUsername());

            updateUsernameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        class UpdateUsername implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUsernameText.getText();
                boolean found = false;

                updateUsernameFrame.add(msg);
                msg.setBounds(0, 250, 300, 25);
                msg.setHorizontalAlignment(0);

                if (newUsername.equals(username)) {
                    msg.setText("New Username Cannot be the Same as old one");
                } else {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(db));
                        String str;
                        while ((str = reader.readLine()) != null) {
                            if (str.split(" ")[0].equals(newUsername)) {
                                found = true;
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception ignored) {
                    }

                    if (found) {
                        msg.setText("Username Already taken. Try with Another One");
                    } else {
                        int result = JOptionPane.showConfirmDialog(updateUsernameFrame, "Are You Sure?", "Confirmation",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            boolean updated = new UpdateUsernameMainCode().updateUsername(username, newUsername, password);
                            File th = new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + username + ".txt");
                            File enq = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + username + ".txt");
                            File newTH = new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + newUsername + ".txt");
                            File newENQ = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + newUsername + ".txt");

                            if (th.renameTo(newTH) && enq.renameTo(newENQ) && updated) {
                                updateUsernameFrame.dispose();

                                username = newUsername;

                                usernameLabel.setText("Username: " + username);

                                JOptionPane.showConfirmDialog(frame, "Username Changed Successfully", "Successful",
                                        JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.PLAIN_MESSAGE);

                                displayMenu();
                            } else {
                                JOptionPane.showConfirmDialog(updateUsernameFrame, "Error Occurred. Username Didn't Change", "Error",
                                        JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.PLAIN_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }


    class PasswordChangeUI {
        JFrame passwordChangeFrame;
        JLabel newPasswordLabel;
        JPasswordField newPasswordField;

        void passwordChangeUI() {
            passwordChangeFrame = new JFrame("Change Password");
            passwordChangeFrame.setVisible(true);
            passwordChangeFrame.setLayout(null);
            passwordChangeFrame.setSize(400, 400);
            passwordChangeFrame.setLocationRelativeTo(frame);

            backButton = new JButton("Back");
            newPasswordLabel = new JLabel("New Password:");
            rePasswordLabel = new JLabel("Retype New Password:");
            newPasswordField = new JPasswordField();
            rePasswordField = new JPasswordField();
            JButton passwordChangeButton = new JButton("Change Password");

            passwordChangeFrame.add(newPasswordLabel);
            passwordChangeFrame.add(rePasswordLabel);
            passwordChangeFrame.add(newPasswordField);
            passwordChangeFrame.add(rePasswordField);
            passwordChangeFrame.add(passwordChangeButton);

            newPasswordLabel.setBounds(90, 100, 120, 25);
            newPasswordLabel.setFont(timesNewRoman);

            newPasswordField.setBounds(210, 100, 100, 25);

            rePasswordLabel.setBounds(40, 150, 180, 25);
            rePasswordLabel.setFont(timesNewRoman);

            rePasswordField.setBounds(210, 150, 100, 25);

            passwordChangeButton.setBounds(100, 200, 180, 25);
            passwordChangeButton.setBackground(Color.RED);
            passwordChangeButton.setForeground(Color.WHITE);
            passwordChangeButton.setFont(timesNewRoman);
            passwordChangeButton.addActionListener(new PasswordChange());

            passwordChangeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        class PasswordChange implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                msg = new JLabel();
                msg.setOpaque(true);

                passwordChangeFrame.add(msg);

                msg.setBounds(0, 300, 400, 50);
                msg.setForeground(Color.RED);
                msg.setFont(timesNewRoman);
                msg.setHorizontalAlignment(0);

                String newPassword = String.valueOf(newPasswordField.getPassword());
                String rePassword = String.valueOf(rePasswordField.getPassword());

                if (newPassword.equals("") || rePassword.equals("")) {
                    msg.setText("Please fill all the fields");
                } else if (password.equals(newPassword)) {
                    msg.setText("New Password cannot be same as Old one");
                } else if (!newPassword.equals(rePassword)) {
                    msg.setText("Passwords doesn't match");
                } else if (isPasswordNotAccepted(newPassword)) {
                    msg.setText("New Password Doesn't Follow Rules");
                } else {
                    int result = JOptionPane.showConfirmDialog(passwordChangeFrame, "Are You Sure?", "Confirm",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (new PasswordChangeMainCode().passwordChange(username, password, newPassword)) {
                            password = newPassword;
                            msg.setForeground(Color.GREEN);
                            msg.setText("Password Changed Successfully");
                        } else {
                            msg.setText("Some Error Occurred");
                        }
                    } else if (result == JOptionPane.NO_OPTION) {
                        passwordChangeFrame.dispose();
                        displayMenu();
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }


    void AccountDeletion() {
        int result = JOptionPane.showConfirmDialog(frame, "Are You Sure?", "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            boolean deleted = new AccountDeletionMainCode().accountDeletion(username, password);
            if (deleted) {
                HomePage();

                msg = new JLabel();
                frame.add(msg);

                msg.setForeground(Color.RED);
                msg.setFont(timesNewRoman);
                msg.setOpaque(true);

                msg.setText("Account Deleted Successfully");

                msg.setBounds(25, 200, 250, 25);
                msg.setHorizontalAlignment(0);
            } else {
                msg.setText("<html>Some Error Occurred<br>Account not Deleted</html>");
            }
        } else {
            displayMenu();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class SwitchAccounts {
        JFrame switchAccountsFrame;

        void switchAccountsUI() {
            switchAccountsFrame = new JFrame();
            switchAccountsFrame.setLayout(null);
            switchAccountsFrame.setSize(300, 300);
            switchAccountsFrame.setVisible(true);
            switchAccountsFrame.setLocationRelativeTo(frame);
            switchAccountsFrame.setTitle("Switching Accounts");

            msg = new JLabel();
            userLabel = new JLabel("Username:");
            passwordLabel = new JLabel("Password:");
            userText = new JTextField();
            passwordField = new JPasswordField();
            JButton switchAccountButton = new JButton("Switch Account");

            switchAccountsFrame.add(userLabel);
            switchAccountsFrame.add(passwordLabel);
            switchAccountsFrame.add(userText);
            switchAccountsFrame.add(passwordField);
            switchAccountsFrame.add(switchAccountButton);
            switchAccountsFrame.add(msg);

            userText.setText("");
            passwordField.setText("");
            rePasswordField.setText("");
            msg.setText("");

            userLabel.setBounds(50, 80, 80, 25);
            userLabel.setFont(timesNewRoman);

            userText.setBounds(130, 80, 120, 25);
            userText.setFont(timesNewRoman);

            passwordLabel.setBounds(50, 110, 80, 25);
            passwordLabel.setFont(timesNewRoman);

            passwordField.setBounds(130, 110, 120, 25);

            switchAccountButton.setBounds(75, 150, 150, 25);
            switchAccountButton.setBackground(Color.DARK_GRAY);
            switchAccountButton.setForeground(Color.WHITE);
            switchAccountButton.setFont(timesNewRoman);
            switchAccountButton.addActionListener(new SwitchAccountsMainCode());

            msg.setBounds(0, 200, 300, 25);
            msg.setFont(timesNewRoman);
            msg.setHorizontalAlignment(0);
        }

        class SwitchAccountsMainCode implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String localUsername = userText.getText();
                String localPassword = String.valueOf(passwordField.getPassword());

                if (localUsername.equals("") || localPassword.equals("")) {
                    msg.setText("Please fill all the fields");
                } else if (localUsername.equals(username) && localPassword.equals(password)) {
                    msg.setText("Cannot Switch with Same Account");
                } else {
                    String str;
                    String[] credentials;
                    boolean found = false;
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(db));
                        while ((str = reader.readLine()) != null) {
                            credentials = str.split(" ");
                            if (localUsername.equals(credentials[0]) && String.valueOf(encryptPassword(localPassword)).equals(credentials[1])) {
                                found = true;
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception ex) {
                        msg.setText("Error in reading file");
                    }
                    if (!found) {
                        msg.setText("No User With Given Credentials");
                    } else {
                        int result = JOptionPane.showConfirmDialog(switchAccountsFrame, "Are You Sure?", "Confirmation",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            username = localUsername;
                            password = localPassword;
                            switchAccountsFrame.dispose();
                            usernameLabel.setText("Username: " + username);
                            JOptionPane.showConfirmDialog(frame, "Switched Accounts Successfully", "Successful",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE);
                        } else {
                            displayMenu();
                        }
                    }
                }
            }
        }
    }
}
