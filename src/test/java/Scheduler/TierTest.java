package test.java.Scheduler;

import static org.junit.Assert.*;
import org.junit.Test;
import main.java.Scheduler.*;

public class TierTest {
    @Test
    public void testFromInteger() {
        assertEquals(Tier.ZERO, Tier.fromInteger(0));
        assertEquals(Tier.ONE, Tier.fromInteger(1));
        assertEquals(Tier.TWO, Tier.fromInteger(2));
        assertEquals(Tier.THREE, Tier.fromInteger(3));
        assertEquals(Tier.FOUR, Tier.fromInteger(4));
    }

    @Test
    public void testFromEnum() {
        assertEquals(0, Tier.fromEnum(Tier.ZERO));
        assertEquals(1, Tier.fromEnum(Tier.ONE));
        assertEquals(2, Tier.fromEnum(Tier.TWO));
        assertEquals(3, Tier.fromEnum(Tier.THREE));
        assertEquals(4, Tier.fromEnum(Tier.FOUR));
    }
}