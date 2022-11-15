package Scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.Scheduler.*;
import main.java.Scheduler.Round;
import main.java.Scheduler.Game;

class ScheduleTest {

	private Schedule schedule;
	//private ArrayList<Object> games;
	private ArrayList<Object> teams;
	//private ArrayList<Round> rounds;
	
    @BeforeEach
    void setUp() {

    }
	
	@Test
	void testOddRR() {
    	teams = new ArrayList<Object>();
    	teams.add("Team 1");
    	teams.add("Team 2");
    	teams.add("Team 3");
    	teams.add("Team 4");
    	teams.add("Team 5");
    	teams.add("Team 6");
    	teams.add("Team 7");
    	schedule = new Schedule(teams);
		schedule.matchRR();
		Round curr_round = schedule.getRounds().get(0);
		//First Match Up
		assertEquals("Team 1", ((Game) curr_round.getGame(0)).getHomeTeam());
		assertEquals("Team 7", ((Game) curr_round.getGame(0)).getAwayTeam());
		//Second Match Up
		assertEquals("Team 2", ((Game) curr_round.getGame(1)).getHomeTeam());
		assertEquals("Team 6", ((Game) curr_round.getGame(1)).getAwayTeam());
		//Third Match Up
		assertEquals("Team 3", ((Game) curr_round.getGame(2)).getHomeTeam());
		assertEquals("Team 5", ((Game) curr_round.getGame(2)).getAwayTeam());
		//Test number of match ups
		assertEquals(3, ((ArrayList<Object>)curr_round.getMatchups()).size());
	}
	
	@Test
	void testEvenRR() {
    	teams = new ArrayList<Object>();
    	teams.add("Team 1");
    	teams.add("Team 2");
    	teams.add("Team 3");
    	teams.add("Team 4");
    	teams.add("Team 5");
    	teams.add("Team 6");
    	schedule = new Schedule(teams);
		schedule.matchRR();
		Round curr_round = schedule.getRounds().get(0);
		//First Match Up
		assertEquals("Team 1", ((Game) curr_round.getGame(0)).getHomeTeam());
		assertEquals("Team 6", ((Game) curr_round.getGame(0)).getAwayTeam());
		//Second Match Up
		assertEquals("Team 2", ((Game) curr_round.getGame(1)).getHomeTeam());
		assertEquals("Team 5", ((Game) curr_round.getGame(1)).getAwayTeam());
		//Third Match Up
		assertEquals("Team 3", ((Game) curr_round.getGame(2)).getHomeTeam());
		assertEquals("Team 4", ((Game) curr_round.getGame(2)).getAwayTeam());
		//Test number of match ups
		assertEquals(3, ((ArrayList<Object>)curr_round.getMatchups()).size());
	}

}
