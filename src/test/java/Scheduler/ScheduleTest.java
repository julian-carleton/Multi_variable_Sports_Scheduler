package test.java.Scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.Scheduler.*;
import main.java.Scheduler.Exception;

class ScheduleTest {

	private Schedule schedule;
	//private ArrayList<Object> games;
	private ArrayList<Team> teams;
	//private ArrayList<Round> rounds;
	
	//Broken because of teams type.
	
    @BeforeEach
    void setUp() {

    }
	
	@Test
	void testOddRR() {
    teams = new ArrayList<Team>();
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
    	
    schedule = new Schedule(teams, timeSlots, actualNumRounds);
		schedule.matchRR();
		Round curr_round; 
		
		////First Round
		curr_round = schedule.getRounds().get(0);
		//First Match Up
		assertEquals("Team 1", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 7", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 2", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 6", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 3", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 5", curr_round.getGame(2).getAwayTeam().getName());
		//Test number of match ups
		assertEquals(3, (curr_round.getMatchups()).size());
		
		////Second Round
		curr_round = schedule.getRounds().get(1);
		//First Match Up
		assertEquals("Team 2", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 1", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 3", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 7", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 4", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 6", curr_round.getGame(2).getAwayTeam().getName());
		//Test number of match ups
		assertEquals(3, (curr_round.getMatchups()).size());
	}
	
	@Test
	void testEvenRR() {
    teams = new ArrayList<Team>();
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
    	
    schedule = new Schedule(teams, timeSlots, actualNumRounds);
		schedule.matchRR();
		Round curr_round;
		
		////First Round
		curr_round = schedule.getRounds().get(0);
		//First Match Up
		assertEquals("Team 1", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 6", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 2", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 5", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 3", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 4", curr_round.getGame(2).getAwayTeam().getName());
		//Test number of match up
		assertEquals(3, (curr_round.getMatchups()).size());
		
		//Second Round
		curr_round = schedule.getRounds().get(1);
		//First Match Up
		assertEquals("Team 2", curr_round.getGame(0).getHomeTeam().getName());
		assertEquals("Team 6", curr_round.getGame(0).getAwayTeam().getName());
		//Second Match Up
		assertEquals("Team 3", curr_round.getGame(1).getHomeTeam().getName());
		assertEquals("Team 1", curr_round.getGame(1).getAwayTeam().getName());
		//Third Match Up
		assertEquals("Team 4", curr_round.getGame(2).getHomeTeam().getName());
		assertEquals("Team 5", curr_round.getGame(2).getAwayTeam().getName());
		//Test number of match ups
		assertEquals(3, (curr_round.getMatchups()).size());
	}

	@Test
	void testOrderExceptionNumber() {
    	teams = new ArrayList<Team>();
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
    	
    	schedule = new Schedule(teams, timeSlots, actualNumRounds);
		schedule.matchRR();
		
		Round curr_round;
		//Before ordering
		curr_round = schedule.getRounds().get(1);  //Second round, because for first all have the same # exceptions
		assertEquals(8,curr_round.getGame(0).getExceptionsNumber());
		assertEquals(4,curr_round.getGame(1).getExceptionsNumber());
		assertEquals(9,curr_round.getGame(2).getExceptionsNumber());
		
		schedule.orderExceptionNumber();
		//After ordering
		curr_round = schedule.getRounds().get(1);
		assertEquals(9,curr_round.getGame(0).getExceptionsNumber());
		assertEquals(8,curr_round.getGame(1).getExceptionsNumber());
		assertEquals(4,curr_round.getGame(2).getExceptionsNumber());
	}
	
	@Test
	void testGetListGames() {
    	teams = new ArrayList<Team>();
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
    	
    	schedule = new Schedule(teams, timeSlots, actualNumRounds);
		schedule.matchRR();
		schedule.orderExceptionNumber();
		//schedule.makeListGames();
	}
}
