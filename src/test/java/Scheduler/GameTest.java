package Scheduler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import Scheduler.Game;
import Scheduler.Team;
import Scheduler.Exception;
import Scheduler.TimeSlot;

public class GameTest {
    private Team homeTeam;
    private Team awayTeam;
    private Game game;

    @Before
    public void setUp() {
        homeTeam = new Team("Home Team");
        awayTeam = new Team("Away Team");
        game = new Game(homeTeam, awayTeam);
    }

    @Test
    public void testGetNumExecpRange() {
        LocalDateTime start = LocalDateTime.of(2022, 01, 01, 8, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 01, 01, 10, 0, 0);
        Exception exception1 = new Exception(LocalDateTime.of(2022, 01, 01, 9, 0, 0), LocalDateTime.of(2022, 01, 01, 10, 0, 0));
        Exception exception2 = new Exception(LocalDateTime.of(2022, 01, 01, 7, 0, 0), LocalDateTime.of(2022, 01, 01, 9, 0, 0));
        homeTeam.addException(exception1);
        awayTeam.addException(exception2);
        int result = game.getNumExecpRange(start, end);
        assertEquals(2, result);
    }

    @After
    public void tearDown() {
        homeTeam = null;
        awayTeam = null;
        game = null;
    }
}