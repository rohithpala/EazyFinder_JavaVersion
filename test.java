package EazyFinderGUI;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        Date passwordTypedAt = new Date();
        Date currentTime = new Date();
        SimpleDateFormat ct = new SimpleDateFormat("HH:mm:ss");

        ct.format(passwordTypedAt.getTime());
        passwordTypedAt.setTime(passwordTypedAt.getTime() + 1801000);
        System.out.println(passwordTypedAt.getTime());
        ct.format(currentTime.getTime());
        System.out.println(currentTime.getTime());
    }
}
