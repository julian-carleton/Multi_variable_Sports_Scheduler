package main.java.Scheduler;

import java.util.ArrayList;

public class Team {
	
	private String name;
	private float longitude;
	private float latitude;
	private Division division;
	private Tier tier;
	private League league;
	private ArrayList<Exception> exceptions;
	private float radius;
	private ArrayList<Arena> homeArenas;
	
	
	/**
	 * Default Constructor
	 * @param name, division, tier, league
	 */
	public Team(String name, float longitude, float latitude,  Division division, Tier tier) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.division = division;
		this.tier = tier;
		exceptions = new ArrayList<Exception>();
	}
	
	/**
	 * Name only constructor
	 * @param name
	 */
	public Team(String name) {
		this.name = name;
		exceptions = new ArrayList<Exception>();
	}
	
	/**
	 * Adds exception to exceptions list for a Team
	 * @param newException
	 */
	public void addException(Exception newException) {
		this.exceptions.add(newException);
	}
	
	
	//Getters and setters
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @return the exceptions
	 */
	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}

	/**
	 * @return the division
	 */
	public Division getDivision() {
		return division;
	}

	/**
	 * @return the tier
	 */
	public Tier getTier() {
		return tier;
	}

	/**
	 * @return the league
	 */
	public League getLeague() {
		return league;
	}

	/**
	 * 
	 * @return radius
	 */
	public float getRadius() {
		return radius;
	}

	
	 * Setter method for the longitude component of the teams location
	 *
	 * @param x
	 */
	public void setLongitude(float x) { longitude = x; }

	/**
	 * Setter method for the latitude component of the teams location
	 *
	 * @param y
	 */
	public void setLatitude(float y) { latitude = y; }

	public void addArena(Arena a) { homeArenas.add(a); }

	public ArrayList<Arena> getHomeArenas() { return homeArenas; }
	
}
