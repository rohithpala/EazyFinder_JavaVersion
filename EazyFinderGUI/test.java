package EazyFinderGUI;

import javax.swing.*;

public class test {
    public static void main(String[] args) {
        String[] s = {"-Select-", "Hyd", "Ben", "Che"};
        JComboBox<String> cb = new JComboBox<>(s);
        JButton but = new JButton("Click");

        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(cb);
        frame.add(but);

        cb.setBounds(0, 0, 100, 25);
        but.setBounds(100, 100, 100, 25);
        but.addActionListener(e -> {
            cb.setSelectedItem("Ben");
        });
    }
}
