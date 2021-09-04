package EazyFinder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class ModeOfTransportation {
    String source, destination, mst;
    float cost;

    ModeOfTransportation(String source, String destination, float cost, String mst) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.mst = mst;
    }
}

class Bookings {
    String username, password;
    int i, j, cityChoice;
    String city, source, destination;
    int noOfPlaces;
    String[] places;
    int[][] cityAdjMat;
    int[] metro;
    int noOfVehicles = 5;
    String[] vehicles = {"Bus", "Bike", "Auto", "Metro", "Cab"};
    int[] costPerKM = {2, 3, 4, 5, 6};
    String currentTime;
    Scanner input = new Scanner(System.in);

    Bookings(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void loadDetails(String source, String destination, float cost, String couponName, int couponDiscount, float totalCost) {
        File th = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\TransactionHistories\\" + username + ".txt");
        String date = new SimpleDateFormat("dd:MM:yyyy").format(new Date());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(th, true));
            writer.write("City: " + city.toUpperCase() + " " +
                    "From: " + source.toUpperCase() + " " +
                    "To: " + destination.toUpperCase() + " " +
                    "Actual Cost: " + cost + " " +
                    "Coupon Code: " + couponName + " " +
                    "Coupon Discount: " + couponDiscount + " " +
                    "Total Cost: " + totalCost + " " +
                    "Booking Date: " + date + " " +
                    "Booking Time: " + currentTime +
                    "\n");
            writer.close();
        } catch (Exception ex) {
            System.out.println("Error while Writing to file");
        }
    }

    int discount(float cost, ModeOfTransportation[] mst_array, int l) {
        int couponsAvailable = 0;
        String discountFileString = Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\discountCoupons.txt";
        File discountFile = new File(discountFileString);
        Path discountFilePath = Paths.get(discountFileString);
        try {
            couponsAvailable = (int) Files.lines(discountFilePath).count();
        } catch (Exception ex) {
            System.out.println("File Operation Error");
        }
        String[] couponName = new String[couponsAvailable], couponNDP;
        int[] couponDiscount = new int[couponsAvailable], couponPrice = new int[couponsAvailable];
        String str;
        boolean couponApplicable = false, welcome = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(discountFile));
            for (i = 0; (str = reader.readLine()) != null; i++) {
                couponNDP = str.split(" ");
                couponName[i] = couponNDP[0];
                couponDiscount[i] = Integer.parseInt(couponNDP[1]);
                couponPrice[i] = Integer.parseInt(couponNDP[2]);
                if (cost >= couponPrice[i]) couponApplicable = true;
            }
        } catch (Exception ex) {
            System.out.println("Error while reading a file");
        }

        File th = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\TransactionHistories\\" + username + ".txt");
        if (th.length() == 0) welcome = true;
        if (couponApplicable) {
            System.out.println("Coupons Applicable for you are:");
            if (welcome)
                System.out.printf("Coupon Code: %s\tDiscount: %d%%\ton bookings of Rs.%d or above.\n",
                        couponName[0], couponDiscount[0], couponPrice[0]);
            for (i = 1; i < couponsAvailable; i++) {
                if (cost >= couponPrice[i]) {
                    System.out.printf("Coupon Code: %s\tDiscount: %d%%\ton bookings of Rs.%d or above.\n",
                            couponName[i], couponDiscount[i], couponPrice[i]);
                }
            }

            String coupon, applyCoupon;
            System.out.print("Do you want to apply any coupon [Y/N]? ");
            applyCoupon = input.next();
            if (applyCoupon.equalsIgnoreCase("y")) {
                System.out.print("Enter the Coupon Code: ");
                coupon = input.next();
                if (coupon.equalsIgnoreCase("welcome") && !welcome)
                    return 0;
                for (i = 0; i < couponsAvailable; i++) {
                    if (coupon.equalsIgnoreCase(couponName[i]) && cost >= couponPrice[i]) {
                        System.out.print("Do You Want to Proceed[Y/N]? ");
                        applyCoupon = input.next();
                        if (applyCoupon.equalsIgnoreCase("y")) {
                            System.out.println("Coupon Applied Successfully");
                            float totalCost = cost - ((float) couponDiscount[i] / 100) * cost;
                            System.out.printf("Total Cost After Applying Coupon: %.2f\n", totalCost);

                            loadDetails(mst_array[0].source, mst_array[l - 1].destination, cost, couponName[i], couponDiscount[i], totalCost);

                            return 1;
                        }
                    }
                }
            } else {
                System.out.printf("No Coupon Applied\nTotal Cost: %.2f\n", cost);
                return -1;
            }
        } else {
            System.out.println("No Coupons Applicable");
            return -1;
        }
        return 0;
    }

    void generateBill(ModeOfTransportation[] mst_array, int l) {
        float cost = 0;
        System.out.println("Mode of Transport      From\t\t    To\t\t     Price");
        for (i = 0; i < l; i++) {
            System.out.printf("%-20s %-20s %-20s %.2f\n", mst_array[i].mst, mst_array[i].source, mst_array[i].destination, mst_array[i].cost);
            cost += mst_array[i].cost;
        }
        System.out.println("Total Cost: " + cost);

        int discountSuccess = discount(cost, mst_array, l);
        String tryAgain;
        while (discountSuccess == 0) {
            System.out.println("You might have entered an invalid coupon");
            System.out.print("Do you want to try again[Y/N]? ");
            tryAgain = input.next();
            if (tryAgain.equalsIgnoreCase("y"))
                discountSuccess = discount(cost, mst_array, l);
            else
                break;
        }
        if (discountSuccess == -1) {
            loadDetails(mst_array[0].source, mst_array[l - 1].destination,
                    cost, "-", 0, cost);
        }
    }

    int inputID() {
        int id;
        do {
            id = input.nextInt();
            if (id < 1 || id > 5)
                System.out.print("Invalid ID\nSelect a Mode of Transportation: ");
        } while (id < 1 || id > 5);
        return id;
    }

    void modeOfTransportBasedOnTraffic(int k, String[] route) {
        int id, sourceIndex = -1, destinationIndex = -1;
        int[] extraCost = new int[noOfVehicles], extraCostApplied = new int[noOfVehicles];
        String[] startTime = new String[noOfVehicles - 1], endTime = new String[noOfVehicles - 1], startEndExtra;
        String trafficChoice, str;
        ModeOfTransportation[] mst_array = new ModeOfTransportation[k];
        File availabilityTimesFile = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\CitiesInfo\\availability-times.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(availabilityTimesFile));
            extraCost[0] = 0;
            for (i = 1; (str = reader.readLine()) != null; i++) {
                startEndExtra = str.split(" ");
                startTime[i-1] = startEndExtra[0];
                endTime[i-1] = startEndExtra[1];
                extraCost[i] = Integer.parseInt(startEndExtra[2]);
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex + "\nError While reading a file");
        }

        do {
            System.out.print("Do You Want to Select Mode of Transportation based on Traffic [Y/N]: ");
            trafficChoice = input.next();
            if (!trafficChoice.equalsIgnoreCase("y") && !trafficChoice.equalsIgnoreCase("n")) {
                System.out.println("Please Select a Valid Option");
            }
        } while (!trafficChoice.equalsIgnoreCase("y") && !trafficChoice.equalsIgnoreCase("n"));

        // Display Cost Per Vehicle Table
        System.out.println("\nCost Per Kilometer of Vehicles Available:");
        System.out.println("Id\tVehicle \t Price \t   ExtraCost");
        System.out.printf("%d \t %s \t\t %d \t\t -\n", 1, vehicles[0], costPerKM[0]);
        extraCostApplied[0] = 0;
        // get the current time
        currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        for (i = 1; i < noOfVehicles; i++) {
            System.out.printf("%d \t %s \t\t %d", i + 1, vehicles[i], costPerKM[i]);
            if (currentTime.compareTo(startTime[i - 1]) < 0 || currentTime.compareTo(endTime[i - 1]) > 0) {
                System.out.printf(" \t\t %d\n", extraCost[i]);
                extraCostApplied[i] = 1;
            } else {
                System.out.println(" \t\t -");
                extraCostApplied[i] = 0;
            }
        }

        int l = 0;
        double traffic;
        float cost;
        if (trafficChoice.equalsIgnoreCase("y")) {
            traffic = Math.random();
            for (i = k; i > 0; i--) {
                if (traffic == 0)
                    System.out.println("\nThe route is Clear from " + route[i].toUpperCase() + " to " + route[i - 1].toUpperCase());
                else if (traffic > 0 && traffic <= 0.5)
                    System.out.println("\nThere is Moderate Traffic from " + route[i].toUpperCase() + " to " + route[i - 1].toUpperCase());
                else if (traffic > 0.5)
                    System.out.println("\nThere is Heavy Traffic from " + route[i].toUpperCase() + " to " + route[i - 1].toUpperCase());
                for (j = 0; j < noOfPlaces; j++) {
                    if (places[j].equals(route[i])) sourceIndex = j;
                    else if (places[j].equals(route[i-1])) destinationIndex = j;
                }
                System.out.print("Enter the ID of Mode of Transportation you Prefer: ");
                id = inputID();
                while (id == 4) {
                    if (metro[sourceIndex] == 0 && metro[destinationIndex] == 0) {
                        System.out.println("Metro not Available at " + route[i] + " and " + route[i - 1]);
                    } else if (metro[sourceIndex] == 0) {
                        System.out.println("Metro not Available at " + route[i]);
                    } else if (metro[destinationIndex] == 0) {
                        System.out.println("Metro not Available at " + route[i - 1]);
                    } else {
                        break;
                    }
                    System.out.print("Please Select another mode of transport: ");
                    id = inputID();
                }

                if(extraCostApplied[id-1] == 0)
                    cost = (float)((singleSourceShortestPath(sourceIndex, destinationIndex, 2)) * costPerKM[id - 1]);
                else
                    cost = (float)((singleSourceShortestPath(sourceIndex, destinationIndex, 2)) * (costPerKM[id - 1] + extraCost[id - 1]));
                mst_array[l] = new ModeOfTransportation(route[i], route[i - 1], cost, vehicles[id - 1]);
                l++;
            }
        } else {
            for (j = 0; j < noOfPlaces; j++) {
                if (places[j].equals(route[k])) sourceIndex = j;
                if (places[j].equals(route[0])) destinationIndex = j;
            }
            System.out.print("Select a Mode of Transportation: ");
            id = inputID();
            if(extraCostApplied[id-1] == 0)
                cost = (float)((singleSourceShortestPath(sourceIndex, destinationIndex, 2)) * costPerKM[id - 1]);
            else
                cost = (float)((singleSourceShortestPath(sourceIndex, destinationIndex, 2)) * (costPerKM[id - 1] + extraCost[id - 1]));
            mst_array[l] = new ModeOfTransportation(route[k], route[0], cost, vehicles[id - 1]);
            l = 1;
        }

        System.out.println("\nYour Bill:");
        generateBill(mst_array, l);
    }

    void printRoute(int sourceIndex, int destinationIndex, int[] shortestPath, int[] path, int case_) {
        int i, j, k;
        String[] route = new String[noOfPlaces];
        j = destinationIndex;
        route[0] = places[destinationIndex];
        k = 1;
        while (path[j] != sourceIndex) {
            route[k] = places[path[j]];
            j = path[j];
            k++;
        }
        route[k] = places[sourceIndex];

        // Display the Path
        System.out.print("Route: ");
        for (i = k; i >= 0; i--) {
            System.out.print(route[i]);
            if (i != 0) System.out.print(" -> ");
        }
        System.out.println("\nTotal Distance: " + shortestPath[destinationIndex]);
        if (case_ != 0)
            modeOfTransportBasedOnTraffic(k, route);
    }

    int singleSourceShortestPath(int sourceIndex, int destinationIndex, int case_) {
        int i, j, k, min_dist, min_dist_vertex = sourceIndex, calc;
        int[] shortestPath = new int[noOfPlaces], visited = new int[noOfPlaces], path = new int[noOfPlaces];

        // Initializations
        for (i = 0; i < noOfPlaces; i++) {
            if (cityAdjMat[sourceIndex][i] != 0)
                shortestPath[i] = cityAdjMat[sourceIndex][i];
            else
                shortestPath[i] = Integer.MAX_VALUE;
            visited[i] = 0;
            path[i] = sourceIndex;
        }
        shortestPath[sourceIndex] = 0;

        for (i = 0; i < noOfPlaces; i++) {
            min_dist = Integer.MAX_VALUE;
            for (k = 0; k < noOfPlaces; k++) {
                // Select the vertices that are unvisited and are nearer to sourceVertex
                if (visited[k] == 0 && min_dist >= shortestPath[k]) {
                    min_dist = shortestPath[k];
                    min_dist_vertex = k;
                }
            }

            // min_dist_vertex is at minimum distance to sourceVertex, So put it in solution
            visited[min_dist_vertex] = 1;

            for (j = 0; j < noOfPlaces; j++) {
                calc = shortestPath[min_dist_vertex] + cityAdjMat[min_dist_vertex][j];
                if ((visited[j] == 0) && (cityAdjMat[min_dist_vertex][j] != 0) &&
                        shortestPath[min_dist_vertex] != Integer.MAX_VALUE &&
                        shortestPath[j] > calc) {
                    shortestPath[j] = calc;
                    path[j] = min_dist_vertex;
                }
            }
        }
        // case_ = 1 indicates we are good to go
        if (case_ == 1) {
            printRoute(sourceIndex, destinationIndex, shortestPath, path, case_);
        } else if (case_ == 2) { // When we require the shortest distance from source to destination then case_ = 2
            System.out.println("Shortest Path: " + shortestPath[destinationIndex]);
            return shortestPath[destinationIndex];
        }
        return 0;
    }

    void changeLocation(int case_) {
        String choice;
        do {
            System.out.print("Want to Change the Location(s) (Type 'N' to exit) [Y/N]: ");
            choice = input.next();
            if (!(choice).equalsIgnoreCase("y") && !(choice).equalsIgnoreCase("n")) {
                System.out.println("Please Select a Valid Option");
            }
        } while (!(choice).equalsIgnoreCase("y") && !(choice).equalsIgnoreCase("n"));
        if ((choice).equalsIgnoreCase("y")) {
            if (case_ == 1 || case_ == 4) {
                System.out.print("Enter the Source Location Again: ");
                source = input.next();
                System.out.print("Enter the Destination Again: ");
                destination = input.next();
            } else if (case_ == 2) {
                System.out.print("Enter the Source Location Again: ");
                source = input.next();
            } else if (case_ == 3) {
                System.out.print("Enter the Destination Again: ");
                destination = input.next();
            }
            // Check the location again after re-entering
            locationCheck(); // This calls checkForCase()
        } else {
            System.out.println("Have A Great Day Ahead :)\n");
            System.exit(0);
        }
    }

    void checkForCase(int sourceIndex, int destinationIndex, int case_) {
        if (case_ == 5) {
            singleSourceShortestPath(sourceIndex, destinationIndex, 1);
        } else if (case_ == 1) {
            System.out.println("Sorry! Our Services are not available at " + source + " and " + destination + "\nOr you have entered wrong locations. Please recheck the spellings");
        } else if (case_ == 2) {
            System.out.println("Sorry! Our Services are not available at " + source);
        } else if (case_ == 3) {
            System.out.println("Sorry! We do not serve from " + source + " to " + destination + "\nOr You have entered a wrong Destination");
            System.out.println("\nYou can Reach the following destinations from " + source);
        } else if (case_ == 4) {
            System.out.println("Source and Destination cannot be the same");
        }
        if (case_ != 5) {
            changeLocation(case_);
        }
    }

    void locationCheck() {
        int case_;
        int sourceIndex = -1, destinationIndex = -1;
        for (i = 0; i < noOfPlaces; i++)
            if (places[i].equals(source)) {
                sourceIndex = i;
                break;
            }
        for (i = 0; i < noOfPlaces; i++)
            if (places[i].equals(destination)) {
                destinationIndex = i;
                break;
            }
        if (sourceIndex == -1 && destinationIndex == -1) case_ = 1;
        else if (sourceIndex == -1) case_ = 2;
        else if (destinationIndex == -1) case_ = 3;
        else if (sourceIndex == destinationIndex) case_ = 4;
        else case_ = 5;
        checkForCase(sourceIndex, destinationIndex, case_);
    }

    void locationInput() {
        System.out.print("Enter the Starting Point: ");
        source = input.next().toLowerCase();
        System.out.print("Enter the Destination: ");
        destination = input.next().toLowerCase();
        locationCheck();
    }

    void displayMap() {
        System.out.println("\n From\t\t\t To\t\tDistance\n");
        for (i = 0; i < noOfPlaces; i++) {
            for (j = 0; j < noOfPlaces; j++)
                if (cityAdjMat[i][j] != 0)
                    System.out.printf("%-20s %-20s %-3d\n", places[i], places[j], cityAdjMat[i][j]);
            System.out.println();
        }
    }

    void connectPlaces(int source, int destination, int distance) {
        cityAdjMat[source][destination] = cityAdjMat[destination][source] = distance;
    }

    void formCity() {
        File cityConnectionsFile = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\CitiesInfo\\" +
                city + "-connections.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cityConnectionsFile));
            String str;
            String[] sourceDestinationDistance;
            while ((str = reader.readLine()) != null) {
                sourceDestinationDistance = str.split(" ");
                connectPlaces(Integer.parseInt(sourceDestinationDistance[0]),
                        Integer.parseInt(sourceDestinationDistance[1]),
                        Integer.parseInt(sourceDestinationDistance[2]));
            }
        } catch (Exception ex) {
            System.out.println("Error while Reading a file");
        }
    }

    void bookings() {
        System.out.println("---------------------Welcome to EazyFinder!!!!---------------------");
        System.out.println("Select one of the City IDs:\n1) Hyderabad\n2) Bengaluru\n3) Chennai");
        do {
            cityChoice = input.nextInt();
            switch (cityChoice) {
                case 1 -> city = "hyderabad";
                case 2 -> city = "bengaluru";
                case 3 -> city = "chennai";
                default -> System.out.print("Please Select a valid Option\nSelect one of the City IDs: ");
            }
        } while (cityChoice < 1 || cityChoice > 3);
        String cityPathString = Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\CitiesInfo\\" + city + ".txt";
        Path cityPath = Paths.get(cityPathString);
        File cityFile = new File(cityPathString);
        try {
            noOfPlaces = (int) Files.lines(cityPath).count();
        } catch (Exception ex) {
            System.out.println("Some Error Occurred");
        }
        places = new String[noOfPlaces];
        cityAdjMat = new int[noOfPlaces][noOfPlaces];
        metro = new int[noOfPlaces];
        try {
            String str;
            String[] placesMetro;
            BufferedReader reader = new BufferedReader(new FileReader(cityFile));
            for (i = 0; (str = reader.readLine()) != null; i++) {
                placesMetro = str.split(" ");
                places[i] = placesMetro[0];
                metro[i] = Integer.parseInt(placesMetro[1]);
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("Error while Reading a File");
        }
        // Initializing the adj_mat
        for (i = 0; i < noOfPlaces; i++)
            for (j = 0; j < noOfPlaces; j++)
                cityAdjMat[i][j] = 0;

        formCity();

        System.out.println("Map:");
        displayMap();

        locationInput();
    }
}

