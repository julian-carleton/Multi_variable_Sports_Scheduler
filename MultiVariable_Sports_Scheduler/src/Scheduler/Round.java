package Scheduler;

import java.util.ArrayList;
import Scheduler.Game;

public class Round {

	ArrayList<Object> matchups;   //List of games in the round

	public Round() {
		this.matchups = new ArrayList<Object>();
	}
	
	public void add(Game game) {
		this.matchups.add(game);
	}
	
}
