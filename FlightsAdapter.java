package com.example.airport_project_nitzan_mor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// our adapter that extends from ArrayAdapter and connecting between the xml and the list view
public class FlightsAdapter extends ArrayAdapter {

    private Context _context;
    private int _layout;
    private ArrayList<Flight> _flights;

    // C'tor
    public FlightsAdapter(Context context, int layout, ArrayList<Flight> flights) {
        super(context, layout, flights);
        _context = context;
        _layout = layout;
        _flights = flights;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // check if the current view is null, in inflating it if needed
        View result = convertView;
        if (result == null) {
            result = LayoutInflater.from(_context).inflate(_layout,
                    parent, false);
        }

        // pulling the TextViews from the xml file
        TextView departureCity_TV = result.findViewById(R.id.departureCity_TV);
        TextView expectedLandingTime_TV = result.findViewById(R.id.expectedLandingTime_TV);
        TextView departureAirport_TV = result.findViewById(R.id.departureAirport_TV);
        TextView flightNumber_TV = result.findViewById(R.id.flightNumber_TV);


        // check if all field are null except the departure city field
        // we need this check for the FlightSearchActivity
        if (_flights.get(position).getFlightStatus() == null
                && _flights.get(position).getAirlineLogo() == null
                && _flights.get(position).getDepartureAirport() == null
                && _flights.get(position).getExpectedLandingTime() == null
                && _flights.get(position).getFinalLandingTime() == null
                && _flights.get(position).getFlightNumber() == null
                && _flights.get(position).getDepartureCity() != null) {
            departureCity_TV.setText(_flights.get(position).getDepartureCity());
            departureCity_TV.setTextSize(30);
            departureCity_TV.setTextColor(Color.BLUE);
            return result;
        }

        //  giving values to the TextViews from the Flight Objects in our flight list
        expectedLandingTime_TV.setText(_flights.get(position).getExpectedLandingTime());
        departureCity_TV.setText(_flights.get(position).getDepartureCity());
        departureAirport_TV.setText(_flights.get(position).getDepartureAirport());
        flightNumber_TV.setText(_flights.get(position).getFlightNumber());

        // pulling the image view from the xml and the airline logo field from the Flight Object
        // check which airline is listed as the operator in the current Flight using switch-case
        // and assign the correct image using picasso and url that is stored in our Values -> String
        ImageView airlineLogo_IV = result.findViewById(R.id.airlineLogo_IV);
        String airlineLogo = _flights.get(position).getAirlineLogo();
        switch (airlineLogo) {
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

        // pulling the 3 Text views for the flight status - landed, inflight , delayed
        // and the flight status field from our Flight Object in the list
        TextView flightStatusLanded_TV = result.findViewById(R.id.flightStatusLanded_TV);
        TextView flightStatusInFlight_TV = result.findViewById(R.id.flightStatusInFlight_TV);
        TextView flightStatusDelay_TV = result.findViewById(R.id.flightStatusDelay_TV);
        String flightStatus = _flights.get(position).getFlightStatus();
        TextView finalLandingTime_TV = result.findViewById(R.id.finalLandingTime_TV);

        // check which one of the 3 flight status is equals to our Flight Object status
        // change the flight status text views and the final landing time text view accordingly
        // if we won't take care of it, than when change the display settings wrong data's colors
        // will be will shown in the display as the items where already inflated
        if (flightStatus.equals(_context.getString(R.string.FLIGHT_STATUS_LANDED))) {
            flightStatusLanded_TV.setText(flightStatus);
            finalLandingTime_TV.setText(_flights.get(position).getFinalLandingTime());
            flightStatusDelay_TV.setText("");
            flightStatusInFlight_TV.setText("");
        }
        if (flightStatus.equals(_context.getString(R.string.FLIGHT_STATUS_INFLIGHT))) {
            flightStatusInFlight_TV.setText(flightStatus);
            finalLandingTime_TV.setText("");
            flightStatusLanded_TV.setText("");
            flightStatusDelay_TV.setText("");
        }
        if (flightStatus.equals(_context.getString(R.string.FLIGHT_STATUS_DELAYES))) {
            flightStatusDelay_TV.setText(flightStatus);
            finalLandingTime_TV.setText("");
            flightStatusInFlight_TV.setText("");
            flightStatusLanded_TV.setText("");
        }
        return result;
    }
}
