package main.java.Optimization;

import java.util.ArrayList;
import java.util.Random;

import main.java.Scheduler.Game;
import main.java.Scheduler.League;
import main.java.Scheduler.Schedule;
import main.java.Scheduler.TimeSlot;

/**
 * NeighbourSelector is a class used to choose the next possible Move during optimization of a Schedule
 *
 * Should have access to:
 *  1. List of scheduled games
 *  2. List of unscheduled games
 *  3. List of available timeslots
 *  4. List of unavailable timeslots
 *  5. Tabu List
 *
 * Functionality (Unguided):
 *  1. Randomly select a scheduled Game and find all possible TimeSlots for it
 *  2. Randomly select a TimeSlot to move the Game to
 *  3. Create Move object -> (Game, TimeSlot)
 *  4. Return Move object
 *
 * Functionality (Guided):
 *  1. Use quality checker to find lowest scoring quality check
 *  2. Select Game that if moved could increase quality determined in step 1
 *  3. Find all valid TimeSlots for moving Game to
 *  4. Create Move object using valid TimeSlot
 *  
 *  @author Quinn Sondermeyer, Julian Obando
 *  
 */
public class NeighbourSelector {
	private Schedule schedule;
	private ArrayList<Game> UnschduledGames;
	private ArrayList<Game> games;
	//private ArrayList<TimeSlot> usedTimeslots;
	private ArrayList<TimeSlot> timeSlots;
	private ArrayList<TimeSlot> availalbeTimeslots;
	private Move move;
	private TabuList tabuList;
	
	
	
	/**
	 * 
	 */
	public NeighbourSelector() {
		
	}
	
	/**
	 * 
	 * @param timeSlots
	 * @param games
	 * @author Quinn Sondermeyer, Julian Obando
	 */
	public NeighbourSelector(ArrayList<TimeSlot> timeSlots, ArrayList<Game> games, TabuList tabuList) {
		this.games = games;
		this.timeSlots = timeSlots;
		availalbeTimeslots = getAvailableGames(timeSlots);
		UnschduledGames = UnschduledGames(this.games);
		this.tabuList = tabuList;
		// Check Timelslot to make sure that you do not double book
		
		
	}
	
	/**
	 * Makes Move with non Scheduled games and available timelsots
	 * @return move made 
	 * @author Quinn Sondermeyer, Julian Obando
	 */
	public Move makeNeighbourScheduleFirst() {
		move = newMove(UnschduledGames, availalbeTimeslots);		
		return move;
	}
	
	
	/**
	 * Makes Move with non Scheduled games and all timeslots 
	 * 
	 * @return move made
	 * 
	 * @author Quinn Sondermeyer, Julian Obando
	 */
	public ArrayList<Move> makeNeighbourScheduleSecond() {
		
		ArrayList<Move> moves = new ArrayList<Move>();
		Move tempMove = newMove(UnschduledGames, timeSlots);
		if (!(tempMove == null)) {
			moves.add(tempMove);
			if (!move.getTimeSlot().isAvailable()) {
				for (Game g: games) {
					if (g.getTimeSlot().equals(move.getTimeSlot())) {
						tempMove = new Move(g,availalbeTimeslots.get(selectRandom(availalbeTimeslots.size())));
						moves.add(tempMove);
					}
				}
			}
		}
		
		return moves;
	}





	/**
	 * Create a move object with random move
	 * @return
	 * 
	 * @author Quinn Sondermeyer, Julian Obando
	 */
	private Move newMove(ArrayList<Game> games, ArrayList<TimeSlot> timeslotList) {
		ArrayList<Object> tempGames= new ArrayList<Object>();
		for (Game g: games) {
			tempGames.add(g);
		}
		Move tempMove = new Move( games.get(selectRandom(games.size())), timeslotList.get(selectRandom(timeslotList.size())));
		int count = 0;
		while(tabuList.isTabu(tempMove)&& !tempMove.getGame().getHomeTeam().exceptionCheck(tempMove.getTimeSlot()) &&	// Checks exceptions for Home team
										  !tempMove.getGame().getAwayTeam().exceptionCheck(tempMove.getTimeSlot()) &&	// Checks exceptions for Away team
										  !tempMove.getTimeSlot().getDivisions().contains(tempMove.getGame().getHomeTeam().getDivision())) {	// Checks timeSlot is the right division
			tempMove = new Move(games.get(selectRandom(games.size())), timeslotList.get(selectRandom(timeslotList.size()))); // 
			if (count > 1000000000) {
				System.out.println("No more possible moves");
				tempMove = null;
				break;
			}
			
		}
		return tempMove;
	}
	
	
	


	/**
	 * Gets list Games that have not been assigned
	 * @param newGames
	 * @return returns sublist of available games
	 * @author Quinn Sondermeyer
	 */
	private ArrayList<Game> UnschduledGames(ArrayList<Game> newGames) {
		ArrayList<Game> tempGames =  new ArrayList<Game>();
		for (Game g: newGames) {
			if (g.getTimeSlot() == null) {
				tempGames.add(g);
			}
		}
		return tempGames;
	}


	/**
	 * Gets list Timeslots that have not been assigned
	 * @param timeslots
	 * @return returns sublist of available timeslots
	 * @author Quinn Sondermeyer
	 */
	private ArrayList<TimeSlot> getAvailableGames(ArrayList<TimeSlot> timeslots) {
		ArrayList<TimeSlot> tempTimeSlot = new ArrayList<TimeSlot>();
		for (TimeSlot t: timeslots) {
			if (t.isAvailable()) {
				tempTimeSlot.add(t);
			}
		}
		return tempTimeSlot;
	}




	
	/**
     * Returns a random object from the a list of objects
     * 
     * @param object list, list of objects
     * @return return a random object
     * @author Julian Obando
     * */
    private int selectRandom(int size) {
        Random rand = new Random();
        return rand.nextInt(size);
    }
	
    
    
    
    /*
     * Getters and Setters
     */
    
	public Move getMove() {
		return move;
	}
	
	
	
	
}
