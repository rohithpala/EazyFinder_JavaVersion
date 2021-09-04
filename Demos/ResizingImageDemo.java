package EazyFinderGUI.Demos;

import javax.swing.*;
import java.awt.*;

public class ResizingImageDemo {
    public static void main(String[] args) {
        // Edit Configurations while working in an IDE before running the program
        String dirname = System.getProperty("user.dir"); // Path till EazyFinder

        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(new ImageIcon(dirname + "\\Images\\no_transactions.png")
                .getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));

        JFrame frame = new JFrame("Image in Label");
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(label);

        label.setBounds(0, 0, 200, 200);
    }
}
