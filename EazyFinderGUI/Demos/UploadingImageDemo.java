package EazyFinderGUI.Demos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UploadingImageDemo implements ActionListener {
    JFrame fr = new JFrame("Image loading program Using awt");
    Label Label1 = new Label("Choose your image");
    Button Button1 = new Button("select");
    Image Image1;
    FileDialog fd = new FileDialog(fr, "Open", FileDialog.LOAD);

    void initialize() {
        fr.setSize(500, 500);
        fr.setLocation(200, 200);
        fr.setBackground(Color.lightGray);
        fr.setLayout(new FlowLayout());
        fr.add(Label1);
        fr.add(Button1);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Button1.addActionListener(this);
        fr.setVisible(true);
    }

    void imageLoad() {
        fd.setVisible(true);
        if (fd.getFile() == null) {
            Label1.setText("You have not select");
        } else {
            String d = (fd.getDirectory() + fd.getFile());
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image1 = toolkit.getImage(d);
            Label1.setText(fd.getDirectory() + fd.getFile());
        }
    }

    public void actionPerformed(ActionEvent event) {
        Button b = (Button) event.getSource();
        if (b == Button1) {
            imageLoad();
        }
    }

    public static void main(String[] args) {
        new UploadingImageDemo().initialize();
    }
} 