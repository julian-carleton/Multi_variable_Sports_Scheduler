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
	public static int fromEnum(Tier x) {
        switch(x) {
        case ZERO:
            return 0;
        case ONE:
            return 1;
        case TWO:
            return 2;
        case THREE:
            return 3;
        case FOUR:
            return 4;
        
        }
        return -1;
    }
	
}
