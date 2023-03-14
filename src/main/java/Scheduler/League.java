package Scheduler;

import java.util.ArrayList;




/**
 *  Stores the leagues in the schedule and keeps track of the teams, divisions, and time slots assigned to them
 * 
 * @author Quinn Sondermeyer
 */
public class League {
	private String name;
	private ArrayList<Division> divisions;
	private ArrayList<TimeSlot> timeslots;
	private ArrayList<Arena> arenas;
	private ArrayList<Schedule> schedules;
	private double gamesPerWeek = 1;

	
	/**
	 * Default Constructor
	 */
	public League(String name, ArrayList<Division> divisions) {
		this.name = name;
		this.divisions = divisions;
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param divisions
	 * @param timeslots
	 */
	public League(String name, ArrayList<Division> divisions, ArrayList<TimeSlot> timeslots, ArrayList<Arena> arenas) {
		this.name = name;
		this.divisions = divisions;
		this.timeslots = timeslots;
		this.arenas = arenas;

	}
	/**
	 * Adds new division to league
	 * 
	 * @param division
	 */
	public void addDivision(Division division) {
		divisions.add(division);
	}
	

	public String getName() {
		return name;
	}

	public ArrayList<Division> getDivisions() {
		return divisions;
	}
	
	public void setDivisions(ArrayList<Division> divisions) {
		this.divisions = divisions;
	}

	public double getGamesPerWeek() {
		return gamesPerWeek;
	}

	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}

	public ArrayList<TimeSlot> getTimeslots() {
		return timeslots;
	}


	public ArrayList<Arena> getArenas() {
		return arenas;
	}
	
	
	
	
}
