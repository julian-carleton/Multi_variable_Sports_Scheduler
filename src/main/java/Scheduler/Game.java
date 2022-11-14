package main.java.Scheduler;

public class Game {
	Object homeTeam;
	Object awayTeam;
	Object timeSlot;
	
	public Game() {
		
	}
	
	public Game(Object homeTeam, Object awayTeam) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}
	
	public Object getHomeTeam() {
		return this.homeTeam;
	}
	
	public Object getAwayTeam() {
		return this.awayTeam;
	}
	
	public void setHomeTeam(Object homeTeam) {
		this.homeTeam = homeTeam;
	}
	
	public void setAwayTeam(Object awayTeam) {
		this.awayTeam = awayTeam;
	}
}
