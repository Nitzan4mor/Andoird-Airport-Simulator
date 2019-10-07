# 23_09-Final-Project-Airport-Nitzan-Mor

Nitzan Mor final Android project app - Airport.

the app as the following features -


1. the app is connected to a database in FireBase.
  - the database URL is -  

  https://console.firebase.google.com/project/airport-project-nitzan-mor/database/airport-project-nitzan-mor/data/airport
  
  - the database fields are - 
  airlineLogo , departureAirport , departureCity , expectedLandingTime , finalLandingTime , flightNumber , flightStatus
  - the airlines i used are - DL , TG ,IB , AZ
  
  
 2. the app display the flights data from the database using Listview and Adapter which is customise to show the relevant fields
  of each specific data, for example:
  if a flight is not landed yet, the flight status color will be different according to the flight status
  and the flight arrival time won't be showed.


3. the app is listening to any changes occuring in the data base and will change the display accordingly


4. the app has a setting menu that allows you to chose the following preferences -
  - show only flights that have landed
  - show only flights that have not landed yet
  - show only fligts that have landed using a time filter
  the app will still listen to changes in the database and will change the display accordingly to both the change that was made and the settings that was chosen 
  - work offline using backup data in SQLite when the airplane mode is on
  
 5. the app have a Receiver that is connected to the Device's airplane mode and will "freeze" the screen if the airplane mode is on
 
 6. the app have a search button with the following features - 
  - display all of the current database flight's departure cities using a Listview and Adapter
  - the items display will be changed if a any change to a departure city will occur in the data base
  - when a city is clicked, it will display all of the flights from the chosen city
  - if a flight from the chosen city is added or removed in the database, it will be shown accordingly in the app
  - a return button that allows you to go back to the Main Activity
  
  7. the app have a backup service using SQLite with the followin features - 
  - each time the app is created and each time data has been changed in the fire base, the service will backup the data in a SQLite data base according to the display preferences selected in the settings.
  - if the work offline preference is on and the airplane mode is on, the app will display the data that is saved in the SQLite data base (according to the display preferences that was chosen) until the air plane mode is off.
  
  ** all of the Java code have notes with explanations **
