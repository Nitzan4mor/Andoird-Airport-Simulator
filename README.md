# 23_09-Final-Project-Airport-Nitzan-Mor

Nitzan Mor final Android project app - Airport.

the app as the following features -


1. the app is connected to a database in FireBase with the following URL -  
  https://console.firebase.google.com/project/airport-project-nitzan-mor/database/airport-project-nitzan-mor/data/airport
  the database fields are - 
  airlineLogo , departureAirport , departureCity , expectedLandingTime , finalLandingTime , flightNumber , flightStatus
  the airlines i used are - DL , TG ,IB , AZ
  
  
 2. the app display the flights data from the database using Listview and Adapter which is customise to show the relevant fields
  of each specific data, for example:
  if a flight is not landed yet, the flight status color will be different according to the flight status
  and the flight arrival time won't be showed.


3. the app is listening to any changes occuring in the data base and will change the display accordingly


4. the app has a setting menu that allows you to chose the following preferences -
  - show only flights that have landed
  - show only flights that have not landed yet
  - show only fligts that have landed using a time filter
