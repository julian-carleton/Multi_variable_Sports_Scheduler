package Scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Excel_Import_Export.*;
import Optimization.*;
import Scheduler.Exception;



/**
 *  Main Class that:
 *	 	Imports data, creates Data types, generates Schedules
 * 
 * @author Quinn Sondermeyer
 */
public class Runner {
	
	private static final String outputFile = "./output/main/";
	private static final String inputFile = "/Input_Proposal.xlsx";
	
	private String name;
	private ArrayList<Division> divisions;
	private ArrayList<TimeSlot> timeslots;
	private ArrayList<Arena> arenas;
	private ArrayList<Schedule> schedules;
	private double gamesPerWeek = 1;
	private TimeSlot emptySlot;
	private long optimizationTime;
	private TabuSearch leagueTS;
	
	

	
	/**
	 * Default Constructor
	 * 
	 * @param name
	 * @param divisions
	 * @param timeslots
	 */
	public Runner(String name, ArrayList<Division> divisions, ArrayList<TimeSlot> timeslots, ArrayList<Arena> arenas) {
		this.name = name;
		this.divisions = divisions;
		this.timeslots = sortTimeSlots(timeslots); // Sorts Time slots by date
		this.arenas = arenas;
		this.schedules = new ArrayList<Schedule>(); 
		
		// Empty slot for creating blank objects
		Arena a = new Arena("null", 0, 0);
		LocalDateTime time; 
		time = LocalDateTime.of(0, 1, 1, 0, 0);
		ArrayList<Division> divs = new ArrayList<Division>();
		divs.add(this.divisions.get(0));
		this.emptySlot = new TimeSlot(time,a, divs);

	}
	
	/**
	 * Calls Schedule to generate Schedules for each of the tier division pairs
	 * 
	 */
	public void generateSchedules() {
		
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		
		for (Division d: divisions) {
			ArrayList<Team> tempTeam = new ArrayList<Team>();
			int numTiers = 0;
			for (Team t: d.getTeams()) {		// Get total tier for each division
				if (Tier.fromEnum(t.getTier())> numTiers ) {
					numTiers = Tier.fromEnum(t.getTier());
				}
			}
			arenas = new ArrayList<Arena>();
			
			for (int i = 0; i <= numTiers; i++) {			// get teams and home arenas from each tier
				tempTeam = new ArrayList<Team>();
				for (Team t: d.getTeams()) {
					if (t.getTier() == Tier.fromInteger(i)) {
						tempTeam.add(t);						//add team to list for Schedule object
						for (Arena a: t.getHomeArenas()) {
							if (!a.equals(null)) {
								arenas.add(a);				// add home arena to maximize the ability to schedule all time slots
							}
						}
					}
				}

				if (!tempTeam.isEmpty() && !tempTeam.get(0).getDivision().getName().equals("ALL")) {
					ArrayList<TimeSlot> tempTimeSlots = seletTimeslot(tempTeam,arenas);// Select subset time slots from full list
					this.schedules.add(new Schedule(tempTeam, tempTimeSlots, (int)(getNumberWeeks()*this.gamesPerWeek))); // call schedule
				}
			}
		}
	}
	
	/**
	 * Calls Schedule to generate Schedules for each of the tier division pairs
	 * 
	 */
	public void generateRankedSchedules() {
		
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		for (Division d: divisions) {
		
			arenas = new ArrayList<Arena>();
			ArrayList<Team> tempTeam = new ArrayList<Team>();
			for (Team t: d.getTeams()) {
				tempTeam.add(t);						//add team to list for Schedule object
				for (Arena a: t.getHomeArenas()) {
					if (!a.equals(null)) {
						arenas.add(a);				// add home arena to maximize the ability to schedule all time slots
					}
				}
			}

			if (!tempTeam.isEmpty() && !tempTeam.get(0).getDivision().getName().equals("ALL")) {
				ArrayList<TimeSlot> tempTimeSlots = seletTimeslot(tempTeam,arenas);// Select subset time slots from full list
				this.schedules.add(new Schedule(tempTeam, tempTimeSlots, (int)(getNumberWeeks()*this.gamesPerWeek))); // call schedule
			}
		}
	}
	
