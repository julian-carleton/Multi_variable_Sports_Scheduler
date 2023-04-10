package Demo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Scheduler.*;
import Scheduler.Exception;

public class DemoSchedule {
	
	/*
	 * Print Functions
	 */
	public static void printTeams(Schedule schedule) {
		ArrayList<Team> teams = schedule.getTeams();
		Team currTeam;
		System.out.print("The teams in this schedule are:\n");
		for (int j = 0; j < teams.size(); j++) {
			currTeam = teams.get(j);
			ArrayList<Arena> homeArenas = currTeam.getHomeArenas();
			System.out.print("Team Name: ");
			System.out.print(currTeam.getName());
			System.out.print("  Team Home Arenas: ");
			for (int i = 0; i < homeArenas.size(); i++) {
				System.out.print(homeArenas.get(i).getName());
			}
			System.out.print("	This team has (# exceptions):" +currTeam.getExceptions().size());
			System.out.print("\n");
		}
	}

	/*
	 * Print Functions
	 */
	public static void printRounds(Schedule schedule) {
		
		System.out.print("The following matchups are possible for the given teams:\n");
		for (int j = 0; j < schedule.getRounds().size(); j++) {
			Round curr_round = schedule.getRounds().get(j);
			for (int i = 0; i <  Math.floorDiv(schedule.getRounds().size(), 2); i++) {
				System.out.print("	");
				System.out.print(((Game) curr_round.getGame(i)).getHomeTeam().getName());
				System.out.print(" vs ");
				System.out.print(((Game) curr_round.getGame(i)).getAwayTeam().getName());
				System.out.print("	This match up has (# exceptions):" +curr_round.getGame(i).getExceptionsNumber());
				System.out.print("\n");
			}
			System.out.print("This round has: ");
			System.out.print(((ArrayList<Game>)curr_round.getMatchups()).size());
			System.out.print(" matchups\n\n");
		}
	}
	
	public static void printGames(Schedule schedule) {
		
		System.out.print("The games for this schedule are:\n");
		for (int j = 0; j < schedule.getGames().size(); j++) {
			Game currGame = schedule.getGames().get(j);
			System.out.print("	");
			System.out.print(currGame.getHomeTeam().getName());
			System.out.print(" vs ");
			System.out.print(currGame.getAwayTeam().getName());
			System.out.print("	assigned	"+currGame.getTimeSlot());
			System.out.print("\n");
		}
	}
	
	public static void printTimeSlots(ArrayList<TimeSlot> timeslots) {
		System.out.print("The time slots are:\n");
		for (int j = 0; j < timeslots.size(); j++) {
			System.out.print("	"+timeslots.get(j) + "\n");
		}
		System.out.print("\n\n");
	}
	
	/*
	 * Creates and prints the information of the schedule
	 */
	public static void printSchedule(Schedule schedule) {
		
		System.out.print("Before scheduling...\n");
		printTeams(schedule);
		
		System.out.print("\n");
		System.out.print("The timeSlots BEFORE scheduling are as follows: \n");
		printTimeSlots(schedule.getTimeSlots());
		
		schedule.createSchedule();
		printRounds(schedule);		
		
		//Showing the assigning of games to timeSlots
		System.out.print("After scheduling...\n");
		printGames(schedule);
		
		System.out.print("\n");
		System.out.print("The timeSlots AFTER scheduling are as follows: \n");
		printTimeSlots(schedule.getTimeSlots());
	}
	
	/*
	 * Demo with simulated data
	 */
	public static void demo() {
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		ArrayList<Team> teams = new ArrayList<Team>();
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		int actualNumRounds = 22;
		
		//Making list of Arenas
		int numArenas = 7;
		for (int i = 0; i < numArenas; i++) {
			Arena tempArena = new Arena("Arena "+ (i+1), (float) 0.0, (float) 0.0);
			arenas.add(tempArena);
		}
		//Adding two extra non schedulable arenas
		Arena tempArena = new Arena("Arena X", (float) 0.0, (float) 0.0);
		arenas.add(tempArena);
		tempArena = new Arena("Arena Y", (float) 0.0, (float) 0.0);
		arenas.add(tempArena);
		
		
		//Making list of teams with home arena in form Arena numArenas%
		System.out.print("Assume that each team has as many exceptions as its team number\n");
		int numTeams = 7;
		for (int i = 0; i < numTeams; i++) {
			Team tempTeam = new Team("Team " + (i+1));
			tempTeam.addArena(arenas.get(i % numArenas));    //The home arenas wrap around based on the available ones.
			//Adding as many exceptions as team number
			for (int j = 0; j < i + 1; j++) {
				tempTeam.addException(new Exception(LocalDateTime.now(), LocalDateTime.now()));
			}
			teams.add(tempTeam);
		}
		
		//Making list of timeSlots
		int numTimeSlots = 40;
		for (int i = 0; i < numTimeSlots; i++) {
			//The arenas are wrapped around
			ArrayList<Division> divs = new ArrayList<Division>();
			divs.add(new Division("Div Test"));
			TimeSlot tempTimeSlot = new TimeSlot(LocalDateTime.now(), arenas.get(i % arenas.size()), divs);
			timeSlots.add(tempTimeSlot);
		}
		
		Schedule schedule = new Schedule(teams, timeSlots, actualNumRounds);
		printSchedule(schedule);
	}
	
	/*
	 * Main Function
	 * 
	 * Demo to show the functionality of the scheduling
	 * 
	 */
	public static void main(String[] args) {
		demo();
	}
}
