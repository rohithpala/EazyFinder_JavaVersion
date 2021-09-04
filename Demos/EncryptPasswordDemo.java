package EazyFinderGUI.Demos;

import java.util.Scanner;

public class EncryptPasswordDemo {
    public static void main(String[] args) {
        System.out.print("Password: ");
        String password = new Scanner(System.in).next();
        long encryptedPassword = 0, a = 1;
        short i, len = (short) password.length();
        for (i = 0; i < len; i++) {
            encryptedPassword += ((int) password.charAt(i)) * a;
            a *= 100;
        }
        System.out.println(encryptedPassword);
    }
}
