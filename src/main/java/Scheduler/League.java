package main.java.Scheduler;

import java.io.IOException.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

import main.java.Excel_Import_Export.CreateDataStrucs;
import main.java.Excel_Import_Export.ExcelImport;



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
	private double gamesPerWeek = 1;


	
	
	
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
		
		generateSchedules();
	}
	
	/**
	 * Calls Schedule to generate Schedule
	 */
	private void generateSchedules() {
		ArrayList<Team> tempTeam = new ArrayList<Team>();
		ArrayList<Arena> arenas = new ArrayList<Arena>();
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		for (Division d: divisions) {
			int numTiers = 0;
			for (Team t: d.getTeams()) {
				if (Tier.fromEnum(t.getTier())> numTiers ) {
					numTiers = Tier.fromEnum(t.getTier());
				}
			}
			arenas = new ArrayList<Arena>();
			tempTeam = new ArrayList<Team>();
			for (int i = 0; i < numTiers; i++) {
				for (Team t: d.getTeams()) {
					if (t.getTier() == Tier.fromInteger(i)) {
						tempTeam.add(t);
						for (Arena a: arenas) {
							if (a.isInrad(t.getLatitude(),t.getLongitude(),t.getRadius())) {
								arenas.add(a);
							}
						}
					}
				}
				ArrayList<TimeSlot> tempTimeSlots = seletTimeslot(tempTeam,arenas);
				Schedule tempSchedule = new Schedule(tempTeam); // call schedule
				schedules.add(tempSchedule);
			}
		}
		
	}



	
	/**
    * Selects a list of timeslotss for a list of teams
    * 
    * @param team
    * @param arenas
    * @return list of timeslots for given team
    */
   public ArrayList<TimeSlot> seletTimeslot(ArrayList<Team> team, ArrayList<Arena> arenas) {
	   ArrayList<TimeSlot> tempTimeSlots = new ArrayList<TimeSlot>();
	   
       int slotsPerWeek = (int) ((team.size()/2) * gamesPerWeek) ;
       LocalDateTime curDay = timeslots.get(0).getStartDateTime();
       int curSlot = 0;
       int weeks = getNumberWeeks();
       for (int i = 0; i < weeks;i++) {
    	   int availableSlotsPerWk = slotsAvaialblePerWeek(curSlot);
           for (int j = 0; j < slotsPerWeek;j++) {
        	   int point = curSlot+ j*(availableSlotsPerWk/slotsPerWeek);
        	   TimeSlot slot = timeslots.get(point);
        	   
        	   while(!checkArena(slot, arenas)) {
        		   if  ( point < (availableSlotsPerWk+curSlot)) {
        			   point++;
        			   slot = timeslots.get(point);
        		   }else {
        			   return null; // not enoughTimeslots exit to prevent being stuck if there are no good time slots
        		   }
        	   }
        	   slot.useTimeslot();	// Set as no longer available
        	   tempTimeSlots.add(slot);
           }
           curDay.plusDays(7);

       }
       return null;
   }

   /**
    * returns how many time slots there are between curSlot and end of the week
    * 
    * @param curSlot
    * @return int of how many slots there are between curSlot and end of week
    */
   private int slotsAvaialblePerWeek(int curSlot) {
	   LocalDateTime curDay = timeslots.get(curSlot).getStartDateTime();
	   LocalDateTime endDay = curDay;
	   endDay.plusDays(7);
	   int startDay = (int) (curDay.getDayOfYear() + curDay.getYear()*365.25);
	   int count = 0;
	   while ( 0 < curDay.compareTo(endDay) ) {
			count++;
			curDay.plusDays(1);
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
	 * @param arr
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
	 * Main Funtion to of Scheduling Program
	 * Imports data, creates Data types
	 * Creates Leagues
	 * @param args
	 */
	public static void main(String[] args) {
		ExcelImport excelImport = new ExcelImport();

        // Import sheets
		excelImport.importData();
		//Create Data Types
		CreateDataStrucs strucs = new CreateDataStrucs(excelImport.getTeams(), excelImport.getTimeExceptions(),excelImport.getDateExceptions(), excelImport.getArenas(),excelImport.getTimeSlots(),excelImport.getHomeArenas());
		
		League league = new League("League", strucs.getDivisions(),strucs.getTimeslots(), strucs.getArenas());
		
	}
	
	
	
	
	/**
	 * Adds new division to league
	 * 
	 * @param division
	 */
	public void addDivision(Division division) {
		divisions.add(division);
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

	
	

}
