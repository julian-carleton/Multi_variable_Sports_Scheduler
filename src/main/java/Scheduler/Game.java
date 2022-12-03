package main.java.Scheduler;

/**
 * Data Type of arena storing its name, and position
 * 
 * @author Julian Obando
 */
public class Game {
	Team homeTeam;
	Team awayTeam;
	TimeSlot timeSlot;
	int exceptions_num;
		
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
		this.exceptions_num = homeTeam.getExceptions().size() + awayTeam.getExceptions().size();
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
	}
	
	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}
	
	public int getExceptionsNumber() {
		return this.exceptions_num;
	}
}
