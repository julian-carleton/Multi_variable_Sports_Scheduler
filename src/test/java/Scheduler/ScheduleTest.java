package Scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Scheduler.*;
import Scheduler.Exception;

class ScheduleTest {
	
	/*
	 *  Tests the team matching for a odd number of teams
	 */
	@Test
	void testCreateScheduleOdd() {
		ArrayList<Team> teams = new ArrayList<Team>();
	    ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
	    int actualNumRounds = 1;
	    	
		int numTeams = 7;
		//Creating teams name Team # from 1 to numTeams
		for (int i = 0; i < numTeams; i++) {
			Team tempTeam = new Team("Team " + (i+1));
			//Adding as many exceptions as team number
			for (int j = 0; j < i + 1; j++) {
				tempTeam.addException(new Exception(LocalDateTime.now(), LocalDateTime.now()));
			}
			teams.add(tempTeam);
		}
	    	
		Schedule schedule = new Schedule(teams, timeSlots, actualNumRounds);
		schedule.createSchedule();
		
		////First Round
		Round curr_round = schedule.getRounds().get(0);
		//First Match Up
		assertEquals("Team 1", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 7", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 2", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 6", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 3", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 5", curr_round.getGame(2).getAwayTeam().getName());
		
		////Second Round
		curr_round = schedule.getRounds().get(1);
		//First Match Up
		assertEquals("Team 5", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 4", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 6", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 3", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 7", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 2", curr_round.getGame(2).getAwayTeam().getName());
		
		
		//Test number of match ups
		assertEquals(3, (curr_round.getMatchups()).size());
		
		//Test number of possible round combinations (Same as number of teams)
		assertEquals(numTeams, schedule.getNumRounds()); 
		
		//Test number of games in the schedule
		assertEquals(Math.floorDiv(numTeams*actualNumRounds,2), schedule.getGames().size());
	}
	
	/*
	 *  Tests the team matching for an even number of teams
	 */
	@Test
	void testCreateScheduleEven() {
		ArrayList<Team> teams = new ArrayList<Team>();
	    ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
	    int actualNumRounds = 1;
	    	
		int numTeams = 6;
		//Creating teams name Team # from 1 to numTeams
		for (int i = 0; i < numTeams; i++) {
			Team tempTeam = new Team("Team " + (i+1));
			//Adding as many exceptions as team number
			for (int j = 0; j < i + 1; j++) {
				tempTeam.addException(new Exception(LocalDateTime.now(), LocalDateTime.now()));
			}
			teams.add(tempTeam);
		}
	    	
		Schedule schedule = new Schedule(teams, timeSlots, actualNumRounds);
		schedule.createSchedule();
		
		////First Round
		Round curr_round = schedule.getRounds().get(0);
		//First Match Up
		assertEquals("Team 1", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 6", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 2", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 5", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 3", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 4", curr_round.getGame(2).getAwayTeam().getName());
		
		////Second Round
		curr_round = schedule.getRounds().get(1);
		//First Match Up
		assertEquals("Team 6", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 4", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 5", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 3", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 1", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 2", curr_round.getGame(2).getAwayTeam().getName());
		
		
		//Test number of match ups
		assertEquals(3, (curr_round.getMatchups()).size());
		
		//Test number of possible round combinations
		//The multiplication by 2 is because of doubling
		assertEquals((numTeams - 1) * 2, schedule.getNumRounds()); 
		
		//Test number of games in the schedule
		assertEquals(Math.floorDiv(numTeams*actualNumRounds,2), schedule.getGames().size());
	}
}
