package Optimization;

/**
 * This class is used to determine when the Tabu Search algorithm must stop iterating
 *
 * Stop Condition can be based off either:
 *  1. Quality of the best current Schedule
 *  2. Iteration maximum reached
 */
public class StopCondition {
    private double quialityLimit;
    private int iterationLimit;


    /**
     *  Constructor
     * @param iteration
     * @param quality
     */
    public StopCondition(int iterationLimit, double quialityLimit) {    
    	this.quialityLimit = quialityLimit;
    	this.iterationLimit = iterationLimit;
    }

    public boolean checkCondition(int iteration, double quality) {
        return checkIteration(iteration)&&checkQuality(quality);
    }

    // For both if iteration => iterationLimit ; return true otherwise false
    
    /**
     * compare the iteration return true
     * if iteration is bigger or equal to limit
     * @param iteration
     * @return if bigger or equal to limit than return true
     * 
     */
    public boolean checkIteration(int iteration) {

    	if (iteration >= iterationLimit) {
    		return true;
    	}
    	
        return false;

    }
/**
 * compare the quality return true
 * if quality is bigger or equal to limit
 * @param quality
 * @return if bigger or equal to limit than return true
 */
    public boolean checkQuality(double quality) {
    	
    	if (quality <= quialityLimit) {
    		return true;
    	}
    	return false;

    }
}