	/**
    * Selects a list of time slots for a list of teams
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
       for (int i = 0; i < weeks;i++) {					// Go through each week for duration of time slots
    	   int availableSlotsPerWk = slotsAvaialblePerWeek(curSlot);	// Determine number of slots needed based on number of teams
           for (int j = 0; j < slotsPerWeek;j++) {			// repeat for each time slot needed
        	   int point = curSlot+ j*(availableSlotsPerWk/slotsPerWeek);
        	   TimeSlot slot = timeslots.get(point);
        	   
        	   while(!arenas.contains(slot.getArena())| slot.isSelected()| !checkDivision(slot, team.get(0).getDivision())) {	// keep moving down until you find an acceptable timeslot or run out of timeslots
        		   if  ( point < (availableSlotsPerWk+curSlot)) {
        			   point++;
        			   slot = timeslots.get(point);		// assign appropriate time slot

        		   }else {
        			   slot = emptySlot;
        			   break;
        			   }
        	   }
        	   
    		   slot.selectTimeslot();	// Set as no longer available
    		   tempTimeSlots.add(slot);
			   for(int k = 0 ; k < arenas.size(); k++ ) {		// add back arenas that were removed when assigning timeslots
				   if (arenas.get(k).equals(slot.getArena())) {
					   tempArenas.add(arenas.remove(k));
					   break;
				   }
			   }
    		   
        	   
           }
           curDay = curDay.plusDays(7);
           while (timeslots.get(curSlot).getStartDateTime().compareTo(curDay) < 0) {	// move current slot to next week
        	   curSlot++;
        	   if (curSlot >= timeslots.size()) {
        		   break;
        	   }
           }
           for (int k = 0 ; k < tempArenas.size(); k++ ) {	// add back removed arenas
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
    * Checks to make sure timeslot is a valid division
    * @param slot
    * @return
    */
   private boolean checkDivision(TimeSlot slot, Division div) {
		return slot.getDivisions().contains(div);
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
 
        if (largest != i) {// If largest is not root
        	TimeSlot tempSlot = timeSlots1.get(i);
            timeSlots1.set(i, (timeSlots1).get(largest));
            timeSlots1.set(largest, tempSlot);
 
            
            heapify(timeSlots1, n, largest); // Recursively heapify the affected sub-tree
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
		InputStream is = League.class.getResourceAsStream(inputFile);
		ExcelImport excelImport = new ExcelImport(League.class.getResourceAsStream(inputFile));

        /*
         * Import sheets
         */
		excelImport.importData();
		
		/*
		 * Create Data Types
		 */
		CreateDataStrucs strucs = new CreateDataStrucs(excelImport.getSheets());

		Runner runner = new Runner("League", strucs.getDivisions(),strucs.getTimeslots(), strucs.getArenas());
		
		ExcelExport export = new ExcelExport(runner);

		ArrayList<TabuSearch> tabuSearches = new ArrayList<TabuSearch>();

		long start = System.currentTimeMillis();
		boolean system;
		double temp = (double) strucs.getModes().get(3).get(1);
		int tierRank = (int) temp;
		if ( tierRank == 0 ) {
			runner.generateRankedSchedules();	
			system = false;
		}
		else {
			runner.generateSchedules();
			system = true;
		}
		
		for (Schedule s: runner.getSchedules()) {
			if (!s.getTimeSlots().isEmpty()) {
				System.out.println("\n--------------------------------------------------------");
				System.out.println("\nCreating Schedule for: " + s.getScheduleName());
				s.createSchedule(system);
				TabuSearch tempTabuSearch = new TabuSearch(s.getGames(),s.getTimeSlots(), s.getTeams());
				System.out.println("\nOptimizing: " + s.getName());
				tempTabuSearch.optimize();
				tempTabuSearch.setScheduleName(s.getName());
				tabuSearches.add(tempTabuSearch);
			}
		}
		int count = 0;
		for(TabuSearch ts : tabuSearches) {
			System.out.println("Index: " + count + " & Schedule: " + ts.getScheduleName());
			count++;
		}
		

		// Optimization for entire League
		TabuSearch entireLeague = new TabuSearch(runner.getGames(),runner.getTimeslots(), runner.getTeams());
		System.out.println("\nLeague Stats: Pre-Optimization");
		export.initializeLeagueStats();

		System.out.println("--------------------------------------------------------");
		System.out.println("Optimizing League");
		System.out.println("--------------------------------------------------------");
		entireLeague.optimize();
		entireLeague.setScheduleName("Entire League");

		long end = System.currentTimeMillis();
		long total = (end - start);
		runner.setOptimizationTime(total);

		// Need to process entireLeague to get the following

		int tsCount = 0;
		for(TabuSearch ts : tabuSearches) {
			// use schedule name to find division/tier
			String divName = runner.getSchedules().get(tsCount).getTeams().get(0).getDivision().getName();
			
			if ( tierRank == 1 ) { String tierName = ts.getScheduleName().substring(8,20); }
			

			boolean divLocated = false;
			int divIndex = 0;
			for(int i = 0; i < runner.getDivisions().size(); ++i) {
				int index = 0;
				if(divName.equals(runner.getDivisions().get(i).getName())) {
					divLocated = true;
					divIndex = i;
				}
			}

			// Iterate over all Attempted Moves
			for(Move m : entireLeague.getAttemptedMoves()) {
				// Checking for Move object in both
				boolean inList = false;
				for(int i = 0; i < ts.getTabuList().size() && !inList; ++i) {
					if(m == ts.getTabuList().get(i)) {
						inList = true;
					}
				}
			}
			tsCount++;
		}

		System.out.println("League Stats: Post-Optimization");
		export.updateLeagueStats();

		// Export Schedules and update Stats
		export.exportSchedule(outputFile);
		export.exportStats(outputFile);
		runner.setLeagueTS(entireLeague);
		export.exportTabuStats(tabuSearches, outputFile);
	}

	

	/*
	 * Getters and Setters
	 */
	
	public ArrayList<Game> getGames() {
		ArrayList<Game> tempGames = new ArrayList<Game>();
		for (Schedule s: this.schedules) {
			for (Game g: s.getGames()) {
				tempGames.add(g);
			}
		}
		return tempGames;
	}
	
	
	public ArrayList<Team> getTeams() {
		ArrayList<Team> tempTeams = new ArrayList<Team>();
		for (Division d: this.divisions) {
			for (Team t: d.getTeams()) {
				if (!(t.getName().equals("Exhibition"))) {
					tempTeams.add(t);
				}
			}
		}
		return tempTeams;
	}
	

	public String getName() {
		return name;
	}

	public ArrayList<Division> getDivisions() {
		return divisions;
	}
	
	public void setDivisions(ArrayList<Division> divisions) {
		this.divisions = divisions;
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public double getGamesPerWeek() {
		return gamesPerWeek;
	}

	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}

	public ArrayList<TimeSlot> getTimeslots() {
		return timeslots;
	}

	
	public String getOptimizationTime() {
		return String.format("%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(optimizationTime),
				TimeUnit.MILLISECONDS.toSeconds(optimizationTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(optimizationTime))
		);
	}

	public void setLeagueTS(TabuSearch ts) {
		leagueTS = ts;
	}

	public TabuSearch getLeagueTS() {
		return leagueTS;
	}
	
	public void setOptimizationTime(long optimizationTime) {
		this.optimizationTime = optimizationTime;
	}

	
	
	

}
