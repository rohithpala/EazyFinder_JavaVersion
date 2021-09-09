package EazyFinderGUI.MainCodes;

import java.io.*;
import java.util.Objects;

public class AccountDeletionMainCode {
    long encryptPassword(String password) {
        long encryptedPassword = 0, a = 1;
        int i, len = password.length();
        for (i = 0; i < len; i++) {
            encryptedPassword += ((int) password.charAt(i)) * a;
            a *= 100;
        }
        return encryptedPassword;
    }

    public boolean accountDeletion(String username, String password, long refID) {
        String str, dirname = System.getProperty("user.dir") + "\\EazyFinderGUI";
        StringBuilder credentials = new StringBuilder();
        File db = new File(dirname + "\\Databases\\LogInSignUpDatabase.txt");
        File ud = new File(dirname + "\\Databases\\UserDetails.txt");

        try {
            // removing user's data in db
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                if (!(username + "," + encryptPassword(password) + "," + refID).equals(str)) {
                    credentials.append(str).append("\n;");
                }
            }
            reader.close();

            // writing data leaving current user's data to db
            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            String[] s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            // removing user's data in ud
            reader = new BufferedReader(new FileReader(ud));
            while ((str = reader.readLine()) != null) {
                if (!username.equals(str.split(",")[0])) {
                    credentials.append(str).append("\n;");
                }
            }
            reader.close();

            // writing data leaving current user's data to ud
            writer = new BufferedWriter(new FileWriter(ud));
            s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            // Deleting profile picture
            File ppDir = new File(dirname + "\\Databases\\ProfilePictures");
            String fileName;
            for(File file : Objects.requireNonNull(ppDir.listFiles())){
                fileName = file.getName();
                if(username.equals(fileName.substring(0, fileName.lastIndexOf(".")))){
                    return new File(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt").delete() &&
                            new File(dirname + "\\Databases\\Enquiries\\" + username + ".txt").delete() && file.delete();
                }
            }

        } catch (Exception ignored) {
        }
        return false;
    }
}
