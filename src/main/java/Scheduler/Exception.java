<<<<<<< Updated upstream
package main.java.Scheduler;
=======
package Scheduler;
>>>>>>> Stashed changes

import java.time.LocalDateTime;

public class Exception {
	public LocalDateTime start;
	public LocalDateTime end;
	
	
	public Exception(LocalDateTime start,LocalDateTime end) {
		this.start = start;
		this.end = end;
	}
		
	
	
	
	/**
	 * returns values based if one is earlier than the other
	 * @param compare
	 * @return negative is lesshtan, posative if greaterthan
	 */
	public int compareStart(LocalDateTime compare) {
		return start.compareTo(compare);
	}
	
	/**
	 * returns values based if one is earlier than the other
	 * @param compare
	 * @return negative is lesshtan, posative if greaterthan
	 */
	public int compareEnd(LocalDateTime compare) {
		return end.compareTo(compare);
	}



	// Getters and Setters
	/**
	 * @return the start
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public LocalDateTime getEnd() {
		return end;
	}
	
	
	
	
}
