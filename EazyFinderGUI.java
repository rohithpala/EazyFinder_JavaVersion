package EazyFinderGUI;

import EazyFinderGUI.MainCodes.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/*
TODO:
1. add refID related things in MainCodes and wherever needed
1.1. add forgot password option for every password field and prompt for refID
2. caps lock warning while typing passwords
3. look at problems with guest mode like account ...
4. guest mode option (show option panes like "Guests cannot update their usernames or passwords or cannot switch accounts" after clicking the
button. Give options like "Look at UI" and show the UI)
5. add profile and settings option (dark mode (optional))
6. give ref no. while signup and store them in the file for verification
7. add clear form option and also cross symbol in text fields
9. add sorting option based on filters wherever possible
10. save the previous frame state while redirection
11. add change name, phone, email id options in account
*/

public class EazyFinderGUI {
    /**
     * @implSpec While using an IDE "Edit Configurations" by setting the Working Directory path
     * till src if it is not already present.
     * System.getProperty("user.dir") returns path upto src, or change the program accordingly
     */
    String dirname = System.getProperty("user.dir") + "\\EazyFinderGUI";
    String username, password, name, phoneNumber, emailID; // User Details
    long refID;
    final File db = new File(dirname + "\\Databases\\LogInSignUpDatabase.txt");
    final File ud = new File(dirname + "\\Databases\\UserDetails.txt");

    // frame related
    JFrame frame = new JFrame(); //Main frame
    final short frameSize = 700;

    // common components in every frame change
    JButton backButton;
    JLabel msg; // Used to print corresponding messages

    final Font timesNewRoman = new Font("Times New Roman", Font.BOLD, 15); // font equipped by every component
    final Font forgotPasswordFont = new Font("Times New Roman", Font.BOLD, 10); // font equipped by forgot password label

    JLabel optionPaneLabel = new JLabel(); // label added into JOptionPanes to show corresponding messages
    int optionPaneResult; // used for storing option pane results

    public static void main(String[] args) {
        new EazyFinderGUI().Homepage();
    }

