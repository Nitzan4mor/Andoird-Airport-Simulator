package com.example.airport_project_nitzan_mor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

// our broadcast receiver class that change the application display
// in our case, each time the flight mode is changed, we will check if it's on or off
// and change the app display accordingly
public class FlightModeReceiver extends BroadcastReceiver {

    private MainActivity _mainActivity;
    private DatabaseHelper databaseHelper;

    public FlightModeReceiver(MainActivity mainActivity) {
        _mainActivity = mainActivity;
        databaseHelper = new DatabaseHelper(_mainActivity);
    }

    public FlightModeReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
        if (isAirplaneModeOn) {
            // handle Airplane Mode on considering 2 possible cases -
            // 1. if the work offline pref is true we will clear the adapter and get all the
            // data from the sql lite data base, turn the data into Flight Objects
            // and add it to the adapter
            // 2. if the work offline pref is false we will change the
            // display settings of the list view in the MainActivity
            // and will set the list view enabled to be false
            SharedPreferences flightPreferences = PreferenceManager.getDefaultSharedPreferences(_mainActivity);
            boolean workOffline = flightPreferences.getBoolean(_mainActivity.
                    getString(R.string.PREFERENCES_WORK_OFFLINE_KEY), false);
            if (workOffline) {
                Toast.makeText(context, "Airplane mode on - work offline enabled", Toast.LENGTH_SHORT).show();
                MainActivity.adapter.clear();
                Cursor cursor = databaseHelper.getAllFlight();
                if (cursor.getCount() == 0) {
                    Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
                    return;
                }
                while (cursor.moveToNext()) {
                    Flight sqlFlight = new Flight();
                    sqlFlight.setAirlineLogo(cursor.getString(1));
                    sqlFlight.setDepartureAirport(cursor.getString(2));
                    sqlFlight.setDepartureCity(cursor.getString(3));
                    sqlFlight.setExpectedLandingTime(cursor.getString(4));
                    sqlFlight.setFinalLandingTime(cursor.getString(5));
                    sqlFlight.setFlightNumber(cursor.getString(6));
                    sqlFlight.setFlightStatus(cursor.getString(7));
                    MainActivity.adapter.add(sqlFlight);
                }
            } else {
                Toast.makeText(context, "Airplane mode on - work offline disabled", Toast.LENGTH_SHORT).show();
                _mainActivity.getFlights_LV().setAlpha(0.75f);
                _mainActivity.getFlights_LV().setBackgroundColor(Color.GRAY);
                _mainActivity.getFlights_LV().setEnabled(false);
            }
        } else {
            // handle Airplane Mode off
            // we will change the display settings of the list view in the MainActivity
            // and will set the list view enabled to be true
            Toast.makeText(context, "Airplane mode off - BACK ONLINE", Toast.LENGTH_SHORT).show();
            _mainActivity.getFlights_LV().setAlpha(1f);
            _mainActivity.getFlights_LV().setBackgroundColor(Color.WHITE);
            _mainActivity.getFlights_LV().setEnabled(true);
        }
    }
}
