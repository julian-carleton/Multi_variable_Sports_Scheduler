package main.java.Scheduler;

import java.math.*;

/**
 * Data Type of arena storing its name, and position
 * 
 * @author Quinn Sondermeyer
 */
public class Arena {
	private String name;
	private float longitude;
	private float latitude;
	
	/**
	 * Default Constructor
	 * @param name
	 * @param longitude
	 * @param latitude
	 */
	public Arena(String name, float longitude, float latitude) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * Checks if arena is in a teams radius
	 * 
	 * @param latitude2
	 * @param longitude2
	 * @param radius
	 * @return
	 */
	public boolean isInrad(double latitude2, double longitude2, double radius) {
		float length = (float) Math.sqrt((Math.pow((longitude-longitude2), 2) + Math.pow((latitude-latitude2), 2)));
		if (radius < length) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * Getters and Setters
	 */
	public String getName() {
		return name;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	public float getLatitude() {
		return latitude;
	}



	
	
	
	
	
	
}
