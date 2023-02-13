package Optimization;

import java.util.ArrayList;
import java.util.Random;

import Scheduler.*;

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
	private ArrayList<Game> UnschduledGames; // list of all unscheduled games
	private ArrayList<Game> games; // list of ALL games (scheduled & unscheduled)
	//private ArrayList<TimeSlot> usedTimeslots;
	private ArrayList<TimeSlot> timeSlots; // list of all Timeslots
	private ArrayList<TimeSlot> availalbeTimeslots; // list of available timeslots
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
		ArrayList<Move> moves = new ArrayList<Move>(); // pair of moves

		/**
		Move tempMove = newMove(UnschduledGames, timeSlots); // create new move from unscheduled games and list of ALL timeslots
		if (!(tempMove == null)) { // If Move was created (unscheduled game matched with a timeslot)
			moves.add(tempMove); // add Move to pair of moves
			if (!move.getTimeSlot().isAvailable()) { // check if the timeslot has a Game assigned to it already
				for (Game g: games) { // iterate over full list of games
					if (g.getTimeSlot().equals(move.getTimeSlot())) { // find game that already has the timeslot now being used by Move for different game
						tempMove = new Move(g,availalbeTimeslots.get(selectRandom(availalbeTimeslots.size()))); // create second move using the Game originally in first timeslot and a timeslot from available timeslots
						moves.add(tempMove); // add second move to the pair of moves
					}
				}
			}
		}
		**/
		// Create Move from full list of games and timeslots
		Move move1 = newMove(games, timeSlots);
		Game game1 = move1.getGame();
		TimeSlot timeslot1 = move1.getTimeSlot();

		ArrayList<Game> game2 = new ArrayList<>();

		// check if timeslot1 has a Game
		if(!timeslot1.isAvailable()) {
			for(Game g : games) {
				if(g.getTimeSlot().equals(move1.getTimeSlot())){
					// Try to pair game2 (originally paired with timeslot1) with an availableTimeslot
					game2.add(g);
					Move move2 = newMove(game2, availalbeTimeslots);

					// Check if Move2 can't be created with availableTimeslots
					if(move2 == null) {
						// Try again with all timeslots
						move2 = newMove(game2, timeSlots);

						if(!move2.getTimeSlot().isAvailable()) {
							if(game1.getTimeSlot() == move2.getTimeSlot()) { // swap
								Move swap = new Move(move2.getGame(), game1.getTimeSlot());
								moves.add(swap);
							}
						}
					}
					else { // no conflict with move2
						moves.add(move2);
					}
				}
			}
		}else {
			moves.add(move1);
		}

		return moves; // return list of moves
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
										  !tempMove.getTimeSlot().getDivisions().contains(tempMove.getGame().getHomeTeam().getDivision()) && // Checks timeSlot is the right division
										  !tempMove.getGame().getHomeTeam().isHomeArena(tempMove.getTimeSlot().getArena())) {	
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
     * @param
     * @return return a random object
     * @author Julian Obando
     * */
    private int selectRandom(int size) {
    	if (size > 0) {
    		Random rand = new Random();
            return rand.nextInt(size);
    	}
        return 0;
    }
	
    
    
    
    /*
     * Getters and Setters
     */
    
	public Move getMove() {
		return move;
	}
	
	
	
	
}
