package EazyFinderGUI.MainCodes;

import java.io.*;
import java.util.Objects;

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

    public boolean updateUsername(String username, String newUsername, String password) {
        String str, dirname = System.getProperty("user.dir");
        StringBuilder credentials = new StringBuilder();
        File db = new File(dirname + "\\EazyFinderGUI\\Databases\\LogInSignUpDatabase.txt");
        File ud = new File(dirname + "\\EazyFinderGUI\\Databases\\UserDetails.txt");
        File th = new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + username + ".txt");
        File enq = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + username + ".txt");
        File newTH = new File(dirname + "\\EazyFinderGUI\\TransactionHistories\\" + newUsername + ".txt");
        File newENQ = new File(dirname + "\\EazyFinderGUI\\Enquiries\\" + newUsername + ".txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                if (!(username + "," + encryptPassword(password)).equals(str)) {
                    credentials.append(str).append("\n;");
                } else {
                    credentials.append(newUsername).append(",").append(encryptPassword(password)).append("\n;");
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            String[] s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            String[] details;
            reader = new BufferedReader(new FileReader(ud));
            credentials = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                details = str.split(",");
                if (!username.equals(details[0])) {
                    credentials.append(str).append("\n;");
                } else {
                    credentials.append(newUsername).append(",").
                            append(details[1]).append(",").
                            append(details[2]).append(",").
                            append(details[3]).
                            append("\n;");
                }
            }
            reader.close();

            writer = new BufferedWriter(new FileWriter(ud));
            s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            // Updating Profile Picture Name
            File ppDir = new File(dirname + "\\EazyFinderGUI\\ProfilePictures");
            String fileName, temp;
            boolean ppUpdated;
            int i;
            for(File file : Objects.requireNonNull(ppDir.listFiles())){
                fileName = file.getName();
                i = fileName.lastIndexOf(".");
                temp = fileName.substring(0, i);
                if(username.equals(temp)){
                    ppUpdated = file.renameTo(new File(dirname + "\\EazyFinderGUI\\ProfilePictures\\" + newUsername + fileName.substring(i)));
                    return th.renameTo(newTH) && enq.renameTo(newENQ) && ppUpdated;
                }
            }

        } catch (Exception ignored) {
        }
        return false;
    }
}
