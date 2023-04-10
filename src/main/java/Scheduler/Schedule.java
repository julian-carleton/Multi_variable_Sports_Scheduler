package Scheduler;

import Excel_Import_Export.ExcelExport;


import java.io.IOException;
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
	}
	


	/**
	 * Wrapper function for the sequence of calls required to create the schedule
	 *
	 * @author Julian Obando
	 **/
	public void createSchedule() {
		matchRR();
		shuffleRounds();
		//Duplicating for even number of teams
		if (this.teams.size() % 2 == 0) {
			doubleRounds();
		}
		orderExceptionNumber();	//Ordering rounds based on the number of exceptions
		makeListGames();   		//Concatenates the rounds that will be used
		assignGames();
	}
	
	/**
	 * Makes matchups using round robin
	 *
	 * @author Julian Obando
	 **/
	private void matchRR() {
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
				if(even && i == 0 && j%2 == 1) {
					//Swap home and away for special case of last team not playing as home
					//Swaps every second round
					currHomeTeam = teams.get(numTeams - 1);
					currAwayTeam = teams.get(currX); 
				} else if (even && i == 0 && j%2 == 0) {
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
	 * Shuffle the rounds to eliminate home and away teams bias towards the first teams in the list.
	 * How? Interleaves one round from beginning and one from middle of rounds list.
	 * 
	 * @author Julian Obando
	 * */
	private void shuffleRounds() {
		ArrayList<Round> tempRounds = new ArrayList<Round>();
		
		int halfOffset = Math.floorDiv(this.rounds.size(), 2) + 1;
		for (int i = 0; i < Math.floorDiv(this.rounds.size() + 1, 2); i++) {
			tempRounds.add(this.rounds.get(i));
			if (i < halfOffset - 1) {
				tempRounds.add(this.rounds.get(i + halfOffset));
			}
		}
		this.rounds = tempRounds;
	}
	
	/**
	 * Double rounds, where the duplicated have home and away swapped
	 * 
	 * @author Julian Obando
	 * */
	private void doubleRounds() {

		int initialRoundsNum = this.rounds.size(); 
		for (int i = initialRoundsNum - 1; i >= 0; i--) {    //Reverse loop to double from bottom to top
			ArrayList<Game> tempRoundMatchups = this.rounds.get(i).getMatchups();
			int initialMatchupsNumber = tempRoundMatchups.size();
			Round newRound = new Round();
			for (int j = 0; j < initialMatchupsNumber; j++) {
				Game tempGame = tempRoundMatchups.get(j);
				Game newGame = new Game(tempGame.getAwayTeam(), tempGame.getHomeTeam());   //new game with home and away swapped
				newRound.add(newGame);
			}
			this.rounds.add(newRound);
		}
		
		//update number of rounds
		this.numRounds *= 2;
	 }
	
	/**
	 * Orders the rounds so that the games with matchups with most exceptions are ordered first.
	 * 
	 * @author Julian Obando
	 * */
	private void orderExceptionNumber() {
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
					if ( currGame.getHomeTeam().exceptionCheck(currTimeSlot) && (currGame.getAwayTeam().exceptionCheck(currTimeSlot)) && currGame.getHomeTeam().getHomeArenas().contains(currTimeSlot.getArena())) {
						currGame.setTimeSlot(currTimeSlot);
						currTimeSlot.useTimeslot();     //Update availability of timeSlot
						break;		//Go to next game
					}
				}
			}
		}
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

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	public int getActualNumRounds() {
		return actualNumRounds;
	}
	
	public int getNumRounds() {
		return numRounds;
	}

	public String getScheduleName() {
		if(teams.size() > 1) {
			String division = teams.get(1).getDivision().getName();
			int tier = teams.get(1).getTier().ordinal();
			return "Division " + division + " (Tier: " + tier + ")";
		}
		return "All Schedules in League";
	}

	
}


