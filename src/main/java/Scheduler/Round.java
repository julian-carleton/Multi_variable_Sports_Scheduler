package main.java.Scheduler;

import java.util.ArrayList;

public class Round {

	ArrayList<Object> matchups;   //List of games in the round

	public Round() {
		this.matchups = new ArrayList<Object>();
	}
	
	public void add(Game game) {
		this.matchups.add(game);
	}
	
	public Object getGame(int index) {
		return this.matchups.get(index);
	}
	
	public Object getMatchups() {
		return this.matchups;
	}
}
