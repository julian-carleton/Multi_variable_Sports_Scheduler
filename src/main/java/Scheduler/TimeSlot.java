package main.java.Scheduler;
import java.time.LocalDateTime;

public class TimeSlot {
	private LocalDateTime StartDateTime;
	private Arena arena;
	private Division division;
	
	public TimeSlot(LocalDateTime start, Arena arena, Division division) {
		this.arena = arena;
		this.division = division;
		this.StartDateTime = start;
		
	}

	
	//Getters and setters
	/**
	 * @return the startDateTime
	 */
	public LocalDateTime getStartDateTime() {
		return StartDateTime;
	}

	/**
	 * @return the arena
	 */
	public Arena getArena() {
		return arena;
	}

	/**
	 * @return the division
	 */
	public Division getDivision() {
		return division;
	}
	
	
	
	
	
	
}
