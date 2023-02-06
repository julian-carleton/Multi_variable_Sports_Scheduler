package Scheduler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import Scheduler.*;

public class RoundTest {
    private Round round;
    private Team homeTeam;
    private Team awayTeam;
    private Game game;

    @Before
    public void setUp() {
        round = new Round();
        homeTeam = new Team("Home");
        awayTeam = new Team("Away");
        game = new Game(homeTeam, awayTeam);
    }

    @Test
    public void testAdd() {
        round.add(game);
        assertEquals(1, round.getMatchups().size());
        assertEquals(game, round.getMatchups().get(0));
    }

    @Test
    public void testGetGame() {
        round.add(game);
        assertEquals(game, round.getGame(0));
    }

    @After
    public void tearDown() {
        round = null;
        homeTeam = null;
        awayTeam = null;
        game = null;
    }
}
