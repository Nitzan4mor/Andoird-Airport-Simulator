package com.example.airport_project_nitzan_mor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

// our broadcast receiver class that change the application display
// in our case, each time the flight mode is changed, we will if it's on or off
// and change the app display accordingly
public class FlightModeReceiver extends BroadcastReceiver {

    private MainActivity _mainActivity;

    public FlightModeReceiver(MainActivity mainActivity) {
        _mainActivity = mainActivity;
    }

    public FlightModeReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
        if (isAirplaneModeOn) {
            // handle Airplane Mode on
            Toast.makeText(context, "Airplane mode on - NO INTERNET", Toast.LENGTH_SHORT).show();
            _mainActivity.getFlights_LV().setAlpha(0.75f);
            _mainActivity.getFlights_LV().setBackgroundColor(Color.GRAY);
            _mainActivity.getFlights_LV().setEnabled(false);
        } else {
            // handle Airplane Mode off
            Toast.makeText(context, "Airplane mode off - BACK ONLINE", Toast.LENGTH_SHORT).show();
            _mainActivity.getFlights_LV().setAlpha(1f);
            _mainActivity.getFlights_LV().setBackgroundColor(Color.WHITE);
            _mainActivity.getFlights_LV().setEnabled(true);
        }
    }

}
