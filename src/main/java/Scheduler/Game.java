package main.java.Scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Data Type of arena storing its name, and position
 * 
 * @author Julian Obando
 */
public class Game {
	private Team homeTeam;
	private Team awayTeam;
	private TimeSlot timeSlot;
	private int exceptionsNum;
		
	/**
	 * Default Constructor
	 */
	public Game() {
		
	}
	
	/**
	 * Constructor with known home and away team
	 * 
	 * @param homeTeam
	 * @param awayTeam
	 */
	public Game(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.exceptionsNum = homeTeam.getExceptions().size() + awayTeam.getExceptions().size();
	}
	
	/**
	 * Returns the number of Exceptions within a time frame for home and away team
	 * 
	 * @author Quinn Sondermeyer
	 * @param start	
	 * @param length days to check
	 * @return number of exceptions in given time
	 */
	public int getNumExecpRange(LocalDateTime start, LocalDateTime end) {
		ArrayList<Exception> homeExcep = homeTeam.getExceptions();
		ArrayList<Exception> awayExcep = awayTeam.getExceptions();
		int count = 0;
		count += numExepRangehelper(start, end, homeExcep);
		count += numExepRangehelper(start, end, awayExcep);
		
		return count; 
	}
	
	/**
	 * Returns the number of Exceptions within a time frame
	 * 
	 * @author Quinn Sondermeyer
	 * @param start
	 * @param end
	 * @param exeptions
	 * @return number of exceptions in given time
	 */
	private int numExepRangehelper(LocalDateTime start, LocalDateTime end, ArrayList<Exception> exeptions) {
		int count = 0;
		for (Exception e:exeptions) {
			if (e.getStart().isBefore(start)) {
				if (e.getEnd().isAfter(start)) {
					count++;
				}
			}
			else if(e.getStart().compareTo(end) < 0) {// check of start is earlier than exception - given timeslot length
                	count++;
			}
		}
		return count;
	}
	
	
	/*
	 * Getters and Setters
	 */
	public Team getHomeTeam() {
		return this.homeTeam;
	}
	
	public Team getAwayTeam() {
		return this.awayTeam;
	}
	
	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
		this.exceptionsNum = this.homeTeam.getExceptions().size() + this.awayTeam.getExceptions().size();
	}
	
	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
		this.exceptionsNum = this.homeTeam.getExceptions().size() + this.awayTeam.getExceptions().size();
	}
	
	public int getExceptionsNumber() {
		return this.exceptionsNum;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}
	
	
	
	

}
