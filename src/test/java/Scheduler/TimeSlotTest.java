package Scheduler;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import Scheduler.*;

public class TimeSlotTest {
    @Test
    public void testTimeSlot() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 12, 0);
        Arena arena = new Arena("...", 0, 0);
        Division division = new Division("East");
        
        TimeSlot slot = new TimeSlot(start, arena, division);
        
        assertEquals(start, slot.getStartDateTime());
        assertEquals(arena, slot.getArena());
        assertEquals(division, slot.getDivision());
        assertTrue(slot.isAvailable());
        assertFalse(slot.isSelected());
        
        slot.useTimeslot();
        assertFalse(slot.isAvailable());
        
        slot.selectTimeslot();
        assertTrue(slot.isSelected());
    }
}
