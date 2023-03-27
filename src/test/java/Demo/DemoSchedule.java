package Demo;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Scheduler.*;
import Scheduler.Exception;

public class DemoSchedule {

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
		
		//Round Robin 
		schedule.matchRR();
		printRounds(schedule);		
		schedule.shuffleRounds();
		//Duplicating for even number of teams
		if (schedule.getTeams().size() % 2 == 0) {
			schedule.doubleRounds();
		}
		//orderExceptionNumber();			//Ordering rounds based on the number of exceptions
		System.out.print("After ordering...\n");
		printRounds(schedule);
		
		//Getting the list of games in schedule
		schedule.makeListGames();	
		System.out.print("Before scheduling...\n");
		printGames(schedule);
		
		System.out.print("\n");
		System.out.print("The timeSlots BEFORE scheduling are as follows: \n");
		printTimeSlots(schedule.getTimeSlots());
		
		//Assigning the Games
		schedule.assignGames();
		
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
