package Optimization;

import java.util.ArrayList;

/**
 * This class implements a data structure to track Tabu moves made on a Schedule
 * during the Tabu Search
 *
 * Should be implemented to keep track of all the forbidden moves on a specific
 * Schedule object. Each Schedule undergoing optimization must have its own TL.
 */
public class TabuList {
    // Variables
    private ArrayList<Move> tabuList;
    private int tabuTenure;

    /**
     * Constructor for TabuList
     * Initializes the HashMap for keeping track of forbidden moves
     */
    public TabuList() {
        tabuList = new ArrayList<>();
    }

    /**
     * Adds a "move" containing a Game and TimeSlot to the Tabu List
     *
     * @param move the Move to be added to Tabu List
     */
    public void addMove(Move move) {
        tabuList.add(move);
    }

    /**
     * Checks if a move is in the Tabu List and deemed tabu (forbidden)
     *
     * @param move the move to check
     * @return true if move Tabu, false if not
     */
    public boolean isTabu(Move move) {
        for(Move m: tabuList) {
            if(m.equals(move)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the size of the tabu list
     *
     * @return the number of moves in the tabu list as an int
     */
    public int getTabuSize() {
        return this.tabuList.size();
    }
}
