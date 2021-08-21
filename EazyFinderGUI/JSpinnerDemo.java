package EazyFinderGUI;

import javax.swing.*;

public class JSpinnerDemo {
    public static void main(String[] args) {
        JFrame f = new JFrame("Spinner Demo");
        f.setSize(300, 300);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpinnerModel value = new SpinnerNumberModel(5, 0, 10, 1);
        JSpinner spinner = new JSpinner(value);

        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setEditable(false);
        tf.setHorizontalAlignment(JLabel.LEFT);

        spinner.setBounds(100, 100, 50, 30);
        f.add(spinner);
    }
} 