package EazyFinderGUI;

import EazyFinderGUI.MainCodes.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/*
TODO:
1. use sudo mode as in GitHub
2. caps lock warning while typing passwords
3. BUGS: menu buttons are bringing "n" verification frames for nth clicking, When logout button is pressed it is giving multiple option panes
4. guest mode option (show option panes like "Guests cannot update their usernames or passwords or cannot switch accounts" after clicking the
button. Give options like "Look at UI" and show the UI)
5. add profile and settings option (dark mode (optional))
6. give ref no. while signup and store them in the file for verification
7. add clear form option and also cross symbol in text fields
8. go from login to signup if credentials are not found
9. sorting th based on filters
10. add sorting option based on filters wherever possible
*/

public class EazyFinderGUI {
    // While using an IDE "Edit Configurations" by setting the Working Directory path till src if it is not already present
    // System.getProperty("user.dir") return path upto src or change the program accordingly
    String dirname = System.getProperty("user.dir") + "\\EazyFinderGUI";
    String username, password, name, phoneNumber, emailID;
    final File db = new File(dirname + "\\Databases\\LogInSignUpDatabase.txt");
    final File userDetailsFile = new File(dirname + "\\Databases\\UserDetails.txt");

    JFrame frame = new JFrame();
    JButton backButton, logoutButton = new JButton("Logout");
    JLabel msg; // Used to print corresponding messages
    final Font timesNewRoman = new Font("Times New Roman", Font.BOLD, 15);
    final short frameSize = 700;
    final Image icon = Toolkit.getDefaultToolkit().getImage(dirname + "\\finder.png");

    JLabel optionPaneLabel = new JLabel(); // label added into JOptionPanes
    int optionPaneResult; // used for option pane results

    // All Objects
    LoginUI loginUIObj = new LoginUI();
    SignUpUI signUpUIObj = new SignUpUI();
    GuestMode guestModeObj = new GuestMode();

    public static void main(String[] args) {
        new EazyFinderGUI().Homepage();
    }

    final JButton homepageLoginButton = new JButton("LogIn");
    final JButton homepageSignupButton = new JButton("SignUp");
    final JButton homepageGuestButton = new JButton("Browse as Guest");
    final JLabel infoLabel = new JLabel(new ImageIcon(dirname + "\\Images\\information.png"));

