package Scheduler;

import java.io.IOException;
import java.io.IOException.*;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;




/**
 *  Stores the leagues in the schedule and keeps track of the teams, divisions, and time slots assigned to them
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
	private long optimizationTime;
	private TabuSearch leagueTS;

	
	/**
	 * Default Constructor
	 */
	public League(String name, ArrayList<Division> divisions) {
		this.name = name;
		this.divisions = divisions;
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param divisions
	 * @param timeslots
	 */
	public League(String name, ArrayList<Division> divisions, ArrayList<TimeSlot> timeslots, ArrayList<Arena> arenas) {
		this.name = name;
		this.divisions = divisions;
		this.timeslots = timeslots;
		this.arenas = arenas;

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
		InputStream is = League.class.getResourceAsStream("/Input_Proposal.xlsx");
		ExcelImport excelImport = new ExcelImport(League.class.getResourceAsStream("/Input_Proposal.xlsx"));

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
		ExcelExport export = new ExcelExport(league);

		ArrayList<TabuSearch> tabuSearches = new ArrayList<TabuSearch>();

		long start = System.currentTimeMillis();

		for (Schedule s: league.getSchedules()) {
			if (!s.getTimeSlots().isEmpty()) {
				System.out.println("\n--------------------------------------------------------");
				System.out.println("\nCreating Schedule for: " + s.getScheduleName());
				s.createSchedule();
				TabuSearch tempTabuSearch = new TabuSearch(s.getGames(),s.getTimeSlots(), s.getTeams());
				System.out.println("\nOptimizing: " + tempTabuSearch.getScheduleName());
				tempTabuSearch.optimize();
				tabuSearches.add(tempTabuSearch);
			}
		}
		int count = 0;
		for(TabuSearch ts : tabuSearches) {
			System.out.println("Index: " + count + " & Schedule: " + ts.getScheduleName());
			count++;
		}

		// Optimization for entire League
		TabuSearch entireLeague = new TabuSearch(league.getGames(),league.getTimeslots(), league.getTeams());
		System.out.println("\nLeague Stats: Pre-Optimization");
		export.initializeLeagueStats();

		System.out.println("--------------------------------------------------------");
		System.out.println("Optimizing League");
		System.out.println("--------------------------------------------------------");
		entireLeague.optimize();

		long end = System.currentTimeMillis();
		long total = (end - start);
		league.setOptimizationTime(total);

		// Need to process entireLeague to get the following

		int tsCount = 0;
		for(TabuSearch ts : tabuSearches) {
			// use schedule name to find division/tier
			String divName = ts.getScheduleName().substring(9,11);
			String tierName = ts.getScheduleName().substring(8,20);

			boolean divLocated = false;
			int divIndex = 0;
			for(int i = 0; i < league.getDivisions().size(); ++i) {
				int index = 0;
				if(divName.equals(league.getDivisions().get(i).getName())) {
					divLocated = true;
					divIndex = i;
				}
			}
			Division tmpDiv = league.getDivisions().get(divIndex);
			Tier tmpTier = tmpDiv.getTeams().get(tmpDiv.getTeams().size() - 1).getTier();

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
		export.exportSchedule();
		export.exportStats();
		league.setLeagueTS(entireLeague);
		export.exportTabuStats(tabuSearches);
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
				tempTeams.add(t);
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

	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}

	public ArrayList<TimeSlot> getTimeslots() {
		return timeslots;
	}

	public void setOptimizationTime(long optimizationTime) {
		this.optimizationTime = optimizationTime;
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
}
