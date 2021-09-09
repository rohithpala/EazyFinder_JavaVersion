package EazyFinderGUI;

import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JLabel label = new JLabel("Enter Password:");
        label.setFont(new Font("Times New Roman", Font.BOLD, 15));
        JPasswordField pf = new JPasswordField();

        if (JOptionPane.showOptionDialog(null, new Object[]{label, pf}, "Demo", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Ok, I'm Good to go", "No"}, null) == JOptionPane.YES_OPTION) {
            System.out.println(pf.getPassword());
        }
    }
}
