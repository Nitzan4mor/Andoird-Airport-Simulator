package com.example.airport_project_nitzan_mor;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //declare on the fields that we going to use in the MainActivity
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FlightsAdapter adapter;
    private ArrayList<Flight> flights = new ArrayList<>();
    private Toolbar toolbar;
    private ListView MainActivity_flights_LV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign id to the toolbar and setting it up so we will have access to the settings
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // registering new broadcast receiver that listen for each time the flight mode is changed
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");

        getBaseContext().registerReceiver(new FlightModeReceiver(this), intentFilter);

        // setting our flight adapter and connecting it to the list view
        setAdapter();

        // communicating with the FireBase database, more explanation on how it works in the method
        getFlightsAndShowByPreferences();

        // search button by city of departure
        // when clicked will start new Intent to the FlightSearchActivity
        FloatingActionButton MainActivity_search_Btn = findViewById(R.id.MainActivity_search_Btn);
        MainActivity_search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FlightSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    // when we return to the MainActivity we will again need to communicate
    // with the FireBase database, more explanation on how it works in the method
    @Override
    protected void onResume() {
        super.onResume();
        getFlightsAndShowByPreferences();
    }

    // creating value event listener for each change in the FireBase database
    // each time the listener is activated, we will clear the adapter so it won't show duplicated data
    // we iterate over the data received from the data base, get the setting preferences
    // check the condition values and customise the data displayed according
    // to the settings chosen using if and else if conditions
    // and adding only the relevant data to our flight adapter
    private void getFlightsAndShowByPreferences() {
        database.getReference().child(getString(R.string.TABLE_NAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);

                    //creating connection to our setting preferences
                    SharedPreferences flightPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                    //pulling from setting preferences - the display landed flights by time filter
                    // checking if it's different than the default value of 0
                    // if it's different than 0 and the current Flight Object from the data base
                    // has status of "landing" then we use Calender to receive the current time
                    // in hours and in minutes and convert it to total time in minutes (hours * 60 + minutes)
                    // pull the current Flight final landing time and use String.split in order to get
                    // the hour and minutes separated and put them into int variables
                    // convert it to total time in minutes (hours * 60 + minutes)
                    // and check if the landing time is smaller than the current time
                    // and bigger than the current time - time factor
                    int landedFlightsFilter = 0;
                    try {
                        landedFlightsFilter = Integer.parseInt(flightPreferences.
                                getString(getString(R.string.PREFERENCES_LATEST_FLIGHTS_KEY), "0"));
                    }catch (Exception e){
                        Log.e("MainActivity" , e.getMessage());
                    }
                    if (landedFlightsFilter != 0) {
                        if (!flight.getFlightStatus().equals(getString(R.string.FLIGHT_STATUS_LANDED))) {
                            continue;
                        }
                        Calendar CurrentTime = Calendar.getInstance();
                        int currentHour = CurrentTime.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = CurrentTime.get(Calendar.MINUTE);
                        int currentTimeInMinutes = (currentHour * 60 + currentMinute);
                        int currentTimeMinusFilter = currentTimeInMinutes - (landedFlightsFilter * 60);
                        String landingTime = flight.getFinalLandingTime();
                        String[] hourAndMinute = landingTime.split(":");
                        int landingHour = Integer.valueOf(hourAndMinute[0]);
                        int landingMinute = Integer.valueOf(hourAndMinute[1]);
                        int totalLandingTime = landingHour * 60 + landingMinute;
                        if (totalLandingTime > currentTimeMinusFilter && totalLandingTime < currentTimeInMinutes) {
                            adapter.add(flight);
                        }
                        continue;
                    }

                    //pulling from setting preferences - 2 booleans which represents the display settings chosen
                    // check 4 possible scenarios - (false, false) , (true, true) , (true ,false) , (false, true)
                    // and add only the Flight Objects that has the same fields as the display preferences
                    boolean showOnlyLandedFlights = flightPreferences.getBoolean(
                            getString(R.string.PREFERENCES_LANDED_FLIGHTS_KEY), false);
                    boolean showOnlyNotLandedFlights = flightPreferences.getBoolean(
                            getString(R.string.PREFERENCES_FLYING_FLIGHTS_KEY), false);
                    if ((!showOnlyLandedFlights && !showOnlyNotLandedFlights) || (showOnlyLandedFlights && showOnlyNotLandedFlights)) {
                        adapter.add(flight);
                    } else if (showOnlyLandedFlights) {
                        if (flight.getFlightStatus().equals(getString(R.string.FLIGHT_STATUS_LANDED))) {
                            adapter.add(flight);
                        }
                        // the else if() condition below can be replaced with else{}
                        // because if we get to this stage it will always be true
                        // but in my opinion it will more organised and
                        // easier to understand that way
                    } else if (showOnlyNotLandedFlights) {
                        if (!flight.getFlightStatus().equals(getString(R.string.FLIGHT_STATUS_LANDED))) {
                            adapter.add(flight);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Firebase error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // giving values to our list view and adapter and connecting them
    private void setAdapter() {
        adapter = new FlightsAdapter(this, R.layout.flights_list_view_layout, flights);
        MainActivity_flights_LV = findViewById(R.id.MainActivity_flights_LV);
        MainActivity_flights_LV.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // creating Intent that will start our settings fragment when clicked on settings
        if (id == R.id.action_settings) {
            Intent goToPreferencesIntent = new Intent(this, FlightPreferencesActivity.class);
            startActivity(goToPreferencesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ListView getFlights_LV() {
        return MainActivity_flights_LV;
    }

}
