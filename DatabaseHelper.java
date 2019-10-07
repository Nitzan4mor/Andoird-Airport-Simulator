package com.example.airport_project_nitzan_mor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// a class that extends SQLiteOpenHelper, this class will help us
// to backup our data using sql lite - select , insert and delete
public class DatabaseHelper extends SQLiteOpenHelper {

    // constant fields for our sql lite database
    public static final String DATABASE_NAME = "airport_fb_db.db";
    public static final String TABLE_NAME = "FLIGHTS";
    public static final String COL_1_ID = "ID";
    public static final String COL_2_airlineLogo = "airlineLogo ";
    public static final String COL_3_departureAirport = "departureAirport ";
    public static final String COL_4_departureCity = "departureCity";
    public static final String COL_5_expectedLandingTime = "expectedLandingTime";
    public static final String COL_6_finalLandingTime = "finalLandingTime";
    public static final String COL_7_flightNumber = "flightNumber";
    public static final String COL_8_flightStatus = "flightStatus";

    // C'tor that will create the data base in sql lite
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // create new table in the sql lite database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME
                + "(" + COL_1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_2_airlineLogo + " TEXT, "
                + COL_3_departureAirport + " TEXT, "
                + COL_4_departureCity + " TEXT, "
                + COL_5_expectedLandingTime + " TEXT, "
                + COL_6_finalLandingTime + " TEXT, "
                + COL_7_flightNumber + " TEXT, "
                + COL_8_flightStatus + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // send insert query to the sql lite data base
    public boolean addNewFlight(Flight flight) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_airlineLogo, flight.getAirlineLogo());
        contentValues.put(COL_3_departureAirport, flight.getDepartureAirport());
        contentValues.put(COL_4_departureCity, flight.getDepartureCity());
        contentValues.put(COL_5_expectedLandingTime, flight.getExpectedLandingTime());
        contentValues.put(COL_6_finalLandingTime, flight.getFinalLandingTime());
        contentValues.put(COL_7_flightNumber, flight.getFlightNumber());
        contentValues.put(COL_8_flightStatus, flight.getFlightStatus());
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    // send select query to the sql lite database
    public Cursor getAllFlight() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    // send delete query to the sql lite database for all data
    public void clearAllFlights() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}