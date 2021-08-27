package EazyFinderGUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class FlushWholeData {
    public static void main(String[] args) throws IOException {
        // Please check "Configurations" before running the program
        String dirname = System.getProperty("user.dir"); // Path till EazyFinderGUI

        // Directories
        File enq = new File(dirname + "\\Enquiries");
        File th = new File(dirname + "\\TransactionHistories");
        File pp = new File(dirname + "\\ProfilePictures");

        // Files
        File db = new File(dirname + "\\Databases\\LogInSignUpDatabase.txt");
        File ud = new File(dirname + "\\Databases\\UserDetails.txt");

        File[] directories = {enq, th, pp};

        // Removing the files in the directories
        for(File dir : directories) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                file.delete();
            }
        }

        // Clearing databases
        new FileWriter(db, false).close();
        new FileWriter(ud, false).close();

        System.out.println("All Data Deleted Successfully");
    }
}
