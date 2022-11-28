package main.java.Excel_Import_Export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Brady Norton
 *
 */
public class Excel_Import {
    // 2D Arraylists for each sheet
    List<List<String>> teams = new ArrayList<List<String>>();
    List<List<String>> timeExceptions =  new ArrayList<List<String>>();
    List<List<String>> dateExceptions = new ArrayList<List<String>>();
    List<List<String>> arenas = new ArrayList<List<String>>();
    List<List<String>> timeSlots = new ArrayList<List<String>>();
    List<List<String>> homeArenas = new ArrayList<List<String>>();

    // Excel Variables
    XSSFWorkbook wb;
    XSSFSheet teamsSheet;
    XSSFSheet timeExceptionsSheet;
    XSSFSheet dateExceptionsSheet;
    XSSFSheet arenasSheet;
    XSSFSheet timeSlotsSheet;
    XSSFSheet homeArenasSheet;

    /**
     * Constructor for Excel_Import class
     */
    public Excel_Import() {
        try {
            // InputStream
            InputStream schedulingData = Excel_Import.class.getResourceAsStream("/Input_Proposal.xlsx");

            // Input_Proposal Workbook
            XSSFWorkbook wb = new XSSFWorkbook(schedulingData);

            // Sheets
            teamsSheet = wb.getSheetAt(0);
            timeExceptionsSheet = wb.getSheetAt(1);
            dateExceptionsSheet = wb.getSheetAt(2);
            arenasSheet = wb.getSheetAt(3);
            timeSlotsSheet = wb.getSheetAt(4);
            homeArenasSheet = wb.getSheetAt(5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Import data from sheet 0
     */
    public void importTeams() {
        // Initialize Internal Arrays
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> league = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> org = new ArrayList<>();
        ArrayList<String> division = new ArrayList<>();
        ArrayList<String> tier = new ArrayList<>();
        // ArrayList<String> locationX = new ArrayList<>();
        // ArrayList<String> locationY = new ArrayList<>();

        // Add internal arrays to 2D array
        teams.add(league);
        teams.add(name);
        teams.add(org);
        teams.add(division);
        teams.add(tier);
        // teams.add(locationX);
        // teams.add(locationY);

        // Iterator
        Iterator<Row> rowIterator = teamsSheet.rowIterator();

        // Iterate over data
        while (rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();

            // Iterate over cells in row
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                // Next cell
                Cell cell = cellIterator.next();

                if (row.getRowNum() == 0) {
                    headers.add(cell.getStringCellValue());
                } else {
                    if (cell.getColumnIndex() == 0) {
                        //league.add(cell.getStringCellValue());
                        teams.get(0).add(cell.getStringCellValue     ());
                    }
                    else if (cell.getColumnIndex() == 1) {
                        teams.get(1).add(cell.getStringCellValue());
                    }
                    else if (cell.getColumnIndex() == 2) {
                        teams.get(2).add(cell.getStringCellValue());
                    }
                    else if (cell.getColumnIndex() == 3) {
                        teams.get(3).add(cell.getStringCellValue());
                    }
                    else if (cell.getColumnIndex() == 4) {
                        teams.get(4).add(String.valueOf(cell.getNumericCellValue()));
                    }
                }
            }
        }
    }

    /**
     * Import data from sheet 1 (Time Exceptions)
     */
    public void importTimeExceptions() {
        // Initialize Internal Arrays
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> division = new ArrayList<>();
        ArrayList<String> team = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<String> startTime = new ArrayList<>();
        ArrayList<String> endTime = new ArrayList<>();
        ArrayList<String> rank = new ArrayList<>();
        ArrayList<String> weekly = new ArrayList<>();
        ArrayList<String> day = new ArrayList<>();

        // Add internal arrays to sheet array
        timeExceptions.add(division);
        timeExceptions.add(team);
        timeExceptions.add(date);
        timeExceptions.add(startTime);
        timeExceptions.add(endTime);
        timeExceptions.add(rank);
        timeExceptions.add(weekly);
        timeExceptions.add(day);

        // Iterator
        Iterator<Row> rowIterator = timeExceptionsSheet.rowIterator();

        // Iterate over data
        while(rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(row.getRowNum() == 0) {
                    headers.add(cell.getStringCellValue());
                }
                else {
                    if(cell.getColumnIndex() == 0) {
                        timeExceptions.get(0).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 1) {
                        timeExceptions.get(1).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 2) {
                        timeExceptions.get(2).add(String.valueOf(cell.getDateCellValue()));
                    }
                    else if(cell.getColumnIndex() == 3) {
                        String format = String.valueOf(cell.getLocalDateTimeCellValue());
                        timeExceptions.get(3).add(format.substring(format.length() - 5));
                    }
                    else if(cell.getColumnIndex() == 4) {
                        String format = String.valueOf(cell.getLocalDateTimeCellValue());
                        timeExceptions.get(4).add(format.substring(format.length() - 5));
                    }
                    else if(cell.getColumnIndex() == 5) {
                        timeExceptions.get(5).add(String.valueOf(cell.getStringCellValue()));
                    }
                    else if(cell.getColumnIndex() == 6) {
                        timeExceptions.get(6).add(String.valueOf(cell.getNumericCellValue()));
                    }
                    else if(cell.getColumnIndex() == 7) {
                        timeExceptions.get(7).add(cell.getStringCellValue());
                    }
                }
            }
        }
    }

    /**
     * Import data from sheet 2 (Date Exceptions)
     */
    public void importDateExceptions() {
        // Initialize internal arrays
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> division = new ArrayList<>();
        ArrayList<String> team = new ArrayList<>();
        ArrayList<String> startDate = new ArrayList<>();
        ArrayList<String> endDate = new ArrayList<>();

        // Add internal arrays to sheet array
        dateExceptions.add(division);
        dateExceptions.add(team);
        dateExceptions.add(startDate);
        dateExceptions.add(endDate);

        // Iterator
        Iterator<Row> rowIterator = dateExceptionsSheet.rowIterator();

        // Iterate over data
        while(rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(row.getRowNum() != 0) {
                    if(cell.getColumnIndex() == 0) {
                        dateExceptions.get(0).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 1) {
                        dateExceptions.get(1).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 2) {
                        String date = String.valueOf(cell.getDateCellValue());
                        date = date.substring(0, 10) + date.substring(date.length() - 5);
                        dateExceptions.get(2).add(date);
                    }
                    else if(cell.getColumnIndex() == 3) {
                        String date = String.valueOf(cell.getDateCellValue());
                        date = date.substring(0, 10) + date.substring(date.length() - 5);
                        dateExceptions.get(3).add(date);
                    }
                }
            }
        }
    }

