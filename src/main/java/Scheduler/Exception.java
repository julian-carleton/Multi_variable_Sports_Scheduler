package main.java.Scheduler;

import java.time.LocalDateTime;

/**
 * Data Type of arena storing its start and end LocalDateTime
 * 
 * @author Quinn Sondermeyer
 */
public class Exception {
	public LocalDateTime start;
	public LocalDateTime end;
	
	/**
	 * Default Constructor 
	 * 
	 * @param start
	 * @param end
	 */
	public Exception(LocalDateTime start,LocalDateTime end) {
		this.start = start;
		this.end = end;
	}
		
	/**
	 * Returns values based if one is earlier than the other
	 * 
	 * @param compare
	 * @return negative is lesshtan, posative if greaterthan
	 */
	public int compareStart(LocalDateTime compare) {
		return start.compareTo(compare);
	}
	
	/**
	 * Returns values based if one is earlier than the other
	 * 
	 * @param compare
	 * @return negative is lesshtan, posative if greaterthan
	 */
	public int compareEnd(LocalDateTime compare) {
		return end.compareTo(compare);
	}



	/*
	 * Getters and Setters
	 */
	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}
	
	
	
	
}
