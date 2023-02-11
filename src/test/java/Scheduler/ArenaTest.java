package Scheduler;

import org.junit.*;
import Scheduler.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ArenaTest {
    @Test
    public void testArena() {
        // create a new Arena object with name ",,,", longitude of 0.0f and latitude of 0.0f
        Arena arena = new Arena("...", 0.0f, 0.0f);
        assertNotNull(arena);
        // check if the name, longitude and latitude getters return the expected values
        assertEquals("...", arena.getName());
        assertEquals(0.0f, arena.getLongitude(), 0.01f);
        assertEquals(0.0f, arena.getLatitude(), 0.01f);
    }
}