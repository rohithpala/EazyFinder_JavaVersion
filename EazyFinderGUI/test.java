package EazyFinderGUI;

import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JLabel label = new JLabel("MESSAGE");
        label.setFont(new Font("Times New Roman",Font.BOLD, 18));
        JOptionPane.showMessageDialog(null,label,"ERROR",JOptionPane.WARNING_MESSAGE);
    }
}
