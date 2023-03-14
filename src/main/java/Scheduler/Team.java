package Scheduler;

import java.util.ArrayList;

/**
 * Data Type for teams stores their name, division, tier, league, location, radius of travel, home arenas, and exceptions
 * 
 * @author Quinn Sondermeyer
 */
public class Team {
	
	private String name;
	private double longitude;
	private double latitude;
	private Division division;
	private Tier tier;
	private League league;
	private ArrayList<Exception> exceptions;
	private double radius;
	private ArrayList<Arena> homeArenas;
	
	
	/**
	 * Default Constructor
	 * 
	 * @param name, division, tier, league
	 */
	public Team(String name, double longitude, double latitude,  Division division, Tier tier) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.division = division;
		this.tier = tier;
		exceptions = new ArrayList<Exception>();
		homeArenas = new ArrayList<Arena>();
	}
	
	/**
	 * Name only constructor
	 * 
	 * @param name
	 */
	public Team(String name) {
		this.name = name;
		exceptions = new ArrayList<Exception>();
		homeArenas = new ArrayList<Arena>();
	}
	
	/**
	 * Adds exception to exceptions list for a Team
	 * 
	 * @param newException
	 */
	public void addException(Exception newException) {
		this.exceptions.add(newException);
	}
	
	/**
	 * Adds Arena to home arena list
	 * 
	 * @param a - Arena that you want to add
	 */
	public void addArena(Arena a) { 
		homeArenas.add(a); 
	}
	
	/**
	 * Generates radius based on home the arenas Longitude and Latitude values
	 */
	public void generateRad() {
		double rad = 0;
		for (Arena a: this.getHomeArenas()) {					
			if ( rad < ((latitude - a.getLatitude()) * (longitude - a.getLongitude())) / 2) {		// find longest distance between center point and home arena and set to radius
				rad = ((latitude - a.getLatitude()) * (longitude - a.getLongitude())) / 2;
			}
		}
		this.radius = rad;
	}
	
	
	/**
	 * Check if Team has arena in home arena List
	 * 
	 * @param arena
	 * @return true if arena is in homeArenas 
	 */
	public boolean isHomeArena(Arena arena) {
		if (this.homeArenas.contains(arena)) {
			return true;
		}
		return false;
		
	}
	
	
	
	/**
	 * Checks if team is available 
	 * 
     * @author Faris
     * @param team
     * @param timeSlot
     * @return
     */
    public boolean exceptionCheck(TimeSlot timeSlot ) {
        int lengthofTimeSlot = 3;
        ArrayList<Exception> exceptions = this.getExceptions();                 // get exceptions
        for (Exception e: exceptions) {                                        // Loop Through all exceptions

            if (e.getStart().compareTo(timeSlot.getStartDateTime()) < 0) {     // check of start is earlier than exception
                if (e.getEnd().compareTo(timeSlot.getStartDateTime()) > 0) { // check of end is later than exception
                    return false;
                }
            }else if(e.getStart().compareTo(timeSlot.getStartDateTime().plusHours(lengthofTimeSlot)) < 0) {// check of start is earlier than exception - given timeslot length
                    return false;

            }
        }
        return true;

    }
	
	/*
	 * Getters and setters
	 */
	public String getName() {
		return name;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}

	public Division getDivision() {
		return division;
	}

	public Tier getTier() {
		return tier;
	}

	public League getLeague() {
		return league;
	}

	public double getRadius() {
		return radius;
	}

	public void setLongitude(double x) { 
		longitude = x; 
	}
	
	public void setLatitude(double y) { 
		latitude = y; 
	}

	public ArrayList<Arena> getHomeArenas() { 
		return homeArenas; 
	}
	
}
