package EazyFinderGUI.MainCodes;

import java.io.*;

public class BookingAndEnquireMainCode {
    String city;
    public String source, destination;
    short noOfPlaces, noOfAdults, noOfChildren;
    public short noOfVehicles = 5;
    String[] places, tempArray;
    short[] metro;
    short[][] cityAdjMat;
    public String[] vehicles = {"Bus", "Bike", "Auto", "Metro", "Cab"};
    public short[] costPerKM = {2, 3, 4, 5, 6};
    short[] adultCost = {20, 30, 15, 15, 35};
    short[] childCost = {10, 15, 7, 7, 15};
    short i, j, k;

    String str, dirname = System.getProperty("user.dir") + "\\EazyFinderGUI";
    BufferedReader cityReader, cityConnectionsReader;

    public BookingAndEnquireMainCode() {
    }

    short motIndex;

    public BookingAndEnquireMainCode(short motIndex, short noOfAdults, short noOfChildren) {
        this.motIndex = motIndex;
        this.noOfAdults = noOfAdults;
        this.noOfChildren = noOfChildren;
    }

    public BookingAndEnquireMainCode(String city, String source, String destination) {
        this.city = city;
        this.source = source.toLowerCase();
        this.destination = destination.toLowerCase();
    }

    public BookingAndEnquireMainCode(String city, String source, String destination, short noOfAdults, short noOfChildren) {
        this.city = city;
        this.source = source.toLowerCase();
        this.destination = destination.toLowerCase();
        this.noOfAdults = noOfAdults;
        this.noOfChildren = noOfChildren;
    }

    public void loadDetails(String username, String city, String source, String destination, float cost, String name, String phone, String email,
                            short noOfAdults, short noOfChildren, String date, String time) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dirname + "\\Databases\\TransactionHistories\\" + username + ".txt", true));
            writer.write("Name: " + name + "," +
                    "Phone Number: " + phone + "," +
                    "Email ID: " + email + "," +
                    "City: " + city.toUpperCase() + "," +
                    "Source: " + source.toUpperCase() + "," +
                    "Destination: " + destination.toUpperCase() + "," +
                    "Mode Of Transportation: " + "," +
                    "Adults: " + noOfAdults + "," +
                    "Children: " + noOfChildren + "," +
                    "Total Cost: " + cost + "," +
                    "Booking Date: " + date + "," +
                    "Booking Time: " + time +
                    "\n");
            writer.flush();
            writer.close();
        } catch (Exception ignored) {
        }
    }

    public float calculateTotalCost() {
        return ((noOfAdults * adultCost[motIndex]) + (noOfChildren * childCost[motIndex])) * costPerKM[motIndex];
    }

    public String[] route;
    public short routeLen;
    public float cost;

    public class BookingAndEnquire {
        void getRoute(short sourceIndex, short destinationIndex, short[] shortestPath, short[] path) {
            route = new String[noOfPlaces];
            j = destinationIndex;
            route[0] = places[destinationIndex];
            k = 1;
            while (path[j] != sourceIndex) {
                route[k] = places[path[j]];
                j = path[j];
                k++;
            }
            route[k] = places[sourceIndex];
            routeLen = k;
            cost = shortestPath[destinationIndex];
        }

        void singleSourceShortestPath(short sourceIndex, short destinationIndex) {
            short min_dist, min_dist_vertex = sourceIndex, calc;
            short[] shortestPath = new short[noOfPlaces], visited = new short[noOfPlaces], path = new short[noOfPlaces];

            // Initializations
            for (i = 0; i < noOfPlaces; i++) {
                if (cityAdjMat[sourceIndex][i] != 0)
                    shortestPath[i] = cityAdjMat[sourceIndex][i];
                else
                    shortestPath[i] = Short.MAX_VALUE;
                visited[i] = 0;
                path[i] = sourceIndex;
            }
            shortestPath[sourceIndex] = 0;

            for (i = 0; i < noOfPlaces; i++) {
                min_dist = Short.MAX_VALUE;
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
                    calc = (short) (shortestPath[min_dist_vertex] + cityAdjMat[min_dist_vertex][j]);
                    if ((visited[j] == 0) && (cityAdjMat[min_dist_vertex][j] != 0) &&
                            shortestPath[min_dist_vertex] != Short.MAX_VALUE &&
                            shortestPath[j] > calc) {
                        shortestPath[j] = calc;
                        path[j] = min_dist_vertex;
                    }
                }
            }

            getRoute(sourceIndex, destinationIndex, shortestPath, path);
        }

        void connectPlaces(short source, short destination, short distance) {
            cityAdjMat[source][destination] = cityAdjMat[destination][source] = distance;
        }

        void formCity() {
            try {
                File cityConnectionsFile = new File(dirname + "\\CitiesInfo\\" + city + "-connections.txt");
                cityConnectionsReader = new BufferedReader(new FileReader(cityConnectionsFile));
                while ((str = cityConnectionsReader.readLine()) != null) {
                    tempArray = str.split(" ");
                    connectPlaces(Short.parseShort(tempArray[0]), Short.parseShort(tempArray[1]), Short.parseShort(tempArray[2]));
                }
            } catch (Exception ignored) {
            }
        }

        void getPlaces() {
            File cityFile = new File(dirname + "\\CitiesInfo\\" + city + ".txt");
            try {
                noOfPlaces = 0;
                cityReader = new BufferedReader(new FileReader(cityFile));
                while (cityReader.readLine() != null) noOfPlaces++;
                cityReader.close();

                places = new String[noOfPlaces];
                metro = new short[noOfPlaces];

                cityReader = new BufferedReader(new FileReader(cityFile));
                for (i = 0; (str = cityReader.readLine()) != null; i++) {
                    tempArray = str.split(" ");
                    places[i] = tempArray[0];
                    metro[i] = Short.parseShort(tempArray[1]);
                }
                cityReader.close();
            } catch (Exception ignored) {
            }
        }

        public void bookings() {
            getPlaces();

            cityAdjMat = new short[noOfPlaces][noOfPlaces];
            for (i = 0; i < noOfPlaces; i++)
                for (j = 0; j < noOfPlaces; j++)
                    cityAdjMat[i][j] = 0;

            formCity();

            short sourceIndex = -1, destinationIndex = -1;
            for (i = 0; i < noOfPlaces; i++) {
                if (places[i].equals(source)) sourceIndex = i;
                else if (places[i].equals(destination)) destinationIndex = i;
                if (sourceIndex != -1 && destinationIndex != -1) break;
            }

            singleSourceShortestPath(sourceIndex, destinationIndex);
        }
    }
}
