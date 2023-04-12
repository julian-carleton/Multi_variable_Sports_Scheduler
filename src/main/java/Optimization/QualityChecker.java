package Optimization;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import Scheduler.*;
import Scheduler.Exception;

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


        return penalty;
    }

    /**
     * Checks the quality of a schedule based on:
     *  1. Ratio of scheduled home/away games per team
     *  2. Difference in H/A ratio compared to other teams in Schedule
     */
    public double checkHomeAwayEquality() {
        double penalty = 0;

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
        }

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
     *  1. Teams MSE of the rest days between games for season per team
     */
    public double checkRestDayEquality() {
        double penalty = 0;
        
        //double DaysInSchedule = daysInSchedule();
        //Collections.sort(timeSlots);
        //double DaysInSchedule = (double) ChronoUnit.DAYS.between(timeSlots.get(0).getStartDateTime(), timeSlots.get(timeSlots.size()-1).getStartDateTime());
        ///double idealRestDays = Math.floorDiv((int) DaysInSchedule, games.size()/(teams.size()/2));
        double idealRestDays = 7;   //This value until bug is fixed
        ArrayList<Double> teamsMSE = new ArrayList<Double>();

        // Separating exhibition games
        ArrayList<Game> nonExhibitionGames = new ArrayList<>();
        for(Game g : games) {
            if(!g.getAwayTeam().getName().equals("Exhibition")) {
                nonExhibitionGames.add(g);
            }
        }

        //Obtaining average rest day per team
        for (Team currTeam: teams) {
            if (currTeam.getName().equals("Exhibition")) {
                //Getting the team's timeSlots
                ArrayList<TimeSlot> teamTimeSlots = new ArrayList<TimeSlot>();
                for (Game currGame : nonExhibitionGames) {
                    if (currGame.getAwayTeam().equals(currTeam) || currGame.getHomeTeam().equals(currTeam)) {
                        TimeSlot currTimeSlot = currGame.getTimeSlot();
                        if (currTimeSlot != null) {
                            teamTimeSlots.add(currTimeSlot);
                        }
                    }
                }
                Collections.sort(teamTimeSlots);

                //Getting the days between timeSlots of the team (starting from the first team's timeSlots to the last)
                ArrayList<Double> teamRestDays = new ArrayList<Double>();
                for (int i = 0; i < teamTimeSlots.size() - 1; i++) {
                    TimeSlot currTimeSlot = teamTimeSlots.get(i);
                    TimeSlot nextTimeSlot = teamTimeSlots.get(i + 1);

                    double restDay = (double) ChronoUnit.DAYS.between(currTimeSlot.getStartDateTime(), nextTimeSlot.getStartDateTime());
                    //Checking if there are exceptions in between timeSlots
                    ArrayList<Exception> teamExceptions = currTeam.getExceptions();
                    if (teamExceptions.size() > 0) {
                        for (Exception currException : teamExceptions) {
                            //Checking if the exception starts and ends in the rest period
                            if (currException.compareStart(currTimeSlot.getStartDateTime()) > 0 && currException.compareStart(nextTimeSlot.getStartDateTime()) < 0 &&
                                    currException.compareEnd(currTimeSlot.getStartDateTime()) > 0 && currException.compareEnd(nextTimeSlot.getStartDateTime()) < 0) {
                                //Checking if exception is larger than idealRestDays
                                double daysInException = (double) ChronoUnit.DAYS.between(currException.getStart(), currException.getEnd());
                                if (daysInException > idealRestDays) {
                                    //unscheduledDaysPenalization = StartOfcurrTimeSlotToStartOfException + EndOfExceptionToStartOfNextTimeSlot
                                    double unscheduledDaysPenalization = (double) (ChronoUnit.DAYS.between(currTimeSlot.getStartDateTime(), currException.getStart()) + ChronoUnit.DAYS.between(currException.getEnd(), nextTimeSlot.getStartDateTime()));
                                    restDay = idealRestDays + unscheduledDaysPenalization;
                                    //System.out.println("Exception larger than ideal rest days");
                                    break; //Assume that there are no more exceptions between these timeSlots
                                }
                            }
                        }
                    }
                    teamRestDays.add(restDay);
                }

                //MSE to desired Method
                double error = 0.0;
                for (int j = 0; j < teamRestDays.size(); j++) {
                    error += Math.pow((teamRestDays.get(j) - idealRestDays), 2);
                }
                error = error / (teamRestDays.size());
                error = Math.sqrt(error);
                teamsMSE.add(error);
            }
        }

        //Adding the penalty of each team (Using MSE)
        for (int k = 0; k < teamsMSE.size(); k++) {
        	penalty += teamsMSE.get(k) / teams.size();
        }
        
        if (idealRestDays * 2 < penalty) {
        	//max out to 
        	penalty = 1;
        } else {
        	penalty = penalty / (idealRestDays * 2);
        }
        
        penalty = round(restDaysWeight * (penalty) * 100, 2);
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
        }
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
        quality += checkRestDayEquality();

        return quality;
    }    
    
    /**
     * Get the total number of days for the time slots
     *  
     * @return number of days in for the time slots
     */
    private double daysInSchedule() {
    	
	   	LocalDateTime startDay = null;
    	LocalDateTime endDay = null;
    	if (timeSlots != null) {
    	   	startDay = timeSlots.get(0).getStartDateTime();
        	endDay = timeSlots.get(0).getStartDateTime();
        	for (TimeSlot currTimeSlot: timeSlots) {
        		if (startDay.compareTo(currTimeSlot.getStartDateTime()) > 0) {
        			startDay = currTimeSlot.getStartDateTime();
        		}
        		if (endDay.compareTo(currTimeSlot.getStartDateTime()) < 0) {
        			endDay = currTimeSlot.getStartDateTime();
        		}
        	}
    	}
 
    	return (double) ChronoUnit.DAYS.between(startDay, endDay);
    }
}
