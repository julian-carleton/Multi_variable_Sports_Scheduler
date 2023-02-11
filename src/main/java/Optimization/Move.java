package Optimization;

import Scheduler.Game;
import Scheduler.TimeSlot;

/**
 *  Move class represents the moving of a Game object to a TimeSlot object
 *
 */
public class Move {
    // Variables
    Game game;
    TimeSlot timeSlot;

    /**
     * Constructor for creating a Move to be used on a Schedule during optimization
     *
     * @param game the game to be moved
     * @param timeSlot the TimeSlot the Game is being moved to
     */
    public Move(Game game, TimeSlot timeSlot) {
        this.game = game;
        this.timeSlot = timeSlot;
    }

    /**
     * Getter for the Game being moved
     *
     * @return Game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * Getter for TimeSlot to be used for Game
     *
     * @return TimeSlot object
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
}