    void Homepage() {
        frame.getContentPane().removeAll();
        frame.repaint();

        JButton homepageLoginButton = new JButton("LogIn");
        JButton homepageSignupButton = new JButton("SignUp");
        JButton homepageGuestButton = new JButton("Browse as Guest");

        frame.setSize(350, 300);
        frame.setTitle("Homepage");
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
//        frame.setResizable(false);

        optionPaneLabel.setFont(timesNewRoman);

        frame.add(homepageLoginButton);
        frame.add(homepageSignupButton);
        frame.add(homepageGuestButton);

        homepageLoginButton.setBounds(50, 95, 100, 30);
        homepageLoginButton.setForeground(Color.BLACK);
        homepageLoginButton.setBackground(Color.ORANGE);
        homepageLoginButton.setFont(timesNewRoman);
        homepageLoginButton.addActionListener(new LoginUI());

        homepageSignupButton.setBounds(180, 95, 100, 30);
        homepageSignupButton.setForeground(Color.BLACK);
        homepageSignupButton.setBackground(Color.ORANGE);
        homepageSignupButton.setFont(timesNewRoman);
        homepageSignupButton.addActionListener(e -> new SignUpUI().signUpUI("homepage"));

        homepageGuestButton.setBounds(50, 142, 230, 30);
        homepageGuestButton.setForeground(Color.WHITE);
        homepageGuestButton.setBackground(Color.DARK_GRAY);
        homepageGuestButton.setFont(timesNewRoman);
        homepageGuestButton.addActionListener(new GuestMode());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Guest Mode enables the users to have the experience of a registered user
     * without creating their own account.
     * <p>
     * Guest Mode can also be used by the users to know about our services and the
     * UI too
     */
    class GuestMode implements ActionListener {
        String[] guestPaneOptions = {"Browse as Guest", "Go Back"};

        @Override
        public void actionPerformed(ActionEvent e) {
            generateReferenceID();
            optionPaneLabel.setText("<html>We are giving you the guest credentials so that you can have the same experience as a registered user\n\nUsername: Guest\nPassword: Guest@123\nReference ID: 1\n\nWe delete all the date provided by you in the guest mode once you logout\nSo feel free to be a registered user\n\nHappy Browsing 😃</html>"
                    .replaceAll("\n", "<br>"));
            if (JOptionPane.showOptionDialog(frame, optionPaneLabel, "Guest Mode", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, guestPaneOptions, null) == JOptionPane.YES_OPTION) {
                username = "Guest";
                password = "Guest@123";
                refID = 1;
                profilePicturePath = dirname + "\\Images\\defaultPP.png";
                profilePicture = new ImageIcon(profilePicturePath);
                profilePictureExtension = ".png";
                profilePictureWidth = profilePicture.getIconWidth();
                profilePictureHeight = profilePicture.getIconHeight();

                displayMenu();
            }
            // Create files, entry in database
            // delete all data after logout
        }
    }

    /**
     * This method shows an option pane for confirmation from the user about a
     * particular action
     */
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
                    if (num == 1) {
                        // resetting all the variables to their default values after logging out
                        username = password = name = phoneNumber = emailID = "";
                        refID = 0;
                        // resetting profile picture details
                        tempProfilePicturePath = profilePicturePath = dirname + "\\Images\\defaultPP.png";
                        profilePictureExtension = ".png";
                        tempProfilePicture = profilePicture = new ImageIcon(profilePicturePath);
                        // resetting sudo mode related variables
                        sudoModeAccepted = false;
                        passwordTypedAt = null;
                        Homepage();
                    } else if (num == 2) displayMenu();
                }
            } else if (num == 3) {
                displayMenu();
            }
        }
    }

    void generateReferenceID() {
        refID = 0;
        long a = 1;
        short i, len = (short) username.length();
        for (i = 0; i < len; i++) {
            refID += ((int) username.charAt(i)) * a;
            a *= 100;
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

    // components used in Login and SignUp
    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel rePasswordLabel = new JLabel("Re-Type Password:");

    // variables needed for sudo mode
    boolean sudoModeAccepted = false;
    String passwordTypedAt;

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

                msg.setText("");
                msg.setForeground(Color.BLACK);

                if (username.equals("") || password.equals("")) {
                    msg.setText("Please Fill all the Fields");
                } else {
                    String str;
                    String[] credentials = new String[3];
                    boolean usernameFound = false, passwordFound = false;
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(db));
                        while ((str = reader.readLine()) != null) {
                            credentials = str.split(",");
                            if (username.equals(credentials[0])) {
                                usernameFound = true;
                                if (String.valueOf(encryptPassword(password)).equals(credentials[1])) {
                                    passwordFound = true;
                                    break;
                                }
                                break;
                            }
                        }
                        reader.close();
                    } catch (Exception ex) {
                        msg.setText("Error in reading file");
                    }

                    if (!usernameFound && !passwordFound) {
                        optionPaneLabel.setText("<html>No User With Given Credentials\nDo You Want to SignUp?</html>".replaceAll("\n", "<br>"));
                        if (JOptionPane.showConfirmDialog(frame, optionPaneLabel,
                                "Account Not Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            SignUpUI signUpObj = new SignUpUI();
                            signUpObj.userField = new JTextField(username);
                            signUpObj.passwordField = new JPasswordField(password);
                            signUpObj.signUpUI("login");
                        }
                    } else if (!passwordFound) {
                        msg.setForeground(Color.RED);
                        msg.setText("Password Incorrect");
                    } else if (usernameFound && passwordFound) {
                        refID = Long.parseLong(credentials[2]);

                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(ud));
                            while ((str = reader.readLine()) != null) {
                                credentials = str.split(",");
                                name = credentials[1];
                                phoneNumber = credentials[2];
                                emailID = credentials[3];
                            }
                        } catch (Exception ignored) {
                        }

                        optionPaneLabel.setText("Do you want to enter sudo mode?");
                        optionPaneResult = JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Sudo Mode Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (optionPaneResult == JOptionPane.YES_OPTION) {
                            sudoModeAccepted = true;
                            passwordTypedAt = setCurrentTime();
                            showMessageDialogJOP(frame, "<html>Sudo Mode is On\nPassword will be prompted only for\n1 minute of Interval</html>".replaceAll("\n", "<br>"),
                                    "Sudo Mode is On", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            showMessageDialogJOP(frame, "Sudo Mode is Off", "Sudo Mode Off", JOptionPane.INFORMATION_MESSAGE);
                        }

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
    String profilePicturePath = dirname + "\\Images\\defaultPP.png", profilePictureExtension = ".png";
    ImageIcon profilePicture = new ImageIcon(profilePicturePath);
    int profilePictureWidth, profilePictureHeight;
    String tempProfilePicturePath = profilePicturePath;
    ImageIcon tempProfilePicture = new ImageIcon(tempProfilePicturePath);
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
                    int[] wh = {profilePictureWidth, profilePictureHeight};
                    changeWH(wh);
                    JLabel ppLabel = new JLabel();
                    ppLabel.setHorizontalAlignment(0);
                    ppLabel.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                    optionPaneResult = JOptionPane.showOptionDialog(frame, ppLabel, "Profile Picture",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, optionPaneButtonNames, null);
                } else {
                    showMessageDialogJOP(frame,
                            "<html>You have selected an unsupported file\nPlease choose another file\nSupported Files: .png, .jpg, .jpeg</html>"
                                    .replaceAll("\n", "<br>"),
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
        File ppDir = new File(dirname + "\\Databases\\ProfilePictures");
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

    // https://stackoverflow.com/questions/1146153/copying-files-from-one-directory-to-another-in-java
    void savePP() {
        try {
            boolean fileCreated = true;
            File destinationFile = new File(dirname + "\\Databases\\ProfilePictures\\" + username + profilePictureExtension);

            if (!destinationFile.exists()) fileCreated = destinationFile.createNewFile();

            if (fileCreated) {
                FileChannel source = new FileInputStream(profilePicturePath).getChannel();
                FileChannel destination = new FileOutputStream(destinationFile).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            }
        } catch (IOException ignored) {
        }
    }

    // used in signup and settings
    void changeWH(int[] wh) {
        if (wh[0] >= 375) wh[0] /= 2;
        else if (wh[0] >= 250) wh[0] /= 1.5;

        if (wh[1] >= 375) wh[1] /= 2;
        else if (wh[1] >= 250) wh[1] /= 1.5;
    }

    class SignUpUI {
        JTextField userField;
        JPasswordField passwordField, rePasswordField;
        JButton nextButton = new JButton("Next");
        JCheckBox showPasswordCB1, showPasswordCB2;

        void signUpUI(String str) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("SignUp");

            backButton = new JButton("Back");
            showPasswordCB1 = new JCheckBox("Show Password");
            rePasswordField = new JPasswordField();
            showPasswordCB2 = new JCheckBox("Show Password");
            msg = new JLabel();

            if (str.equals("homepage")) {
                userField = new JTextField();
                passwordField = new JPasswordField();
            }

            frame.add(userLabel);
            frame.add(passwordLabel);
            frame.add(userField);
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

            userField.setBounds(130, 50, 120, 25);
            userField.setFont(timesNewRoman);

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
                username = userField.getText().trim();
                password = String.valueOf(passwordField.getPassword());
                String rePassword = String.valueOf(rePasswordField.getPassword());
                boolean found = false;

                if (username.equals("") || password.equals("") || rePassword.equals("")) {
                    msg.setText("Please Fill all the fields");
                } else if (!password.equals(rePassword)) {
                    msg.setText("Passwords doesn't match");
                } else { // CheckingPassword if username is already present
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
            JButton logoutButton = new JButton("Logout");

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
            JLabel message = new JLabel();
            JLabel emailErrorMessage = new JLabel();

            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText().trim();
                phoneNumber = phoneField.getText().trim();
                emailID = emailField.getText().trim();

                frame.add(message);
                frame.add(emailErrorMessage);

                message.setText("");
                message.setBounds(0, 320, registrationFrameSize, 25);
                message.setFont(timesNewRoman);
                message.setForeground(Color.RED);
                message.setOpaque(true);
                message.setHorizontalAlignment(0);

                emailErrorMessage.setText("");
                emailErrorMessage.setBounds(0, 350, registrationFrameSize, 25);
                emailErrorMessage.setFont(timesNewRoman);
                emailErrorMessage.setForeground(Color.RED);
                emailErrorMessage.setOpaque(true);
                emailErrorMessage.setHorizontalAlignment(0);

                if (selectProfilePictureButton.getText().equals("Select")) {
                    message.setText("Please select a Profile Picture");
                } else if (name.equals("") || phoneNumber.equals("") || emailID.equals("")) {
                    message.setText("Please fill all the Fields");
                } else if (!phoneNumber.matches(phoneNumberRegex) || !emailID.matches(emailIDRegex)) {
                    if (!phoneNumber.matches(phoneNumberRegex)) message.setText("Invalid Phone Number");
                    if (!emailID.matches(emailIDRegex)) emailErrorMessage.setText("Invalid Email Address");
                } else {
                    try {
                        generateReferenceID();
                        // Adding the new User Credentials into the database
                        BufferedWriter writer;
                        writer = new BufferedWriter(new FileWriter(db, true));
                        writer.write(username + "," + encryptPassword(password) + "," + refID + "\n");
                        writer.flush();
                        writer.close();

                        // Adding the user details into the UserDetails.txt file
                        writer = new BufferedWriter(new FileWriter(ud, true));
                        writer.write(username + "," + name + "," + phoneNumber + "," + emailID + "\n");
                        writer.flush();
                        writer.close();

                        // save the profile picture
                        savePP();

                        // Creating Transaction History and Enquiry Files for the user
                        File th = new File(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt");
                        File en = new File(dirname + "\\Databases\\Enquiries\\" + username + ".txt");
                        if (th.createNewFile() && en.createNewFile()) {
                            showMessageDialogJOP(frame, "Account Created Successfully", "SignUp Successful", JOptionPane.INFORMATION_MESSAGE);
                            showMessageDialogJOP(frame, "<html>Your Reference ID is " + refID + "\nStore or Remember this as it will be helpful when you forget password</html>".replaceAll("\n", "<br>"),
                                    "Reference ID", JOptionPane.INFORMATION_MESSAGE);
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

    // sets the current time to a String object
    String setCurrentTime() {
        String timeString = String.valueOf(LocalTime.now());
        return timeString.substring(0, timeString.lastIndexOf("."));
    }

    /**
     * sudoMode() method checks if the prescribed time of sudo mode is already over or not by
     * checking the difference b/w hours, minutes, seconds of the current time and the last time
     * at which the password was typed.
     * <p>
     * Return true if the sudo mode is over, false otherwise
     */
    Date ct = new Date(), pta = new Date(); // ct -> current time, pta -> password typed at
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    boolean isSudoModeTimeOver() {
        String currentTime;
        long diffInMilliSeconds;
        int diffInHours, diffInMinutes;

        currentTime = setCurrentTime();
        try {
            ct = sdf.parse(currentTime);
            pta = sdf.parse(passwordTypedAt);
        } catch (Exception ignored) {
        }

        // sudo mode will be active only for 1 minute, change code accordingly
        /* calculating differences between times ct and pta */
        diffInMilliSeconds = ct.getTime() - pta.getTime();
        diffInHours = (int) (diffInMilliSeconds / (60 * 60 * 1000));
        if (diffInHours > 1) {
            return true;
        }
        diffInMinutes = (int) (diffInMilliSeconds / (60 * 1000));
        return diffInMinutes >= 1;
    }

    class Verification implements ActionListener {
        String calledBy;

        Verification(String calledBy) {
            this.calledBy = calledBy;
        }

        JFrame verificationFrame;
        JLabel verificationPasswordLabel = new JLabel("Enter Password:");
        JPasswordField verificationPasswordField;
        JLabel forgotPassword = new JLabel("<html><u>Forgot Password?</u></html>");

        // All Objects
        BookingUI bookingObj = new BookingUI();
        EnquireUI enqObj = new EnquireUI();
        UpdateUsernameUI updateUsernameObj = new UpdateUsernameUI();
        PasswordChangeUI passwordChangeObj = new PasswordChangeUI();
        SwitchAccountsUI switchAccountsObj = new SwitchAccountsUI();

        @Override
        public void actionPerformed(ActionEvent e) {
            verificationFrame = new JFrame("Verification");

            if (sudoModeAccepted) {
                if (isSudoModeTimeOver()) showVerificationFrame();
                else callingCorrespondingFunction();
            } else {
                showVerificationFrame();
            }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        void showVerificationFrame() {
            verificationFrame.setLayout(null);
            verificationFrame.setVisible(true);
            verificationFrame.setSize(300, 300);
            verificationFrame.setLocationRelativeTo(frame);

            verificationPasswordField = new JPasswordField();
            JCheckBox verificationCB = new JCheckBox("Show Password");
            JButton verifyButton = new JButton("Verify");

            verificationFrame.add(verificationPasswordLabel);
            verificationFrame.add(verificationPasswordField);
            verificationFrame.add(forgotPassword);
            verificationFrame.add(verificationCB);
            verificationFrame.add(verifyButton);

            verificationPasswordLabel.setBounds(40, 100, 120, 25);
            verificationPasswordLabel.setFont(timesNewRoman);

            verificationPasswordField.setBounds(160, 100, 100, 25);
            verificationPasswordField.setFont(timesNewRoman);

            forgotPassword.setBounds(110, 130, 80, 10);
            forgotPassword.setFont(forgotPasswordFont);
            forgotPassword.setForeground(Color.RED);
            forgotPassword.setHorizontalAlignment(0);
            forgotPassword.addMouseListener(new ForgotPassword(verificationFrame, forgotPassword, "verification"));

            verificationCB.setBounds(90, 150, 150, 20);
            verificationCB.setFont(timesNewRoman);
            verificationCB.addActionListener(new ShowPasswordsCheckBox(verificationPasswordField));

            verifyButton.setBounds(75, 190, 150, 25);
            verifyButton.setBackground(Color.BLUE);
            verifyButton.setForeground(Color.WHITE);
            verifyButton.setFont(timesNewRoman);
            if (calledBy.equals("AccountDeletion")) {
                verifyButton.setText("Delete Account");
                verifyButton.setBackground(Color.RED);
            }

            verifyButton.addActionListener(new CheckingPassword());

            verificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        class ForgotPassword implements MouseListener {
            JLabel forgotPassword;
            JFrame jFrame;
            String calledBy;

            ForgotPassword(JFrame jFrame, JLabel forgotPassword, String calledBy) {
                this.jFrame = jFrame;
                this.forgotPassword = forgotPassword;
                this.calledBy = calledBy;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                String refIDString = JOptionPane.showInputDialog(jFrame, "Reference ID:");
                if (String.valueOf(refID).equals(refIDString)) {
                    passwordTypedAt = setCurrentTime();
                    showMessageDialogJOP(jFrame, "Hey " + name + "! Please reset the Password", "Reset the Password", JOptionPane.PLAIN_MESSAGE);
                    callingCorrespondingFunction();
                } else {
                    showMessageDialogJOP(jFrame, "Reference ID is wrong", "Reference ID is wrong", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        }

        class CheckingPassword implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                msg = new JLabel();
                msg.setOpaque(true);

                verificationFrame.add(msg);

                msg.setBounds(0, 225, 300, 50);
                msg.setFont(timesNewRoman);
                msg.setHorizontalAlignment(0);
                msg.setVerticalAlignment(0);

                if (String.valueOf(verificationPasswordField.getPassword()).equals("")) {
                    msg.setText("Please type Password");
                } else if (!password.equals(String.valueOf(verificationPasswordField.getPassword()))) {
                    msg.setForeground(Color.RED);
                    msg.setText("Password Incorrect");
                } else {
                    passwordTypedAt = setCurrentTime();
                    callingCorrespondingFunction();
                }
            }
        }

        void callingCorrespondingFunction() {
            if (!calledBy.equals("AccountDeletion")) verificationFrame.dispose();
            else AccountDeletion(verificationFrame);

            switch (calledBy) {
                case "Booking" -> bookingObj.bookingUI("verification");
                case "TH" -> TransactionHistory();
                case "Enquiry" -> enqObj.enquireUI();
                case "UpdateUsername" -> updateUsernameObj.updateUsernameUI();
                case "PasswordChange" -> passwordChangeObj.passwordChangeUI();
                case "SwitchAccounts" -> switchAccountsObj.switchAccountsUI();
            }
        }
    }

    JLabel usernameLabel = new JLabel();

    final int menuButtonWidth = 300, menuButtonHeight = 30, buttonsX = 200, diffInYs = 70;
    int buttonsY = 130;
    final int IMAGE_WIDTH = 64, IMAGE_HEIGHT = 64, imageX = (frameSize - IMAGE_WIDTH) / 2, imageY = (buttonsY - IMAGE_HEIGHT) / 2;
    final JLabel finderImage = new JLabel(new ImageIcon(dirname + "\\Images\\finder.png"));

    //    String[] settingsMenu = {"Menu", "Account", "Settings"};
//    JComboBox<String> settingsJCB = new JComboBox<>(settingsMenu);
    JMenuBar menuBar = new JMenuBar();
    JMenuItem menu = new JMenuItem("Menu");
    JMenuItem account = new JMenuItem("Account");
    JMenuItem settings = new JMenuItem("Settings");

    // use type of singleton class because the frame ui is static, not needed to always set bounds and all TODO
    void displayMenu() {
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.setSize(frameSize, frameSize);
        frame.setTitle("EazyFinder");
        frame.setLocationRelativeTo(null);

        JButton menuBookingButton = new JButton("Book for a Journey");
        JButton menuTHButton = new JButton("See Transaction History");
        JButton menuUpdateUsernameButton = new JButton("Update Username");
        JButton menuEnquiryButton = new JButton("Enquire");
        JButton menuPasswordChangeButton = new JButton("Change Password");
        JButton menuAccountDeleteButton = new JButton("Delete my Account");
        JButton menuSwitchAccountsButton = new JButton("Switch Accounts");
        JButton logoutButton = new JButton("Logout");

        menuBar.add(menu);
        menuBar.add(account);
        menuBar.add(settings);

        frame.add(menuBar);
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

        menuBar.setBounds(0, 0, frameSize, 25);
        menu.addActionListener(new Settings());
        menu.setFont(timesNewRoman);
        account.addActionListener(new Settings());
        account.setFont(timesNewRoman);
        settings.addActionListener(new Settings());
        settings.setFont(timesNewRoman);

        usernameLabel.setText("Username: " + username);
        usernameLabel.setBounds(400, 30, frameSize - 400, 25);
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
        // Components needed for Account Page
        JLabel accountLabel = new JLabel("Account");
        final Font headingFont = new Font("Times New Roman", Font.BOLD, 25);
        boolean[] alreadyDeleted = {false};

        // Settings
//        int sudoButtonClicked = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.repaint();

            if (e.getSource() == menu) { // Menu
                displayMenu();
            } else if (e.getSource() == account) { // Account
                backButton = new JButton("Back");
                JButton viewPhotoButton = new JButton("View Photo");
                JButton changePhotoButton = new JButton("Change Photo");
                JButton deletePhotoButton = new JButton("Delete Photo");
                JButton logoutButton = new JButton("Logout");
                JLabel usernameLabel = new JLabel("Username: " + username);
                JButton updateUsernameButton = new JButton("Update Username");
                JLabel passwordLabel = new JLabel("Password: ********");
                JButton passwordButton = new JButton("Show Password");
                JLabel noOfTransactionsLabel = new JLabel();
                JButton goToTHButton = new JButton("Go to Transactions Page");
                JLabel noOfEnquiriesLabel = new JLabel();
                JButton goToENQButton = new JButton("Go to Enquiries Page");
                JLabel profilePictureInAccount = new JLabel();

                frame.add(backButton);
                frame.add(accountLabel);
                frame.add(profilePictureInAccount);
                frame.add(viewPhotoButton);
                frame.add(changePhotoButton);
                frame.add(deletePhotoButton);
                frame.add(usernameLabel);
                frame.add(updateUsernameButton);
                frame.add(passwordLabel);
                frame.add(passwordButton);
                frame.add(noOfTransactionsLabel);
                frame.add(goToTHButton);
                frame.add(noOfEnquiriesLabel);
                frame.add(goToENQButton);
                frame.add(logoutButton);

                backButton.setBounds(0, 0, 80, 30);
                backButton.setBackground(Color.BLACK);
                backButton.setForeground(Color.WHITE);
                backButton.setFont(timesNewRoman);
                backButton.addActionListener(new Back((byte) 3));

                accountLabel.setBounds(250, 30, 200, 25);
                accountLabel.setHorizontalAlignment(0);
                accountLabel.setFont(headingFont);
                accountLabel.setToolTipText(username + "'s Account");

                int width = profilePictureWidth, height = profilePictureHeight;
                int[] wh = {width, height};
                changeWH(wh);

                profilePictureInAccount.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                profilePictureInAccount.setBounds((frameSize - wh[0]) / 2, ((250 - wh[1]) / 2) + 70, wh[0], wh[1]);
                profilePictureInAccount.setHorizontalAlignment(0);
                profilePictureInAccount.setVerticalAlignment(0);

                // profile picture operations
                viewPhotoButton.setBounds(500, 150, 150, 25);
                viewPhotoButton.setForeground(Color.WHITE);
                viewPhotoButton.setBackground(Color.DARK_GRAY);
                viewPhotoButton.setFont(timesNewRoman);
                viewPhotoButton.addActionListener(ae -> {
                    JLabel ppLabel = new JLabel();
                    ppLabel.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                    JOptionPane.showMessageDialog(frame, ppLabel, "Profile Picture", JOptionPane.PLAIN_MESSAGE);
                });

                changePhotoButton.setBounds(500, 200, 150, 25);
                changePhotoButton.setForeground(Color.WHITE);
                changePhotoButton.setBackground(Color.DARK_GRAY);
                changePhotoButton.setFont(timesNewRoman);
                changePhotoButton.addActionListener(ae -> {
                    if (setPPDetails() == JOptionPane.YES_OPTION) {
                        wh[0] = profilePictureWidth;
                        wh[1] = profilePictureHeight;
                        changeWH(wh);

                        profilePictureInAccount.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                        profilePictureInAccount.setBounds((frameSize - wh[0]) / 2, ((250 - wh[1]) / 2) + 70, wh[0], wh[1]);

                        // save the profile picture
                        savePP();
                        alreadyDeleted[0] = false;
                    }
                });

                // https://stackoverflow.com/questions/27379059/determine-if-two-files-store-the-same-content
                deletePhotoButton.setBounds(500, 250, 150, 25);
                deletePhotoButton.setForeground(Color.WHITE);
                deletePhotoButton.setBackground(Color.RED);
                deletePhotoButton.setFont(timesNewRoman);
                deletePhotoButton.addActionListener(ae -> {
                    // checking if the PP is already deleted TODO check this a small problem
                    try {
                        BufferedReader reader1 = new BufferedReader(new FileReader(dirname + "\\Images\\defaultPP.png"));
                        BufferedReader reader2 = new BufferedReader(new FileReader(dirname + "\\Databases\\ProfilePictures\\" + username + profilePictureExtension));
                        String str;
                        StringBuilder buf1 = new StringBuilder(), buf2 = new StringBuilder();
                        while ((str = reader1.readLine()) != null) buf1.append(str);
                        while ((str = reader2.readLine()) != null) buf2.append(str);
                        alreadyDeleted[0] = String.valueOf(buf1).equals(String.valueOf(buf2));
                        reader1.close();
                        reader2.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (alreadyDeleted[0]) {
                        showMessageDialogJOP(frame, "The Profile Picture is already Deleted", "Profile Picture Already Deleted", JOptionPane.PLAIN_MESSAGE);
                    } else if (areYouSureJOP(frame) == JOptionPane.YES_OPTION) {
                        profilePicturePath = dirname + "\\Images\\defaultPP.png";
                        profilePicture = new ImageIcon(dirname + "\\Images\\defaultPP.png");
                        profilePictureExtension = ".png";
                        profilePictureWidth = wh[0] = profilePicture.getIconWidth();
                        profilePictureHeight = wh[1] = profilePicture.getIconHeight();
                        changeWH(wh);

                        profilePictureInAccount.setIcon(new ImageIcon(profilePicture.getImage().getScaledInstance(wh[0], wh[1], Image.SCALE_DEFAULT)));
                        profilePictureInAccount.setBounds((frameSize - wh[0]) / 2, ((250 - wh[1]) / 2) + 55, wh[0], wh[1]);

                        // save the profile picture
                        savePP();
                        showMessageDialogJOP(frame, "Deleted Profile Picture Successfully", "Profile Picture Deleted", JOptionPane.INFORMATION_MESSAGE);
                        alreadyDeleted[0] = true;
                    }
                });

                usernameLabel.setBounds(75, 350, 350, 25);
                usernameLabel.setFont(timesNewRoman);

                updateUsernameButton.setBounds(400, 350, 200, 25);
                updateUsernameButton.setForeground(Color.WHITE);
                updateUsernameButton.setBackground(Color.BLUE);
                updateUsernameButton.setFont(timesNewRoman);
                updateUsernameButton.setHorizontalAlignment(0);
                updateUsernameButton.addActionListener(new Verification("UpdateUsername"));

                passwordLabel.setBounds(75, 390, 350, 25);
                passwordLabel.setFont(timesNewRoman);

                passwordButton.setBounds(400, 390, 200, 25);
                passwordButton.setForeground(Color.WHITE);
                passwordButton.setBackground(Color.RED);
                passwordButton.setFont(timesNewRoman);
                passwordButton.setHorizontalAlignment(0);
                passwordButton.addActionListener(ae -> {
                    if (passwordButton.getText().equals("Show Password")) {
                        String refIDString = JOptionPane.showInputDialog(frame, "Enter Reference ID:");
                        if (String.valueOf(refID).equals(refIDString)) {
                            passwordLabel.setText("Password: " + password);
                            passwordButton.setText("Hide Password");
                            passwordButton.setBackground(Color.BLUE);
                        } else {
                            showMessageDialogJOP(frame, "Reference ID is incorrect", "Reference ID is incorrect", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        passwordLabel.setText("Password: ********");
                        passwordButton.setText("Show Password");
                        passwordButton.setBackground(Color.RED);
                    }
                });

                // Calculating no. of enquiries made
                int noOfEnquiries = 0;
                try {
                    Scanner scanner = new Scanner(new File(dirname + "\\Databases\\Enquiries\\" + username + ".txt"));
                    while (scanner.hasNextLine()) {
                        scanner.nextLine();
                        noOfEnquiries++;
                    }
                } catch (Exception ignored) {
                }

                noOfEnquiriesLabel.setBounds(75, 430, 350, 25);
                noOfEnquiriesLabel.setText("Number of Enquiries made: " + noOfEnquiries);
                noOfEnquiriesLabel.setFont(timesNewRoman);

                goToENQButton.setBounds(400, 430, 200, 25);
                goToENQButton.setForeground(Color.WHITE);
                goToENQButton.setBackground(Color.BLUE);
                goToENQButton.setFont(timesNewRoman);
                goToENQButton.setHorizontalAlignment(0);
                goToENQButton.addActionListener(new Verification("Enquiry"));

                // Calculating no. of transactions made
                int noOfTransactions = 0;
                try {
                    Scanner scanner = new Scanner(new File(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt"));
                    while (scanner.hasNextLine()) {
                        scanner.nextLine();
                        noOfTransactions++;
                    }
                } catch (Exception ignored) {
                }

                noOfTransactionsLabel.setBounds(75, 470, 350, 25);
                noOfTransactionsLabel.setText("Number of Transactions made: " + noOfTransactions);
                noOfTransactionsLabel.setFont(timesNewRoman);

                goToTHButton.setBounds(400, 470, 200, 25);
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
            } else { // Settings. TODO put the account deletion, password change, update username options in here
                backButton = new JButton("Back");
                JLabel settingsLabel = new JLabel("Settings");
                JButton deleteTHButton = new JButton("Delete all the Transaction Histories");
                JButton deleteEnqButton = new JButton("Delete all the Enquiries");
                JButton deleteAccountData = new JButton("Delete all my account Data");
                JButton deleteAccount = new JButton("Delete My Account");
                JLabel sudoModeLabel = new JLabel("Sudo Mode");
                JButton sudoModeButton = new JButton();
                JButton logoutButton = new JButton("Logout");

                frame.add(backButton);
                frame.add(settingsLabel);
                frame.add(deleteTHButton);
                frame.add(deleteEnqButton);
                frame.add(deleteAccountData);
                frame.add(deleteAccount);
                frame.add(sudoModeLabel);
                frame.add(sudoModeButton);
                frame.add(logoutButton);

                backButton.setBounds(0, 0, 80, 30);
                backButton.setBackground(Color.BLACK);
                backButton.setForeground(Color.WHITE);
                backButton.setFont(timesNewRoman);
                backButton.addActionListener(new Back((byte) 3));

//                settingsJCB.setBounds(550, 35, 100, 25);
//                settingsJCB.setSelectedItem("Settings");
//                settingsJCB.setFont(timesNewRoman);
//                settingsJCB.addActionListener(new Settings());

                settingsLabel.setBounds(0, 30, frameSize, 25);
                settingsLabel.setForeground(Color.DARK_GRAY);
                settingsLabel.setFont(headingFont);
                settingsLabel.setHorizontalAlignment(0);

                File th = new File(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt");
                File enq = new File(dirname + "\\Databases\\Enquiries\\" + username + ".txt");

                deleteTHButton.setBounds(200, 65, 300, 30);
                deleteTHButton.setForeground(Color.BLACK);
                deleteTHButton.setBackground(Color.orange);
                deleteTHButton.setFont(timesNewRoman);
                deleteTHButton.addActionListener(ae -> {
                    if (th.length() == 0) {
                        showMessageDialogJOP(frame, "No Transaction History", "No Transaction History", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        optionPaneLabel.setText("<html>Are You Sure?\nAll your Transaction History will be lost</html>".replaceAll("\n", "<br>"));
                        if (JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Confirmation",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                            try {
                                new FileWriter(th, false).close();
                                showMessageDialogJOP(frame, "Transaction History Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                            } catch (Exception ex) {
                                showMessageDialogJOP(frame, "<html>Some Error Occurred\nTransaction History not Deleted\nSorry for the inconvenience caused</html>".replaceAll("\n", "<br>"),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });

                deleteEnqButton.setBounds(200, 110, 300, 30);
                deleteEnqButton.setForeground(Color.BLACK);
                deleteEnqButton.setBackground(Color.orange);
                deleteEnqButton.setFont(timesNewRoman);
                deleteEnqButton.addActionListener(ae -> {
                    if (enq.length() == 0) {
                        showMessageDialogJOP(frame, "No Enquiries", "No Enquiries", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        optionPaneLabel.setText("<html>Are You Sure?\nAll your Enquiries will be lost</html>".replaceAll("\n", "<br>"));
                        if (JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Confirmation",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                            try {
                                new FileWriter(enq, false).close();
                                showMessageDialogJOP(frame, "All Enquiries Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                            } catch (Exception ex) {
                                showMessageDialogJOP(frame, "<html>Some Error Occurred\nEnquiries not Deleted\nSorry for the inconvenience caused</html>".replaceAll("\n", "<br>"),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });

                deleteAccountData.setBounds(200, 155, 300, 30);
                deleteAccountData.setForeground(Color.WHITE);
                deleteAccountData.setBackground(Color.red);
                deleteAccountData.setFont(timesNewRoman);
                deleteAccountData.addActionListener(ae -> {
                    if (th.length() == 0 && enq.length() == 0) {
                        showMessageDialogJOP(frame, "Their is no Account Data left", "Their is no Account Data left", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        optionPaneLabel.setText(("<html>Are You Sure?\n" + "All your Account Data will be lost\n\nAccount Data Includes:\n" +
                                "1) Transaction History till date\n" + "2) Enquiries Made till date</html>".replaceAll("\n", "<br>")));
                        if (JOptionPane.showConfirmDialog(frame, optionPaneLabel, "Confirmation",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                            try {
                                new FileWriter(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt", false).close();
                                new FileWriter(dirname + "\\Databases\\Enquiries\\" + username + ".txt", false).close();
                                showMessageDialogJOP(frame, "All the Account Data Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
                            } catch (Exception ex) {
                                showMessageDialogJOP(frame, "<html>Some Error Occurred\nAccount Data not Deleted\nSorry for the inconvenience caused</html>".replaceAll("\n", "<br>"),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });

                deleteAccount.setBounds(200, 200, 300, 30);
                deleteAccount.setForeground(Color.WHITE);
                deleteAccount.setBackground(Color.RED);
                deleteAccount.setFont(timesNewRoman);
                deleteAccount.addActionListener(new Verification("AccountDeletion"));

                sudoModeLabel.setBounds(265, 245, 100, 25);
                sudoModeLabel.setFont(timesNewRoman);

                if (sudoModeAccepted) sudoModeButton.setText("OFF");
                else sudoModeButton.setText("ON");
                sudoModeButton.setBounds(370, 245, 70, 25);
                sudoModeButton.setForeground(Color.WHITE);
                sudoModeButton.setBackground(Color.DARK_GRAY);
                sudoModeButton.addActionListener(ae -> {
                    if (sudoModeButton.getText().equals("ON")) {
                        sudoModeButton.setText("OFF");
                        sudoModeAccepted = true;
                        showMessageDialogJOP(frame, "<html>Sudo Mode is On\nPassword will be prompted only for\n1 minute of Interval</html>".replaceAll("\n", "<br>"),
                                "Sudo Mode is On", JOptionPane.INFORMATION_MESSAGE);
                    } else if (sudoModeButton.getText().equals("OFF")) {
                        sudoModeButton.setText("ON");
                        sudoModeAccepted = false;
                        showMessageDialogJOP(frame, "Sudo Mode is Off", "Sudo Mode is Off", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                logoutButton.setBounds(586, 0, 100, 30);
                logoutButton.setBackground(Color.RED);
                logoutButton.setForeground(Color.WHITE);
                logoutButton.setFont(timesNewRoman);
                logoutButton.addActionListener(new Back((byte) 1));
            }
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


    class BookingUI {
        final int bookingLabelWidth = 100, bookingFieldWidth = 200, bookingComponentHeight = 25;
        final int messageX = 510, bookingLabelX = 200, bookingFieldX = 330, diffInYs = 50;
        int componentY = 50;

        JLabel nameLabel, phoneLabel, emailLabel, adultLabel, childrenLabel, cityLabel, sourceLabel, destinationLabel;
        JLabel phoneMessage, emailMessage;
        JTextField nameField, phoneField, emailField;
        JComboBox<String> cityField, sourceField, destinationField;
        JSpinner adultField, childrenField;

        // MOT related
        String[] motOptions = {"Choose Mode Of Transportation Option", "One Transport for Whole Journey",
                "Mode Of Transportation Place to Place"};
        JComboBox<String> modeOfTransportationCB = new JComboBox<>(motOptions);
        JButton modeOfTransportationButton = new JButton();

        void bookingUI(String calledBy) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.setTitle("Booking");

            backButton = new JButton("Back");
            nameLabel = new JLabel("Name:");
            nameField = new JTextField();
            phoneLabel = new JLabel("Mobile No.:");
            phoneField = new JTextField();
            phoneMessage = new JLabel();
            emailLabel = new JLabel("Email ID:");
            emailField = new JTextField();
            emailMessage = new JLabel();
            cityLabel = new JLabel("City");
            sourceLabel = new JLabel("Source:");
            destinationLabel = new JLabel("Destination:");
            adultLabel = new JLabel("Adults:");
            childrenLabel = new JLabel("Children:");
            JButton nextButton = new JButton("Next");
            JButton clearFormButton = new JButton("Clear Form");
            msg = new JLabel();
            JButton logoutButton = new JButton("Logout");

            if (calledBy.equals("verification")) {
                cityField = new JComboBox<>(citiesArray);
                sourceField = new JComboBox<>(temp);
                destinationField = new JComboBox<>(temp);
                adultField = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
                childrenField = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
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
            frame.add(clearFormButton);
            frame.add(nextButton);
            frame.add(logoutButton);

            if (modeOfTransportationCB.isEnabled()) frame.remove(modeOfTransportationCB);
            if (modeOfTransportationButton.isEnabled()) frame.remove(modeOfTransportationButton);
            msg.setText("");

            backButton.setBounds(0, 0, 80, 30);
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(timesNewRoman);
            backButton.addActionListener(new Back((byte) 2));

            nameLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            nameLabel.setFont(timesNewRoman);

            nameField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            nameField.setFont(timesNewRoman);
            nameField.setText(name);
            componentY += diffInYs;

            phoneLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            phoneLabel.setFont(timesNewRoman);

            phoneField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            phoneField.setFont(timesNewRoman);
            phoneField.setText(phoneNumber);

            phoneMessage.setBounds(messageX, componentY, bookingFieldWidth, bookingComponentHeight);
            phoneMessage.setFont(timesNewRoman);
            phoneMessage.setForeground(Color.RED);
            phoneMessage.setHorizontalAlignment(0);
            phoneMessage.setVerticalAlignment(0);
            componentY += diffInYs;

            emailLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            emailLabel.setFont(timesNewRoman);

            emailField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            emailField.setFont(timesNewRoman);
            emailField.setText(emailID);

            emailMessage.setBounds(messageX, componentY, bookingFieldWidth, bookingComponentHeight);
            emailMessage.setFont(timesNewRoman);
            emailMessage.setForeground(Color.RED);
            emailMessage.setHorizontalAlignment(0);
            emailMessage.setVerticalAlignment(0);
            componentY += diffInYs;

            cityLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            cityLabel.setFont(timesNewRoman);

            cityField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            cityField.setFont(timesNewRoman);
            cityField.addActionListener(new InitializeCombos(cityField, sourceField, destinationField));
            componentY += diffInYs;

            sourceLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            sourceLabel.setFont(timesNewRoman);

            sourceField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            sourceField.setFont(timesNewRoman);
            componentY += diffInYs;

            destinationLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            destinationLabel.setFont(timesNewRoman);

            destinationField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            destinationField.setFont(timesNewRoman);
            componentY += diffInYs;

            adultLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            adultLabel.setFont(timesNewRoman);

            adultField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            adultField.setFont(timesNewRoman);
            componentY += diffInYs;

            childrenLabel.setBounds(bookingLabelX, componentY, bookingLabelWidth, bookingComponentHeight);
            childrenLabel.setFont(timesNewRoman);

            childrenField.setBounds(bookingFieldX, componentY, bookingFieldWidth, bookingComponentHeight);
            childrenField.setFont(timesNewRoman);
            componentY += diffInYs;

            clearFormButton.setBounds(0, componentY, 100, bookingComponentHeight);
            clearFormButton.setForeground(Color.WHITE);
            clearFormButton.setBackground(Color.RED);
            clearFormButton.setFont(timesNewRoman);
            clearFormButton.addActionListener(e -> {
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                cityField.setSelectedItem("--Select--");
                adultField.setValue(1);
                childrenField.setValue(0);
            });

            nextButton.setBounds(300, componentY, 100, bookingComponentHeight);
            nextButton.setBackground(Color.DARK_GRAY);
            nextButton.setForeground(Color.WHITE);
            nextButton.setFont(timesNewRoman);
            nextButton.addActionListener(new ContinueToModeOfTransportation());
            componentY += diffInYs;

            logoutButton.setBounds(586, 0, 100, 30);
            logoutButton.setBackground(Color.RED);
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(timesNewRoman);
            logoutButton.addActionListener(new Back((byte) 1));

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // user details related objects/variables
        short noOfAdults, noOfChildren;
        String typedName, typedPhoneNumber, typedEmailID;

        class ContinueToModeOfTransportation implements ActionListener {
            BookingMainCode bookingObj;

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.add(msg);

                msg.setFont(timesNewRoman);
                msg.setForeground(Color.RED);
                msg.setHorizontalAlignment(0);
                msg.setOpaque(true);

                msg.setText("");
                phoneMessage.setText("");
                emailMessage.setText("");

                name = nameField.getText().trim();
                typedPhoneNumber = phoneField.getText().trim();
                typedEmailID = emailField.getText().trim();
                city = String.valueOf(cityField.getSelectedItem());
                source = String.valueOf(sourceField.getSelectedItem());
                destination = String.valueOf(destinationField.getSelectedItem());
                noOfAdults = Short.parseShort(String.valueOf(adultField.getValue()));
                noOfChildren = Short.parseShort(String.valueOf(childrenField.getValue()));

                if (name.equals("") || typedPhoneNumber.equals("") || source.equals("--Select--") || destination.equals("--Select--")) {
                    msg.setBounds(225, 480, 250, 25);
                    msg.setText("Please Complete all the fields");
                } else if (source.equals(destination)) {
                    msg.setBounds(200, 480, 350, 25);
                    msg.setText("Source and Destination Cannot be the same");
                } else if (!typedPhoneNumber.matches(phoneNumberRegex) || !typedEmailID.matches(emailIDRegex)) {
                    if (!typedPhoneNumber.matches(phoneNumberRegex)) phoneMessage.setText("Invalid Phone Number");
                    if (!typedEmailID.matches(emailIDRegex)) emailMessage.setText("Invalid Email Address");
                } else {
                    msg.setText("");
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

                    String[] optionPaneButtons = {"Go Back", "Continue to Booking", "Break the Fare"};
                    optionPaneLabel.setText(String.valueOf(routeCost).replaceAll("\n", "<br>"));
                    optionPaneLabel.setFont(timesNewRoman);
                    optionPaneResult = JOptionPane.showOptionDialog(frame, optionPaneLabel,
                            "Route and Details", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                            null, optionPaneButtons, null);

                    if (optionPaneResult == JOptionPane.NO_OPTION) {
                        frame.add(modeOfTransportationCB);

                        modeOfTransportationCB.setBounds(200, 510, 300, 25);
                        modeOfTransportationCB.setFont(timesNewRoman);

                        modeOfTransportationButton = new JButton("GO");
                        frame.add(modeOfTransportationButton);
                        modeOfTransportationButton.setBounds(300, 550, 100, 25);
                        modeOfTransportationButton.setBackground(Color.GREEN);
                        modeOfTransportationButton.setForeground(Color.WHITE);
                        modeOfTransportationButton.setFont(timesNewRoman);
                        modeOfTransportationButton.addActionListener(new AfterMOT());
                    } else if (optionPaneResult == JOptionPane.CANCEL_OPTION) {
                        showMessageDialogJOP(frame,
                                ("<html>No. of Adults: " + noOfAdults + "\n" + "Fare: " + cost * noOfAdults + "\n\n" +
                                        "No. of Children: " + noOfChildren + "\n" + "Fare: " + (cost / 2) * noOfChildren + "\n\n</html>").replaceAll("\n", "<br>"),
                                "Fare break", JOptionPane.INFORMATION_MESSAGE);
                    }
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
                        msg.setBounds(0, 600, frameSize, 25);
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
                JButton logoutButton = new JButton("Logout");

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

                book.addActionListener(e -> {
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

                    bookingObj.loadDetails(username, city, source, destination, bookingObj.calculateTotalCost(),
                            typedName, typedPhoneNumber, typedEmailID, noOfAdults, noOfChildren,
                            new SimpleDateFormat("dd:MM:yyyy").format(date),
                            new SimpleDateFormat("HH:mm:ss").format(date));

                    backButton = new JButton("Back");
                    msg = new JLabel(("<html>Successfully Booked a Ticket from " +
                            source.toUpperCase() + " to " + destination.toUpperCase() + "\n" + "Total Cost: " + cost +
                            "\n" + "</html>").replaceAll("\n", "<br>"));
                    JButton logoutButton = new JButton("Logout");

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

        backButton = new JButton("Back");
        msg = new JLabel();
        JButton logoutButton = new JButton("Logout");

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
            JButton logoutButton = new JButton("Logout");

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

            enquireChildrenLabel.setBounds(200, 400, 120, 25);
            enquireChildrenLabel.setFont(timesNewRoman);

            enquireChildrenField.setBounds(320, 400, 200, 25);
            enquireChildrenField.setFont(timesNewRoman);

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

        /**
         * @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html#:~:text=the%20user%20entered.-,Stopping%20Automatic%20Dialog%20Closing,-By%20default%2C%20when">How to Stop automatic closing of JOptionPane</a>
         */
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

                    if (optionPaneResult == JOptionPane.YES_OPTION) {
                        // store the enquiries
                        File enquiryFile = new File(dirname + "\\Databases\\Enquiries\\" + username + ".txt");
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(enquiryFile, true));
                            writer.write(enquireCity.toUpperCase() + "," + enquireSource.toUpperCase() + "," + enquireDestination.toUpperCase() + "," + cost + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception ex) {
                            msg.setText("Error Occurred");
                        }
                    } else if (optionPaneResult == JOptionPane.NO_OPTION) {
                        BookingUI bookingObj = new BookingUI();

                        bookingObj.cityField = enquireCityField;
                        bookingObj.sourceField = enquireSourceField;
                        bookingObj.destinationField = enquireDestinationField;
                        bookingObj.adultField = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
                        bookingObj.childrenField = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

                        bookingObj.cityField.setSelectedItem(enquireCity);
                        bookingObj.sourceField.setSelectedItem(enquireSource);
                        bookingObj.destinationField.setSelectedItem(enquireDestination);
                        bookingObj.adultField.setValue(enquireAdults);
                        bookingObj.childrenField.setValue(enquireChildren);

                        bookingObj.bookingUI("enquiry");
                    } else {
                        JLabel fareDivision = new JLabel();

                        fareDivision.setText(
                                ("<html>No. of Adults: " + enquireAdults + "\nFare: " + cost * enquireAdults + "\n\n\n" +
                                        "No. of Children: " + enquireChildren + "\nFare: " + (cost / 2) * enquireChildren + "\n\n\n" +
                                        "Total Cost: " + totalCost + "</html>").replaceAll("\n", "<br>"));
                        fareDivision.setHorizontalAlignment(0);
                        fareDivision.setVerticalAlignment(0);
                        fareDivision.setFont(timesNewRoman);

                        JOptionPane.showMessageDialog(frame, fareDivision, "Fare Division", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }
    }


    class UpdateUsernameUI {
        JLabel newUsernameLabel = new JLabel("New Username:");
        JFrame updateUsernameFrame = new JFrame();
        JTextField newUsernameField = new JTextField();
        JButton changeUsernameButton;

        void updateUsernameUI() {
            updateUsernameFrame.setSize(300, 300);
            updateUsernameFrame.setTitle("Update Username");
            updateUsernameFrame.setLocationRelativeTo(frame);
            updateUsernameFrame.setVisible(true);
            updateUsernameFrame.setLayout(null);

            changeUsernameButton = new JButton("Change Username");

            updateUsernameFrame.add(newUsernameLabel);
            updateUsernameFrame.add(newUsernameField);
            updateUsernameFrame.add(changeUsernameButton);

            newUsernameLabel.setBounds(40, 90, 120, 25);
            newUsernameLabel.setFont(timesNewRoman);

            newUsernameField.setBounds(160, 90, 100, 25);
            newUsernameField.setText("");
            newUsernameField.setFont(timesNewRoman);

            changeUsernameButton.setBounds(60, 130, 180, 25);
            changeUsernameButton.setBackground(Color.RED);
            changeUsernameButton.setForeground(Color.WHITE);
            changeUsernameButton.setFont(timesNewRoman);
            changeUsernameButton.addActionListener(new UpdateUsername());

            updateUsernameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        class UpdateUsername implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUsernameField.getText();
                msg = new JLabel();

                updateUsernameFrame.add(msg);

                msg.setBounds(0, 180, 300, 25);
                msg.setFont(timesNewRoman);
                msg.setHorizontalAlignment(0);

                if (newUsername.equals("")) {
                    msg.setText("Please type new Username");
                } else {
                    boolean found = false;
                    if (newUsername.equals(username)) {
                        msg.setText("<html>New Username Cannot be the Same as\nold one</html>".replaceAll("\n", "<br>"));
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
                                if (new UpdateUsernameMainCode().updateUsername(username, newUsername, password, refID)) {
                                    updateUsernameFrame.dispose();

                                    username = newUsername;

                                    usernameLabel.setText("Username: " + username);

                                    showMessageDialogJOP(frame, "Username Changed Successfully", "Successful", JOptionPane.INFORMATION_MESSAGE);
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
                        if (new PasswordChangeMainCode().passwordChange(username, password, newPassword, refID)) {
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
        optionPaneLabel.setText("<html>Are You Sure?\nAll Your Transactions, Enquiries will be lost\nThis is irreversible</html>".replaceAll("\n", "<br>"));
        optionPaneResult = JOptionPane.showConfirmDialog(frame1, optionPaneLabel, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (optionPaneResult == JOptionPane.YES_OPTION) {
            frame1.dispose();
            boolean deleted = new AccountDeletionMainCode().accountDeletion(username, password, refID);
            if (deleted) {
                Homepage();
                showMessageDialogJOP(frame, "<html>Account Deleted Successfully\nWe are Sorry to see you go</html>".replaceAll("\n", "<br>"),
                        "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessageDialogJOP(frame1, "Some Error Occurred, Account not Deleted", "Account Not Deleted", JOptionPane.ERROR_MESSAGE);
            }
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
