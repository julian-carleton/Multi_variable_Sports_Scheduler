package main.java.Scheduler;

import java.util.ArrayList;

public class Round {

	ArrayList<Game> matchups;   //List of games in the round

	public Round() {
		this.matchups = new ArrayList<Game>();
	}
	
	public void add(Game game) {
		this.matchups.add(game);
	}
	
	public Game getGame(int index) {
		return this.matchups.get(index);
	}
	
	public ArrayList<Game> getMatchups() {
		return this.matchups;
	}
}
