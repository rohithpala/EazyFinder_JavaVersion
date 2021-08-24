package EazyFinderGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// use sudo mode as in GitHub TODO
// caps lock warning TODO

public class EazyFinderGUI {

    // While using an IDE "Edit Configurations" by setting the Working Directory path till src if it is not already present
    String dirname = System.getProperty("user.dir");

    JFrame frame = new JFrame();
    JButton backButton, logoutButton = new JButton("Logout");
    JLabel msg; // Used to print corresponding messages
    final Font timesNewRoman = new Font("Times New Roman", Font.BOLD, 15);
    final short frameSize = 700;
    Image icon = Toolkit.getDefaultToolkit().getImage(dirname + "\\EazyFinderGUI\\finder.png");

    public static void main(String[] args) {
        new EazyFinderGUI().Homepage();
    }

    JButton homepageLoginButton = new JButton("LogIn");
    JButton homepageSignupButton = new JButton("SignUp");
    void Homepage() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setIconImage(icon);

        frame.setSize(350, 300);
        frame.setTitle("Homepage");
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        frame.add(homepageLoginButton);
        frame.add(homepageSignupButton);

        homepageLoginButton.setBounds(50, 103, 100, 30);
        homepageLoginButton.setForeground(Color.BLACK);
        homepageLoginButton.setBackground(Color.ORANGE);
        homepageLoginButton.setFont(timesNewRoman);
        homepageLoginButton.addActionListener(new LoginUI());

        homepageSignupButton.setBounds(180, 103, 100, 30);
        homepageSignupButton.setForeground(Color.BLACK);
        homepageSignupButton.setBackground(Color.ORANGE);
        homepageSignupButton.setFont(timesNewRoman);
        homepageSignupButton.addActionListener(new SignUpUI());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class Back implements ActionListener {
        byte num;

        Back(byte num) {
            this.num = num;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (num == 0) {
                Homepage();
            } else if (num == 1) {
                int result = JOptionPane.showConfirmDialog(frame, "Are You Sure?", "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    Homepage();
                }
            } else if (num == 2) {
                displayMenu();
            }
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

    static class ShowPasswordsCheckBox implements ActionListener {
        JPasswordField pf;

        ShowPasswordsCheckBox(JPasswordField pf) {
            this.pf = pf;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            pf.setEchoChar(c.isSelected() ? '\u0000' : (Character) UIManager.get("PasswordField.echoChar"));
        }
    }

    // variables used in Login and SignUp
    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel rePasswordLabel = new JLabel("Re-Type Password:");
    String username, password;
    final File db = new File(dirname + "\\EazyFinderGUI\\LogInSignUpDatabase.txt");

    class LoginUI implements ActionListener {
        JTextField userText;
        JPasswordField passwordField;
        JButton loginButton = new JButton("LogIn");
        JCheckBox showPasswordCB1;

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("Login");

            backButton = new JButton("Back");
            userText = new JTextField();
            passwordField = new JPasswordField();
            showPasswordCB1 = new JCheckBox("Show Password");
            msg = new JLabel();

            frame.add(backButton);
            frame.add(userLabel);
            frame.add(passwordLabel);
            frame.add(userText);
            frame.add(passwordField);
            frame.add(showPasswordCB1);
            frame.add(loginButton);
            frame.add(msg);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 1));

            userLabel.setBounds(50, 50, 80, 25);
            userLabel.setFont(timesNewRoman);

            userText.setBounds(130, 50, 120, 25);
            userText.setFont(timesNewRoman);

            passwordLabel.setBounds(50, 80, 80, 25);
            passwordLabel.setFont(timesNewRoman);

            passwordField.setBounds(130, 80, 120, 25);
            passwordField.setFont(timesNewRoman);

            showPasswordCB1.setBounds(90, 110, 150, 25);
            showPasswordCB1.setFont(timesNewRoman);
            showPasswordCB1.addActionListener(new ShowPasswordsCheckBox(passwordField));

            loginButton.setBounds(120, 140, 80, 25);
            loginButton.setBackground(Color.DARK_GRAY);
            loginButton.setForeground(Color.WHITE);
            loginButton.setFont(timesNewRoman);
            loginButton.addActionListener(new LoginMainCode());

            msg.setBounds(0, 190, 350, 25);
            msg.setFont(timesNewRoman);
            msg.setHorizontalAlignment(0);
        }

