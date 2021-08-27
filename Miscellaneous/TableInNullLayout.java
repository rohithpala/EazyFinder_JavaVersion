package Miscellaneous;

import javax.swing.*;

public class TableInNullLayout {
    void abc(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);
        frame.setVisible(true);

        JButton button = new JButton("Button");
        panel.add(button);

        button.setBounds(10, 200, 100, 25);

        String[][] data = {{"1.", "ABC"}, {"2.", "DEF"}, {"3.", "GHI"}};
        String[] col = {"Sr. No", "Name"};
        JTable table = new JTable(data, col);
        table.setBounds(100, 100, 100, 80);
        panel.add(table);
        table.setVisible(true);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new TableInNullLayout().abc();
    }
}