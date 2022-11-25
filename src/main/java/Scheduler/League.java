package main.java.Scheduler;

import java.io.IOException.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.Excel_Import_Export.CreateDataStrucs;
import main.java.Excel_Import_Export.Excel_Import;



/**
 * 
 * @author Quinn Sondermeyer
 *	Main Class that:
 *	 	Imports data, creates Data types, generartes Schedules
 */
public class League {
	private String name;
	private ArrayList<Division> divisions;
	private ArrayList<TimeSlot> timeslots;
	private ArrayList<Arena> arenas;
	private float gamesperweek = 1;


	
	
	
	/**
	 * Default Constructor
	 * @param name
	 * @param divisions
	 * @param timeslots
	 */
	public League(String name, ArrayList<Division> divisions, ArrayList<TimeSlot> timeslots, ArrayList<Arena> arenas) {
		this.name = name;
		this.divisions = divisions;
		this.timeslots = timeslots;
		this.arenas = arenas;
		generateSchedules();
	}
	
	/**
	 * Calls Schedule to generate Schedule
	 */
	private void generateSchedules() {
		ArrayList<Team> tempteam = new ArrayList<Team>();
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		for (Division d: divisions) {
			int numTiers = 0;
			for (Team t: d.getTeams()) {
				if (Tier.fromEnum(t.getTier())> numTiers ) {
					numTiers = Tier.fromEnum(t.getTier());
				}
			}
			arenas = new ArrayList<Arena>();
			tempteam = new ArrayList<Team>();
			for (int i = 0; i > numTiers; i++) {
				for (Team t: d.getTeams()) {
					if (t.getTier() == Tier.fromInteger(i)) {
						tempteam.add(t);
						for (Arena a: arenas) {
							if (a.isInrad(t.getLatitude(),t.getLongitude(),t.getRadius())) {
								arenas.add(a);
							}
						}
					}
				}
				ArrayList<TimeSlot> tempTimeSlots = getTimeslots(tempteam,arenas);
			}
		}
		
	}


	/**
	 * march break
	 * @param tempteam
	 * @return list of time slots for teams
	 */
	private ArrayList<TimeSlot> getTimeslots(ArrayList<Team> teams, ArrayList<Arena> arenas) {
		int timeslotsCount = (int) ((teams.size()/getTotalteams()) *  this.timeslots.size());
		ArrayList<TimeSlot> tempTimeSlots = new ArrayList<TimeSlot>();
		while (timeslotsCount > 0) {
			timeslotsCount--;
			
		}
		return tempTimeSlots;
	}
	
	/**
    * Selects a list of timeslotss for a list of teams
    * @param team
    * @param arenas
    * @return list of timeslots for given team
    */
   public ArrayList<TimeSlot> seletTimeslot(ArrayList<Team> team, ArrayList<Arena> arenas) {
       int slotsPerWeek = (int) ((team.size()/2) * gamesperweek) ;
       LocalDateTime FirstDay = timeslots.get(0).getStartDateTime();
       int weeks = getNumberWeeks();
       for (int i = 0; i > weeks;i++) {
           for (int j = 0; j > slotsPerWeek;j++) {
        	   //Select Slot
           }

       }
       return null;
   }

   private int getNumberWeeks() {
		// TODO Auto-generated method stub
		return 0;
	}

/**
    * Checks if timeslots arena is in the provided list of arenas
    * @param timeslot
    * @param arenas
    * @return true if timeslot's arena is in arena list
    */
   private boolean checkArena(TimeSlot timeslot, ArrayList<Arena> arenas) {
       for (Arena a:arenas) {
           if (a.equals(timeslot.getArena())) {
               return true;
           }
       }
       return false;

   }
	

	/**
	 * returns the total number of teams in the league
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
	 * Main Funtion to of Scheduling Program
	 * Imports data, creates Data types
	 * Creates Leagues
	 * @param args
	 */
	public static void main(String[] args) {
		Excel_Import Import = new Excel_Import();

        // Import sheets
		Import.importTeams();
		Import.importTimeExceptions();
		Import.importDateExceptions();
		Import.importArenas();
		Import.importTimeSlots();
		Import.importHomeArenas();
		//Create Data Types
		CreateDataStrucs strucs = new CreateDataStrucs(Import.getTeams(), Import.getTimeExceptions(),Import.getDateExceptions(), Import.getArenas(),Import.getTimeSlots());
		
		League league = new League("League", strucs.getDivisions(),strucs.getTimeslots(), strucs.getArenas());
		
	}
	
	
	
	
	/**
	 * Adds new division to league
	 * @param division
	 */
	public void addDivision(Division division) {
		divisions.add(division);
	}
	
	
	//Getters and Setters
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the divisions
	 */
	public ArrayList<Division> getDivisions() {
		return divisions;
	}
	
	/**
	 * 
	 * @param divisions
	 */
	public void setDivisions(ArrayList<Division> divisions) {
		this.divisions = divisions;
	}

	
	

}
