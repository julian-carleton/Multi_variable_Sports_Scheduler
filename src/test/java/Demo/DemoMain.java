package Demo;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ArrayList;

import Excel_Import_Export.*;
import Scheduler.*;
import Scheduler.Exception;

import Demo.DemoMain;

public class DemoMain {
	
	private static final String inputFile = "/Input_Proposal.xlsx";
	
	
    public static void main(String[] args) {
    	InputStream schedulingData = ExcelImport.class.getResourceAsStream(inputFile);
		ExcelImport excelImport = new ExcelImport(schedulingData);

        /*
         * Import sheets
         */
		excelImport.importData();
		CreateDataStrucs strucs = new CreateDataStrucs(excelImport.getSheets());
		
        printCreateDataStruc(strucs);

        Runner runner = new Runner("League", strucs.getDivisions(),strucs.getTimeslots(), strucs.getArenas());
        runner.generateSchedules();


        int t1 = 0;
        int t2 = 0;
        for (Schedule s: runner.getSchedules()) {
            if (!s.getTimeSlots().isEmpty()) {
                s.createSchedule(true);
                for (Game g :s.getGames()) {
                	if(g.getTimeSlot() == null) {
                		t2++;
                	}else {t1++;}
                }
            }
        }
        printTeamTimeSlotSelect(runner);
        System.out.println((float)t1/(t1+t2));
    }

	/**
     * Prints out the objects created from import
     */
    private void demoCreateDataStruc() {
    	InputStream schedulingData = ExcelImport.class.getResourceAsStream(inputFile);
		ExcelImport importData = new ExcelImport(schedulingData);
		importData.importData();
		CreateDataStrucs data = new CreateDataStrucs(importData.getSheets());
        printCreateDataStruc(data);
        
        
        
        Runner runner = new Runner("League", data.getDivisions(),data.getTimeslots(), data.getArenas());
        runner.generateSchedules();
    }

	private static void printCreateDataStruc(CreateDataStrucs data) {
		int count = 0;
		/*
		 * Divisions
		 */
		System.out.println("Disivsions:");
		for (Division d: data.getDivisions()) {
			count++;
			System.out.println(count + "    " + count + ".Name:   " + d.getName());
		}
		
		
		/*
		 * Arenas
		 */
		count = 0;
		System.out.println();
		System.out.println();
		System.out.println("Arenas:");
		for(Arena a:data.getArenas()) {
			count++;
			System.out.println("    " + count + "." + a.getName());
		}
		
		
		/*
		 * Teams
		 */
		count = 0;
		System.out.println();
		System.out.println();
		System.out.println("Teams:");
		for (Team t:data.getTeams()) {
			count++;
			System.out.print("    " +count + ".Name: " + t.getName() + "    ");
			System.out.print("Division: " + t.getDivision().getName() + "    ");
			System.out.println("Tier: " + t.getTier() + "    ");
			
			System.out.println();
			System.out.println("        " + "Exeptions: (DD/MM/YYYY)");
			for (Exception e: t.getExceptions()) {
				System.out.print("           " + "StartDate: " + e.getStart().getDayOfMonth() + "/" + e.getStart().getMonthValue() + "/" + e.getStart().getYear());
				System.out.print("   " + e.getStart().getHour()+ ":" + e.getStart().getMinute());
				System.out.print(",    EndDate: " + e.getEnd().getDayOfMonth() + "/" + e.getEnd().getMonthValue() + "/" + e.getEnd().getYear());
				System.out.println("   " + e.getEnd().getHour()+ ":" + e.getEnd().getMinute());
			}
			
			System.out.println();
			System.out.println("        " + "Home Arena(s):");
			for(Arena a:t.getHomeArenas()) {
				System.out.println("           " + a.getName());
			}
			System.out.println();
			
		}
		
		/*
		 * Time Slots
		 */
		count = 0;
		System.out.println();
		System.out.println();
		System.out.println("Time Slots: ");
		for(TimeSlot t: data.getTimeslots()) {
			count++;
			LocalDateTime time = t.getStartDateTime();
			System.out.print("    " + count + ".Game Day: " + time.getDayOfMonth() + "/" + time.getMonthValue() + "/" + time.getYear());
			System.out.print("   " + time.getHour()+ ":" + time.getMinute());
			//System.out.print(",     " + "Division: " + t.getDivision().getName());
			System.out.println(",    " + "Arena: " + t.getArena().getName() + "    ");
		}

	}
	
	

	/**
	 * Prints the Selection of the time slots and teams
	 * @param runner
	 */
    private static void printTeamTimeSlotSelect(Runner runner) {
		for (Schedule s: runner.getSchedules()) {
			System.out.println();
			System.out.print("Division: " + s.getTeams().get(0).getDivision().getName());
			System.out.println("   Tier: " + s.getTeams().get(0).getTier());
			
			System.out.println();
			System.out.println("     " + "Teams:");
			for(Team t : s.getTeams()) {
				System.out.print("         " + "Name: " + t.getName() + "    ");
				System.out.print("Division: " + t.getDivision().getName() + "    ");
				System.out.println("Tier: " + t.getTier() + "    ");
				
			}
			System.out.println();
			System.out.println("     Time Slots:");
			for (TimeSlot t: s.getTimeSlots()) {
				LocalDateTime time = t.getStartDateTime();
				System.out.print("    " + "Game Day: " + time.getDayOfMonth() + "/" + time.getMonthValue() + "/" + time.getYear());
				System.out.print("   " + time.getHour()+ ":" + time.getMinute());
				//System.out.print(",     " + "Division: " + t.getDivision().getName());
				System.out.println(",    " + "Arena: " + t.getArena().getName() + "    ");
			}
		}
		
	}
    
    


}
