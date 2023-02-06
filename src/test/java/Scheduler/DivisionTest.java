package Scheduler;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Scheduler.Division;
import Scheduler.Team;

import java.util.ArrayList;

public class DivisionTest {
    Division division;
    Team team1;
    Team team2;

    @Before
    public void setUp() {
        division = new Division("Division 1");
        team1 = new Team("Team A");
        team2 = new Team("Team B");
    }

    @Test
    public void testAddTeam() {
        division.addTeam(team1);
        ArrayList<Team> teams = division.getTeams();
        assertEquals(1, teams.size());
        assertEquals("Team A", teams.get(0).getName());
    }

    @Test
    public void testGetName() {
        assertEquals("Division 1", division.getName());
    }
    @Test
    public void testGetAge() {
        assertEquals(0, division.getAge());
    }
    @Test
    public void testGetTeams() {
        division.addTeam(team1);
        division.addTeam(team2);
        ArrayList<Team> teams = division.getTeams();
        assertEquals(2, teams.size());
        assertEquals("Team A", teams.get(0).getName());
        assertEquals("Team B", teams.get(1).getName());
    }
}