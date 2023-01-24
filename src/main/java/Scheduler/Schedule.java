package main.java.Scheduler;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Creates a set of matchups and a schedule for a set of teams provided
 * 
 * @author Julian Obando, Quinn Sondermeyer
 */
public class Schedule {
	private ArrayList<Round> rounds;    //List of all possible RR matchups in rounds
	private ArrayList<Game> games;		//List of the actual games in the schedule
	private ArrayList<Team> teams;
	private int numRounds;				//Number of different rounds possible
	private int actualNumRounds; 		//Number of rounds in the schedule
	private ArrayList<TimeSlot> timeSlots;
	private boolean printInfo;


	/**
	 * Default constructor
	 * @author Quinn Sondermeyer Julian Obando 
	 * @param teams
	 */
	public Schedule(ArrayList<Team> teams, ArrayList<TimeSlot> timeSlots, int actualNumRounds) {
		this.teams = teams;
		this.rounds = new ArrayList<Round>();
		this.games = new ArrayList<Game>();
		this.timeSlots = timeSlots;
		this.actualNumRounds = actualNumRounds;
		this.printInfo = false;
	}
	
	/**
	 * Constructor with option to print the information of the schedule
	 * @author Quinn Sondermeyer Julian Obando 
	 * @param teams
	 */
	public Schedule(ArrayList<Team> teams, ArrayList<TimeSlot> timeSlots, int actualNumRounds, boolean printInfo) {
		this.teams = teams;
		this.rounds = new ArrayList<Round>();
		this.games = new ArrayList<Game>();
		this.timeSlots = timeSlots;
		this.actualNumRounds = actualNumRounds;
		this.printInfo = printInfo;
	}

	/**
	 * Wrapper function for the sequence of calls required to create the schedule
	 *
	 * @author Julian Obando
	 **/
	public void createSchedule() {
		if (this.printInfo) {
			printSchedule();
		} else {
			matchRR();
			orderExceptionNumber();	//Ordering rounds based on the number of exceptions
			makeListGames();   		//Concatenates the rounds that will be used
			assignGames();
		}
	}
	
	/*
	 * Creates and prints the information of the schedule
	 */
	public void printSchedule() {
		
		//Round Robin 
		matchRR();
		printRounds();		
		orderExceptionNumber();			//Ordering rounds based on the number of exceptions
		System.out.print("After ordering...\n");
		printRounds();
		
		//Getting the list of games in schedule
		makeListGames();	
		System.out.print("Before scheduling...\n");
		printGames();
		
		System.out.print("\n");
		System.out.print("The timeSlots BEFORE scheduling are as follows: \n");
		printTimeSlots();
		
		//Assigning the Games
		assignGames();
		
		//Showing the assigning of games to timeSlots
		System.out.print("After scheduling...\n");
		printGames();
		
		System.out.print("\n");
		System.out.print("The timeSlots AFTER scheduling are as follows: \n");
		printTimeSlots();
	}
	
	/**
	 * Makes mathcups based on teams provided in constructor
	 *
	 * @author Julian Obando
	 **/
	public void matchRR() {
		//Determine if even or odd # teams
		boolean even = false;
		int numTeams = this.teams.size();
		if (numTeams % 2 == 0) {
			this.numRounds = numTeams - 1;
			even = true;
		} else {
			this.numRounds = numTeams;
		}

		for (int j = 0; j < this.numRounds; j++) {
			//Matching a round
			int currX = (0 + j) % this.numRounds;
			int currY = (numTeams - 1 + j) % this.numRounds;
			Round currRound = new Round();
			for (int i = 0; i < Math.floorDiv(numTeams, 2); i++) {
				Team currHomeTeam = teams.get(currX);
				Team currAwayTeam;
				if(even && i == 0) {
					currAwayTeam = teams.get(numTeams - 1);
				} else {
					currAwayTeam = teams.get(currY);
				}
				currRound.add(new Game(currHomeTeam, currAwayTeam));
				currX = (currX + 1) % this.numRounds;
				currY --;
				if (currY < 0) {
					currY += this.numRounds;
				}

			}
			this.rounds.add(currRound);
		}
	}

	/**
	 * Orders the rounds so that the games with matchups with most exceptions are ordered first.
	 * 
	 * @author Julian Obando
	 * */
	public void orderExceptionNumber() {
		ArrayList<Round> newRounds = new ArrayList<Round>();
		
		//Iterating through the rounds
		for (int i = 0; i < this.rounds.size(); i++) {
			//Creating the new round
			Round newRound = new Round();
			Round currRound = rounds.get(i);
			for (int j = 0; j < currRound.getMatchups().size(); j++) {
				Game currGame = currRound.getGame(j);
				int currGameExcpsNumr = currGame.getExceptionsNumber();
				ArrayList<Game> newRoundMatchups = newRound.getMatchups();
				if (newRoundMatchups.size() == 0) {
					newRoundMatchups.add(currGame);
				} else {
					for (int k = 0; k < newRoundMatchups.size(); k++) {
						int nextGameExcpsNum = newRoundMatchups.get(k).getExceptionsNumber();
						if (currGameExcpsNumr > nextGameExcpsNum) {
							newRoundMatchups.add(k, currGame);
							break;
						} else if (k == newRoundMatchups.size() - 1) {
							newRoundMatchups.add(currGame);
							break;
						}
					}
				}
			}
			newRounds.add(newRound);
		}
		this.rounds = newRounds;
	}
	