        class LoginMainCode implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = userText.getText().trim();
                password = String.valueOf(passwordField.getPassword()).trim();

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
    }

    boolean isPasswordAccepted(String password) {
        boolean inRange = false, hasWhiteSpace = false, hasLowerCaseLetter = false, hasUpperCaseLetter = false, hasDigit = false,
                hasSpecialCharacter = false;
        byte i, len = (byte) password.length();
        if (len >= 8 && len <= 16)
            inRange = true;

        msg.setText("");

        if (!inRange) {
            JOptionPane.showMessageDialog(frame, "Password isn't in the Range of 8-16\nPlease try with another Password",
                    "Password Not Accepted",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            for (i = 0; i < len; i++) {
                if (Character.isAlphabetic(password.charAt(i))) {
                    if (Character.isLowerCase(password.charAt(i))) hasLowerCaseLetter = true;
                    else hasUpperCaseLetter = true;
                } else if (Character.isDigit(password.charAt(i))) {
                    hasDigit = true;
                } else if (Character.isWhitespace(password.charAt(i))) {
                    hasWhiteSpace = true;
                } else hasSpecialCharacter = true;
            }

            if (!(hasLowerCaseLetter && hasUpperCaseLetter && hasDigit && !hasWhiteSpace && hasSpecialCharacter)) {
                StringBuilder message = new StringBuilder();
                if (!hasLowerCaseLetter) message.append("Password Must Contain at least one Lowercase letter").append("\n");
                if (!hasUpperCaseLetter) message.append("Password Must Contain at least one Uppercase letter").append("\n");
                if (!hasDigit) message.append("Password Must Contain at least one Digit").append("\n");
                if (!hasSpecialCharacter) message.append("Password Must Contain at least one Special Character").append("\n");
                if (hasWhiteSpace) message.append("Password Shouldn't Contain a White space Character");

                JOptionPane.showMessageDialog(frame, message, "Password Not Accepted", JOptionPane.ERROR_MESSAGE);
            }
        }

        return inRange && !hasWhiteSpace && hasLowerCaseLetter && hasUpperCaseLetter && hasDigit && hasSpecialCharacter;
    }

    class SignUpUI implements ActionListener {
        JTextField userText;
        JPasswordField passwordField, rePasswordField;
        JButton signupButton = new JButton("SignUp");
        JCheckBox showPasswordCB1, showPasswordCB2;

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("SignUp");

            backButton = new JButton("Back");
            userText = new JTextField();
            passwordField = new JPasswordField();
            showPasswordCB1 = new JCheckBox("Show Password");
            rePasswordField = new JPasswordField();
            showPasswordCB2 = new JCheckBox("Show Password");
            msg = new JLabel();

            frame.add(userLabel);
            frame.add(passwordLabel);
            frame.add(userText);
            frame.add(passwordField);
            frame.add(showPasswordCB1);
            frame.add(showPasswordCB2);
            frame.add(rePasswordLabel);
            frame.add(rePasswordField);
            frame.add(signupButton);
            frame.add(msg);
            frame.add(backButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 1));

            userLabel.setBounds(50, 50, 80, 25);
            userLabel.setFont(timesNewRoman);

            userText.setBounds(130, 50, 120, 25);
            userText.setFont(timesNewRoman);

            passwordLabel.setBounds(50, 80, 80, 25);
            passwordLabel.setFont(timesNewRoman);

            passwordField.setBounds(130, 80, 120, 25);
            passwordField.setFont(timesNewRoman);

            showPasswordCB1.setBounds(90, 110, 150, 25);
            showPasswordCB1.setFont(timesNewRoman);
            showPasswordCB1.addActionListener(new ShowPasswordsCheckBox(passwordField));

            rePasswordLabel.setBounds(0, 140, 130, 25);
            rePasswordLabel.setFont(timesNewRoman);

            rePasswordField.setBounds(130, 140, 120, 25);
            rePasswordField.setFont(timesNewRoman);

            showPasswordCB2.setBounds(90, 170, 150, 25);
            showPasswordCB2.setFont(timesNewRoman);
            showPasswordCB2.addActionListener(new ShowPasswordsCheckBox(rePasswordField));

            signupButton.setBounds(125, 200, 100, 25);
            signupButton.setBackground(Color.DARK_GRAY);
            signupButton.setForeground(Color.WHITE);
            signupButton.setFont(timesNewRoman);
            signupButton.addActionListener(new SignUpMainCode());

            msg.setBounds(0, 230, 350, 25);
            msg.setFont(timesNewRoman);
            msg.setHorizontalAlignment(JLabel.CENTER);
        }

        class SignUpMainCode implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = userText.getText().trim();
                password = String.valueOf(passwordField.getPassword());
                String rePassword = String.valueOf(rePasswordField.getPassword());
                boolean found = false;

                if (username.equals("") || password.equals("") || rePassword.equals("")) {
                    msg.setText("Please Fill all the fields");
                } else if (!password.equals(rePassword)) {
                    msg.setText("Passwords doesn't match");
                } else { // Checking if username is already present
                    String str;
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(db));
                        while ((str = reader.readLine()) != null) {
                            if (username.equals(str.split(" ")[0])) {
                                msg.setText("Username Already Taken");
                                found = true;
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception ex) {
                        msg.setText("Error in reading file");
                    }

                    if (!found && isPasswordAccepted(password)) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(db, true));
                            writer.write(username + " " + encryptPassword(password) + "\n");
                            writer.flush();
                            writer.close();
                            File th = new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + username + ".txt");
                            File en = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + username + ".txt");
                            if (th.createNewFile() && en.createNewFile()) {
                                JOptionPane.showMessageDialog(frame, "Account Created Successfully",
                                        "SignUp Successful", JOptionPane.INFORMATION_MESSAGE);
                                displayMenu();
                            }
                        } catch (Exception ex) {
                            msg.setText("Due to some Error we couldn't create your account");
                        }
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }

    class Verification implements ActionListener {
        char case_;

        Verification(char case_) {
            this.case_ = case_;
        }

        JFrame verificationFrame;
        JPasswordField verificationPasswordField;
        EnquireUI enqObj = new EnquireUI();
        UpdateUsernameUI updateUsernameObj = new UpdateUsernameUI();
        PasswordChangeUI passwordChangeObj = new PasswordChangeUI();
        SwitchAccountsUI switchAccountsObj = new SwitchAccountsUI();

        @Override
        public void actionPerformed(ActionEvent e) {
            verificationFrame = new JFrame("Verification");
            verificationFrame.setLayout(null);
            verificationFrame.setVisible(true);
            verificationFrame.setSize(300, 300);
            verificationFrame.setLocationRelativeTo(frame);

            JLabel verificationPasswordLabel = new JLabel("Enter Password:");
            verificationPasswordField = new JPasswordField();
            JCheckBox verificationCB = new JCheckBox("Show Password");
            JButton verifyButton = new JButton("Verify");

            verificationFrame.add(verificationPasswordLabel);
            verificationFrame.add(verificationPasswordField);
            verificationFrame.add(verificationCB);
            verificationFrame.add(verifyButton);

            verificationPasswordLabel.setBounds(40, 100, 120, 25);
            verificationPasswordLabel.setFont(timesNewRoman);

            verificationPasswordField.setBounds(160, 100, 100, 25);
            verificationPasswordField.setFont(timesNewRoman);

            verificationCB.setBounds(90, 130, 150, 20);
            verificationCB.addActionListener(new ShowPasswordsCheckBox(verificationPasswordField));

            verifyButton.setBounds(75, 170, 150, 25);
            verifyButton.setBackground(Color.BLUE);
            verifyButton.setForeground(Color.WHITE);
            if (case_ == 'B') {
                verifyButton.addActionListener(new BookingsUI());
            } else if (case_ == 'A') {
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

                if (String.valueOf(verificationPasswordField.getPassword()).equals("")) {
                    msg.setText("Please type Password");
                } else if (!password.equals(String.valueOf(verificationPasswordField.getPassword()))) {
                    msg.setForeground(Color.RED);
                    msg.setText("Password Incorrect");
                } else {
                    if (case_ != 'A') verificationFrame.dispose();
                    else AccountDeletion(verificationFrame);

                    if (case_ == 'T') TransactionHistory();
                    else if (case_ == 'E') enqObj.enquireUI();
                    else if (case_ == 'U') updateUsernameObj.updateUsernameUI();
                    else if (case_ == 'P') passwordChangeObj.passwordChangeUI();
                    else if (case_ == 'S') switchAccountsObj.switchAccountsUI();
                }
            }
        }
    }

    JLabel usernameLabel = new JLabel();

    int menuButtonWidth = 300, menuButtonHeight = 30;
    int menuButtonX, diffInYs, buttonsY;
    final int IMAGE_WIDTH = 64, IMAGE_HEIGHT = 64;
    int imageX, imageY;

    JButton menuBookingButton = new JButton("Book for a Journey");
    JButton menuTHButton = new JButton("See Transaction History");
    JButton menuUpdateUsernameButton = new JButton("Update Username");
    JButton menuEnquiryButton = new JButton("Enquire");
    JButton menuPasswordChangeButton = new JButton("Change Password");
    JButton menuAccountDeleteButton = new JButton("Delete my Account");
    JButton menuSwitchAccountsButton = new JButton("Switch Accounts");
    final JLabel finderImage = new JLabel(new ImageIcon(dirname + "\\EazyFinderGUI\\finder.png"));

    // use type of singleton class because the frame ui is static, not needed to always set bounds and all TODO
    void displayMenu() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setSize(frameSize, frameSize);
        frame.setTitle("EazyFinder");
        frame.setLocationRelativeTo(null);

        menuButtonX = 200; diffInYs = 70;
        buttonsY = 130;
        imageX = (frameSize - IMAGE_WIDTH) / 2;
        imageY = (buttonsY - IMAGE_HEIGHT) / 2; // 130 - starting button's(booking button) y

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

        usernameLabel.setText("Username: " + username);
        usernameLabel.setBounds(0, 0, frameSize, 25);
        usernameLabel.setBackground(Color.cyan);
        usernameLabel.setForeground(Color.DARK_GRAY);
        usernameLabel.setHorizontalAlignment(0);
        usernameLabel.setFont(timesNewRoman);
        usernameLabel.setOpaque(true);

        finderImage.setBounds(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT);

        menuBookingButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuBookingButton.setBackground(Color.DARK_GRAY);
        menuBookingButton.setForeground(Color.WHITE);
        menuBookingButton.setFont(timesNewRoman);
        menuBookingButton.addActionListener(new Verification('B'));
        buttonsY += diffInYs;

        menuTHButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuTHButton.setBackground(Color.DARK_GRAY);
        menuTHButton.setForeground(Color.WHITE);
        menuTHButton.setFont(timesNewRoman);
        menuTHButton.addActionListener(new Verification('T'));
        buttonsY += diffInYs;

        menuEnquiryButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuEnquiryButton.setBackground(Color.DARK_GRAY);
        menuEnquiryButton.setForeground(Color.WHITE);
        menuEnquiryButton.setFont(timesNewRoman);
        menuEnquiryButton.addActionListener(new Verification('E'));
        buttonsY += diffInYs;

        menuUpdateUsernameButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuUpdateUsernameButton.setBackground(Color.DARK_GRAY);
        menuUpdateUsernameButton.setForeground(Color.WHITE);
        menuUpdateUsernameButton.setFont(timesNewRoman);
        menuUpdateUsernameButton.addActionListener(new Verification('U'));
        buttonsY += diffInYs;

        menuPasswordChangeButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuPasswordChangeButton.setBackground(Color.DARK_GRAY);
        menuPasswordChangeButton.setForeground(Color.WHITE);
        menuPasswordChangeButton.setFont(timesNewRoman);
        menuPasswordChangeButton.addActionListener(new Verification('P'));
        buttonsY += diffInYs;

        menuAccountDeleteButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuAccountDeleteButton.setBackground(Color.DARK_GRAY);
        menuAccountDeleteButton.setForeground(Color.WHITE);
        menuAccountDeleteButton.setFont(timesNewRoman);
        menuAccountDeleteButton.addActionListener(new Verification('A'));
        buttonsY += diffInYs;

        menuSwitchAccountsButton.setBounds(menuButtonX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuSwitchAccountsButton.setBackground(Color.DARK_GRAY);
        menuSwitchAccountsButton.setForeground(Color.WHITE);
        menuSwitchAccountsButton.setFont(timesNewRoman);
        menuSwitchAccountsButton.addActionListener(new Verification('S'));
        buttonsY += diffInYs;

        logoutButton.setBounds(300, buttonsY, 100, menuButtonHeight);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(timesNewRoman);
        logoutButton.addActionListener(new Back((byte) 1));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    String[] places, temp = {"--Select--"};
    String city, source, destination;
    final String[] citiesArray = {"--Select--", "HYDERABAD", "BENGALURU", "CHENNAI"};
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
    class ChangingCombos implements ActionListener {
        JComboBox<String> cityField, sourceField, destinationField;

        ChangingCombos(JComboBox<String> cityField, JComboBox<String> sourceField, JComboBox<String> destinationField) {
            this.cityField = cityField;
            this.sourceField = sourceField;
            this.destinationField = destinationField;
        }

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

    void positioningTextAndDisablingEditingInJSpinner(JSpinner spinner) {
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setEditable(false);
        tf.setHorizontalAlignment(JLabel.LEFT);
    }


    class BookingsUI implements ActionListener {
        JLabel nameLabel, phoneLabel, adultLabel, childrenLabel, cityLabel, sourceLabel, destinationLabel, phoneMessage;
        JTextField nameText, phoneText;
        JComboBox<String> cityField, sourceField, destinationField;
        JSpinner adultField, childrenField;
        short noOfAdults, noOfChildren;
        String name, phone;

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();

            frame.setTitle("Booking");

            backButton = new JButton("Back");
            JButton continueButton = new JButton("Continue");

            nameLabel = new JLabel("Name:");
            nameLabel.setFont(timesNewRoman);
            nameText = new JTextField();
            nameText.setFont(timesNewRoman);

            phoneLabel = new JLabel("Mobile Number:");
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

            adultField = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            adultField.setFont(timesNewRoman);
            positioningTextAndDisablingEditingInJSpinner(adultField);

            childrenLabel = new JLabel("Children:");
            childrenLabel.setFont(timesNewRoman);

            childrenField = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            childrenField.setFont(timesNewRoman);
            positioningTextAndDisablingEditingInJSpinner(childrenField);

            msg = new JLabel();
            phoneMessage = new JLabel();

            frame.add(backButton);
            frame.add(nameLabel);
            frame.add(phoneLabel);
            frame.add(cityLabel);
            frame.add(sourceLabel);
            frame.add(destinationLabel);
            frame.add(adultLabel);
            frame.add(childrenLabel);
            frame.add(nameText);
            frame.add(phoneText);
            frame.add(cityField);
            frame.add(sourceField);
            frame.add(destinationField);
            frame.add(adultField);
            frame.add(childrenField);
            frame.add(continueButton);
            frame.add(msg);
            frame.add(phoneMessage);
            frame.add(logoutButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 2));

            nameLabel.setBounds(200, 50, 100, 25);
            phoneLabel.setBounds(200, 100, 130, 25);
            cityLabel.setBounds(200, 150, 100, 25);
            sourceLabel.setBounds(200, 200, 100, 25);
            destinationLabel.setBounds(200, 250, 100, 25);
            adultLabel.setBounds(200, 300, 100, 25);
            childrenLabel.setBounds(200, 350, 100, 25);

            nameText.setBounds(330, 50, 200, 25);
            nameText.setText(username);
            phoneText.setBounds(330, 100, 200, 25);
            cityField.setBounds(330, 150, 200, 25);
            sourceField.setBounds(330, 200, 200, 25);
            destinationField.setBounds(330, 250, 200, 25);
            adultField.setBounds(330, 300, 200, 25);
            childrenField.setBounds(330, 350, 200, 25);

            cityField.addActionListener(new ChangingCombos(cityField, sourceField, destinationField));

            continueButton.setBounds(275, 400, 150, 25);
            continueButton.setBackground(Color.GREEN);
            continueButton.setForeground(Color.WHITE);
            continueButton.setFont(timesNewRoman);
            continueButton.addActionListener(new ContinueToModeOfTransportation());

            phoneMessage.setBounds(510, 100, 200, 25);
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

        class ContinueToModeOfTransportation implements ActionListener {
            BookingMainCode bookingObj;
            String[] motOptions = {"Choose Mode Of Transportation Option", "One Transport for Whole Journey",
                    "Mode Of Transportation Place to Place"};
            JComboBox<String> modeOfTransportationCB = new JComboBox<>(motOptions);

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
                noOfAdults = Short.parseShort(String.valueOf(adultField.getValue()));
                noOfChildren = Short.parseShort(String.valueOf(childrenField.getValue()));

                if (name.equals("") || phone.equals("") || source.equals("--Select--") || destination.equals("--Select--")) {
                    msg.setBounds(225, 470, 250, 25);
                    msg.setText("Please Complete all the fields");
                } else if (source.equals(destination)) {
                    msg.setBounds(200, 470, 350, 25);
                    msg.setText("Source and Destination Cannot be the same");
                } else if (!phone.matches("^[6-9]\\d{9}")) {
                    phoneMessage.setText("Invalid Phone Number");
                } else {
                    bookingObj = new BookingMainCode(city, source, destination, noOfAdults, noOfChildren);

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
                            Short.parseShort(String.valueOf(adultField.getValue())),
                            Short.parseShort(String.valueOf(childrenField.getValue())));
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
        thHeading.setVerticalAlignment(0);
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


    class EnquireUI {
        JComboBox<String> enquireCityField = new JComboBox<>(citiesArray);
        JComboBox<String> enquireSourceField = new JComboBox<>(temp);
        JComboBox<String> enquireDestinationField = new JComboBox<>(temp);

        void enquireUI() {
            frame.getContentPane().removeAll();
            frame.repaint();

            backButton = new JButton("Back");

            JLabel enquireCityLabel = new JLabel("City:");
            JLabel enquireSourceLabel = new JLabel("Source:");
            JLabel enquireDestinationLabel = new JLabel("Destination:");

            msg = new JLabel();

            JButton enquireButton = new JButton("Enquire");

            frame.add(backButton);
            frame.add(enquireCityLabel);
            frame.add(enquireCityField);
            frame.add(enquireSourceLabel);
            frame.add(enquireSourceField);
            frame.add(enquireDestinationLabel);
            frame.add(enquireDestinationField);
            frame.add(enquireButton);
            frame.add(logoutButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.addActionListener(new Back((byte) 2));

            enquireCityLabel.setBounds(200, 200, 100, 25);
            enquireCityLabel.setFont(timesNewRoman);

            enquireSourceLabel.setBounds(200, 250, 100, 25);
            enquireSourceLabel.setFont(timesNewRoman);

            enquireDestinationLabel.setBounds(200, 300, 100, 25);
            enquireDestinationLabel.setFont(timesNewRoman);

            enquireCityField.setBounds(300, 200, 200, 25);
            enquireCityField.setFont(timesNewRoman);

            enquireSourceField.setBounds(300, 250, 200, 25);
            enquireSourceField.setFont(timesNewRoman);

            enquireDestinationField.setBounds(300, 300, 200, 25);
            enquireDestinationField.setFont(timesNewRoman);

            enquireCityField.addActionListener(new ChangingCombos(enquireCityField, enquireSourceField, enquireDestinationField));

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
                msg.setBounds(0, 400, frameSize, frameSize);
                msg.setFont(timesNewRoman);
                msg.setForeground(Color.RED);
                msg.setHorizontalAlignment(0);
                msg.setOpaque(true);

                frame.add(msg);

                String city = String.valueOf(enquireCityField.getSelectedItem());
                String source = String.valueOf(enquireSourceField.getSelectedItem());
                String destination = String.valueOf(enquireDestinationField.getSelectedItem());

                if (city.equals("--Select--") || source.equals("--Select--") || destination.equals("--Select--")) {
                    msg.setText("Please Complete all the Fields");
                } else if (source.equals(destination)) {
                    msg.setText("Source and Destination Cannot be same");
                } else {
                    JLabel routeCostMessage = new JLabel();

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

                    routeCostMessage.setBounds(0, 0, 500, 500);
                    routeCostMessage.setHorizontalAlignment(0);
                    routeCostMessage.setVerticalAlignment(0);
                    routeCostMessage.setFont(timesNewRoman);
                    routeCostMessage.setText(String.valueOf(routeCost).replaceAll("\n", "<br>"));
                    routeCostMessage.setText(routeCostMessage.getText().toUpperCase());

                    JOptionPane.showMessageDialog(frame, routeCostMessage,
                            enquiryObj.source.toUpperCase() + " to " + enquiryObj.destination.toUpperCase(),
                            JOptionPane.INFORMATION_MESSAGE);

                    // store the enquiries
                    File enquiryFile = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + username + ".txt");
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(enquiryFile, true));
                        writer.write(city.toUpperCase() + "," + source.toUpperCase() + "," + destination.toUpperCase() + "," + cost + "\n");
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
        JTextField newUsernameText = new JTextField();

        void updateUsernameUI() {
            updateUsernameFrame.setSize(400, 400);
            updateUsernameFrame.setTitle("Update Username");
            updateUsernameFrame.setLocationRelativeTo(frame);
            updateUsernameFrame.setVisible(true);
            updateUsernameFrame.setLayout(null);

            JLabel newUsernameLabel = new JLabel("New Username:");
            JButton changeUsernameButton = new JButton("Change Username");

            updateUsernameFrame.add(newUsernameLabel);
            updateUsernameFrame.add(newUsernameText);
            updateUsernameFrame.add(changeUsernameButton);

            newUsernameLabel.setBounds(90, 160, 120, 25);
            newUsernameLabel.setFont(timesNewRoman);

            newUsernameText.setBounds(210, 160, 100, 25);
            newUsernameText.setFont(timesNewRoman);

            changeUsernameButton.setBounds(100, 230, 200, 25);
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

                updateUsernameFrame.add(msg);

                msg.setBounds(0, 300, 400, 25);
                msg.setHorizontalAlignment(0);

                if (newUsername.equals("")) {
                    msg.setText("Please type new Username");
                } else {
                    boolean found = false;
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
                            msg.setText("");
                            int result = JOptionPane.showConfirmDialog(updateUsernameFrame, "Are You Sure?", "Confirmation",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
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

                                    JOptionPane.showMessageDialog(frame, "Username Changed Successfully", "Successful",
                                            JOptionPane.INFORMATION_MESSAGE);

                                    displayMenu();
                                } else {
                                    JOptionPane.showMessageDialog(updateUsernameFrame, "Error Occurred. Username Didn't Change", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    class PasswordChangeUI {
        JFrame passwordChangeFrame;
        JPasswordField newPasswordField, rePasswordField;

        void passwordChangeUI() {
            passwordChangeFrame = new JFrame("Change Password");
            passwordChangeFrame.setVisible(true);
            passwordChangeFrame.setLayout(null);
            passwordChangeFrame.setSize(400, 400);
            passwordChangeFrame.setLocationRelativeTo(frame);

            backButton = new JButton("Back");
            JLabel newPasswordLabel = new JLabel("New Password:");
            JLabel rePasswordLabel = new JLabel("Retype New Password:");
            newPasswordField = new JPasswordField();
            rePasswordField = new JPasswordField();
            JButton passwordChangeButton = new JButton("Change Password");
            JCheckBox passwordChangeCB1 = new JCheckBox("Show Password");
            JCheckBox passwordChangeCB2 = new JCheckBox("Show Password");

            passwordChangeFrame.add(newPasswordLabel);
            passwordChangeFrame.add(rePasswordLabel);
            passwordChangeFrame.add(newPasswordField);
            passwordChangeFrame.add(rePasswordField);
            passwordChangeFrame.add(passwordChangeCB1);
            passwordChangeFrame.add(passwordChangeCB2);
            passwordChangeFrame.add(passwordChangeButton);

            newPasswordLabel.setBounds(90, 100, 120, 25);
            newPasswordLabel.setFont(timesNewRoman);

            newPasswordField.setBounds(210, 100, 100, 25);
            newPasswordField.setFont(timesNewRoman);

            passwordChangeCB1.setBounds(140, 130, 150, 25);
            passwordChangeCB1.setFont(timesNewRoman);
            passwordChangeCB1.addActionListener(new ShowPasswordsCheckBox(newPasswordField));

            rePasswordLabel.setBounds(40, 160, 180, 25);
            rePasswordLabel.setFont(timesNewRoman);

            rePasswordField.setBounds(210, 160, 100, 25);
            rePasswordField.setFont(timesNewRoman);

            passwordChangeCB2.setBounds(140, 190, 150, 25);
            passwordChangeCB2.setFont(timesNewRoman);
            passwordChangeCB2.addActionListener(new ShowPasswordsCheckBox(rePasswordField));

            passwordChangeButton.setBounds(100, 225, 180, 25);
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
                } else if (isPasswordAccepted(newPassword)) {
                    int result = JOptionPane.showConfirmDialog(passwordChangeFrame, "Are You Sure?", "Confirm",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        if (new PasswordChangeMainCode().passwordChange(username, password, newPassword)) {
                            password = newPassword;
                            passwordChangeFrame.dispose();
                            JOptionPane.showMessageDialog(frame, "Password Changed Successfully",
                                    "Password Change Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(passwordChangeFrame, "Some Error Occurred. Couldn't Change Password\nTry After Some time",
                                    "Error Occurred", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }


    void AccountDeletion(JFrame frame1) {
        int result = JOptionPane.showConfirmDialog(frame1,
                "Are You Sure?\nAll Your Transactions, Enquiries will be lost\nThis is un-reversible",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            frame1.dispose();
            boolean deleted = new AccountDeletionMainCode().accountDeletion(username, password);
            if (deleted) {
                Homepage();
                JOptionPane.showMessageDialog(frame, "Account Deleted Successfully\nWe are Sorry to see you go",
                        "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Some Error Occurred, Account not Deleted",
                        "Account Deleted", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            displayMenu();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class SwitchAccountsUI {
        JFrame switchAccountsFrame;
        JTextField switchingUserText;
        JPasswordField switchingPassword;

        void switchAccountsUI() {
            switchAccountsFrame = new JFrame();
            switchAccountsFrame.setLayout(null);
            switchAccountsFrame.setSize(300, 300);
            switchAccountsFrame.setVisible(true);
            switchAccountsFrame.setLocationRelativeTo(frame);
            switchAccountsFrame.setTitle("Switching Accounts");

            msg = new JLabel();
            JLabel switchingUserLabel = new JLabel("Username:");
            JLabel switchingPasswordLabel = new JLabel("Password:");
            switchingUserText = new JTextField();
            switchingPassword = new JPasswordField();
            JCheckBox switchingCB = new JCheckBox("Show Password");
            JButton switchAccountButton = new JButton("Switch Account");

            switchAccountsFrame.add(switchingUserLabel);
            switchAccountsFrame.add(switchingPasswordLabel);
            switchAccountsFrame.add(switchingUserText);
            switchAccountsFrame.add(switchingPassword);
            switchAccountsFrame.add(switchingCB);
            switchAccountsFrame.add(switchAccountButton);
            switchAccountsFrame.add(msg);

            switchingUserLabel.setBounds(50, 80, 80, 25);
            switchingUserLabel.setFont(timesNewRoman);

            switchingUserText.setBounds(130, 80, 120, 25);
            switchingUserText.setFont(timesNewRoman);

            switchingPasswordLabel.setBounds(50, 110, 80, 25);
            switchingPasswordLabel.setFont(timesNewRoman);

            switchingPassword.setBounds(130, 110, 120, 25);
            switchingPassword.setFont(timesNewRoman);

            switchingCB.setBounds(90, 140, 150, 25);
            switchingCB.addActionListener(new ShowPasswordsCheckBox(switchingPassword));

            switchAccountButton.setBounds(75, 180, 150, 25);
            switchAccountButton.setBackground(Color.DARK_GRAY);
            switchAccountButton.setForeground(Color.WHITE);
            switchAccountButton.setFont(timesNewRoman);
            switchAccountButton.addActionListener(new SwitchAccountsMainCode());

            msg.setBounds(0, 210, 300, 25);
            msg.setFont(timesNewRoman);
            msg.setHorizontalAlignment(0);
        }

        class SwitchAccountsMainCode implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String localUsername = switchingUserText.getText();
                String localPassword = String.valueOf(switchingPassword.getPassword());

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
                            if (localUsername.equals(credentials[0]) &&
                                    String.valueOf(encryptPassword(localPassword)).equals(credentials[1])) {
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
                                JOptionPane.WARNING_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            username = localUsername;
                            password = localPassword;
                            switchAccountsFrame.dispose();
                            usernameLabel.setText("Username: " + username);
                            JOptionPane.showMessageDialog(frame, "Switched Accounts Successfully", "Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        }
    }
}
