package Scheduler;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import Scheduler.Exception;

public class ExceptionTest {
    Exception exception;
    @Before
    public void setUp() {
        exception = new Exception(LocalDateTime.of(2022,01,01,8,30,0),LocalDateTime.of(2022,01,01,9,30,0));
    }
    @Test
    public void testCompareStart() {
        LocalDateTime compare = LocalDateTime.of(2022,01,01,8,0,0);
        int result = exception.compareStart(compare);
        assertTrue(result > 0);
    }
    @Test
    public void testCompareEnd() {
        LocalDateTime compare = LocalDateTime.of(2022,01,01,10,0,0);
        int result = exception.compareEnd(compare);
        assertTrue(result < 0);
    }
    @Test
    public void testGetStart() {
        assertEquals(LocalDateTime.of(2022,01,01,8,30,0), exception.getStart());
    }
    @Test
    public void testGetEnd() {
        assertEquals(LocalDateTime.of(2022,01,01,9,30,0), exception.getEnd());
    }
}