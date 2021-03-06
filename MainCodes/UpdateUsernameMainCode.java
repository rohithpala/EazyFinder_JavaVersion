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

    public boolean updateUsername(String username, String newUsername, String password, long refID) {
        String str, dirname = System.getProperty("user.dir") + "\\EazyFinderGUI";
        StringBuilder credentials = new StringBuilder();
        File db = new File(dirname + "\\Databases\\LogInSignUpDatabase.txt");
        File ud = new File(dirname + "\\Databases\\UserDetails.txt");
        File th = new File(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt");
        File newTH = new File(dirname + "\\Databases\\TransactionHistories\\" + newUsername + ".txt");
        File enq = new File(dirname + "\\Databases\\Enquiries\\" + username + ".txt");
        File newENQ = new File(dirname + "\\Databases\\Enquiries\\" + newUsername + ".txt");

        try {
            // reading db and loading correct details into "credentials"
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                if (!(username + "," + encryptPassword(password) + "," + refID).equals(str)) {
                    credentials.append(str).append("\n;");
                } else {
                    credentials.append(newUsername).append(",").append(encryptPassword(password)).append(",").append(refID).append("\n;");
                }
            }
            reader.close();

            // writing details into db
            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            String[] s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            // reading ud and loading correct details into "credentials"
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

            // writing details into ud
            writer = new BufferedWriter(new FileWriter(ud));
            s = String.valueOf(credentials).split(";");
            for (String s1 : s)
                writer.write(s1);
            writer.flush();
            writer.close();

            // Updating Profile Picture Name
            File ppDir = new File(dirname + "\\Databases\\ProfilePictures");
            String fileName;
            int i;
            for(File file : Objects.requireNonNull(ppDir.listFiles())){
                fileName = file.getName();
                i = fileName.lastIndexOf(".");
                if(username.equals(fileName.substring(0, i))){
                    // renaming Transaction History, Enquiry and Profile Picture files
                    return th.renameTo(newTH) && enq.renameTo(newENQ) &&
                            file.renameTo(new File(dirname + "\\Databases\\ProfilePictures\\" + newUsername + fileName.substring(i)));
                }
            }

        } catch (Exception ignored) {
        }
        return false;
    }
}
