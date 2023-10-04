package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AirportService {

    public AirportRepository airportRepo=new AirportRepository();

    //Method 1: add_Airport
    public String addAirport(Airport airport){
        return airportRepo.addAirport(airport);
    }

    //Method 2:
    public String addPassenger(Passenger passenger){
        return airportRepo.addPassenger(passenger);
    }

    //Method 3:
    public String addFlight(Flight flight){
        return airportRepo.addFlight(flight);
    }

    //Method 4:
    public String bookATicket(Integer flightId, Integer passengerId){
        HashMap<Integer, List<Integer>>flightBookings=airportRepo.getFlightBookingsDatabase();
        HashMap<Integer,List<Integer>>passengerBookings=airportRepo.getPassengerBookingsDatabase();
        HashMap<Integer, Flight>flights=airportRepo.getFlightsDatabase();
        if(flightBookings.containsKey(flightId)==false||passengerBookings.containsKey(passengerId)==false){ //checking for a valid flightId and passengerId
            return "FAILURE";
        }else if(flightBookings.get(flightId).size()==flights.get(flightId).getMaxCapacity()){ //checking for space availability
            return "FAILURE";
        }else if(flightBookings.get(flightId).contains(passengerId)){
            return "FAILURE";
        } else{
            return airportRepo.bookATicket(flightId, passengerId);
        }
    }

    //Method 5: cancel a ticket
    public String cancelATicket(Integer flightId, Integer passengerId){
        HashMap<Integer, List<Integer>>flightBookings=airportRepo.getFlightBookingsDatabase();
        HashMap<Integer,List<Integer>>passengerBookings=airportRepo.getPassengerBookingsDatabase();
        HashMap<Integer, Flight>flights=airportRepo.getFlightsDatabase();
        if(flightBookings.containsKey(flightId)==false||passengerBookings.containsKey(passengerId)==false){ //checking for a valid flightId and passengerId
            return "FAILURE";
        }else if(flightBookings.get(flightId).contains(passengerId)==false){ //checking for space availability
            return "FAILURE";
        }else{
            return airportRepo.cancelATicket(flightId, passengerId);
        }
    }

    //Method 6: count-of-bookings-done-by-a-passenger
    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        HashMap<Integer,List<Integer>>passengerBookings=airportRepo.getPassengerBookingsDatabase();
        if(passengerBookings.containsKey(passengerId)==true)
            return passengerBookings.get(passengerId).size();
        else return 0;
    }

    //Method 7: calculateFlightFare
    public int calculateFlightFare(Integer flightId){
        HashMap<Integer, List<Integer>>flightBookings=airportRepo.getFlightBookingsDatabase();
        if(flightBookings.containsKey(flightId)==true)
            return 3000+(flightBookings.get(flightId).size()*50);
        else return 0;
    }

    //Method 8: calculate-revenue-collected
    public int calculateRevenueOfAFlight(Integer flightId){
        HashMap<Integer, List<Integer>>flightBookings=airportRepo.getFlightBookingsDatabase();
        int totalRevenue=0;
        for(int i=0; i<flightBookings.get(flightId).size(); i++){
            totalRevenue+=3000+(i*50);
        }
        return totalRevenue;
    }

    //Method 9: get-number-of-people-on-airport-on/{date}
    public int getNumberOfPeopleOnAirport(Date date, String airportName){
        HashMap<Integer, List<Integer>>flightBookings=airportRepo.getFlightBookingsDatabase();
        HashMap<Integer, Flight>flights=airportRepo.getFlightsDatabase();
        HashMap<String, Airport>airports=airportRepo.getAirportsDatabase();
        if(flightBookings.size()==0 || flights.size()==0 || airports.size()==0)return 0;
        int numberOfPeople=0;
        for(Flight flight: flights.values()){
            if((flight!=null) && (date.equals(flight.getFlightDate()) == true) &&
                    (flight.getFromCity().equals(airports.get(airportName).getCity()) || flight.getToCity().equals(airports.get(airportName).getCity()))){
                numberOfPeople+=flightBookings.get(flight.getFlightId()).size();
            }
        }
        return numberOfPeople;
    }

    //Method 10: shortest-time-travel-between-cities
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        HashMap<Integer, Flight>flights=airportRepo.getFlightsDatabase();
        double time=Double.MAX_VALUE;
        for(Flight flight:flights.values()){
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)){
                if(flight.getDuration()<time)time=flight.getDuration();
            }
        }
        if(time==Double.MAX_VALUE)return -1;
        else return time;
    }

    //Method 11: get-largest-airport
    public String getLargestAirportName(){
        HashMap<String, Airport>airportDatabase=airportRepo.getAirportsDatabase();
        String airportName=null;
        int count=Integer.MIN_VALUE;
        for(Airport airport:airportDatabase.values()){
            if(airport.getNoOfTerminals()>=count){
                if(airportName==null || airport.getNoOfTerminals()>count){
                    airportName=airport.getAirportName();
                    count=airport.getNoOfTerminals();
                }else if(airport.getNoOfTerminals()==count){
                    if(airportName.compareToIgnoreCase(airport.getAirportName())>0){
                        airportName=airport.getAirportName();
                    }
                }
            }
        }
        return airportName;
    }

    //Method 12: takeoff airportName-from-flightId
    public String getAirportNameFromFlightId(Integer flightId){
        HashMap<Integer, Flight>flights=airportRepo.getFlightsDatabase();
        HashMap<String, Airport>airportDatabase=airportRepo.getAirportsDatabase();
        if(flights.containsKey(flightId)==true){
            for(Airport airport: airportDatabase.values()){
                if(flights.get(flightId).getFromCity().equals(airport.getCity()))return airport.getAirportName();
            }
        }
        return null;
    }
}
