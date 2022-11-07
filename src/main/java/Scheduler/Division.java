package Scheduler;

import java.util.ArrayList;

public class Division {
	private String name;
	private int age;
	private ArrayList<Team> teams;

	/**
	 * Default Constructor
	 */
	public Division(String name, int age) {
		this.name = name;
		this.age = age;
	}
	


	/**
	 * Adds team in Division 
	 * @param name
	 */
	public void addTeam(Team team) {
		teams.add(team);
	}
	
	
	
	
// -----------------------------------Getters------------------------------------------------
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	
	
	/**
	 * @return the teams
	 */
	public ArrayList<Team> getTeams() {
		return teams;
	}
}
