package Scheduler;

public class Game {
	Object homeTeam;
	Object awayTeam;
	Object timeSlot;
	
	public Game(Object teamHome, Object teamAway) {
		this.homeTeam = teamHome;
		this.awayTeam = teamAway;
	}
	
	public Object getHomeTeam() {
		return this.homeTeam;
	}
	
	public Object getAwayTeam() {
		return this.awayTeam;
	}
}
