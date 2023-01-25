package main.java.Scheduler;

import java.io.IOException;
import java.io.IOException.*;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

//import main.java.Excel_Import_Export.CreateDataStrucs;
import main.java.Excel_Import_Export.CreateDataStrucs;
import main.java.Excel_Import_Export.ExcelExport;
import main.java.Excel_Import_Export.ExcelImport;
//import main.java.Excel_Import_Export.ExcelImport;



/**
 *  Main Class that:
 *	 	Imports data, creates Data types, generates Schedules
 * 
 * @author Quinn Sondermeyer
 */
public class League {
	private String name;
	private ArrayList<Division> divisions;
	private ArrayList<TimeSlot> timeslots;
	private ArrayList<Arena> arenas;
	private ArrayList<Schedule> schedules;
	private double gamesPerWeek = 1;
	private TimeSlot emptySlot;

	
	/**
	 * Default Constructor
	 * 
	 * @param name
	 * @param divisions
	 * @param timeslots
	 */
	public League(String name, ArrayList<Division> divisions, ArrayList<TimeSlot> timeslots, ArrayList<Arena> arenas) {
		this.name = name;
		this.divisions = divisions;
		this.timeslots = sortTimeSlots(timeslots); // Sorts Time slots by date
		this.arenas = arenas;
		this.schedules = new ArrayList<Schedule>(); 
		Arena a = new Arena("null", 0, 0);
		LocalDateTime time; 
		time = LocalDateTime.of(0, 1, 1, 0, 0);
		this.emptySlot = new TimeSlot(time,a, this.divisions.get(0));

	}
	
	/**
	 * Calls Schedule to generate Schedule
	 */
	public void generateSchedules() {
		ArrayList<Team> tempTeam = new ArrayList<Team>();
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		for (Division d: divisions) {
			int numTiers = 0;
			for (Team t: d.getTeams()) {
				if (Tier.fromEnum(t.getTier())> numTiers ) {
					numTiers = Tier.fromEnum(t.getTier());
				}
			}
			arenas = new ArrayList<Arena>();
			
			for (int i = 0; i <= numTiers; i++) {
				tempTeam = new ArrayList<Team>();
				for (Team t: d.getTeams()) {
					if (t.getTier() == Tier.fromInteger(i)) {
						tempTeam.add(t);
						for (Arena a: t.getHomeArenas()) {
							if (!a.equals(null)) {
								arenas.add(a);
							}
						}
					}
				}

				if (!tempTeam.isEmpty()) {
					ArrayList<TimeSlot> tempTimeSlots = seletTimeslot(tempTeam,arenas);
					this.schedules.add(new Schedule(tempTeam, tempTimeSlots, (int)(getNumberWeeks()*this.gamesPerWeek))); // call schedule
				}
			}
		}
	}

	/**
	 * Generates schedules for each division, for testing only...
	 * It gives the schedules access to all of the timeSlots
	 */
	private void testGeneratedSchedules() {
		Schedule currSchedule;
		for (Division d: divisions) {
			currSchedule = new Schedule(d.getTeams(), this.timeslots, (int)(getNumberWeeks()*this.gamesPerWeek)); 
			currSchedule.createSchedule();
			this.schedules.add(currSchedule);
		}
	}

	
	/**
    * Selects a list of timeslotss for a list of teams
    * 
    * @param team
    * @param arenas
    * @return list of timeslots for given team
    */
   private ArrayList<TimeSlot> seletTimeslot(ArrayList<Team> team, ArrayList<Arena> arenas) {
	   ArrayList<TimeSlot> tempTimeSlots = new ArrayList<TimeSlot>();
	   ArrayList<Arena> tempArenas = new ArrayList<Arena>();
	   
       int slotsPerWeek = (int) ((team.size()/2) * gamesPerWeek) ;
       LocalDateTime curDay = timeslots.get(0).getStartDateTime().minusHours(timeslots.get(0).getStartDateTime().getHour());
       int curSlot = 0;
       int weeks = getNumberWeeks();
       for (int i = 0; i < weeks;i++) {
    	   int availableSlotsPerWk = slotsAvaialblePerWeek(curSlot);
           for (int j = 0; j < slotsPerWeek;j++) {
        	   int point = curSlot+ j*(availableSlotsPerWk/slotsPerWeek);
        	   TimeSlot slot = timeslots.get(point);
        	   
        	   while(!arenas.contains(slot.getArena())| slot.isSelected()) {
        		   if  ( point < (availableSlotsPerWk+curSlot)) {
        			   point++;
        			   slot = timeslots.get(point);

        		   }else {
        			   slot = emptySlot;
        			   break;
        			   }
//        			   return tempTimeSlots; 
//        			   }
        	   }
        	   
    		   slot.selectTimeslot();	// Set as no longer available
    		   tempTimeSlots.add(slot);
			   for(int k = 0 ; k < arenas.size(); k++ ) {
				   if (arenas.get(k).equals(slot.getArena())) {
					   tempArenas.add(arenas.remove(k));
					   break;
				   }
			   }
    		   
        	   
           }
           curDay = curDay.plusDays(7);
           while (timeslots.get(curSlot).getStartDateTime().compareTo(curDay) < 0) {
        	   curSlot++;
        	   if (curSlot >= timeslots.size()) {
        		   break;
        		   //return tempTimeSlots;
        	   }
           }
           for (int k = 0 ; k < tempArenas.size(); k++ ) {
    		   arenas.add(tempArenas.remove(k));
    	   }
           
       }
       return tempTimeSlots;
   }

