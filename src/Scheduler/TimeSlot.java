package Scheduler;
import java.time.LocalDateTime;

public class TimeSlot {
	private LocalDateTime StartDateTime;
	private Arena arena;
	private Division division;
	private Tier tier;
	
	public TimeSlot(LocalDateTime start, Arena arena, Division division, Tier tier) {
		this.arena = arena;
		this.division = division;
		this.StartDateTime = start;
		this.tier = tier;
		
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

	/**
	 * @return the tier
	 */
	public Tier getTier() {
		return tier;
	}
	
	
	
	
	
	
}
