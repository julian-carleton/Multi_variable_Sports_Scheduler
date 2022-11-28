package main.java.Scheduler;

public class Game {
	Team homeTeam;
	Team awayTeam;
	TimeSlot timeSlot;
	int exceptions_num;
	
	public Game() {
		
	}
	
	public Game(Team homeTeam, Team awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.exceptions_num = homeTeam.getExceptions().size() + awayTeam.getExceptions().size();
	}
	
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
