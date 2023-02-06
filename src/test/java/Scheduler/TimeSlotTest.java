package test.java.Scheduler;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.Scheduler.*; 

public class TimeSlotTest {
    @Test
    public void testTimeSlot() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 12, 0);
        Arena arena = new Arena("...", 0, 0);
        Division division = new Division("East");
        ArrayList<Division> div = new ArrayList<>();
        
        TimeSlot slot = new TimeSlot(start, arena, div);
        
        assertEquals(start, slot.getStartDateTime());
        assertEquals(arena, slot.getArena());
        assertEquals(div, slot.getDivisions());
        assertTrue(slot.isAvailable());
        assertFalse(slot.isSelected());
        
        slot.useTimeslot();
        assertFalse(slot.isAvailable());
        
        slot.selectTimeslot();
        assertTrue(slot.isSelected());
    }
}
