package main.java.Scheduler;

public enum Tier {
	ZERO, ONE, TWO, THREE, FOUR;
	
	public static Tier fromInteger(int x) {
        switch(x) {
        case 0:
            return ZERO;
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 3:
            return THREE;
        case 4:
            return FOUR;
        
        }
        return null;
    }
	
}
