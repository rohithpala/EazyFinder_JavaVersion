package EazyFinderGUI.MainCodes;

import java.io.*;

public class PasswordChangeMainCode {
    long encryptPassword(String password) {
        long encryptedPassword = 0, a = 1;
        int i, len = password.length();
        for (i = 0; i < len; i++) {
            encryptedPassword += ((int) password.charAt(i)) * a;
            a *= 100;
        }
        return encryptedPassword;
    }

    public boolean passwordChange(String username, String oldPassword, String newPassword, long refID) {
        String str, dirname = System.getProperty("user.dir");
        StringBuilder credentials = new StringBuilder();
        File db = new File(dirname + "\\EazyFinderGUI\\Databases\\LogInSignUpDatabase.txt");
        try {
            // initializing "credentials" with correct details
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                if (!(username + "," + encryptPassword(oldPassword) + "," + refID).equals(str)) {
                    credentials.append(str).append("\n;");
                } else {
                    credentials.append(username).append(",").append(encryptPassword(newPassword)).append(",").append(refID).append("\n;");
                }
            }
            reader.close();

            // adding new details to db
            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            String[] s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }
}
