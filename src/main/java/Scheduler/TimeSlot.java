package main.java.Scheduler;
import java.time.LocalDateTime;

/**
 * Data Type for the time slots provided in the excel sheet
 * 
 * @author Quinn Sondermeyer
 */
public class TimeSlot {
	private LocalDateTime startDateTime;
	private Arena arena;
	private Division division;
	private boolean isAvailable;
	
	/**
	 * Default Constructor
	 * 
	 * @param start
	 * @param arena
	 * @param division
	 */
	public TimeSlot(LocalDateTime start, Arena arena, Division division) {
		this.arena = arena;
		this.division = division;
		this.startDateTime = start;
		this.isAvailable = true;
		
	}

	/**
	 * Sets Availability to false
	 */
	public void useTimeslot() {
		this.isAvailable = false;
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

	public Division getDivision() {
		return division;
	}

	public boolean isAvailable() {
		return isAvailable;
	}


	
	
	
	
	
	
	
}
