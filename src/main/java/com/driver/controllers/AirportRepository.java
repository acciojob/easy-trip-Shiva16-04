package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class AirportRepository {
    private HashMap<String, Airport>airportsDatabase;
    private HashMap<Integer, Passenger>passengerDatabase;
    private HashMap<Integer, Flight>flightsDatabase;
    private HashMap<Integer, List<Integer>>flightBookingsDatabase; //flight id vs list of passengers ids who booked the ticket

    private HashMap<Integer, List<Integer>>PassengerBookingsDatabase; //passenger id vs list of flight ids for a passenger

    AirportRepository(){
        airportsDatabase=new HashMap<>();
        passengerDatabase=new HashMap<>();
        flightsDatabase=new HashMap<>();
        flightBookingsDatabase=new HashMap<>();
        PassengerBookingsDatabase=new HashMap<>();
    }

    public HashMap<String, Airport> getAirportsDatabase() {
        return airportsDatabase;
    }

    public HashMap<Integer, Passenger> getPassengerDatabase() {
        return passengerDatabase;
    }

    public HashMap<Integer, Flight> getFlightsDatabase() {
        return flightsDatabase;
    }

    public HashMap<Integer, List<Integer>> getFlightBookingsDatabase() {
        return flightBookingsDatabase;
    }

    public HashMap<Integer, List<Integer>> getPassengerBookingsDatabase() {
        return PassengerBookingsDatabase;
    }

    //Method 1: Add airports details
    public String addAirport(Airport airport){
        getAirportsDatabase().put(airport.getAirportName(), airport);
        return "SUCCESS";
    }

    //Method 2: Add Passenger details
    public String addPassenger(Passenger passenger){
        if(getPassengerDatabase().containsKey(passenger.getPassengerId())==false) {
            getPassengerDatabase().put(passenger.getPassengerId(), passenger);
            getPassengerBookingsDatabase().put(passenger.getPassengerId(),new ArrayList<>());
            return "SUCCESS";
        }else{
            return "";
        }
    }

    //Method 3: Adding a flight
    public String addFlight(Flight flight){
        if(getFlightsDatabase().containsKey(flight.getFlightId())==false){
            getFlightsDatabase().put(flight.getFlightId(), flight);
            getFlightBookingsDatabase().put(flight.getFlightId(), new ArrayList<>());
            return "SUCCESS";
        }else{
            return "";
        }
    }

    //Method 4: Booking a ticket
    public String bookATicket(Integer flightId, Integer passengerId){

        getFlightBookingsDatabase().get(flightId).add(passengerId);
        getPassengerBookingsDatabase().get(passengerId).add(flightId);
        return "SUCCESS";
    }

    //Method 5: cancel a Ticket
    public String cancelATicket(Integer flightId, Integer passengerId){
        getFlightBookingsDatabase().get(flightId).remove(passengerId);
        getPassengerBookingsDatabase().get(passengerId).remove(flightId);
        return "SUCCESS";
    }

}
