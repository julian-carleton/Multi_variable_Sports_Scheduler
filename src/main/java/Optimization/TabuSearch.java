package main.java.Optimization;

import main.java.Scheduler.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Main class of the Optimization package
 *
 * This class implements the algorithm for Tabu Search and returns the best possible
 * corresponding Schedule after optimization process.
 * 
 * @author Quinn Sondermeyer, Julian Obando, Brady Norton
 *
 */
public class TabuSearch {
    // Tabu Variables
    private TabuList tabuList;
    private StopCondition stopCondition;
    private QualityChecker qualityChecker;
    private NeighbourSelector neighbourSelector;

    // Games
    private ArrayList<Game> neighbourSchedule;
    private ArrayList<Game> currentSchedule;
    
    private ArrayList<TimeSlot> timeSlots;

    /**
     * Constructor for TabuSearch
     *
     * @param schedule the Schedule being optimized
     */
    public TabuSearch(ArrayList<Game> games, ArrayList<TimeSlot> timeSlots) {
        tabuList = new TabuList();
        stopCondition = new StopCondition();
        
        
        currentSchedule = games;
        remakeneighbourSchedule();
        
        this.timeSlots = timeSlots;
        
        neighbourSelector = new NeighbourSelector(this.timeSlots, currentSchedule, tabuList);
    }



    /**
     * Run the Tabu Search algorithm on the current schedule
     *
     * @return best schedule after optimization
     * @author Quinn Sondermeyer, Julian Obando
     */
    public ArrayList<Game> optimize() {
        // Iteration counter
    	int x = 100000;
        int iteration = 0;
        while ( x >= iteration) { // stop condition --> iteration and quality check
        	ArrayList<Move> tempMoves;
        	if (iteration <= x/2) {		//Change how games are selected to be more optimal
        		tempMoves = new ArrayList<Move>();
        		neighbourSelector = new NeighbourSelector(this.timeSlots, neighbourSchedule, tabuList);
        		tempMoves.add(neighbourSelector.makeNeighbourScheduleFirst());
        		
        	}else {
        		
        		neighbourSelector = new NeighbourSelector(this.timeSlots, neighbourSchedule, tabuList);
        		tempMoves = neighbourSelector.makeNeighbourScheduleSecond();
        	}
        	
        	ArrayList<Game> tempGames =  new ArrayList<Game>();
        	for (Move m: tempMoves) {
        		Game tempGame = new Game(m.getGame().getHomeTeam(), m.getGame().getAwayTeam());
    			tempGame.setTimeSlot(m.getTimeSlot());
    			int index = currentSchedule.indexOf(m.getGame());
    			this.neighbourSchedule.set(index, tempGame);
        	}
        	
        	if (0 < qualityChecker.compareSchedules(currentSchedule, neighbourSchedule)) {
        		remakeneighbourSchedule();
        		for (Move m: tempMoves) {
        			this.tabuList.addMove(m);
        		}
        		
        	}else if ( 0 == qualityChecker.compareSchedules(currentSchedule, neighbourSchedule)) {
        		remakeneighbourSchedule(); // maybe change later
        		
        	}else {
        		currentSchedule = neighbourSchedule;
        		remakeneighbourSchedule();
        	}
        	
        	if (tempMoves.isEmpty()) { // no more possible moves
        		break;
        	}
        }
        /*
        Algorithm
         */

        return currentSchedule;
    }
    
    
    
    

    private void remakeneighbourSchedule() {
    	neighbourSchedule = new ArrayList<Game>();
        for (Game g : currentSchedule) {
        	neighbourSchedule.add(g);
        }
        
    }
    
    
    
    
    
    
    

    public static void main(String[] args) {
    	
    	

    	ArrayList<Division> test = new ArrayList();
        for (int i = 0; i < 10; i++) {
        	Division temp = new Division( ""+ i);
            test.add(temp);
        }

        ArrayList<Division> test1 = new ArrayList();
        for (int j = 0; j < test.size(); j++) {
            test1.add(test.get(j));
        }
        
        Division div = new Division("200");
        Division d = test1.get(3);
        //d.setName("200");
        test1.set(3, div);

        for (int i = 0; i < 10; i++) {
        	System.out.print(test.get(i).getName() + ", ");
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(test1.get(i).getName()+ ", ");
        }
        
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Prints out the data to be optimized for the given schedule
     *
     * Used for determining which team needs to be optimized first
     *
     * @param schedule the Schedule being analyzed
     */
    public void analyzeSchedule(Schedule schedule) {
        ArrayList<Team> teams = schedule.getTeams();
        ArrayList<Game> games = schedule.getGames();
        ArrayList<TimeSlot> availableTimeslots = new ArrayList<>();
        ArrayList<Game> unscheduledGames = new ArrayList<>();

        String division = teams.get(0).getDivision().getName();
        int tier = teams.get(0).getTier().ordinal();

        System.out.println("\nSchedule for: " + division + " (Tier: " + tier + ")");

        for(TimeSlot t: schedule.getTimeSlots()) {
            if(t.isAvailable()) {
                availableTimeslots.add(t);
            }
        }

        System.out.println("Available Timeslots: " + availableTimeslots.size());

        for(Game g: games) {
            if(g.getTimeSlot() == null) {
                unscheduledGames.add(g);
            }
        }

        System.out.println("Unscheduled Matchups: " + unscheduledGames.size());

        // Iterate over each team in the Schedule
        for(int i = 0; i < teams.size(); i++) {
            String teamName = teams.get(i).getName();
            ArrayList<Game> homeGames = new ArrayList<>();
            ArrayList<Game> awayGames = new ArrayList<>();

            System.out.println("\nTeam: " + teamName);

            // Iterate over each game in the schedule
            for(int j = 0; j < games.size(); ++j) {
                // Iterating over all games with an assigned timeslot and tracking home/away games for team
                if(games.get(j).getTimeSlot() != null) {
                    if(games.get(j).getHomeTeam().getName().equals(teamName)) {
                        homeGames.add(games.get(j));
                    }
                    else if(games.get(j).getAwayTeam().getName().equals(teamName)){
                        awayGames.add(games.get(j));
                    }
                }
            }
            System.out.println("Total Scheduled Games: " + (homeGames.size() + awayGames.size()));
            System.out.println("Home Games: " + homeGames.size());
            System.out.println("Away Games: " + awayGames.size());

            int counter = 0;
            for(Game g: unscheduledGames) {
                if(g.getHomeTeam().getName().equals(teamName) || g.getAwayTeam().getName().equals(teamName)) {
                    counter++;
                }
            }
            System.out.println("Remaining unscheduled games: " + counter);
        }
    }
    
    
    
    
//	/**
//	 * Helper function that creates copy of games
//	 * @param games
//	 * @return new copy of games list
//	 * @author Quinn Sondermeyer
//	 */
//	private ArrayList<Game> copyGames(ArrayList<Game> games) {
//		ArrayList<Game> tempGames = new ArrayList<Game>();
//		for (Game g: games) {
//			Game tempGame = new Game(g.getHomeTeam(), g.getAwayTeam());
//			tempGame.setTimeSlot(g.getTimeSlot());
//			tempGames.add(tempGame);
//		}
//		
//		return tempGames;
//	}
    
    
}
