package MiscellaneousDemos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTime {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
        String str = sdf.format(new Date());
        System.out.println(str);

        sdf.applyPattern("hh:mm:ss");

//        sdf = new SimpleDateFormat("hh:mm:ss");
        str = sdf.format(new Date());
        System.out.println(str);
    }
}
