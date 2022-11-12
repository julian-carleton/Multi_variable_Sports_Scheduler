package main.java.Scheduler;

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

	
	//Getters and Setters
	
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
	
	
	
	
	
	
}
