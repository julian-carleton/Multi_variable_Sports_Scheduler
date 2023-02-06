package main.java.Scheduler;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Data Type for the time slots provided in the excel sheet
 * 
 * @author Quinn Sondermeyer
 */
public class TimeSlot {
	private LocalDateTime startDateTime;
	private Arena arena;
	private ArrayList<Division> divisions;
	private boolean isAvailable;	// whether it has been given a game
	private boolean isSelected; 	// Assigned to division to be given a game
	

	/**
	 * Default Constructor
	 * 
	 * @param start
	 * @param arena
	 * @param division
	 */
	public TimeSlot(LocalDateTime start, Arena arena, ArrayList<Division> division) {
		this.arena = arena;
		this.divisions = division;
		this.startDateTime = start;
		this.isAvailable = true;
		this.isSelected = false;
		
	}

	/**
	 * Sets Availability to false
	 */
	public void useTimeslot() {
		this.isAvailable = false;
	}
	
	/**
	 * Sets Whether it is selected to true
	 */
	public void selectTimeslot() {
		this.isSelected = true;
	}
	
	/*
	 * Getters and setters
	 */
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public Arena getArena() {
		return arena;
	}

	public ArrayList<Division> getDivisions() {
		return divisions;
	}

	public boolean isAvailable() {
		return isAvailable;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public String toString() {
		return "Time Slot at: "+this.arena.getName()+"		is available: " + this.isAvailable;
	}

	

	
	
	
	
	
	
	
}