	/**
	 * Creates a list of all the games to be played in the schedule
	 * 
	 * @author Julian Obando
	 */
	private void makeListGames() {
		int currRoundIndex = 0;
		for (int roundIndex = 0; roundIndex < this.actualNumRounds; roundIndex++) {
			ArrayList<Game> currRoundGames = this.getRounds().get(currRoundIndex).getMatchups();
			int numGames = currRoundGames.size();
			for (int i = 0; i < numGames; i++) {
				Game currGame = currRoundGames.get(i);
				Game newGame = new Game(currGame.getHomeTeam(), currGame.getAwayTeam());    //Making a copy of the game
				this.games.add(newGame);
			}
			currRoundIndex ++;
			currRoundIndex = currRoundIndex % this.numRounds;
		}
	}

	/**
	 * Assigns the games to available timeSlots if the exceptions
	 * of each team allows them and if the timeSlot is at the home arena
	 * of the home team
	 * 
	 * @author Julian Obando
	 */
	private void assignGames() {
		Game currGame;
		TimeSlot currTimeSlot;
		for (int i = 0; i < this.games.size(); i++) {
			currGame = games.get(i);
			for (int j = 0; j < this.timeSlots.size(); j++) {
				currTimeSlot = this.timeSlots.get(j);
				if(currTimeSlot.isAvailable()) {
					if (exceptionCheck(currGame.getHomeTeam(), currTimeSlot) && exceptionCheck(currGame.getAwayTeam(), currTimeSlot) && currGame.getHomeTeam().getHomeArenas().contains(currTimeSlot.getArena())) {
						currGame.setTimeSlot(currTimeSlot);
						currTimeSlot.useTimeslot();     //Update availability of timeSlot
						break;		//Go to next game
					}
				}
			}
		}
	}

	
	
	/**
	 * Checks if team is available 
	 * 
     * @author Faris
     * @param team
     * @param timeSlot
     * @return
     */
    private boolean exceptionCheck(Team team, TimeSlot timeSlot ) {
        int lengthofTimeSlot = 3;
        ArrayList<Exception> exceptions = team.getExceptions();                 // get exceptions
        for (Exception e: exceptions) {                                        // Loop Through all exceptions

            if (e.getStart().compareTo(timeSlot.getStartDateTime()) < 0) {     // check of start is earlier than exception
                if (e.getEnd().compareTo(timeSlot.getStartDateTime()) > 0) { // check of end is later than exception
                    return false;
                }
            }else if(e.getStart().compareTo(timeSlot.getStartDateTime().plusHours(lengthofTimeSlot)) < 0) {// check of start is earlier than exception - given timeslot length
                    return false;

            }
        }
        return true;

    }
	
	/*
	 * Getters and Setters
	 */
	public ArrayList<Round> getRounds() {
		return this.rounds;
	}	

	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public ArrayList<TimeSlot> getTimeSlots() {
		return this.timeSlots;
	}
	
	public ArrayList<Game> getGames() {
		return this.games;
	}

	/*
	 * Print Functions
	 */
	public void printRounds() {
		
		System.out.print("The following matchups are possible for the given teams:\n");
		for (int j = 0; j < this.numRounds; j++) {
			Round curr_round = this.rounds.get(j);

			for (int i = 0; i <  Math.floorDiv(teams.size(), 2); i++) {
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
	
	public void printGames() {
		
		System.out.print("The games for this schedule are:\n");
		for (int j = 0; j < this.games.size(); j++) {
			Game currGame = this.games.get(j);
			System.out.print("	");
			System.out.print(currGame.getHomeTeam().getName());
			System.out.print(" vs ");
			System.out.print(currGame.getAwayTeam().getName());
			System.out.print("	assigned	"+currGame.getTimeSlot());
			System.out.print("\n");
		}
	}
	
	public void printTimeSlots() {
		System.out.print("The time slots are:\n");
		for (int j = 0; j < this.timeSlots.size(); j++) {
			System.out.print("	"+this.timeSlots.get(j) + "\n");
		}
		System.out.print("\n\n");
	}
	
	/*
	 * Demo with simulated data
	 */
	public static void demo() {
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		ArrayList<Team> teams = new ArrayList<Team>();
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		int actualNumRounds = 10;
		
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
		int numTeams = 8;
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
			TimeSlot tempTimeSlot = new TimeSlot(LocalDateTime.now(), arenas.get(i % arenas.size()), new Division("Div Test"));
			timeSlots.add(tempTimeSlot);
		}
		
		Schedule schedule = new Schedule(teams, timeSlots, actualNumRounds, true);
		schedule.createSchedule();
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