    void Homepage() {
        frame.getContentPane().removeAll();
        frame.repaint();

        frame.setSize(350, 300);
        frame.setTitle("Homepage");
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setIconImage(icon);
        frame.setLocationRelativeTo(null);
//        frame.setResizable(false);

        optionPaneLabel.setFont(timesNewRoman);

        frame.add(homepageLoginButton);
        frame.add(homepageSignupButton);
        frame.add(homepageGuestButton);
        frame.add(infoLabel);

        homepageLoginButton.setBounds(50, 95, 100, 30);
        homepageLoginButton.setForeground(Color.BLACK);
        homepageLoginButton.setBackground(Color.ORANGE);
        homepageLoginButton.setFont(timesNewRoman);
        homepageLoginButton.setToolTipText("Click this button to Login");
        homepageLoginButton.addActionListener(loginUIObj);

        homepageSignupButton.setBounds(180, 95, 100, 30);
        homepageSignupButton.setForeground(Color.BLACK);
        homepageSignupButton.setBackground(Color.ORANGE);
        homepageSignupButton.setFont(timesNewRoman);
        homepageSignupButton.setToolTipText("Click this button to Create a new Account for yourself");
        homepageSignupButton.addActionListener(signUpUIObj);

        homepageGuestButton.setBounds(50, 142, 230, 30);
        homepageGuestButton.setForeground(Color.WHITE);
        homepageGuestButton.setBackground(Color.DARK_GRAY);
        homepageGuestButton.setFont(timesNewRoman);
        homepageGuestButton.setToolTipText("Have the experience of a registered user without registering");
        homepageGuestButton.addActionListener(guestModeObj);

        infoLabel.setBounds(320, 0, 16, 16);
        infoLabel.setToolTipText("Email: programmerrohith@gmail.com");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class GuestMode implements ActionListener {
        String[] guestPaneOptions = {"Browse as Guest", "Go Back"};

        @Override
        public void actionPerformed(ActionEvent e) {
            username = "Guest";
            password = "Guest@123";

            if (JOptionPane.showOptionDialog(frame, """
                            We are giving you the guest credentials so that you can have the same experience as a registered user

                            Username: Guest
                            Password: Guest@123
                                                        
                            We delete all the date provided by you in the guest mode once you logout
                            So feel free to be a registered user
                                                        
                            Happy Browsing 😃""", "Guest Mode", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, guestPaneOptions, null) == JOptionPane.YES_OPTION) {
                displayMenu();
            }
            // Create files, entry in database
            // delete all data after logout
        }
    }

    int areYouSureJOP(JFrame jFrame) {
        optionPaneLabel.setText("Are You Sure?");
        return JOptionPane.showConfirmDialog(jFrame, optionPaneLabel, "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
    }

    class Back implements ActionListener {
        byte num;

        Back(byte num) {
            this.num = num;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (num == 1 || num == 2) {
                if (areYouSureJOP(frame) == JOptionPane.YES_OPTION) {
                    if (num == 1) Homepage();
                    else if (num == 2) displayMenu();
                }
            } else if (num == 3) {
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

    // Variables used in Login and SignUp
    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel rePasswordLabel = new JLabel("Re-Type Password:");

    class LoginUI implements ActionListener {
        JTextField userField;
        JPasswordField passwordField;
        JButton loginButton = new JButton("LogIn");
        JCheckBox showPasswordCB;

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("Login");

            backButton = new JButton("Back");
            userField = new JTextField();
            passwordField = new JPasswordField();
            showPasswordCB = new JCheckBox("Show Password");
            msg = new JLabel();

            frame.add(backButton);
            frame.add(userLabel);
            frame.add(passwordLabel);
            frame.add(userField);
            frame.add(passwordField);
            frame.add(showPasswordCB);
            frame.add(loginButton);
            frame.add(msg);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 1));

            userLabel.setBounds(50, 50, 80, 25);
            userLabel.setFont(timesNewRoman);

            userField.setBounds(130, 50, 120, 25);
            userField.setFont(timesNewRoman);

            passwordLabel.setBounds(50, 80, 80, 25);
            passwordLabel.setFont(timesNewRoman);

            passwordField.setBounds(130, 80, 120, 25);
            passwordField.setFont(timesNewRoman);

            userField.setText("r");
            passwordField.setText("Rohith_02");

            showPasswordCB.setBounds(90, 110, 150, 25);
            showPasswordCB.setFont(timesNewRoman);
            showPasswordCB.addActionListener(new ShowPasswordsCheckBox(passwordField));

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
                username = userField.getText().trim();
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
                            credentials = str.split(",");
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
                        getPPDetails();
                        displayMenu();
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }

    /**
     * This Method is used to show a message dialog that contains a JLabel.
     * The messages are kept in JLabel so that they can be equipped with
     * "timesNewRoman" font.
     * optionPaneLabel has been set to have font "timesNewRoman" in Homepage method
     */
    void showMessageDialogJOP(JFrame jFrame, String msg, String title, int magicConstant) {
        optionPaneLabel.setText(msg);
        JOptionPane.showMessageDialog(jFrame, optionPaneLabel, title, magicConstant);
    }

    boolean isPasswordAccepted(String password, JFrame frame1) {
        boolean inRange = false, hasWhiteSpace = false, hasLowerCaseLetter = false, hasUpperCaseLetter = false, hasDigit = false,
                hasSpecialCharacter = false;
        byte i, len = (byte) password.length();
        if (len >= 8 && len <= 16)
            inRange = true;

        msg.setText("");

        if (!inRange) {
            showMessageDialogJOP(frame1, "Password isn't in the Range of 8-16\nPlease try with another Password",
                    "Password Not Accepted", JOptionPane.ERROR_MESSAGE);
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

                JOptionPane.showMessageDialog(frame1, message, "Password Not Accepted", JOptionPane.ERROR_MESSAGE);
            }
        }

        return inRange && !hasWhiteSpace && hasLowerCaseLetter && hasUpperCaseLetter && hasDigit && hasSpecialCharacter;
    }

    String phoneNumberRegex = "^[6-9]\\d{9}", emailIDRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    // profile picture attributes;
    String profilePicturePath = dirname + "\\Images\\finder.png", profilePictureExtension = ".png";
    ImageIcon profilePicture = new ImageIcon(dirname + "\\Images\\finder.png");
    int profilePictureWidth, profilePictureHeight;
    String tempProfilePicturePath = dirname + "\\Images\\finder.png";
    ImageIcon tempProfilePicture = new ImageIcon(dirname + "\\Images\\finder.png");
    int tempProfilePictureWidth, tempProfilePictureHeight;

    int i;
    String[] optionPaneButtonNames = {"Confirm", "No, I want to Select Another"};
    FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);

    int setPPDetails() {
        fd.setVisible(true);
        if (fd.getFile() == null) {
            showMessageDialogJOP(frame, "No Picture Selected", "No Picture Selected", JOptionPane.WARNING_MESSAGE);
            return JOptionPane.NO_OPTION;
        } else {
            if (fd.getFile() != null) {
                tempProfilePicturePath = fd.getDirectory() + fd.getFile();
                tempProfilePicture = new ImageIcon(tempProfilePicturePath);
            }

            tempProfilePictureWidth = tempProfilePicture.getIconWidth();
            tempProfilePictureHeight = tempProfilePicture.getIconHeight();
            if (tempProfilePictureWidth <= 500 && tempProfilePictureHeight <= 500) {
                profilePicturePath = tempProfilePicturePath;
                profilePicture = tempProfilePicture;
                profilePictureWidth = tempProfilePictureWidth;
                profilePictureHeight = tempProfilePictureHeight;

                i = profilePicturePath.lastIndexOf('.');
                if (i > 0) profilePictureExtension = profilePicturePath.substring(i);

                if (profilePictureExtension.equals(".png") || profilePictureExtension.equals(".jpg") || profilePictureExtension.equals(".jpeg")) {
                    optionPaneLabel.setText(profilePicturePath);
                    optionPaneResult = JOptionPane.showOptionDialog(frame, optionPaneLabel, "Profile Picture Path",
                            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, optionPaneButtonNames, null);
                } else {
                    showMessageDialogJOP(frame, "<html>You have selected a non-supported file\nPlease choose another file\n" +
                                    "Supported Files: .png, .jpg, .jpeg</html>".replaceAll("\n", "<br>"),
                            "File Not Supported", JOptionPane.ERROR_MESSAGE);
                    return JOptionPane.NO_OPTION;
                }
            } else {
                if (tempProfilePictureWidth > 500 && tempProfilePictureHeight > 500)
                    optionPaneLabel.setText("The Image you have chosen is larger than dimension 500x500");
                else if (tempProfilePictureWidth > 500) optionPaneLabel.setText("The image you have chosen has a width > 500");
                else optionPaneLabel.setText("The image you have chosen has a height > 500");
                JOptionPane.showMessageDialog(frame, optionPaneLabel, "Dimension Error", JOptionPane.ERROR_MESSAGE);
                return JOptionPane.NO_OPTION;
            }
        }
        return optionPaneResult;
    }

    void getPPDetails() {
        File ppDir = new File(dirname + "\\ProfilePictures");
        int i;
        String fileName;
        for (File file : Objects.requireNonNull(ppDir.listFiles())) {
            fileName = file.getName();
            i = fileName.lastIndexOf(".");
            if (username.equals(fileName.substring(0, i))) {
                profilePicture = new ImageIcon(file.getAbsolutePath());
                profilePictureExtension = fileName.substring(i);
                profilePictureWidth = profilePicture.getIconWidth();
                profilePictureHeight = profilePicture.getIconHeight();
                break;
            }
        }
    }

    boolean fileDeleted;

    void savePP() {
        try {
            fileDeleted = true;
            File destinationFile = new File(dirname + "\\ProfilePictures\\" + username + profilePictureExtension);

            File ppDir = new File(dirname + "\\ProfilePictures");
            int i;
            String fileName;
            for (File file : Objects.requireNonNull(ppDir.listFiles())) {
                fileName = file.getName();
                i = fileName.lastIndexOf(".");
                if (username.equals(fileName.substring(0, i))) {
                    fileDeleted = file.delete();
                    break;
                }
            }

            if (fileDeleted && destinationFile.createNewFile())
                Files.copy(Paths.get(profilePicturePath), new FileOutputStream(destinationFile));
        } catch (IOException ignored) {
        }
    }

    class SignUpUI implements ActionListener {
        JTextField userText;
        JPasswordField passwordField, rePasswordField;
        JButton nextButton = new JButton("Next");
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
            frame.add(nextButton);
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

            nextButton.setBounds(125, 200, 100, 25);
            nextButton.setBackground(Color.DARK_GRAY);
            nextButton.setForeground(Color.WHITE);
            nextButton.setFont(timesNewRoman);
            nextButton.addActionListener(new CredentialCheck());

            msg.setBounds(0, 230, 350, 25);
            msg.setFont(timesNewRoman);
            msg.setHorizontalAlignment(JLabel.CENTER);
        }

        class CredentialCheck implements ActionListener {
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
                            if (username.equals(str.split(",")[0])) {
                                msg.setText("Username Already Taken");
                                found = true;
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception ex) {
                        msg.setText("Error in reading file");
                    }

                    if (!found && isPasswordAccepted(password, frame)) registrationForm();
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }

        // Registration Form Variables
        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneLabel = new JLabel("Mobile Number:");
        JLabel emailLabel = new JLabel("Email ID:");
        JLabel chooseProfilePictureLabel = new JLabel("Choose Profile Picture:");
        JButton selectProfilePictureButton = new JButton("Select");
        JTextField nameField, phoneField, emailField;
        JButton signupButton = new JButton("SignUp");

        final int registrationFrameSize = 450;

        void registrationForm() {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("Registration Form");
            frame.setSize(registrationFrameSize, registrationFrameSize);
            frame.setLocationRelativeTo(null);

            backButton = new JButton("Back");
            nameField = new JTextField();
            phoneField = new JTextField();
            emailField = new JTextField();

            frame.add(backButton);
            frame.add(chooseProfilePictureLabel);
            frame.add(selectProfilePictureButton);
            frame.add(nameLabel);
            frame.add(nameField);
            frame.add(phoneLabel);
            frame.add(phoneField);
            frame.add(emailLabel);
            frame.add(emailField);
            frame.add(signupButton);
            frame.add(logoutButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 1));

            chooseProfilePictureLabel.setBounds(100, 85, 170, 25);
            chooseProfilePictureLabel.setFont(timesNewRoman);

            selectProfilePictureButton.setText("Select");
            selectProfilePictureButton.setBounds(270, 85, 100, 25);
            selectProfilePictureButton.setFont(timesNewRoman);
            selectProfilePictureButton.setBackground(Color.DARK_GRAY);
            selectProfilePictureButton.setForeground(Color.WHITE);
            selectProfilePictureButton.addActionListener(e -> {
                if (setPPDetails() == JOptionPane.YES_OPTION) {
                    selectProfilePictureButton.setText("Selected");
                    selectProfilePictureButton.setBackground(Color.GREEN);
                }
            });

            nameLabel.setBounds(100, 135, 100, 25);
            nameLabel.setFont(timesNewRoman);

            nameField.setBounds(220, 135, 150, 25);
            nameField.setFont(timesNewRoman);

            phoneLabel.setBounds(100, 185, 120, 25);
            phoneLabel.setFont(timesNewRoman);

            phoneField.setBounds(220, 185, 150, 25);
            phoneField.setFont(timesNewRoman);

            emailLabel.setBounds(100, 235, 100, 25);
            emailLabel.setFont(timesNewRoman);

            emailField.setBounds(220, 235, 150, 25);
            emailField.setFont(timesNewRoman);

            signupButton.setBounds(175, 285, 100, 25);
            signupButton.setBackground(Color.DARK_GRAY);
            signupButton.setForeground(Color.WHITE);
            signupButton.setFont(timesNewRoman);
            signupButton.addActionListener(new SignUpMainCode());
        }

