# EazyFinder Java Version

EazyFinder is an app that shows the users shortest path between places in terms of distance, time, cost ... in
a city

This project contains a graph that has places of a city that are connected by the local bus facility, cabs, bike, auto or metro, as nodes and, if they are connected by that vehicle then an edge will be present b/w them with the distance, time, cost as the edge weight and also each vehicle with different costs. If the user enters the starting point and destination locations, if they are connected, the edge weight will be shown i;e distance, time and cost. So the entire project runs by giving the user the mode of transportation possible from start to destination in the best way possible

## Other Versions
EazyFidner is written in <a href="https://github.com/rohithpala/EazyFinder_CVersion">C</a>, <a href="https://github.com/rohithpala/EazyFinder_JavaVersion">Java</a> & <a href="https://github.com/rohithpala/EazyFinder_PythonVersion">Python</a>

### Java:
<ul>
   <li><a href="https://github.com/rohithpala/EazyFinder_JavaVersion/tree/EazyFinderGUI">GUI Version</a></li>
   <li><a href="https://github.com/rohithpala/EazyFinder_JavaVersion/tree/EazyFinderCLI">CLI Version</a></li>
</ul>

<strong><u>Our Main Features are</u></strong>:
<ol>
   <li>Login and Signup that maintains Privacy</li>
   <li>Password is encrypted using a logic by taking the ASCII values of the characters so that no one can crack it</li>
   <li>A menu for all our services: 1) Bookings 2) Transaction History 3) Update Username 4) Password Change 5) Account Deletion 6) Switch Accounts 7) Logout</li>
   <li>Providing our services in Hyderabad, Bengaluru, Chennai Cities</li>
   <li>Displaying the map of the city selected by the user</li>
   <li>Providing different modes of transport</li>
   <li>Checking Availability of Vehicles based on the booking time and if the time is out of range of our service, we cost them some extra price</li>
   <li>Checking Traffic and providing different modes of transport for a route</li>
   <li>Giving Discounts on the Prices by showing some Coupon Codes based on the cost</li>
   <li>Transaction History of the user is stored in a seperate text file named with the users name in which :source, destination, cost, discount coupon (if any), discount percentage (if any), total cost: are stored in comma seperated format.</li>
</ol>

## Overview of the Project:
The user has to sign up or login. We also maintain privacy for password. In the next step the user must choose an option from bookings, transaction history, password change, and account deletion.

If the user selects the first option, then the user must select a city from Hyderabad Bengaluru and Chennai, these are the places where our services are available. Then a map will be displayed showing all the possible directions from a source location to a destination location with the distance between them. Then the user must enter the starting location or the source location and the destination location. Based on the source location and destination location we have taken 5 cases. The first case is that the source and the destination locations are wrong or there is a spelling mistake in them, or our services are not available at that place.
Case 2 is that the source location is wrong or there is a spelling mistake in them, or our services are not available at that source location. Case 3 is that the destination location is wrong or there is a spelling mistake in them, or our services are not available at the destination location. Case 4 comes into play when the source location and the destination location are the same and Case 5 is the case when the source location and the destination locations are correct are our services are available at those places, then the route from the source location to the destination location is displayed on the screen with the distance for the whole route. This distance is calculated using single source shortest path algorithm or the Dijkstra's algorithm.

The the user is prompted with a message saying if he/she wants to select the mode of transportation based on traffic between these places. the traffic from one place to another is calculated using the rand function by taking a range for no traffic, moderate traffic, heavy traffic. if he or she refuses to select mode of transportation from place to place between select one mode of transportation for the whole journey. After selecting the mode of transportation, a bill is generated that contains the mode of transport the places from which the mode of transport is used and cost that is calculated based on mode of transport and the distance between these places. Then based on the cost coupon codes are displayed to the user based on his or her interest they can apply the purple quotes, or they can go further without using the coupon codes. if the coupon code is applied by the user the cost is discounted based on the discount of that coupon and the final price is displayed. If the coupon code is not used, then the same questions displayed. then the total transaction history of this user is stored in the text file that is named after his username. In this text file, the source location, destination location, the cost, discount coupon (if applied), discount percentage (if applied), and the total cost after applying the discount coupon, the booking date, the booking time are stored.

If he chooses transaction history option, then all the transactions made by the user with our services are displayed.

If he chooses a third option, then we prompt him to give the new password this new password is compared with the old password if they are same then we prompt him with a message saying that new password cannot be the same as the old one. also, these passwords must follow some rules namely besides must be between 8 to 16 characters at least one uppercase letter, one lowercase letter, one digit, a special character must be used. all these are taken care by the isPasswordAccepted() function. The new password is accepted then this password is
 
encrypted and stored in the LoginSignupDatabase.txt file. Then from the next login the user must use the new password.

The user selects the fourth option then we asked for confirmation. If the user confirms to delete his account remove these details from the LoginSignupDatabase.txt file, and also the
.txt file that is named after the user’s name is deleted i;e the transaction history of that user is deleted.
