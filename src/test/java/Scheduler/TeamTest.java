package test.java.Scheduler;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import main.java.Scheduler.*;
import main.java.Scheduler.Exception;

public class TeamTest {

    @Test
    public void testDefaultConstructor() {
        Team team = new Team("Team A", 0, 0, Division.NORTH, Tier.PRO);
        assertEquals("Team A", team.getName());
        assertEquals(0, team.getLongitude(), 0.01);
        assertEquals(0, team.getLatitude(), 0.01);
        assertEquals(Division.NORTH, team.getDivision());
        assertEquals(Tier.PRO, team.getTier());
    }

    @Test
    public void testNameOnlyConstructor() {
        Team team = new Team("Team B");
        assertEquals("Team B", team.getName());
        assertEquals(0, team.getExceptions().size());
        assertEquals(0, team.getHomeArenas().size());
    }

    @Test
    public void testAddException() {
        Team team = new Team("Team C");
        Exception exception = new Exception(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        team.addException(exception);
        assertEquals(1, team.getExceptions().size());
        assertEquals(exception, team.getExceptions().get(0));
    }

    @Test
    public void testAddArena() {
        Team team = new Team("Team D");
        Arena arena = new Arena("Arena A", 0, 0);
        team.addArena(arena);
        assertEquals(1, team.getHomeArenas().size());
        assertEquals(arena, team.getHomeArenas().get(0));
    }

    @Test
    public void testIsHomeArena() {
        Team team = new Team("Team E");
        Arena arena = new Arena("Arena B", 0, 0);
        team.addArena(arena);
        assertTrue(team.isHomeArena(arena));
        Arena arena2 = new Arena("Arena C", 0, 0);
        assertFalse(team.isHomeArena(arena2));
    }
}