package EazyFinderGUI;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        Date passwordTypedAt = new Date();
        Date currentTime = new Date();
        SimpleDateFormat ct = new SimpleDateFormat("HH:mm:ss");

        ct.format(passwordTypedAt);
        System.out.println(passwordTypedAt.getTime());
        try{
            Thread.sleep(1000);
        } catch (Exception ignored) {}
        ct.format(currentTime);
        System.out.println(currentTime.getTime());

        System.out.println();
        String currentTime_ = String.valueOf(java.time.LocalTime.now());
        int i = currentTime_.lastIndexOf(".");
        System.out.println(currentTime_.substring(0, i));
        try{
            Thread.sleep(1000);
        } catch (Exception ignored) {}
        currentTime_ = String.valueOf(java.time.LocalTime.now());
        i = currentTime_.lastIndexOf(".");
        System.out.println(currentTime_.substring(0, i));
    }
}
