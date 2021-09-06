package EazyFinderGUI.Demos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UnderlingJLabelOnMouseHover implements MouseListener {
    public static void main(String[] args) {
        new UnderlingJLabelOnMouseHover();
    }

    JFrame frame = new JFrame();
    JLabel label = new JLabel("Hey !!!!");
    UnderlingJLabelOnMouseHover() {
        frame.setSize(200, 200);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(label);

        label.setBounds(0, 0, 100, 25);
        label.setHorizontalAlignment(0);
        label.setVerticalAlignment(0);
        label.addMouseListener(this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(frame, "You Clicked a JLabel", "", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        label.setText("<html><u>Hey !!!!</u></html>");
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        label.setText("Hey !!!!");
    }
}
