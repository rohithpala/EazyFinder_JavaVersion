package EazyFinderGUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JSpinnerDemo {
    int value;
    JSpinner spinner;
    public static void main(String[] args) {
        new JSpinnerDemo().A();
    }

    void A(){
        JFrame f = new JFrame("Spinner Demo");
        f.setSize(300, 300);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        spinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));

        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setEditable(false);
        tf.setHorizontalAlignment(JLabel.LEFT);

        spinner.setBounds(100, 100, 50, 30);
        f.add(spinner);
        value = (int) spinner.getValue();
        spinner.addChangeListener(new GetValue());

        JButton button = new JButton("Click");
        button.setBounds(200, 100, 50, 30);
        f.add(button);
        button.addActionListener(e -> System.out.println(value));
    }

    class GetValue implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e) {
            value = (int) spinner.getValue();
        }
    }
}