   /**
    * returns how many time slots there are between curSlot and end of the week
    * 
    * @param curSlot
    * @return int of how many slots there are between curSlot and end of week
    */
   private int slotsAvaialblePerWeek(int curSlot) {
	   LocalDateTime curDay = timeslots.get(curSlot).getStartDateTime();
	   curDay = curDay.plusHours((long) (-curDay.getHour()));
	   LocalDateTime endDay = curDay.plusDays(7);
	   endDay = endDay.plusHours((long) (24.0));
	   int startDay = (int) (curDay.getDayOfYear() + curDay.getYear()*365.25);
	   int count = 0;
	   int offset = 0;
	   while( 0 > timeslots.get(offset).getStartDateTime().compareTo(curDay)) {
		   offset++;
	   }
	   while ( 0 > curDay.compareTo(endDay) ) {
		   while(0 > timeslots.get(count+offset).getStartDateTime().compareTo(curDay)) {
			   count++;
		   }
			curDay = curDay.plusDays(1);
		}
		return count;
	}

   /**
    * Get number of weeks in total for all of time slots
    * 
    * @return int of how many weeks there are
    */
   private int getNumberWeeks() {
	   LocalDateTime firstDay = timeslots.get(0).getStartDateTime();
	   LocalDateTime lastDay = timeslots.get((timeslots.size())-1).getStartDateTime();
	   float firsDayCount =  (float) ( (float) firstDay.getYear() * (365.25)) + firstDay.getDayOfYear();
	   float LastDayCount =  (float) ( (float) lastDay.getYear() * (365.25)) + lastDay.getDayOfYear();
	   int weekCount =  (int) (LastDayCount - firsDayCount)/7;
	   return weekCount;
	}

   /**
    * Checks if timeslots arena is in the provided list of arenas
    * 
    * @param timeSlot
    * @param arenas
    * @return true if timeslot's arena is in arena list
    */
   private boolean checkArena(TimeSlot timeSlot, ArrayList<Arena> arenas) {
       for (Arena a:arenas) {
           if (a.equals(timeSlot.getArena())) {
               return true;
           }
       }
       return false;

   }
	

