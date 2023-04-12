package Optimization;

import Scheduler.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.math3.util.Precision.round;

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
    // Stop Conditions
	private static final int iterationLimit = 5000;
	private static final float quialityLimit = 100;

    // Tabu Search Variables
    private TabuList tabuList;
    private StopCondition stopCondition;
    private QualityChecker qualityChecker;
    private NeighbourSelector neighbourSelector;

    // Schedule Lists
    private ArrayList<Game> neighbourSchedule;
    private ArrayList<Game> currentSchedule;
    private ArrayList<Team> teams;
    private ArrayList<TimeSlot> timeSlots;

    // Stats
    private String scheduleName;
    private double initialQuality;
    private double finalQuality;
    private ArrayList<Move> acceptedMoves;
    private ArrayList<Move> attemptedMoves;
    private int initialGamesScheduled;
    private int finalGamesScheduled;
    private int remainingGames;
    private long executionTime;


    /**
     * TabuSearch constructor
     *
     * @param games the list of games for the Schedule being optimized
     * @param timeSlots the list of TimeSlots this Schedule is allotted
     * @param teams the list of Teams that follow this Schedule
     */
    public TabuSearch(ArrayList<Game> games, ArrayList<TimeSlot> timeSlots, ArrayList<Team> teams) {
        tabuList = new TabuList();
        stopCondition = new StopCondition(iterationLimit,quialityLimit);
        qualityChecker = new QualityChecker(games,timeSlots,teams);
        
        currentSchedule = games;
        remakeneighbourSchedule();
        
        this.timeSlots = timeSlots;
        this.teams = teams;
        acceptedMoves = new ArrayList<>();
        attemptedMoves = new ArrayList<>();
    }

    /**
     * Run the Tabu Search algorithm on the current schedule
     *
     * @return best schedule after optimization
     * @author Quinn Sondermeyer, Julian Obando
     */
    public ArrayList<Game> optimize() {
        // Iteration counter
    	int x = iterationLimit;
        int iteration = 0;

        // Stats
        setInitialGamesScheduled();
        initialQuality = qualityChecker.getQuality();
        ArrayList<Game> aviableGames = getAvailableGames(currentSchedule);


        // Begin Timer
        long startTime = System.currentTimeMillis();

        while (stopCondition.checkCondition(iteration, qualityChecker.getQuality())) { // stop condition --> iteration and quality check
        	ArrayList<Move> tempMoves;
        	if (aviableGames.size() / this.currentSchedule.size() > 0.1) {		//Change how games are selected to be more optimal
        		tempMoves = new ArrayList<Move>();
        		neighbourSelector = new NeighbourSelector(timeSlots, neighbourSchedule, tabuList);
        		tempMoves.add(neighbourSelector.makeNeighbourScheduleFirst()); // Move: unscheduled game -> available ts

                if (tempMoves.contains(null)) {  // no more possible moves
        			neighbourSelector = new NeighbourSelector(timeSlots, neighbourSchedule, tabuList);
            		tempMoves = neighbourSelector.makeNeighbourScheduleSecond(); // Move: unscheduled game -> all ts
        		}

        	}else {
        		neighbourSelector = new NeighbourSelector(timeSlots, neighbourSchedule, tabuList);
        		tempMoves = neighbourSelector.makeNeighbourScheduleSecond();
        	}

        	if (!(tempMoves.contains(null)||tempMoves.isEmpty())) {  // no more possible moves
	        	for (Move m: tempMoves) {
	        		Game tempGame = new Game(m.getGame().getHomeTeam(), m.getGame().getAwayTeam());
	    			tempGame.setTimeSlot(m.getTimeSlot());
	    			int index = currentSchedule.indexOf(m.getGame());
	    			this.neighbourSchedule.set(index, tempGame);
                    attemptedMoves.add(m);
	        	}
	        	
	        	if (0 < compareSchedules()) {
	        		remakeneighbourSchedule();
	        		// Quality isn't improved -> add Moves to TabuList
                    for (Move m: tempMoves) {
                        if(!tabuList.isTabu(m)){
                            tabuList.addMove(m);
                        }
	        		}
	        		
	        	}else if ( 0 == compareSchedules()) {
	        		remakeneighbourSchedule(); // maybe change later
                    // Quality isn't improved -> add Moves to TabuList
                    for (Move m: tempMoves) {
                        if(!tabuList.isTabu(m)){
                            tabuList.addMove(m);
                        }
                    }
	        		
	        	}else {
                    // Store these moves for stats
                    acceptedMoves.addAll(tempMoves);

                    for (Move m: tempMoves) {
	        			if (m.getGame().getTimeSlot() != null) {
	        				m.getGame().getTimeSlot().freeUptimeslot();
	        				//for alternate version of selecting moves
	        				m.getGame().getTimeSlot().deSelectTimeslot();
	        			}
	        			
	        			//for alternate version of selecting moves:
	        			m.getTimeSlot().deSelectTimeslot();
	        		}

	        		for (Move m: tempMoves) {
	        			m.getGame().setTimeSlot(m.getTimeSlot());
	        			m.getTimeSlot().useTimeslot();
	        		}
	        		remakeneighbourSchedule();
	        	}
        	}
        	iteration++;
        }
        // Stop timer
        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        setExecutionTime(total);

        // Stats
        setFinalGamesScheduled();
        setFinalQuality();
        remainingGames = getAvailableGames(currentSchedule).size();

        return currentSchedule;
    }

    /**
     * Optimize() helper method for populating a Neighbouring Schedule
     */
    private void remakeneighbourSchedule() {
    	neighbourSchedule = new ArrayList<Game>();
        for (Game g : currentSchedule) {
        	neighbourSchedule.add(g);
        }
    }

    /**
     * Compares the quality of the neighbouring Schedule against the quality of the current best Schedule
     *
     * @return true if neighbouring Schedule has better quality than current Schedule, false if not
     */
    public int compareSchedules() {
        QualityChecker checkCurrent = new QualityChecker(currentSchedule, timeSlots, teams);
        QualityChecker checkNeighbour = new QualityChecker(neighbourSchedule, timeSlots, teams);

        double currentPenalty = checkCurrent.getQuality();
        double neighbourPenalty = checkNeighbour.getQuality();

        // Current schedule has lower penalty value than neighbour Schedule
        if(currentPenalty < neighbourPenalty) {
            return 1;
        }
        // Current Schedule has larger penalty value than neighbour Schedule
        else if(currentPenalty > neighbourPenalty) {
            return -1;
        }

        // In the case that they're equal penalty: we stick with the current best Schedule
        return 0;
    }
    
    
    /**
	 * Gets list games that have not been assigned
	 * @param games
	 * @return returns sublist of available games
	 * @author Quinn Sondermeyer
	 */
	private ArrayList<Game> getAvailableGames(ArrayList<Game> games) {
		ArrayList<Game> tempGames = new ArrayList<Game>();
		for (Game g: games) {
			if ((g.getTimeSlot() == null)) {
				tempGames.add(g);
			}
		}
		return tempGames;
	}

    /**
     * Main method for TabuSearch
     *
     * NOTE: May be safe to remove(?)
     *
     * @param args
     */
    public static void main(String[] args) {

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

    /*
    Getters and Setters
     */
    
    public String getScheduleName() {
        return scheduleName;
    }

    public ArrayList<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // Scheduled Games Stats
    public void setInitialGamesScheduled() {
        // Initialize scheduled game counter
        int counter = 0;

        // Iterate over games & count amount of games w/TimeSlot
        for(Game g : currentSchedule) {
            if(g.getTimeSlot() != null) {
                counter++;
            }
        }
        initialGamesScheduled = counter;
    }

    public int getInitialGamesScheduled() {
        return initialGamesScheduled;
    }

    public void setFinalGamesScheduled() {
        // Initialize scheduled game counter
        int counter = 0;

        // Iterate over games & count all games w/TimeSlot
        for(Game g : currentSchedule) {
            if(g.getTimeSlot() != null) {
                counter++;
            }
        }
        finalGamesScheduled = counter;
    }

    public int getFinalGamesScheduled() {
        return finalGamesScheduled;
    }

    public ArrayList<Game> getCurrentSchedule() {
        return currentSchedule;
    }

    public int getRemainingGames() {
        return remainingGames;
    }

    // TimeSlot stats
    public int getRemainingTimeSlots() {
        int counter = 0;
        for(TimeSlot t : timeSlots) {
            if(t.isAvailable()) {
                counter++;
            }
        }
        return counter;
    }

    public double getTimeSlotUsage() {
        int usedTimeslots = 0;
        int availableTimeslots = 0;

        for(TimeSlot t: timeSlots) {
            if(t.isAvailable()) {
                availableTimeslots++;
            }
            else{
                usedTimeslots++;
            }
        }
        int scheduleRate = (usedTimeslots / timeSlots.size()) * 100;
        return round(scheduleRate,2);
    }

    // Quality Stats
    public double getInitialQuality() {
        if(initialQuality == 0.0) {
            initialQuality = qualityChecker.getQuality();
        }
        return initialQuality;
    }

    public void setInitialQuality() {

    }

    public double getFinalQuality() {
        return finalQuality;
    }

    public void setFinalQuality() {
        finalQuality = qualityChecker.getQuality();
    }

    public ArrayList<Move> getAcceptedMoves() {
        return acceptedMoves;
    }

    public ArrayList<Move> getTabuList() {
        return tabuList.getTabuMoves();
    }

    public ArrayList<Move> getAttemptedMoves() {
        return attemptedMoves;
    }

    // Execution Time Stat
    public void setExecutionTime(long time) {
        executionTime = time;
    }

    public String executionTimeToString() {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(executionTime);
        if(seconds < 60L) {
            return seconds + " sec";
        }

        return String.format("%d min, %d sec, %d ms",
                TimeUnit.MILLISECONDS.toMinutes(executionTime),
                TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime)),
                executionTime
        );
    }

    public int getIterationLimit() {
        return iterationLimit;
    }

    public float getQualityThreshold() {
        return quialityLimit;
    }

    public int getNewMoveLimit() {
        return neighbourSelector.getNewMoveLimit();
    }

	public void setScheduleName(String name) {
		this.scheduleName = name;
		
	}
}
