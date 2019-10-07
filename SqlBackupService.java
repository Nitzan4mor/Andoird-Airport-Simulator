package com.example.airport_project_nitzan_mor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

// a class that extends Service,
// we will use this class to backup all the data using Sql lite
public class SqlBackupService extends Service {

    // fields of the class
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private ArrayList<Flight> flights = MainActivity.adapter.get_flights();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // delete the current data and backup the new data using Sql lite with a new Thread
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseHelper.clearAllFlights();
                for (Flight flight : flights) {
                    if (databaseHelper.addNewFlight(flight)) {
                        Log.i("SqlBackupService", "NEW FLIGHT ADDED TO SQLITE");
                    } else {
                        Log.e("SqlBackupService", "FAILED TO ADD NEW FLIGHT TO SQLITE");
                    }
                }
            }
        }).start();
        return Service.START_STICKY;
    }
}