	/**
	 * Returns the total number of teams in the league
	 * 
	 * @return
	 */
	private int getTotalteams() {
		int count = 0;
		for (Division d: divisions) {
			count += d.getTeams().size();
			}
			
		return count;
	}
	
	
	/**
	 * Source: https://www.geeksforgeeks.org/heap-sort/
	 * HeapSort
	 * 
	 * @param timeSlots1
	 */
	private ArrayList<TimeSlot> sortTimeSlots(ArrayList<TimeSlot> timeSlots1){
        int n = timeSlots1.size();
        ArrayList<TimeSlot> timeSlotsTemp = timeSlots1;
        // Build heap (rearrange list)
        for (int i = n / 2 - 1; i >= 0; i--)
        	timeSlotsTemp = heapify(timeSlots1, n, i);
 
        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            TimeSlot tempSlot = timeSlots1.get(0);
            timeSlots1.set(0, (timeSlots1).get(i));
            timeSlots1.set(i, tempSlot);
 
            // call max heapify on the reduced heap
            timeSlotsTemp = heapify(timeSlots1, i, 0);
        }
		return timeSlotsTemp;
    }
 

	/**
	 * Source: https://www.geeksforgeeks.org/heap-sort/
	 * To heapify a subtree rooted with node i which is
	 * an index in arr[]. n is size of heap
	 * 
	 * @param timeSlots1
	 * @param n
	 * @param i
	 */
	private ArrayList<TimeSlot> heapify(ArrayList<TimeSlot> timeSlots1, int n, int i)
    {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2
        
        // If left child is larger than root
        ;
        if (l < n && (timeSlots1.get(l).getStartDateTime().compareTo(timeSlots1.get(largest).getStartDateTime()) > 0))
            largest = l;
 
        // If right child is larger than largest so far
        if (r < n && (timeSlots1.get(r).getStartDateTime().compareTo(timeSlots1.get(largest).getStartDateTime()) > 0))
            largest = r;
 
        // If largest is not root
        if (largest != i) {
        	TimeSlot tempSlot = timeSlots1.get(i);
            timeSlots1.set(i, (timeSlots1).get(largest));
            timeSlots1.set(largest, tempSlot);
 
            // Recursively heapify the affected sub-tree
            heapify(timeSlots1, n, largest);
        }
		return timeSlots1;
    }
	
	
	
	/**
	 * Adds new division to league
	 * 
	 * @param division
	 */
	public void addDivision(Division division) {
		divisions.add(division);
	}

	/**
	 * Method for finding the unused timeslots during scheduling
	 *
	 * @param timeslots
	 * @return
	 */
	public ArrayList<TimeSlot> findEmptyTimeslots(ArrayList<TimeSlot> timeslots) {
		ArrayList<TimeSlot> unusedTimeslots = new ArrayList<>();

		for(TimeSlot timeSlot: timeslots) {
			if (timeSlot.isAvailable()) {
				unusedTimeslots.add(timeSlot);
			}
		}

		return unusedTimeslots;
	}

	/**
	 * Method for finding the unscheduled games in each of the rounds for a league
	 *
	 * @param rounds the rounds holding all the scheduled and unscheduled games
	 * @return ArrayList of unscheduled games
	 */
	public ArrayList<Game> findUnscheduledGames(ArrayList<Round> rounds) {
		ArrayList<Game> unscheduledGames = new ArrayList<>();

		for(Round r: rounds) {
			for(Game g: r.getMatchups()){
				if(g.getTimeSlot() == null){
					unscheduledGames.add(g);
				}
			}
		}
		for(Game ug: unscheduledGames){
			System.out.println("Home Team: " + ug.getHomeTeam());
			System.out.println("Away Team: " + ug.getAwayTeam());
			System.out.println("Exceptions: " + ug.getExceptionsNumber());
			//System.out.println("Is game scheduled: " + ug.getTimeSlot().isSelected());
		}
		return unscheduledGames;
	}

	/**
	 *
	 * @param game
	 * @param timeslot
	 * @return
	 */
	public boolean canGameBeScheduled(Game game, TimeSlot timeslot){
		ArrayList<Exception> totalExceptions = new ArrayList<>();
		for(int i = 0; i < game.getHomeTeam().getExceptions().size(); i++){
			// Add all home team exceptions
			totalExceptions.add(game.getHomeTeam().getExceptions().get(i));

			// Add all away team exceptions
			totalExceptions.add(game.getAwayTeam().getExceptions().get(i));
		}

		// Check all exceptions to determine if they overlap with timeslot
		for(Exception e: totalExceptions){
			if(e.getStart() == timeslot.getStartDateTime()){
				return false;
			}
		}
		return true;
	}

	/**
	 * Temporary method to check if team can play at an arena as a home team
	 *
	 * Note: this should be changed to use location as a check
	 * @author Brady Norton
	 * @param team
	 * @param arena
	 * @return
	 */
	public boolean canTeamPlay(Team team, Arena arena) {
		/*
		 * Check if given arena is in list of home arenas
		 */
		for(Arena a: team.getHomeArenas()) {
			if(arena != a) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method to match the unscheduled Games to available TimeSlots
	 *
	 * @param games unscheduled games from scheduled Rounds
	 * @param timeslots available TimeSlots for this league
	 */
	public void matchEmptyGamesToTimeslots(ArrayList<Game> games, ArrayList<TimeSlot> timeslots) {
		for(Game g: games){
			for(TimeSlot t: timeslots){
				if(canGameBeScheduled(g, t)){ // checking time exceptions
					if(canTeamPlay(g.getHomeTeam(), t.getArena())) // checking home arena exceptions
						// Set the TimeSlot for the Game
						g.setTimeSlot(t);

						// Set the TimeSlot as unavailable
						t.useTimeslot();

						// Remove Game and Timeslot from the unscheduled lists
						games.remove(g);
						timeslots.remove(t);
				}
			}
		}

		// Print remaining unscheduled data
		System.out.println("Unscheduled games remaining: " + games.size());
		System.out.println("Available TimeSlots remaining: " + timeslots.size());
	}
	

	/**
	 * Main Funtion to of Scheduling Program
	 * Imports data, creates Data types
	 * Creates Leagues
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ExcelImport excelImport = new ExcelImport();

        /*
         * Import sheets
         */
		excelImport.importData();
		
		/*
		 * Create Data Types
		 */
		CreateDataStrucs strucs = new CreateDataStrucs(excelImport.getTeams(), excelImport.getTimeExceptions(),excelImport.getDateExceptions(), excelImport.getArenas(),excelImport.getTimeSlots(),excelImport.getHomeArenas());
		

		League league = new League("League", strucs.getDivisions(),strucs.getTimeslots(), strucs.getArenas());	
		league.generateSchedules();
		
		for (Schedule s: league.getSchedules()) {
			if (!s.getTimeSlots().isEmpty()) {
				s.createSchedule();
			}
		}


		// Excel Export
		ExcelExport export = new ExcelExport(league);
		export.printLeagueData();
		export.exportSchedule();

	}

	/*
	 * Getters and Setters
	 */

	public String getName() {
		return name;
	}

	public ArrayList<Division> getDivisions() {
		return divisions;
	}
	
	public void setDivisions(ArrayList<Division> divisions) {
		this.divisions = divisions;
	}

	public double getGamesPerWeek() {
		return gamesPerWeek;
	}

	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}
	

}
