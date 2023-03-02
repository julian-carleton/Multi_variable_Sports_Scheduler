package Optimization;


import java.util.ArrayList;

import Scheduler.*;


import java.util.ArrayList;

import static org.apache.commons.math3.util.Precision.round;


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
    private ArrayList<Game> games; // Games in Schedule
    private ArrayList<TimeSlot> timeSlots; // Timeslots allotted to Schedule
    private ArrayList<Team> teams; // Teams in Schedule

    // Quality Attribute Weights
    private double timeSlotUsageWeight = 3.0;
    private double homeAwayImbalanceWeight = 1.0;
    private double travelDifferenceWeight = 1.0;
    private double restDaysWeight = 2.0;
    private double gameCountWeight = 3.0; 


    /**
     * Constructor for QualityChecker class
     *
     */

    public QualityChecker(ArrayList<Game> games, ArrayList<TimeSlot> timeSlots, ArrayList<Team> teams) {
        this.games = games;
        this.timeSlots = timeSlots;
        this.teams = teams;
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
    public double checkTimeslotUsage() {
        int usedTimeslots = 0;
        int totalTimeslots = timeSlots.size();

        // Iterate over allotted TimeSlots and count how many are unused
        for(TimeSlot t : timeSlots) {
            if(!t.isAvailable()) {
                usedTimeslots++;
            }
        }

        double usage = (double) usedTimeslots / totalTimeslots;
        double penalty = round(timeSlotUsageWeight * (1.0 - usage) * 100, 2);

//        System.out.println("\n--- Checking TimeSlot Usage Quality ---");
//        System.out.println("Total Allotted TimeSlots: " + totalTimeslots);
//        System.out.println("Total TimeSlots Used: " + usedTimeslots);
//        System.out.println("TimeSlot usage penalty: " + penalty);

        return penalty;
    }

    /**
     * Checks the quality of a schedule based on:
     *  1. Ratio of scheduled home/away games per team
     *  2. Difference in H/A ratio compared to other teams in Schedule
     */
    public double checkHomeAwayEquality() {
        double penalty = 0;

//        System.out.println("\n--- Checking Home/Away Imbalance Quality ---");

        // Iterate over all teams in Schedule
        for(Team t : teams) {
            int homeGames = 0;
            int awayGames = 0;
            double tempPenalty = 0;

            // Find amount of Home/Away games for Team
            for (Game g : games) {
                if (g.getTimeSlot() != null) {
                    if(g.getHomeTeam() == t) {
                        homeGames++;
                    }
                    else if(g.getAwayTeam() == t) {
                        awayGames++;
                    }
                }
            }

            int totalGames = homeGames + awayGames;
            int imbalance = Math.abs(homeGames - (totalGames / 2));

            tempPenalty = homeAwayImbalanceWeight * imbalance;
            penalty += tempPenalty;

//            System.out.println("Team: " + t.getName());
//            System.out.println("Home Games: " + homeGames);
//            System.out.println("Away Games: " + awayGames);
//            System.out.println("Imbalance: " + imbalance);
//            System.out.println("Penalty applied to " + t.getName() + " : " + tempPenalty +"\n");
        }

//        System.out.println("Total H/A Equality Penalty: " + penalty);
        return penalty;
    }

    /**
     * Checks the quality of a Schedule based on:
     *  1. Teams avg. travel distance per week
     *  2. Teams avg. travel distance compared to other teams in Schedule
     */
    public double checkAvgTravelDistance() {
        double penalty = 0;

        return penalty;
    }

    /**
     * Checks the quality of a schedule based on:
     *  1. Teams avg. rest days between games for season
     *  2. Teams avg. rest days between away games for season
     *  3. Teams avg. rest days (total) compared to other teams in Schedule
     *  4. Teams avg. rest days (away games) compared to other teams in Schedule
     */
    public double checkRestDayEquality() {
        double penalty = 0;

        return penalty;
    }

    /**
     * Checks the quality of a Schedule based on:
     *  1. Teams total scheduled matches for season compared to other teams in the Schedule
     *  2. Does each team play the same amount of games?
     *
     */
    public double checkScheduledMatchEquality() {
        double penalty = 0;
        int desiredGames = (games.size() / teams.size()) * 2;

//        System.out.println("\n--- Checking Total Scheduled Matches Quality ---");

        // Iterate over each team in schedule
        for(Team t : teams) {
            int gamesScheduled = 0;
            double tempPenalty = 0;

            // Count total amount of scheduled games for team
            for(Game g : games) {
                if(g.getTimeSlot() != null) {
                    if(g.getHomeTeam().equals(t) || g.getAwayTeam().equals(t)) {
                        gamesScheduled++;
                    }
                }
            }

            int imbalance = Math.abs(gamesScheduled - desiredGames);
            tempPenalty = gameCountWeight * imbalance;
            penalty += tempPenalty;

//            System.out.println("Total Scheduled Games for " + t.getName() + ": " + gamesScheduled);
//            System.out.println("Desired Games: " + desiredGames);
//            System.out.println("Imbalance for " + t.getName() + ": " + tempPenalty + "\n");
        }
//
//        System.out.println("Match Equality Penalty: " + penalty);
        return penalty;
    }

    
    /**
     * Get weighted Quality of Schedule
     * 
     * @param current 
     * @return the best Schedule after comparison
     */
    public double getQuality() {
        double quality = 0;

        quality += checkTimeslotUsage();
        quality += checkHomeAwayEquality();
        quality += checkScheduledMatchEquality();

//        System.out.println("\n--- Total Quality of Schedule ---");
//        System.out.println("Schedule Penalty: " + round(quality, 2));
        return quality;
    }
    
    
}
