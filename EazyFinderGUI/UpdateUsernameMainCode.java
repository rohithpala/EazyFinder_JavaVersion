package EazyFinderGUI;

import java.io.*;

public class UpdateUsernameMainCode {
    long encryptPassword(String password) {
        long encryptedPassword = 0, a = 1;
        int i, len = password.length();
        for (i = 0; i < len; i++) {
            encryptedPassword += ((int) password.charAt(i)) * a;
            a *= 100;
        }
        return encryptedPassword;
    }

    boolean updateUsername(String username, String newUsername, String password) {
        String str, dirname = System.getProperty("user.dir");
        StringBuilder credentials = new StringBuilder();
        File db = new File(dirname + "\\EazyFinderGUI\\LogInSignUpDatabase.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                if (!(username + " " + encryptPassword(password)).equals(str)) {
                    credentials.append(str).append("\n:");
                } else {
                    credentials.append(newUsername).append(" ").append(encryptPassword(password)).append("\n:");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            String[] s = String.valueOf(credentials).split(":");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
