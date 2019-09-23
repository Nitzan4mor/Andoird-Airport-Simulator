package com.example.airport_project_nitzan_mor;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //declare on the fields that we going to use in the MainActivity
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FlightsAdapter adapter;
    private ArrayList<Flight> flights = new ArrayList<>();
    private Toolbar toolbar;
    public ListView flights_LV;


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

        // setting our flight adapter
        setAdapter();

        // communicating with the FireBase database, more explanation on how it works in the method
        getFlightsAndShowByPreferences();

        // NOT IN USE - YET
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    // each time the listener is activated, we will clear the adapter so won't show duplicated data
    // we iterate over the data received from the data base, get the setting preferences
    // check the condition values and customise the data we will add to our flight adapter
    // using if and else if conditions
    private void getFlightsAndShowByPreferences() {
        database.getReference().child(getString(R.string.TABLE_NAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);

                    SharedPreferences flightPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    boolean showOnlyLandedFlights = flightPreferences.getBoolean(
                            getString(R.string.PREFERENCES_LANDED_FLIGHTS_KEY), false);
                    boolean showOnlyDelayedFlights = flightPreferences.getBoolean(
                            getString(R.string.PREFERENCES_DELAYED_FLIGHTS_KEY), false);
                    if (!showOnlyLandedFlights && !showOnlyDelayedFlights) {
                        adapter.add(flight);
                    } else if (showOnlyLandedFlights && showOnlyDelayedFlights) {
                        if (!flight.getFlightStatus().equals(getString(R.string.FLIGHT_STATUS_INFLIGHT))) {
                            adapter.add(flight);
                        }
                    } else if (showOnlyLandedFlights) {
                        if (flight.getFlightStatus().equals(getString(R.string.FLIGHT_STATUS_LANDED))) {
                            adapter.add(flight);
                        }

                    } else if (showOnlyDelayedFlights) {
                        if (flight.getFlightStatus().equals(getString(R.string.FLIGHT_STATUS_DELAYES))) {
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
        flights_LV = findViewById(R.id.flights_LV);
        flights_LV.setAdapter(adapter);
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

}
