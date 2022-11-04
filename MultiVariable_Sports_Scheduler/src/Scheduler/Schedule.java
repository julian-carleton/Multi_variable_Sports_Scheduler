package Scheduler;

import java.util.ArrayList;
import Scheduler.Round;

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
	public void make_matchups() {
		
	}
	
	/**
	 * 
	 * 
	 * */
	public void match_RR() {
		//Determine if even or odd # teams
		int num_teams = this.teams.size();
		if (num_teams % 2 == 0) {
			num_rounds = num_teams - 1;
		} else {
			num_rounds = num_teams;
		}
		
		//Matching a round
		int curr_x = 1;
		int curr_y = num_teams;
		
		for (int i = 1; i < Math.floorDiv(num_teams, 2); i++) {
			
		}
		
	}
	
	public void match_round() {
		
	}
}
