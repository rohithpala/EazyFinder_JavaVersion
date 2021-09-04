package EazyFinder;

import java.util.Scanner;
import java.nio.file.Paths;
import java.io.*;

class LogInSignUpClass {
    Scanner input = new Scanner(System.in);
    File db = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\LogInSignUpDatabase.txt");
    String username, password, str;
    String[] credentials;
    boolean found = false;

    boolean logIn() {
        System.out.print("Username: ");
        username = input.next();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(db));
            while ((str = reader.readLine()) != null) {
                credentials = str.split(" ");
                if (username.equals(credentials[0])) {
                    found = true;
                    break;
                }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex + "\nError in Reading File");
        }
        if (!found) {
            System.out.println("No User With Given Username");
        } else {
            System.out.print("Password: ");
            password = input.next();
            if (!password.equals(credentials[1])) {
                System.out.println("No User With Given Credentials");
                found = false;
            }
        }
        return found;
    }

    boolean signUp() {
        boolean found = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(db));
            do {
                System.out.print("Username: ");
                username = input.next();
                while ((str = reader.readLine()) != null) {
                    credentials = str.split(" ");
                    if (username.equals(credentials[0])) {
                        System.out.println("Username already taken, Try with Another one");
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
                reader.close();
            } while (found); // Only comes out when the username is not used by anyone

            System.out.print("Set Password: ");
            password = input.next();

            BufferedWriter writer = new BufferedWriter(new FileWriter(db, true));
            writer.write(username + " " + password + "\n");
            writer.close();
            File userTHFile = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\TransactionHistories\\" + username + ".txt");
            if (userTHFile.createNewFile())
                System.out.println("Account Created Successfully");
        } catch (Exception ex) {
            System.out.println("Error in Reading File");
        }
        return true;
    }

    boolean callLogInSignUp() {
        System.out.print("SignUp or Login: ");
        String SLChoice = input.next();
        if (SLChoice.equalsIgnoreCase("signup")) {
            return signUp();
        } else if (SLChoice.equalsIgnoreCase("login")) {
            return logIn();
        } else {
            System.out.println("It's not a valid choice. Please type again");
            callLogInSignUp();
        }
        return false;
    }
}

public class SignUpLogIn {
    public static void main(String[] args) {
        char choice;
        Scanner input = new Scanner(System.in);
        LogInSignUpClass slObj = new LogInSignUpClass();
        do {
            if (slObj.callLogInSignUp()) {
                new EazyFinder(slObj.username, slObj.password).EazyFinderCode();
                choice = 'N';
            } else { // If login signup fails
                System.out.print("Want to Try Again [Y/N]? ");
                choice = input.next().charAt(0); // If 'Y' the user will get a chance to again signup/login
            }
            if (choice == 'N' || choice == 'n')
                System.out.println("Have A Great Day Ahead! :)");
        } while (choice == 'Y' || choice == 'y');
    }
}
