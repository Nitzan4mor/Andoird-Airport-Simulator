package com.example.airport_project_nitzan_mor;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class FlightSearchActivity extends AppCompatActivity {

    //declare on the fields that we going to use in the FlightSearchActivity
    private ListView FlightSearchActivity_searchFlights_LV;
    private FlightsAdapter adapter;
    private ArrayList<Flight> flights = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String searchChosenCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setting our flight adapter and connecting it to the list view
        setAdapter();

        // communicating with the FireBase database, more explanation on how it works in the method
        getCitiesFromFireBase();

        // when item is clicked we pull the item's (Flight Object) departure city field
        // and assign it to our searchChosenCity String
        // make sure the String in not null and call the method that will display only the
        // flights that departed from that specific city from the data base
        FlightSearchActivity_searchFlights_LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchChosenCity = flights.get(i).getDepartureCity();
                if (searchChosenCity != null) {
                    setAdapterToChosenCityFlights();
                }
                Toast.makeText(FlightSearchActivity.this,
                        "display flights from: " + searchChosenCity,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // return to main activity button, when clicked will finish the intent
        FloatingActionButton return_Btn = findViewById(R.id.FlightSearchActivity_return_Btn);
        return_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchChosenCity = null;
                finish();
            }
        });
    }

    // giving values to our list view and adapter and connecting them
    private void setAdapter() {
        adapter = new FlightsAdapter(this, R.layout.flights_list_view_layout, flights);
        FlightSearchActivity_searchFlights_LV = findViewById(R.id.FlightSearchActivity_searchFlights_LV);
        FlightSearchActivity_searchFlights_LV.setAdapter(adapter);
    }

    // we clear the adapter so it won't show duplicated data
    // create HashSet of String which will enable us to store each departure city only once
    // pull the data from the FireBase database, iterate over the data
    // and add it to the HashSet, after we will iterate again but this time
    // over the HashSet and add the data from the HashSet to the adapter
    // so only the departure city will be shown inside list view
    // that was we can use the same FlightAdapter class we made and the same Layout
    private void getCitiesFromFireBase() {
        database.getReference().child(getString(R.string.TABLE_NAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                HashSet<String> cityFilter = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);
                    cityFilter.add(flight.getDepartureCity());
                }
                for (String city : cityFilter) {
                    adapter.add(new Flight(city));
                }
//                Toast.makeText(FlightSearchActivity.this,
//                        "click on the city to display flights", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FlightSearchActivity.this,
                        "FireBase error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // we clear the adapter so it won't show duplicated data
    // pull the data from the FireBase database, iterate over the data
    // and add it to the adapter ONLY IF the data (Flight Object) as the same
    // departure city as the departure city of the item that was clicked on
    private void setAdapterToChosenCityFlights() {
        database.getReference().child(getString(R.string.TABLE_NAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);
                    if (flight.getDepartureCity().equals(searchChosenCity)) {
                        adapter.add(flight);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FlightSearchActivity.this,
                        "FireBase error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
