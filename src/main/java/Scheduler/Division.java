package main.java.Scheduler;

import java.util.ArrayList;

/**
 * Data Type of arena storing its name, and teams in it
 * 
 * @author Quinn Sondermeyer
 */
public class Division {
	private String name;
	private int age;
	private ArrayList<Team> teams;

	/**
	 * Default Constructor
	 */
	public Division(String name) {
		this.name = name;
		teams = new ArrayList<Team>();
	}
	


	/**
	 * Adds team in Division 
	 * 
	 * @param name
	 */
	public void addTeam(Team team) {
		teams.add(team);
	}
	
	
	/*
	 * Getters and Setters
	 */
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	public ArrayList<Team> getTeams() {
		return teams;
	}
}
