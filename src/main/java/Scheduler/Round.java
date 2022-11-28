package main.java.Scheduler;

import java.util.ArrayList;

/**
 * ---------------------Add Description
 * 
 * @author Julian Obando
 *
 */
public class Round {

	ArrayList<Game> matchups;   //List of games in the round

	/**
	 * Default Constructor
	 * 
	 */
	public Round() {
		this.matchups = new ArrayList<Game>();
	}
	
	/**
	 * Add game to matchups in Round Instance
	 * 
	 * @param game
	 */
	public void add(Game game) {
		this.matchups.add(game);
	}
	
	/*
	 * Getters and Setters
	 */
	public Game getGame(int index) {
		return this.matchups.get(index);
	}
	
	public ArrayList<Game> getMatchups() {
		return this.matchups;
	}
}
