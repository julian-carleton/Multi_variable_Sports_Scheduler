package main.java.Optimization;

import java.util.ArrayList;

import main.java.Scheduler.Game;
import main.java.Scheduler.Schedule;
import main.java.Scheduler.TimeSlot;

/**
 * This class takes a Schedule (or neighboring Schedule) and assigns it a Quality factor
 *
 * This class is a helper for the TabuSearch class when determining whether to update the current best solution
 * or update the Tabu List with the moves from the Schedule
 *
 * Quality of a Schedule is based on:
 *  1. TimeSlot usage
 *  2. Home/Away match equality
 *  3. Average travel distance
 *  4. Rest days between games
 *  5. Equal scheduled matches
 *
 *  NOTE: Methods for checking quality of Schedule should return a Double (quality measured by percentages)
 */
public class QualityChecker {
    // Schedule variables
	private ArrayList<Game> games;
	private ArrayList<TimeSlot> timeSlots;
    
    

    /**
     * Constructor for QualityChecker class
     *
     * @param schedule the Schedule to be checked
     */
    public QualityChecker(ArrayList<Game> games, ArrayList<TimeSlot> timeSlots) {
        this.games = games;
        this.timeSlots = timeSlots;
    }

    /**
     * Default Constructor
     */
    public QualityChecker() {
	}

	/**
     * Checks the quality of a schedule based on:
     *
     * 1. Total unscheduled matchups
     * 2. Total scheduled matchups
     * 3. Total unused timeslots
     *
     */
    public void checkTimeslotUsage(Schedule schedule) {

    }

    /**
     * Checks the quality of a schedule based on:
     *  1. Ratio of scheduled home/away games per team
     *  2. Difference in H/A ratio compared to other teams in Schedule
     */
    public void checkHomeAwayEquality(Schedule schedule) {

    }

    /**
     * Checks the quality of a Schedule based on:
     *  1. Teams avg. travel distance per week
     *  2. Teams avg. travel distance compared to other teams in Schedule
     */
    public void checkAvgTravelDistance(Schedule schedule) {

    }

    /**
     * Checks the quality of a schedule based on:
     *  1. Teams avg. rest days between games for season
     *  2. Teams avg. rest days between away games for season
     *  3. Teams avg. rest days (total) compared to other teams in Schedule
     *  4. Teams avg. rest days (away games) compared to other teams in Schedule
     */
    public void checkRestDayEquality(Schedule schedule) {

    }

    /**
     * Checks the quality of a Schedule based on:
     *  1. Teams total scheduled matches for season compared to other teams in the Schedule
     *  2. Does each team play the same amount of games?
     *
     * @param schedule
     */
    public void checkScheduledMatchEquality(Schedule schedule) {

    }

    /**
     * Compares the current Schedule to a newly generated neighbour Schedule
     *
     * How-to:
     *  - If
     *
     * @param current the current "best" Schedule
     * @param neighbour the newly generated neighbour Schedule
     *
     * @return 1 if current is better, 0 if same, -1 if worse
     */
    public int compareSchedules(ArrayList<Game> current, ArrayList<Game> neighbour) {
        // Compare quality of the two schedules using quality parameters

        // Return the schedule with better overall quality
        // NOTE: At the moment it just returns the current schedule
        return 0;
    }
    
    /**
     * Get weighted Quality of Schedule
     * 
     * @param current 
     * @return return float of weighted Quality
     */
    public float getQuality(ArrayList<Game> current, ArrayList<TimeSlot> timeSlots) {
    	return 0;
    }
    
    
}
