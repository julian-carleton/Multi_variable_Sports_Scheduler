package main.java.Scheduler;

<<<<<<< Updated upstream
=======
import main.java.Excel_Import_Export.ExcelExport;

import java.io.IOException;
>>>>>>> Stashed changes
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
		orderExceptionNumber();
		getListGames();   //Concatenates the rounds that will be used
		assignGames();
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
	private void getListGames() {
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
	 * 
	 */
	private void assignGames() {
		Game currGame;
		int currTimeSlotIndex = 0;
		TimeSlot currTimeSlot = this.timeSlots.get(currTimeSlotIndex);
		for (int i = 0; i < this.games.size(); i++) {
			currGame = this.games.get(i);
			//finding the next Available time S;pt
			while(!currTimeSlot.isAvailable()) {
				currTimeSlotIndex ++;
        
				if (currTimeSlotIndex >= this.timeSlots.size()) {
					//No more timeSlots available
					break;
				}
				currTimeSlot = this.timeSlots.get(currTimeSlotIndex);
			}
			if (exceptionCheck(currGame.getHomeTeam(), currTimeSlot) && exceptionCheck(currGame.getAwayTeam(), currTimeSlot) && currGame.getHomeTeam().isHomeArena(currTimeSlot.getArena())) {
				currGame.setTimeSlot(currTimeSlot);
				currTimeSlot.useTimeslot();     //Update availability of timeSlot
				currTimeSlotIndex = 0;
			} else {
				//TimeSlot was not a match, try next one.
				currTimeSlotIndex ++;
				i--;        //try to set timeSlot for same game.
				if (currTimeSlotIndex >= this.timeSlots.size()) {
					//No more timeSlots available
					i++;
				}else {
					currTimeSlot = this.timeSlots.get(currTimeSlotIndex);
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

	public ArrayList<Game> getGames() {
		return games;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}
	
	public ArrayList<TimeSlot> getTimeSlots() {
		return this.timeSlots;
	}


	/*
	 * Main Function
	 * 
	 */
	public static void main(String[] args) {
		ArrayList<Team> teams = new ArrayList<Team>();
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		int numRounds = 20;
		
		int numTeams = 7;
		for (int i = 0; i < numTeams; i++) {
			Team tempTeam = new Team("Team " + (i+1));
			//Adding as many exceptions as team number
			for (int j = 0; j < i + 1; j++) {
				tempTeam.addException(new Exception(LocalDateTime.now(), LocalDateTime.now()));
			}
			teams.add(tempTeam);
		}
		
		Schedule schedule = new Schedule(teams, timeSlots, numRounds);
		schedule.matchRR();

		boolean even = false;
		int num_teams = teams.size();
		int num_rounds;
		if (num_teams % 2 == 0) {
			num_rounds = num_teams - 1;
			even = true;
		} else {
			num_rounds = num_teams;
		}

		for (int j = 0; j < num_rounds; j++) {
			Round curr_round = schedule.getRounds().get(j);

			for (int i = 0; i <  Math.floorDiv(teams.size(), 2); i++) {
				System.out.print(((Game) curr_round.getGame(i)).getHomeTeam().getName());
				System.out.print(" vs ");
				System.out.print(((Game) curr_round.getGame(i)).getAwayTeam().getName());
				System.out.print("  This match up has (# exceptions):" +curr_round.getGame(i).getExceptionsNumber());
				System.out.print("\n");
			}
			System.out.print("This round has: ");
			System.out.print(((ArrayList<Game>)curr_round.getMatchups()).size());
			System.out.print(" matchups\n\n");
		}
		
		//Ordering rounds based on the number of exceptions
		schedule.orderExceptionNumber();
		System.out.print("\nAfter ordering...\n\n");
		
		for (int j = 0; j < num_rounds; j++) {
			Round curr_round = schedule.getRounds().get(j);

			for (int i = 0; i <  Math.floorDiv(teams.size(), 2); i++) {
				System.out.print(((Game) curr_round.getGame(i)).getHomeTeam().getName());
				System.out.print(" vs ");
				System.out.print(((Game) curr_round.getGame(i)).getAwayTeam().getName());
				System.out.print("  This match up has (# exceptions):" +curr_round.getGame(i).getExceptionsNumber());
				System.out.print("\n");
			}
			System.out.print("This round has: ");
			System.out.print(((ArrayList<Game>)curr_round.getMatchups()).size());
			System.out.print(" matchups\n\n");
		}
	}
}