        class SignUpMainCode implements ActionListener {
            JLabel phoneMessage = new JLabel();
            JLabel emailErrorMessage = new JLabel();

            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText().trim();
                phoneNumber = phoneField.getText().trim();
                emailID = emailField.getText().trim();

                frame.add(phoneMessage);
                frame.add(emailErrorMessage);

                phoneMessage.setText("");
                phoneMessage.setBounds(0, 320, registrationFrameSize, 25);
                phoneMessage.setFont(timesNewRoman);
                phoneMessage.setForeground(Color.RED);
                phoneMessage.setOpaque(true);
                phoneMessage.setHorizontalAlignment(0);

                emailErrorMessage.setText("");
                emailErrorMessage.setBounds(0, 350, registrationFrameSize, 25);
                emailErrorMessage.setFont(timesNewRoman);
                emailErrorMessage.setForeground(Color.RED);
                emailErrorMessage.setOpaque(true);
                emailErrorMessage.setHorizontalAlignment(0);

                if (selectProfilePictureButton.getText().equals("Select")) {
                    phoneMessage.setText("Please select a Profile Picture");
                } else if (name.equals("") || phoneNumber.equals("") || emailID.equals("")) {
                    phoneMessage.setText("Please fill all the Fields");
                } else if (!phoneNumber.matches(phoneNumberRegex) || !emailID.matches(emailIDRegex)) {
                    if (!phoneNumber.matches(phoneNumberRegex)) phoneMessage.setText("Invalid Phone Number");
                    if (!emailID.matches(emailIDRegex)) emailErrorMessage.setText("Invalid Email Address");
                } else {
                    try {
                        // Adding the new User Credentials into the database
                        BufferedWriter writer;
                        writer = new BufferedWriter(new FileWriter(db, true));
                        writer.write(username + "," + encryptPassword(password) + "\n");
                        writer.flush();
                        writer.close();

                        // Adding the user details into the UserDetails.txt file
                        writer = new BufferedWriter(new FileWriter(userDetailsFile, true));
                        writer.write(username + "," + name + "," + phoneNumber + "," + emailID + "\n");
                        writer.flush();
                        writer.close();

                        // save the profile picture
                        savePP();

                        // Creating Transaction History and Enquiry Files for the user
                        File th = new File(dirname + "\\TransactionHistories\\" + username + ".txt");
                        File en = new File(dirname + "\\Enquiries\\" + username + ".txt");
                        if (th.createNewFile() && en.createNewFile()) {
                            showMessageDialogJOP(frame, "Account Created Successfully", "SignUp Successful", JOptionPane.INFORMATION_MESSAGE);
                            displayMenu();
                        }
                    } catch (Exception ex) {
                        if (new File(profilePicturePath).delete()) {
                            showMessageDialogJOP(frame, "Due to some Error we couldn't create your account", "Account Not Created", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }

    class Verification implements ActionListener {
        String case_;

        Verification(String case_) {
            this.case_ = case_;
        }

        JFrame verificationFrame;
        JPasswordField verificationPasswordField;

        BookingUI bookingsObj = new BookingUI();
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
            if (case_.equals("AccountDeletion")) {
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
                    if (!case_.equals("AccountDeletion")) verificationFrame.dispose();
                    else AccountDeletion(verificationFrame);

                    switch (case_) {
                        case "Booking" -> bookingsObj.bookingUI(0);
                        case "TH" -> TransactionHistory();
                        case "Enquiry" -> enqObj.enquireUI();
                        case "UpdateUsername" -> updateUsernameObj.updateUsernameUI();
                        case "PasswordChange" -> passwordChangeObj.passwordChangeUI();
                        case "SwitchAccounts" -> switchAccountsObj.switchAccountsUI();
                    }
                }
            }
        }
    }

    JLabel usernameLabel = new JLabel();

    final int menuButtonWidth = 300, menuButtonHeight = 30, buttonsX = 200, diffInYs = 70;
    int buttonsY = 130;
    final int IMAGE_WIDTH = 64, IMAGE_HEIGHT = 64, imageX = (frameSize - IMAGE_WIDTH) / 2, imageY = (buttonsY - IMAGE_HEIGHT) / 2;

    JButton menuBookingButton = new JButton("Book for a Journey");
    JButton menuTHButton = new JButton("See Transaction History");
    JButton menuUpdateUsernameButton = new JButton("Update Username");
    JButton menuEnquiryButton = new JButton("Enquire");
    JButton menuPasswordChangeButton = new JButton("Change Password");
    JButton menuAccountDeleteButton = new JButton("Delete my Account");
    JButton menuSwitchAccountsButton = new JButton("Switch Accounts");
    final JLabel finderImage = new JLabel(new ImageIcon(dirname + "\\Images\\finder.png"));

    String[] settingsMenu = {"Menu", "Account", "Settings"};
    JComboBox<String> settings = new JComboBox<>(settingsMenu);

    // use type of singleton class because the frame ui is static, not needed to always set bounds and all TODO
    void displayMenu() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setSize(frameSize, frameSize);
        frame.setTitle("EazyFinder");
        frame.setLocationRelativeTo(null);

        frame.add(settings);
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

        settings.setBounds(550, 30, 100, 25);
        settings.setSelectedItem("Menu");
        settings.setFont(timesNewRoman);
        settings.addActionListener(new Settings());

        usernameLabel.setText("Username: " + username);
        usernameLabel.setBounds(0, 0, frameSize, 25);
        usernameLabel.setBackground(Color.cyan);
        usernameLabel.setForeground(Color.DARK_GRAY);
        usernameLabel.setHorizontalAlignment(0);
        usernameLabel.setFont(timesNewRoman);
        usernameLabel.setOpaque(true);

        finderImage.setBounds(imageX, imageY, IMAGE_WIDTH, IMAGE_HEIGHT);

        menuBookingButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuBookingButton.setBackground(Color.DARK_GRAY);
        menuBookingButton.setForeground(Color.WHITE);
        menuBookingButton.setFont(timesNewRoman);
        menuBookingButton.addActionListener(new Verification("Booking"));
        buttonsY += diffInYs;

        menuTHButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuTHButton.setBackground(Color.DARK_GRAY);
        menuTHButton.setForeground(Color.WHITE);
        menuTHButton.setFont(timesNewRoman);
        menuTHButton.addActionListener(new Verification("TH"));
        buttonsY += diffInYs;

        menuEnquiryButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuEnquiryButton.setBackground(Color.DARK_GRAY);
        menuEnquiryButton.setForeground(Color.WHITE);
        menuEnquiryButton.setFont(timesNewRoman);
        menuEnquiryButton.addActionListener(new Verification("Enquiry"));
        buttonsY += diffInYs;

        menuUpdateUsernameButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuUpdateUsernameButton.setBackground(Color.DARK_GRAY);
        menuUpdateUsernameButton.setForeground(Color.WHITE);
        menuUpdateUsernameButton.setFont(timesNewRoman);
        menuUpdateUsernameButton.addActionListener(new Verification("UpdateUsername"));
        buttonsY += diffInYs;

        menuPasswordChangeButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuPasswordChangeButton.setBackground(Color.DARK_GRAY);
        menuPasswordChangeButton.setForeground(Color.WHITE);
        menuPasswordChangeButton.setFont(timesNewRoman);
        menuPasswordChangeButton.addActionListener(new Verification("PasswordChange"));
        buttonsY += diffInYs;

        menuAccountDeleteButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuAccountDeleteButton.setBackground(Color.DARK_GRAY);
        menuAccountDeleteButton.setForeground(Color.WHITE);
        menuAccountDeleteButton.setFont(timesNewRoman);
        menuAccountDeleteButton.addActionListener(new Verification("AccountDeletion"));
        buttonsY += diffInYs;

        menuSwitchAccountsButton.setBounds(buttonsX, buttonsY, menuButtonWidth, menuButtonHeight);
        menuSwitchAccountsButton.setBackground(Color.DARK_GRAY);
        menuSwitchAccountsButton.setForeground(Color.WHITE);
        menuSwitchAccountsButton.setFont(timesNewRoman);
        menuSwitchAccountsButton.addActionListener(new Verification("SwitchAccounts"));
        buttonsY += diffInYs;

        logoutButton.setBounds(300, buttonsY, 100, menuButtonHeight);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(timesNewRoman);
        logoutButton.addActionListener(new Back((byte) 1));

        buttonsY = 130; // starting button is at 130 from top

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class Settings implements ActionListener {
        // Variables needed for Account Page
        JLabel accountLabel = new JLabel("Account");

        // Components related to profile picture
        JLabel profilePictureInAccount = new JLabel();
        JButton viewPhotoButton = new JButton("View Photo");
        JButton changePhotoButton = new JButton("Change Photo");
        JButton deletePhotoButton = new JButton("Delete Photo");

        JLabel usernameLabel = new JLabel("Username: " + username);
        JLabel passwordLabel = new JLabel("Password: " + password);
        JLabel noOfTransactionsLabel = new JLabel();
        JButton goToTHButton = new JButton("Go to Transactions Page");

        final Font headingFont = new Font("Times New Roman", Font.BOLD, 25);

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();

            if (settings.getSelectedIndex() == 0) {
                displayMenu();
            } else if (settings.getSelectedIndex() == 1) {
                backButton = new JButton("Back");

                frame.add(backButton);
                frame.add(accountLabel);
                frame.add(profilePictureInAccount);
                frame.add(viewPhotoButton);
                frame.add(changePhotoButton);
                frame.add(deletePhotoButton);
                frame.add(usernameLabel);
                frame.add(passwordLabel);
                frame.add(noOfTransactionsLabel);
                frame.add(goToTHButton);
                frame.add(logoutButton);

                backButton.setBounds(0, 0, 80, 30);
                backButton.setBackground(Color.BLACK);
                backButton.setForeground(Color.WHITE);
                backButton.setFont(timesNewRoman);
                backButton.addActionListener(new Back((byte) 3));

                accountLabel.setBounds(0, 30, frameSize, 25);
                accountLabel.setHorizontalAlignment(0);
                accountLabel.setFont(headingFont);
                accountLabel.setToolTipText(username + "'s Account");

                int width = profilePictureWidth, height = profilePictureHeight;
                if (width >= 375) width /= 2;
                else if (width >= 250) width /= 1.5;

                if (height >= 375) height /= 2;
                else if (height >= 250) height /= 1.5;
                int[] wh = {width, height};

                profilePictureInAccount.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
                profilePictureInAccount.setBounds((frameSize - width) / 2, ((250 - height) / 2) + 55, width, height);
                profilePictureInAccount.setHorizontalAlignment(0);
                profilePictureInAccount.setVerticalAlignment(0);

                // profile picture operations
                viewPhotoButton.setBounds(500, 75, 120, 25);
                viewPhotoButton.setForeground(Color.WHITE);
                viewPhotoButton.setBackground(Color.DARK_GRAY);
                viewPhotoButton.addActionListener(ae -> {
                    JLabel ppLabel = new JLabel();
                    ppLabel.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                    JOptionPane.showMessageDialog(frame, ppLabel, "Profile Picture", JOptionPane.PLAIN_MESSAGE);
                });

                changePhotoButton.setBounds(500, 125, 120, 25);
                changePhotoButton.setForeground(Color.WHITE);
                changePhotoButton.setBackground(Color.DARK_GRAY);
                changePhotoButton.addActionListener(ae -> {
                    if (setPPDetails() == JOptionPane.YES_OPTION) {
                        wh[0] = profilePictureWidth;
                        wh[1] = profilePictureHeight;
                        changeWH(wh);

                        profilePictureInAccount.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                        profilePictureInAccount.setBounds((frameSize - wh[0]) / 2, ((250 - wh[1]) / 2) + 55, wh[0], wh[1]);

                        // save the profile picture
                        savePP();
                    }
                });

                deletePhotoButton.setBounds(500, 175, 120, 25);
                deletePhotoButton.setForeground(Color.WHITE);
                deletePhotoButton.setBackground(Color.RED);
                deletePhotoButton.addActionListener(ae -> {
                    /*if (profilePicturePath.equals(dirname + "\\Images\\finder.png")) {
                        showMessageDialogJOP(frame, "Profile Picture Already Deleted", "Profile Picture Already Deleted", JOptionPane.ERROR_MESSAGE);
                    } else */
                    if (areYouSureJOP(frame) == JOptionPane.YES_OPTION) {
                        profilePicturePath = dirname + "\\Images\\finder.png";
                        profilePicture = new ImageIcon(dirname + "\\Images\\finder.png");
                        profilePictureExtension = ".png";
                        profilePictureWidth = wh[0] = profilePicture.getIconWidth();
                        profilePictureHeight = wh[1] = profilePicture.getIconHeight();
                        changeWH(wh);

                        profilePictureInAccount.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                        profilePictureInAccount.setBounds((frameSize - wh[0]) / 2, ((250 - wh[1]) / 2) + 55, wh[0], wh[1]);

                        // save the profile picture
                        savePP();
                        JOptionPane.showMessageDialog(frame, "Deleted Profile Picture Successfully", "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                usernameLabel.setBounds(0, 300, frameSize, 25);
                usernameLabel.setFont(timesNewRoman);
                usernameLabel.setHorizontalAlignment(0);

                passwordLabel.setBounds(0, 340, frameSize, 25);
                passwordLabel.setFont(timesNewRoman);
                passwordLabel.setHorizontalAlignment(0);

                // show password by taking reference no. as input

                // No. of Transactions
                int noOfTransactions = 0;
                try {
                    File th = new File(dirname + "\\TransactionHistories\\" + username + ".txt");
                    Scanner scanner = new Scanner(th);
                    while (scanner.hasNextLine()) {
                        scanner.nextLine();
                        noOfTransactions++;
                    }
                } catch (Exception ignored) {
                }

                noOfTransactionsLabel.setBounds(0, 390, 500, 25);
                noOfTransactionsLabel.setText("Number of Transactions made: " + noOfTransactions);
                noOfTransactionsLabel.setFont(timesNewRoman);
                noOfTransactionsLabel.setHorizontalAlignment(0);

                goToTHButton.setBounds(400, 390, 200, 25);
                goToTHButton.setForeground(Color.WHITE);
                goToTHButton.setBackground(Color.BLUE);
                goToTHButton.setFont(timesNewRoman);
                goToTHButton.setHorizontalAlignment(0);
                goToTHButton.addActionListener(new Verification("TH"));

                logoutButton.setBounds(586, 0, 100, 30);
                logoutButton.setBackground(Color.RED);
                logoutButton.setForeground(Color.WHITE);
                logoutButton.setFont(timesNewRoman);
                logoutButton.addActionListener(new Back((byte) 1));
            } else {
                //settings. put the account deletion, password change, update username options in here
                backButton = new JButton("Back");
                JLabel settingsLabel = new JLabel("Settings");
                JButton deleteTHButton = new JButton("Delete all the Transaction Histories");
                JButton deleteEnqButton = new JButton("Delete all the Enquiries");
                JButton deleteAccountData = new JButton("Delete all my account Data");
                JButton deleteAccount = new JButton("Delete My Account");

                frame.add(backButton);
                frame.add(settingsLabel);
                frame.add(deleteTHButton);
                frame.add(deleteEnqButton);
                frame.add(deleteAccountData);
                frame.add(deleteAccount);
                frame.add(logoutButton);

                backButton.setBounds(0, 0, 80, 30);
                backButton.setBackground(Color.BLACK);
                backButton.setForeground(Color.WHITE);
                backButton.setFont(timesNewRoman);
                backButton.addActionListener(new Back((byte) 3));

                settingsLabel.setBounds(0, 30, frameSize, 25);
                settingsLabel.setForeground(Color.GRAY);
                settingsLabel.setFont(headingFont);
                settingsLabel.setHorizontalAlignment(0);

                deleteTHButton.setBounds(200, 65, 300, 30);
                deleteTHButton.setForeground(Color.BLACK);
                deleteTHButton.setBackground(Color.orange);
                deleteTHButton.addActionListener(ae -> {
                    optionPaneLabel.setText("<html>Are You Sure?\nAll your Transaction History will be lost</html>".replaceAll("\n", "<br>"));
                    if (JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            new FileWriter(dirname + "\\TransactionHistories\\" + username + ".txt", false).close();
                            showMessageDialogJOP(frame, "Transaction History Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                        } catch (Exception ex) {
                            showMessageDialogJOP(frame, "<html>Some Error Occurred\nTransaction History not Deleted\nSorry for the inconvenience caused</html>".replaceAll("\n", "<br>"),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                deleteEnqButton.setBounds(200, 110, 300, 30);
                deleteEnqButton.setForeground(Color.BLACK);
                deleteEnqButton.setBackground(Color.orange);
                deleteEnqButton.addActionListener(ae -> {
                    optionPaneLabel.setText("<html>Are You Sure?\nAll your Enquiries will be lost</html>".replaceAll("\n", "<br>"));
                    if (JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            new FileWriter(dirname + "\\Enquiries\\" + username + ".txt", false).close();
                            showMessageDialogJOP(frame, "All Enquiries Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                        } catch (Exception ex) {
                            showMessageDialogJOP(frame, "<html>Some Error Occurred\nEnquiries not Deleted\nSorry for the inconvenience caused</html>".replaceAll("\n", "<br>"),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                deleteAccountData.setBounds(200, 155, 300, 30);
                deleteAccountData.setForeground(Color.BLACK);
                deleteAccountData.setBackground(Color.red);
                deleteAccountData.addActionListener(ae -> {
                    optionPaneLabel.setText((
                            "<html>Are You Sure?\n" +
                                    "All your Account Data will be lost\n\nAccount Data Includes:\n" +
                                    "1) Transaction History till date\n" +
                                    "2) Enquiries Made till date</html>".replaceAll("\n", "<br>")));
                    if (JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            new FileWriter(dirname + "\\TransactionHistories\\" + username + ".txt", false).close();
                            new FileWriter(dirname + "\\Enquiries\\" + username + ".txt", false).close();
                            showMessageDialogJOP(frame, "All the Account Data Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                        } catch (Exception ex) {
                            showMessageDialogJOP(frame, "<html>Some Error Occurred\nAccount Data not Deleted\nSorry for the inconvenience caused</html>".replaceAll("\n", "<br>"),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                deleteAccount.setBounds(200, 200, 300, 30);
                deleteAccount.setForeground(Color.BLACK);
                deleteAccount.setBackground(Color.RED);
                deleteAccount.addActionListener(new Verification("AccountDeletion"));

                logoutButton.setBounds(586, 0, 100, 30);
                logoutButton.setBackground(Color.RED);
                logoutButton.setForeground(Color.WHITE);
                logoutButton.setFont(timesNewRoman);
                logoutButton.addActionListener(new Back((byte) 1));
            }
        }

        void changeWH(int[] wh) {
            if (wh[0] >= 375) wh[0] /= 2;
            else if (wh[0] >= 250) wh[0] /= 1.5;

            if (wh[1] >= 375) wh[1] /= 2;
            else if (wh[1] >= 250) wh[1] /= 1.5;
        }
    }

    String[] places, temp = {"--Select--"};
    String city, source, destination;
    final String[] citiesArray = {"--Select--", "HYDERABAD", "BENGALURU", "CHENNAI"};
    float cost;

    void getPlaces() {
        short noOfPlaces = 0;
        File cityFile = new File(dirname + "\\CitiesInfo\\" + city + ".txt");
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
    class InitializeCombos implements ActionListener {
        JComboBox<String> cityField, sourceField, destinationField;

        InitializeCombos(JComboBox<String> cityField, JComboBox<String> sourceField, JComboBox<String> destinationField) {
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


    class BookingUI {
        JLabel nameLabel, phoneLabel, emailLabel, adultLabel, childrenLabel, cityLabel, sourceLabel, destinationLabel;
        JLabel phoneMessage, emailMessage;
        JTextField nameField, phoneField, emailField;
        JComboBox<String> cityField, sourceField, destinationField;
        JSpinner adultField, childrenField;

        short noOfAdults, noOfChildren;
        String name, phone, email;
        String enqCity, enqSource, enqDestination;
        int enqAdults, enqChildren;

        void bookingUI(int case_) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("Booking");

            backButton = new JButton("Back");
            nameLabel = new JLabel("Name:");
            nameField = new JTextField();
            phoneLabel = new JLabel("Mobile Number:");
            phoneField = new JTextField();
            phoneMessage = new JLabel();
            emailLabel = new JLabel("Email ID:");
            emailField = new JTextField();
            emailMessage = new JLabel();
            cityLabel = new JLabel("City");
            sourceLabel = new JLabel("Source:");
            destinationLabel = new JLabel("Destination:");
            adultLabel = new JLabel("Adults:");
            adultField = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            childrenLabel = new JLabel("Children:");
            childrenField = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            JButton continueButton = new JButton("Continue");
            msg = new JLabel();

            if (case_ == 1) {
                cityField.setSelectedItem(enqCity);
                sourceField.setSelectedItem(enqSource);
                destinationField.setSelectedItem(enqDestination);
                adultField.setValue(enqAdults);
                childrenField.setValue(enqChildren);
            } else {
                cityField = new JComboBox<>(citiesArray);
                sourceField = new JComboBox<>(temp);
                destinationField = new JComboBox<>(temp);
            }

            frame.add(backButton);
            frame.add(nameLabel);
            frame.add(nameField);
            frame.add(phoneLabel);
            frame.add(phoneField);
            frame.add(phoneMessage);
            frame.add(emailLabel);
            frame.add(emailField);
            frame.add(emailMessage);
            frame.add(cityLabel);
            frame.add(cityField);
            frame.add(sourceLabel);
            frame.add(sourceField);
            frame.add(destinationLabel);
            frame.add(destinationField);
            frame.add(adultLabel);
            frame.add(adultField);
            frame.add(childrenLabel);
            frame.add(childrenField);
            frame.add(continueButton);
            frame.add(msg);
            frame.add(logoutButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 2));

            nameLabel.setBounds(200, 50, 100, 25);
            nameLabel.setFont(timesNewRoman);

            nameField.setBounds(330, 50, 200, 25);
            nameField.setFont(timesNewRoman);
            nameField.setText(username);

            phoneLabel.setBounds(20, 100, 130, 25); //200, 100, 130, 25
            phoneLabel.setFont(timesNewRoman);

            phoneField.setBounds(135, 100, 200, 25); //330, 100, 200, 25
            phoneField.setFont(timesNewRoman);

            phoneMessage.setBounds(0, 125, 330, 20); //510, 100, 200, 25
            phoneMessage.setFont(timesNewRoman);
            phoneMessage.setForeground(Color.RED);
            phoneMessage.setHorizontalAlignment(0);
            phoneMessage.setVerticalAlignment(0);

            emailLabel.setBounds(365, 100, 90, 25);
            emailLabel.setFont(timesNewRoman);

            emailField.setBounds(440, 100, 200, 25);
            emailField.setFont(timesNewRoman);

            emailMessage.setBounds(350, 125, 330, 20);
            emailMessage.setFont(timesNewRoman);
            emailMessage.setForeground(Color.RED);
            emailMessage.setHorizontalAlignment(0);
            emailMessage.setVerticalAlignment(0);

            cityLabel.setBounds(200, 150, 100, 25);
            cityLabel.setFont(timesNewRoman);

            cityField.setBounds(330, 150, 200, 25);
            cityField.setFont(timesNewRoman);
            cityField.addActionListener(new InitializeCombos(cityField, sourceField, destinationField));

            sourceLabel.setBounds(200, 200, 100, 25);
            sourceLabel.setFont(timesNewRoman);

            sourceField.setBounds(330, 200, 200, 25);
            sourceField.setFont(timesNewRoman);

            destinationLabel.setBounds(200, 250, 100, 25);
            destinationLabel.setFont(timesNewRoman);

            destinationField.setBounds(330, 250, 200, 25);
            destinationField.setFont(timesNewRoman);

            adultLabel.setBounds(200, 300, 100, 25);
            adultLabel.setFont(timesNewRoman);

            adultField.setBounds(330, 300, 200, 25);
            adultField.setFont(timesNewRoman);
            positioningTextAndDisablingEditingInJSpinner(adultField);

            childrenLabel.setBounds(200, 350, 100, 25);
            childrenLabel.setFont(timesNewRoman);

            childrenField.setBounds(330, 350, 200, 25);
            childrenField.setFont(timesNewRoman);
            positioningTextAndDisablingEditingInJSpinner(childrenField);

            continueButton.setBounds(275, 400, 150, 25);
            continueButton.setBackground(Color.GREEN);
            continueButton.setForeground(Color.WHITE);
            continueButton.setFont(timesNewRoman);
            continueButton.addActionListener(new ContinueToModeOfTransportation());

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
                emailMessage.setText("");

                msg.setFont(timesNewRoman);
                msg.setForeground(Color.RED);
                msg.setHorizontalAlignment(0);

                name = nameField.getText().trim();
                phone = phoneField.getText().trim();
                email = emailField.getText().trim();
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
                } else if (!phone.matches(phoneNumberRegex) || !email.matches(emailIDRegex)) {
                    if (!phone.matches(phoneNumberRegex)) phoneMessage.setText("Invalid Phone Number");
                    if (!email.matches(emailIDRegex)) emailMessage.setText("Invalid Email Address");
                } else {
                    bookingObj = new BookingMainCode(city, source, destination, noOfAdults, noOfChildren);

                    bookingObj.new EnquireAndBookings().bookings();

                    String[] route = bookingObj.route;
                    cost = bookingObj.cost;
                    short i, len = bookingObj.routeLen;

                    StringBuilder routeCost = new StringBuilder("<html>\nRoute: ");
                    for (i = len; i >= 0; i--) {
                        routeCost.append(route[i].toUpperCase());
                        if (i != 0) routeCost.append(" => ");
                    }
                    routeCost.append("\nCost: ").append(cost).append(" /-").append("</html>");

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
                    msg.setOpaque(true);

                    if (selectedOption == 0) {
                        msg.setBounds(0, 570, frameSize, 25);
                        msg.setHorizontalAlignment(0);
                        msg.setText("Please Select an Option for Mode of Transportation first");
                    } else if (selectedOption == 1) {
                        msg.setText("");
                        oneModeOfTransportation(bookingObj.noOfVehicles, bookingObj.vehicles, bookingObj.costPerKM);
                    } else {
                        msg.setText("");
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
                    costLabel.setOpaque(true);

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

                    bookingObj.loadDetails(username, city, source, destination, bookingObj.calculateTotalCost(), name, phone, email,
                            noOfAdults, noOfChildren,
                            new SimpleDateFormat("dd:MM:yyyy").format(date),
                            new SimpleDateFormat("HH:mm:ss").format(date));

                    backButton = new JButton("Back");
                    msg = new JLabel(("<html>Successfully Booked a Ticket from " +
                            source.toUpperCase() + " to " + destination.toUpperCase() + "\n" + "Total Cost: " + cost +
                            "\n" + "</html>").replaceAll("\n", "<br>"));

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
            JLabel noTransactionsImage = new JLabel(new ImageIcon(dirname + "\\Images\\no_transactions.png"));
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
        JLabel enquireCityLabel = new JLabel("City:");
        JLabel enquireSourceLabel = new JLabel("Source:");
        JLabel enquireDestinationLabel = new JLabel("Destination:");
        JLabel enquireAdultLabel = new JLabel("No. of Adults:");
        JLabel enquireChildrenLabel = new JLabel("No. of Children:");

        JComboBox<String> enquireCityField, enquireSourceField, enquireDestinationField;
        JSpinner enquireAdultField, enquireChildrenField;
        String[] optionPaneButtonNames = {"Ok", "Book a Ticket", "Break the Fare"};

        void enquireUI() {
            frame.getContentPane().removeAll();
            frame.repaint();

            backButton = new JButton("Back");
            JButton enquireButton = new JButton("Enquire");
            enquireCityField = new JComboBox<>(citiesArray);
            enquireSourceField = new JComboBox<>(temp);
            enquireDestinationField = new JComboBox<>(temp);
            enquireAdultField = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            enquireChildrenField = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

            frame.add(backButton);
            frame.add(enquireCityLabel);
            frame.add(enquireCityField);
            frame.add(enquireSourceLabel);
            frame.add(enquireSourceField);
            frame.add(enquireDestinationLabel);
            frame.add(enquireDestinationField);
            frame.add(enquireAdultLabel);
            frame.add(enquireAdultField);
            frame.add(enquireChildrenLabel);
            frame.add(enquireChildrenField);
            frame.add(enquireButton);
            frame.add(logoutButton);

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.addActionListener(new Back((byte) 2));

            enquireCityLabel.setBounds(200, 200, 100, 25);
            enquireCityLabel.setFont(timesNewRoman);

            enquireCityField.setBounds(320, 200, 200, 25);
            enquireCityField.setFont(timesNewRoman);

            enquireSourceLabel.setBounds(200, 250, 100, 25);
            enquireSourceLabel.setFont(timesNewRoman);

            enquireSourceField.setBounds(320, 250, 200, 25);
            enquireSourceField.setFont(timesNewRoman);

            enquireDestinationLabel.setBounds(200, 300, 100, 25);
            enquireDestinationLabel.setFont(timesNewRoman);

            enquireDestinationField.setBounds(320, 300, 200, 25);
            enquireDestinationField.setFont(timesNewRoman);

            enquireAdultLabel.setBounds(200, 350, 100, 25);
            enquireAdultLabel.setFont(timesNewRoman);

            enquireAdultField.setBounds(320, 350, 200, 25);
            enquireAdultField.setFont(timesNewRoman);
            positioningTextAndDisablingEditingInJSpinner(enquireAdultField);

            enquireChildrenLabel.setBounds(200, 400, 120, 25);
            enquireChildrenLabel.setFont(timesNewRoman);

            enquireChildrenField.setBounds(320, 400, 200, 25);
            enquireChildrenField.setFont(timesNewRoman);
            positioningTextAndDisablingEditingInJSpinner(enquireChildrenField);

            enquireCityField.addActionListener(new InitializeCombos(enquireCityField, enquireSourceField, enquireDestinationField));

            enquireButton.setBounds(310, 450, 100, 25);
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
            String enquireCity, enquireSource, enquireDestination;
            int enquireAdults, enquireChildren;

            @Override
            public void actionPerformed(ActionEvent e) {
                msg = new JLabel();
                msg.setBounds(0, 500, frameSize, 25);
                msg.setFont(timesNewRoman);
                msg.setForeground(Color.RED);
                msg.setHorizontalAlignment(0);
                msg.setOpaque(true);

                frame.add(msg);

                enquireCity = String.valueOf(enquireCityField.getSelectedItem());
                enquireSource = String.valueOf(enquireSourceField.getSelectedItem());
                enquireDestination = String.valueOf(enquireDestinationField.getSelectedItem());
                enquireAdults = (int) enquireAdultField.getValue();
                enquireChildren = (int) enquireChildrenField.getValue();

                if (enquireCity.equals("--Select--") || enquireSource.equals("--Select--") || enquireDestination.equals("--Select--")) {
                    msg.setText("Please Complete all the Fields");
                } else if (enquireSource.equals(enquireDestination)) {
                    msg.setText("Source and Destination Cannot be same");
                } else {
                    msg.setText("");
                    JLabel routeCostMessage = new JLabel();

                    BookingMainCode enquiryObj = new BookingMainCode(enquireCity, enquireSource, enquireDestination);
                    enquiryObj.new EnquireAndBookings().bookings();

                    String[] route = enquiryObj.route;
                    short i, routeLen = enquiryObj.routeLen;
                    cost = enquiryObj.cost;
                    float totalCost = (cost) * enquireAdults + (cost / 2) * enquireChildren;

                    StringBuilder routeCost = new StringBuilder("<html>\nRoute: ");
                    for (i = routeLen; i >= 0; i--) {
                        routeCost.append(route[i].toUpperCase());
                        if (i != 0) routeCost.append(" => ");
                    }
                    routeCost.append("\n\nTotal Fare: ").append(totalCost).append(" /-").append("</html>");

                    routeCostMessage.setBounds(0, 0, 500, 500);
                    routeCostMessage.setHorizontalAlignment(0);
                    routeCostMessage.setVerticalAlignment(0);
                    routeCostMessage.setFont(timesNewRoman);
                    routeCostMessage.setText(String.valueOf(routeCost).replaceAll("\n", "<br>"));
                    routeCostMessage.setText(routeCostMessage.getText().toUpperCase());

                    optionPaneResult = JOptionPane.showOptionDialog(frame, routeCostMessage,
                            enquiryObj.source.toUpperCase() + " to " + enquiryObj.destination.toUpperCase(),
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, optionPaneButtonNames, null);

                    if (optionPaneResult == JOptionPane.NO_OPTION) {
                        BookingUI bookingObj = new BookingUI();

                        bookingObj.cityField = enquireCityField;
                        bookingObj.sourceField = enquireSourceField;
                        bookingObj.destinationField = enquireDestinationField;

                        bookingObj.enqCity = enquireCity;
                        bookingObj.enqSource = enquireSource;
                        bookingObj.enqDestination = enquireDestination;
                        bookingObj.enqAdults = enquireAdults;
                        bookingObj.enqChildren = enquireChildren;

                        enquireCityField.addActionListener(new InitializeCombos(enquireCityField, bookingObj.sourceField, bookingObj.destinationField));
                        bookingObj.bookingUI(1);
                    } else if (optionPaneResult == JOptionPane.CANCEL_OPTION) {
                        JLabel fareDivision = new JLabel();

                        fareDivision.setText(
                                ("<html>No. of Adults: " + enquireAdults + "     Fare: " + cost * enquireAdults + "\n\n" +
                                        "No. of Children: " + enquireChildren + "     Fare: " + (cost / 2) * enquireChildren + "\n\n" +
                                        "Total Cost: " + totalCost + "</html>").replaceAll("\n", "<br>"));
                        fareDivision.setHorizontalAlignment(0);
                        fareDivision.setVerticalAlignment(0);
                        fareDivision.setFont(timesNewRoman);

                        JOptionPane.showMessageDialog(frame, fareDivision, "Fare Division", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // store the enquiries
                        File enquiryFile = new File(dirname + "\\Enquiries\\" + username + ".txt");
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(enquiryFile, true));
                            writer.write(enquireCity.toUpperCase() + "," + enquireSource.toUpperCase() + "," + enquireDestination.toUpperCase() + "," + cost + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception ex) {
                            msg.setText("Error Occurred");
                        }
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
            newUsernameText.setText("");
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
                                if (str.split(",")[0].equals(newUsername)) {
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
                            if (areYouSureJOP(updateUsernameFrame) == JOptionPane.YES_OPTION) {
                                if (new UpdateUsernameMainCode().updateUsername(username, newUsername, password)) {
                                    updateUsernameFrame.dispose();

                                    username = newUsername;

                                    usernameLabel.setText("Username: " + username);

                                    showMessageDialogJOP(frame, "Username Changed Successfully", "Successful", JOptionPane.INFORMATION_MESSAGE);

                                    displayMenu();
                                } else {
                                    showMessageDialogJOP(updateUsernameFrame, "Error Occurred. Username Didn't Change", "Error", JOptionPane.ERROR_MESSAGE);
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
                } else if (isPasswordAccepted(newPassword, passwordChangeFrame)) {
                    if (areYouSureJOP(passwordChangeFrame) == JOptionPane.YES_OPTION) {
                        if (new PasswordChangeMainCode().passwordChange(username, password, newPassword)) {
                            password = newPassword;
                            passwordChangeFrame.dispose();
                            showMessageDialogJOP(frame, "Password Changed Successfully", "Password Changed Successfully", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            showMessageDialogJOP(passwordChangeFrame, "Some Error Occurred. Couldn't Change Password. Try After Some time",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }


    void AccountDeletion(JFrame frame1) {
        optionPaneResult = JOptionPane.showConfirmDialog(frame1,
                "Are You Sure?\nAll Your Transactions, Enquiries will be lost\nThis is un-reversible",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (optionPaneResult == JOptionPane.YES_OPTION) {
            frame1.dispose();
            boolean deleted = new AccountDeletionMainCode().accountDeletion(username, password);
            if (deleted) {
                Homepage();
                JOptionPane.showMessageDialog(frame, "Account Deleted Successfully\nWe are Sorry to see you go",
                        "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessageDialogJOP(frame1, "Some Error Occurred, Account not Deleted", "Account Not Deleted", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            displayMenu();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class SwitchAccountsUI {
        JFrame switchAccountsFrame;
        JLabel switchingUserLabel = new JLabel("Username:");
        JLabel switchingPasswordLabel = new JLabel("Password:");
        JTextField switchingUserText = new JTextField();
        JPasswordField switchingPassword = new JPasswordField();
        JCheckBox switchingCB;
        JButton switchAccountButton = new JButton("Switch Account");

        void switchAccountsUI() {
            switchAccountsFrame = new JFrame();
            switchAccountsFrame.setLayout(null);
            switchAccountsFrame.setSize(300, 300);
            switchAccountsFrame.setVisible(true);
            switchAccountsFrame.setLocationRelativeTo(frame);
            switchAccountsFrame.setTitle("Switching Accounts");

            msg = new JLabel();
            switchingCB = new JCheckBox("Show Password");

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
            switchingUserText.setText("");
            switchingUserText.setFont(timesNewRoman);

            switchingPasswordLabel.setBounds(50, 110, 80, 25);
            switchingPasswordLabel.setFont(timesNewRoman);

            switchingPassword.setBounds(130, 110, 120, 25);
            switchingPassword.setText("");
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
                            credentials = str.split(",");
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
                        if (areYouSureJOP(switchAccountsFrame) == JOptionPane.YES_OPTION) {
                            username = localUsername;
                            password = localPassword;
                            switchAccountsFrame.dispose();
                            usernameLabel.setText("Username: " + username);
                            showMessageDialogJOP(frame, "Switched Accounts Successfully", "Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        }
    }
}
