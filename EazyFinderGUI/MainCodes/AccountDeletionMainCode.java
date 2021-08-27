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

    public boolean accountDeletion(String username, String password) {
        String str, dirname = System.getProperty("user.dir");
        StringBuilder credentials = new StringBuilder();
        File db = new File(dirname + "\\EazyFinderGUI\\Databases\\LogInSignUpDatabase.txt");
        File ud = new File(dirname + "\\EazyFinderGUI\\Databases\\UserDetails.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                if (!(username + " " + encryptPassword(password)).equals(str)) {
                    credentials.append(str).append("\n,");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            String[] s = String.valueOf(credentials).split(",");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            reader = new BufferedReader(new FileReader(ud));
            while ((str = reader.readLine()) != null) {
                if (!username.equals(str.split(",")[0])) {
                    credentials.append(str).append("\n;");
                }
            }
            reader.close();

            writer = new BufferedWriter(new FileWriter(ud));
            s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            // Deleting profile picture
            File ppDir = new File(dirname + "\\EazyFinderGUI\\ProfilePictures");
            String fileName, temp;
            boolean ppDeleted;
            int i;
            for(File file : Objects.requireNonNull(ppDir.listFiles())){
                fileName = file.getName();
                i = fileName.lastIndexOf(".");
                temp = fileName.substring(0, i);
                if(username.equals(temp)){
                    ppDeleted = file.delete();
                    return new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + username + ".txt").delete() &&
                            new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + username + ".txt").delete() && ppDeleted;
                }
            }

        } catch (Exception ignored) {
        }
        return false;
    }
}
