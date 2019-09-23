package com.example.airport_project_nitzan_mor;

public class Flight {
    private String airlineLogo;
    private String flightNumber;
    private String departureCity;
    private String departureAirport;
    private String expectedLandingTime;
    private String flightStatus;
    private String finalLandingTime;

    public Flight() {
    }

    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getExpectedLandingTime() {
        return expectedLandingTime;
    }

    public void setExpectedLandingTime(String expectedLandingTime) {
        this.expectedLandingTime = expectedLandingTime;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public String getFinalLandingTime() {
        return finalLandingTime;
    }

    public void setFinalLandingTime(String finalLandingTime) {
        this.finalLandingTime = finalLandingTime;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "airlineLogo='" + airlineLogo + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureCity='" + departureCity + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", expectedLandingTime='" + expectedLandingTime + '\'' +
                ", flightStatus='" + flightStatus + '\'' +
                ", finalLandingTime='" + finalLandingTime + '\'' +
                '}';
    }
}