    /**
     * Import data from sheet 3 (Arenas)
     */
    public void importArenas() {
        // Initialize internal arrays
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> locationX = new ArrayList<>();
        ArrayList<String> locationY = new ArrayList<>();

        // Add internal arrays to sheet array
        arenas.add(name);
        arenas.add(locationX);
        arenas.add(locationY);

        // Iterator
        Iterator<Row> rowIterator = arenasSheet.rowIterator();

        // Iterate over data
        while(rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(row.getRowNum() != 0) {
                    if(cell.getColumnIndex() == 0) {
                        arenas.get(0).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 1) {
                        String test = String.valueOf(cell.getNumericCellValue());
                        arenas.get(1).add(String.valueOf(cell.getNumericCellValue()));
                        System.out.println("Added: " + test);
                    }
                    else if(cell.getColumnIndex() == 2) {
                        String test = String.valueOf(cell.getNumericCellValue());
                        arenas.get(2).add(String.valueOf(cell.getNumericCellValue()));
                        System.out.println("Added: " + test);
                    }
                }
            }
        }

    }

    /**
     * Import data from sheet 4 (Arena Slots)
     */
    public void importTimeSlots() {
        // Initialize internal arrays
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> arenaName = new ArrayList<>();
        ArrayList<String> day = new ArrayList<>();
        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> preferredDivisions = new ArrayList<>();

        // Add internal arrays to sheet array
        timeSlots.add(arenaName);
        timeSlots.add(day);
        timeSlots.add(start);
        timeSlots.add(preferredDivisions);

        // Iterator
        Iterator<Row> rowIterator = timeSlotsSheet.rowIterator();

        // Iterate over data
        while(rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(row.getRowNum() != 0) {
                    if(cell.getColumnIndex() == 0) {
                        timeSlots.get(0).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 1) {
                        String date = String.valueOf(cell.getDateCellValue());
                        if(date.length() > 4) {
                            date = date.substring(4,10) + date.substring(date.length() - 5);
                        }
                        //timeSlots.get(1).add(String.valueOf(date.length()));
                        timeSlots.get(1).add(date);
                    }
                    else if(cell.getColumnIndex() == 2) {
                        String format = String.valueOf(cell.getLocalDateTimeCellValue());
                        if(format.length() > 4) {
                            timeSlots.get(2).add(format.substring(format.length() - 5));
                        }
                        else {
                            timeSlots.get(2).add(format.substring(format.length()-4));
                        }
                    }
                    else if(cell.getColumnIndex() == 3) {
                        timeSlots.get(3).add(cell.getStringCellValue());
                    }
                }
            }
        }
    }

    /**
     * Import data from sheet 5 (Home Arena)
     */
    public void importHomeArenas() {
        // Internal Arrays
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<String> team = new ArrayList<>();
        ArrayList<String> division = new ArrayList<>();
        ArrayList<String> arenaName = new ArrayList<>();
        ArrayList<String> rank = new ArrayList<>();

        // Add internal arrays to sheet array
        homeArenas.add(team);
        homeArenas.add(division);
        homeArenas.add(arenaName);
        homeArenas.add(rank);

        // Iterator
        Iterator<Row> rowIterator = homeArenasSheet.rowIterator();

        // Iterate over data
        while(rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(row.getRowNum() == 0) {
                    //headers.add(cell.getStringCellValue());
                }
                else {
                    if(cell.getColumnIndex() == 0) {
                        homeArenas.get(0).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 1) {
                        homeArenas.get(1).add(cell.getStringCellValue());
                    }
                    else if(cell.getColumnIndex() == 2) {
                        homeArenas.get(2).add((cell.getStringCellValue()));
                    }
                    else if(cell.getColumnIndex() == 3) {
                        homeArenas.get(3).add(String.valueOf(cell.getNumericCellValue()));
                    }
                }
            }
        }

    }

    public void printTeamsData(List<List<String>> sheetArray) {
        // Size of internal lists
        int totalLength = sheetArray.get(0).size();

        for(int i = 0; i < totalLength; i++) {
            System.out.println("\n" + "Team: " + (i+1));
            System.out.println("League: " + sheetArray.get(0).get(i));
            System.out.println("Name: " + sheetArray.get(1).get(i));
            System.out.println("Organization: " + sheetArray.get(2).get(i));
            System.out.println("Division: " + sheetArray.get(3).get(i));
            System.out.println("Tier: " + sheetArray.get(4).get(i));
        }
    }

    public void printTimeExceptionsData(List<List<String>> sheetArray) {
        for(int i = 0; i < sheetArray.get(0).size(); i++) {
            System.out.println("\n" + "Time Exception: " + (i+1));
            System.out.println("Division: " + sheetArray.get(0).get(i));
            System.out.println("Team: " + sheetArray.get(1).get(i));
            System.out.println("Date: " + sheetArray.get(2).get(i));
            System.out.println("Start Time: " + sheetArray.get(3).get(i));
            System.out.println("End Time: " + sheetArray.get(4).get(i));
            System.out.println("Weekly?: " + sheetArray.get(5).get(i));
            System.out.println("Rank: " + sheetArray.get(6).get(i));
        }
    }

    public void printDateExceptionsData(List<List<String>> sheetArray) {
        for(int i = 0; i < sheetArray.get(0).size(); i++) {
            System.out.println("\n" + "Date Exception: " + (i+1));
            System.out.println("Division: " + sheetArray.get(0).get(i));
            System.out.println("Team: " + sheetArray.get(1).get(i));
            System.out.println("Start Date: " + sheetArray.get(2).get(i));
            System.out.println("End Date: " + sheetArray.get(3).get(i));
        }
    }

    public void printArenasData(List<List<String>> sheetArray) {
        for(int i = 0; i < sheetArray.get(0).size(); i++) {
            System.out.println("\n" + "Arena: " + (i+1));
            System.out.println("Name: " + sheetArray.get(0).get(i));
            System.out.println("Location x: " + sheetArray.get(1).get(i));
            System.out.println("Location y: " + sheetArray.get(2).get(i));
        }
    }

    public void printTimeSlotsData(List<List<String>> sheetArray) {
        for(int i = 0; i < sheetArray.get(0).size(); i++) {
            System.out.println("\n" + "Time Slot: " + (i+1));
            System.out.println("Arena Name: " + sheetArray.get(0).get(i));
            System.out.println("Day: " + sheetArray.get(1).get(i));
            System.out.println("Start Time: " + sheetArray.get(2).get(i));
            System.out.println("Preferred Divisions: " + sheetArray.get(3).get(i));
        }
    }

    public void printHomeArenasData(List<List<String>> sheetArray) {
        for(int i = 0; i < sheetArray.get(0).size(); i++) {
            System.out.println("\n" + "Home Arena: " + (i+1));
            System.out.println("Team: " + sheetArray.get(0).get(i));
            System.out.println("Division: " + sheetArray.get(1).get(i));
            System.out.println("Arena Name: " + sheetArray.get(2).get(i));
            System.out.println("Rank: " + sheetArray.get(3).get(i));
        }
    }

    public List<List<String>> getHomeArenas() {
        return homeArenas;
    }

    public static void main(String[] args) throws IOException {
        // Create test run
        Excel_Import test = new Excel_Import();

        // Import sheets
        test.importTeams();
        test.importTimeExceptions();
        test.importDateExceptions();
        test.importArenas();
        test.importTimeSlots();
        test.importHomeArenas();

        // Print sheet arrays
        //test.printTeamsData(test.teams);
        //test.printTimeExceptionsData(test.timeExceptions);
        //test.printDateExceptionsData(test.dateExceptions);
        test.printArenasData(test.arenas);
        //test.printTimeSlotsData(test.timeSlots);
        //test.printHomeArenasData(test.homeArenas);
    }
}
