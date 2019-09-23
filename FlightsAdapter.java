package com.example.airport_project_nitzan_mor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlightsAdapter extends ArrayAdapter {

    private Context _context;
    private int _layout;
    private ArrayList<Flight> _flights;

    public FlightsAdapter(Context context, int layout, ArrayList<Flight> flights) {
        super(context, layout , flights);
        _context = context;
        _layout = layout;
        _flights = flights;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;
        if (result == null)
        {
            result = LayoutInflater.from(_context).inflate(_layout,
                    parent, false);
        }

        TextView expectedLandingTime_TV = result.findViewById(R.id.expectedLandingTime_TV);
        expectedLandingTime_TV.setText(_flights.get(position).getExpectedLandingTime());

        TextView departureCity_TV = result.findViewById(R.id.departureCity_TV);
        departureCity_TV.setText(_flights.get(position).getDepartureCity());

        TextView departureAirport_TV = result.findViewById(R.id.departureAirport_TV);
        departureAirport_TV.setText(_flights.get(position).getDepartureAirport());

        ImageView airlineLogo_IV = result.findViewById(R.id.airlineLogo_IV);
        String airlineLogo = _flights.get(position).getAirlineLogo();
        switch (airlineLogo){
            case "DL":
                Picasso.get().load(_context.getString(R.string.AIRLINE_LOGO_DELTA)).into(airlineLogo_IV);
                break;
            case "TG":
                Picasso.get().load(_context.getString(R.string.AIRLINE_LOGO_THAI)).into(airlineLogo_IV);
                break;
            case "IB":
                Picasso.get().load(_context.getString(R.string.AIRLINE_LOGO_IBERIA)).into(airlineLogo_IV);
                break;
            case "AZ":
                Picasso.get().load(_context.getString(R.string.AIRLINE_LOGO_ALITALIA)).into(airlineLogo_IV);
                break;
        }

        TextView flightNumber_TV = result.findViewById(R.id.flightNumber_TV);
        flightNumber_TV.setText(_flights.get(position).getFlightNumber());

        TextView flightStatusLanded_TV = result.findViewById(R.id.flightStatusLanded_TV);
        TextView flightStatusInFlight_TV = result.findViewById(R.id.flightStatusInFlight_TV);
        TextView flightStatusDelay_TV = result.findViewById(R.id.flightStatusDelay_TV);
        String flightStatus = _flights.get(position).getFlightStatus();
        TextView finalLandingTime_TV = result.findViewById(R.id.finalLandingTime_TV);
        if (flightStatus.equals(_context.getString(R.string.FLIGHT_STATUS_LANDED))){
            flightStatusLanded_TV.setText(flightStatus);
            finalLandingTime_TV.setText(_flights.get(position).getFinalLandingTime());
            flightStatusDelay_TV.setText("");
            flightStatusInFlight_TV.setText("");
        }
        if (flightStatus.equals(_context.getString(R.string.FLIGHT_STATUS_INFLIGHT))){
            flightStatusInFlight_TV.setText(flightStatus);
            finalLandingTime_TV.setText("");
            flightStatusLanded_TV.setText("");
            flightStatusDelay_TV.setText("");
        }
        if (flightStatus.equals(_context.getString(R.string.FLIGHT_STATUS_DELAYES))){
            flightStatusDelay_TV.setText(flightStatus);
            finalLandingTime_TV.setText("");
            flightStatusInFlight_TV.setText("");
            flightStatusLanded_TV.setText("");
        }
        return result;
    }
}
