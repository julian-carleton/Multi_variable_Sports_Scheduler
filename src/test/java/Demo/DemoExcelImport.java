package Demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Excel_Import_Export.ExcelImport;

public class DemoExcelImport {

	
	public static void main(String[] args) throws IOException {
        /*
         * Create test run
         */
    	InputStream schedulingData = ExcelImport.class.getResourceAsStream("/Input_Proposal.xlsx");
        ExcelImport test = new ExcelImport(schedulingData);

        // Import sheets
        test.importData();

         //Print sheet arrays
        printTeamsData(test.getSheets().get(0));
        printTimeExceptionsData(test.getSheets().get(1));
        printDateExceptionsData(test.getSheets().get(2));
        printArenasData(test.getSheets().get(3));
        printTimeSlotsData(test.getSheets().get(4));
        printHomeArenasData(test.getSheets().get(5));
    }
    
    /*
     * Print Data Functions
     */
    
    public static void printTeamsData(ArrayList<ArrayList<Object>> sheetArray) {
        // Size of internal lists
        int totalLength = sheetArray.size();

        for(int i = 0; i < totalLength; i++) {
            System.out.println("\n" + "Team: " + (i+1));
            System.out.println("League: " + sheetArray.get(i).get(0));
            System.out.println("Name: " + sheetArray.get(i).get(1));
            System.out.println("Organization: " + sheetArray.get(2).get(2));
            System.out.println("Division: " + sheetArray.get(i).get(3));
            System.out.println("Tier: " + sheetArray.get(i).get(4));
        }
    }

    public static void printTimeExceptionsData(ArrayList<ArrayList<Object>> sheetArray) {
        for(int i = 0; i < sheetArray.size(); i++) {
            System.out.println("\n" + "Time Exception: " + (i+1));
            System.out.println("Division: " + sheetArray.get(i).get(0));
            System.out.println("Team: " + sheetArray.get(i).get(1));
            System.out.println("Date: " + sheetArray.get(i).get(2));
            System.out.println("Start Time: " + sheetArray.get(i).get(3));
            System.out.println("End Time: " + sheetArray.get(i).get(4));
            System.out.println("Weekly?: " + sheetArray.get(i).get(5));
            System.out.println("Rank: " + sheetArray.get(i).get(6));
        }
    }

    public static void printDateExceptionsData(ArrayList<ArrayList<Object>> sheetArray) {
        for(int i = 0; i < sheetArray.size(); i++) {
            System.out.println("\n" + "Date Exception: " + (i+1));
            System.out.println("Division: " + sheetArray.get(i).get(0));
            System.out.println("Team: " + sheetArray.get(i).get(1));
            System.out.println("Start Date: " + sheetArray.get(i).get(2));
            System.out.println("End Date: " + sheetArray.get(i).get(3));
        }
    }

    public static void printArenasData(ArrayList<ArrayList<Object>> sheetArray) {
        for(int i = 0; i < sheetArray.size(); i++) {
            System.out.println("\n" + "Arena: " + (i+1));
            System.out.println("Name: " + sheetArray.get(i).get(0));
            System.out.println("Location x: " + sheetArray.get(i).get(1));
            System.out.println("Location y: " + sheetArray.get(i).get(2));
        }
    }

    public static void printTimeSlotsData(ArrayList<ArrayList<Object>> sheetArray) {
        for(int i = 0; i < sheetArray.size(); i++) {
            System.out.println("\n" + "Time Slot: " + (i+1));
            System.out.println("Arena Name: " + sheetArray.get(i).get(0));
            System.out.println("Day: " + sheetArray.get(i).get(1));
            System.out.println("Start Time: " + sheetArray.get(i).get(2));
            System.out.println("Preferred Divisions: " + sheetArray.get(i).get(3));
        }
    }

    public static void printHomeArenasData(ArrayList<ArrayList<Object>> sheetArray) {
        for(int i = 0; i < sheetArray.size(); i++) {
            System.out.println("\n" + "Home Arena: " + (i+1));
            System.out.println("Team: " + sheetArray.get(i).get(0));
            System.out.println("Division: " + sheetArray.get(i).get(1));
            System.out.println("Arena Name: " + sheetArray.get(i).get(2));
            System.out.println("Rank: " + sheetArray.get(i).get(3));
        }
    }
    
}
