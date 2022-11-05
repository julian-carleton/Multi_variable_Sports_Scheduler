package Scheduler;

import java.util.ArrayList;
//import Scheduler.Round;

public class Schedule {
	ArrayList<Round> rounds;   
	ArrayList<Object> teams;
	int num_rounds;
	
	/** 
	 * Default constructor
	 * */
	public Schedule(ArrayList<Object> teams) {
		this.teams = teams;
		this.rounds = new ArrayList<Round>();
	}
	
	/**
	 * 
	 * 
	 * */
	public void makeMatchups() {
		
	}
	
	/**
	 * 
	 * 
	 * */
	public void matchRR() {
		//Determine if even or odd # teams
		int num_teams = this.teams.size();
		if (num_teams % 2 == 0) {
			num_rounds = num_teams - 1;
		} else {
			num_rounds = num_teams;
		}
		
		//Matching a round
		int curr_x = 0;
		int curr_y = num_teams - 1;
		Round curr_round = new Round();
		for (int i = 0; i < Math.floorDiv(num_teams, 2); i++) {
			Game curr_game = new Game(); 
			curr_game.setHomeTeam(teams.get(curr_x));
			curr_game.setAwayTeam(teams.get(curr_y));
			curr_round.add(curr_game);
			curr_x ++;
			curr_y --;
		}
		this.rounds.add(curr_round);
	}
	
	public void matchRound() {
		
	}
	
	public ArrayList<Round> getRounds() {
		return this.rounds;
	}
}