class Menu {
    String username, password;
    Scanner input = new Scanner(System.in);

    Menu(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void bookings() {
        new Bookings(username, password).bookings();
    }

    void transactionHistory() {
        File th = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\TransactionHistories\\" + username + ".txt");
        if (th.length() == 0) {
            System.out.println("You Have no Transactions Yet");
        } else {
            System.out.println("Your Transactions:");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(th));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.print(line + "\n");
                }
                reader.close();
            } catch (Exception ex) {
                System.out.println("Error in Reading File");
            }
        }
    }

    void accountDeletion(char case_) {
        File th = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\TransactionHistories\\" + username + ".txt");
        File db = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\LogInSignUpDatabase.txt");
        File temp = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\LogInSignUpDatabase.txt");
        String str;
        try {
            BufferedReader dbReader = new BufferedReader(new FileReader(db));
            BufferedWriter tempWriter = new BufferedWriter(new FileWriter(temp));
            while ((str = dbReader.readLine()) != null) {
                if (!str.equals(username + " " + password))
                    tempWriter.write(str + "\n");
            }
            dbReader.close();
            tempWriter.close();

            // Delete LogInSignUpDatabase.txt and rename LogInSignUpDatabase.txt as LogInSignUpDatabase.txt
            if (db.delete() && temp.renameTo(db)) {
                System.out.println();
            }
            if (case_ != 'P' && th.delete()) {
                System.out.println("Account Deleted Successfully\nWe are Sorry to see you go");
                System.exit(0);
            }
        } catch (Exception ex) {
            System.out.println("Error in Reading a File");
        }
    }

    String passwordChange() {
        String newPassword;
        do {
            System.out.print("Enter New Password: ");
            newPassword = input.next();
            if (newPassword.equals(password)) {
                System.out.println("Password cannot be the same as Previous one");
            } else {
                accountDeletion('P');
                System.out.println("Password Changed Successfully");
                File db = new File(Paths.get("").toAbsolutePath() + "\\src\\EazyFinder\\LogInSignUpDatabase.txt");
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(db, true));
                    writer.write(username + " " + newPassword + "\n");
                    writer.close();
                } catch (Exception ex) {
                    System.out.println("Error in reading a file");
                }
            }
        } while (newPassword.equals(password));
        return newPassword;
    }
}

public class EazyFinder {
    String username, password;

    EazyFinder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void EazyFinderCode() {
        String choice;
        int menuChoice;
        Menu menu;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("\nSelect an Option:");
            System.out.println("1) Bookings\n2) Transaction History\n3) Password Change\n4) Account Deletion\n5) Logout");
            menuChoice = input.nextInt();
            menu = new Menu(username, password);
            switch (menuChoice) {
                case 1 -> menu.bookings();
                case 2 -> menu.transactionHistory();
                case 3 -> password = menu.passwordChange();
                case 4 -> {
                    System.out.println("Are You Sure [Y/N]? ");
                    choice = input.next();
                    if(choice.equalsIgnoreCase("y"))
                        menu.accountDeletion('A');
                    else
                        System.out.println("Don't Worry Your Account is Safe");
                }
                case 5 -> {
                    System.out.println("Logged out Successfully");
                    System.exit(0);
                }
                default -> System.out.println("Please select a Valid Option");
            }
            System.out.print("Want to Select between Options Again [Y/N]? ");
            choice = input.next();
        } while (choice.equalsIgnoreCase("y"));
    }
}
