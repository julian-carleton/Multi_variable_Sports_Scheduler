package main.java.Scheduler;

import java.io.IOException.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
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
		this.timeslots = sortTimeSlots(timeslots); // Sorts Time slots by date
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
       LocalDateTime curDay = timeslots.get(0).getStartDateTime();
       int weeks = getNumberWeeks();
       for (int i = 0; i < weeks;i++) {
           for (int j = 0; j > slotsPerWeek;j++) {
        	   //Select Slot
           }
           curDay.plusDays(7);

       }
       return null;
   }

   private int getNumberWeeks() {
	   LocalDateTime firstDay = timeslots.get(0).getStartDateTime();
	   LocalDateTime LastDay = timeslots.get((timeslots.size())-1).getStartDateTime();
	   float firsDayCount =  (float) ( (float) firstDay.getYear() * (365.25)) + firstDay.getDayOfYear();
	   float LastDayCount =  (float) ( (float) LastDay.getYear() * (365.25)) + LastDay.getDayOfYear();
	   int weekcount =  (int) (LastDayCount - firsDayCount)/7;
	   return weekcount;
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
	 * Source: https://www.geeksforgeeks.org/heap-sort/
	 * HeapSort
	 * @param arr
	 */
	private ArrayList<TimeSlot> sortTimeSlots(ArrayList<TimeSlot> timeslots1){
        int N = timeslots1.size();
        ArrayList<TimeSlot> timeslotstemp = timeslots1;
        // Build heap (rearrange list)
        for (int i = N / 2 - 1; i >= 0; i--)
        	timeslotstemp = heapify(timeslots1, N, i);
 
        // One by one extract an element from heap
        for (int i = N - 1; i > 0; i--) {
            // Move current root to end
            TimeSlot tempSlot = timeslots1.get(0);
            timeslots1.set(0, (timeslots1).get(i));
            timeslots1.set(i, tempSlot);
 
            // call max heapify on the reduced heap
            timeslotstemp = heapify(timeslots1, i, 0);
        }
		return timeslotstemp;
    }
 

	/**
	 * Source: https://www.geeksforgeeks.org/heap-sort/
	 * To heapify a subtree rooted with node i which is
	 * an index in arr[]. n is size of heap
	 * @param timeslots1
	 * @param N
	 * @param i
	 */
	private ArrayList<TimeSlot> heapify(ArrayList<TimeSlot> timeslots1, int N, int i)
    {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2
        
        // If left child is larger than root
        ;
        if (l < N && (timeslots1.get(l).getStartDateTime().compareTo(timeslots1.get(largest).getStartDateTime()) > 0))
            largest = l;
 
        // If right child is larger than largest so far
        if (r < N && (timeslots1.get(r).getStartDateTime().compareTo(timeslots1.get(largest).getStartDateTime()) > 0))
            largest = r;
 
        // If largest is not root
        if (largest != i) {
        	TimeSlot tempSlot = timeslots1.get(i);
            timeslots1.set(i, (timeslots1).get(largest));
            timeslots1.set(largest, tempSlot);
 
            // Recursively heapify the affected sub-tree
            heapify(timeslots1, N, largest);
        }
		return timeslots1;
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
		Import.ExtraInfo();
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
