package EazyFinderGUI.Demos;
import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(450, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name:");
        JLabel chooseProfilePictureLabel = new JLabel("Choose Profile Picture:");
        JButton selectProfilePictureButton = new JButton("Select");
        JTextField nameField = new JTextField();

        frame.add(chooseProfilePictureLabel);
        frame.add(selectProfilePictureButton);
        frame.add(nameLabel);
        frame.add(nameField);

        chooseProfilePictureLabel.setBounds(100, 85, 170, 25);

        selectProfilePictureButton.setText("Select");
        selectProfilePictureButton.setBounds(270, 85, 100, 25);
        selectProfilePictureButton.setBackground(Color.DARK_GRAY);
        selectProfilePictureButton.setForeground(Color.WHITE);

        nameLabel.setBounds(100, 135, 100, 25);

        nameField.setBounds(220, 135, 150, 25);
    }
}
