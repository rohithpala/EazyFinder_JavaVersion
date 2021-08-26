package EazyFinderGUI;

import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(700, 700);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel phoneLabel = new JLabel("Phone Number:");
        JLabel emailLabel = new JLabel("Email ID:");
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JLabel phoneMessage = new JLabel("Invalid Phone Number");
        JLabel emailMessage = new JLabel("Invalid Email ID");
        Font timesNewRoman = new Font("Times New Roman", Font.BOLD, 15);


        frame.add(phoneLabel);
        frame.add(phoneField);
        frame.add(phoneMessage);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(emailMessage);

        phoneLabel.setBounds(20, 100, 130, 25); //200, 100, 130, 25
        phoneLabel.setFont(timesNewRoman);

        phoneField.setBounds(130, 100, 200, 25); //330, 100, 200, 25
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
    }
}
