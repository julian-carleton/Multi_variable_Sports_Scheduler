package main.java.Scheduler;

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
