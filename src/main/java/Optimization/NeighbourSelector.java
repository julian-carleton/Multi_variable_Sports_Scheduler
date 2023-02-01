package Optimization;

/**
 * NeighbourSelector is a class used to choose the next possible Move during optimization of a Schedule
 *
 * Should have access to:
 *  1. List of scheduled games
 *  2. List of unscheduled games
 *  3. List of available timeslots
 *  4. List of unavailable timeslots
 *  5. Tabu List
 *
 * Functionality (Unguided):
 *  1. Randomly select a scheduled Game and find all possible TimeSlots for it
 *  2. Randomly select a TimeSlot to move the Game to
 *  3. Create Move object -> (Game, TimeSlot)
 *  4. Return Move object
 *
 * Functionality (Guided):
 *  1. Use quality checker to find lowest scoring quality check
 *  2. Select Game that if moved could increase quality determined in step 1
 *  3. Find all valid TimeSlots for moving Game to
 *  4. Create Move object using valid TimeSlot
 */
public class NeighbourSelector {

}
