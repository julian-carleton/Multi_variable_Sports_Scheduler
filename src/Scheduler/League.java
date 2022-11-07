package Scheduler;

import java.util.ArrayList;

public class League {
	private String name;
	private ArrayList<Division> divisions;

	/**
	 * Default Constructor
	 * @param name
	 */
	public League(String name, ArrayList<Division> divisions) {
		this.name = name;
		this.divisions = divisions;
	}
	
	
	/**
	 * Adds new division to league
	 * @param division
	 */
	public void addDivision(Division division) {
		divisions.add(division);
	}
	
	
	//Getters and Setters
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the divisions
	 */
	public ArrayList<Division> getDivisions() {
		return divisions;
	}

	
	

}
