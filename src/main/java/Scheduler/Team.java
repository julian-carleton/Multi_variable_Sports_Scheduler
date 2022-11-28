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

	
	
	
}
