package main.java.Excel_Import_Export;

import main.java.Scheduler.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Exports the games into Excel
 * 
 * @author Brady Norton
 *
 */
public class ExcelExport {
    // 2D Arrays
    private ArrayList<Round> rounds;
    private ArrayList<Game> games;
    private ArrayList<Team> teams;
    private ArrayList<TimeSlot> timeSlots;

    // Excel Variables
    private XSSFWorkbook wb;

    private Schedule schedule;

    /**
     *  Constructor for Excel_Export class
     */
    public ExcelExport(Schedule schedule) {
        // Initialize the schedule
        this.schedule = schedule;

        // Create workbook
        wb = new XSSFWorkbook();

        // Get array data from schedule
        rounds = schedule.getRounds();
        games = schedule.getGames();
        teams = schedule.getTeams();
        timeSlots = schedule.getTimeSlots();
    }

    /**
     * Adds new sheet to workbook for each round in the schedule
     */
    public void processRounds() {
        int rounds = schedule.getRounds().size();
        System.out.println("Total rounds scheduled: " + rounds);
        for(int i = 0; i < rounds; ++i) {
            // Create Sheet
            XSSFSheet sheet = wb.createSheet("Round " + (i+1));
            System.out.println("Sheet " + (i+1) +" created");

            // Create sheet headers
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("League");
            row.createCell(1).setCellValue("Division");
            row.createCell(2).setCellValue("Home Team");
            row.createCell(3).setCellValue("Away Team");
            row.createCell(4).setCellValue("Arena");
            row.createCell(5).setCellValue("Time");

            // Add match-ups to sheet
            for(int j = 0; j < schedule.getRounds().get(i).getMatchups().size(); ++j) {
                // Create game row
                Row tmp = sheet.createRow(j + 1);

                // Getting list of match-ups (games) for the current round
                ArrayList<Game> roundGames = schedule.getRounds().get(i).getMatchups();

                // Add match-up data to row
                /*
                if(roundGames.get(j).getHomeTeam().getLeague().getName() == null) {
                    tmp.createCell(0).setCellValue("null");
                } else{
                    tmp.createCell(0).setCellValue(roundGames.get(j).getHomeTeam().getLeague().getName());
                }

                 */

                //tmp.createCell(1).setCellValue(roundGames.get(j).getHomeTeam().getDivision().getName());
                tmp.createCell(2).setCellValue(roundGames.get(j).getHomeTeam().getName());
                tmp.createCell(3).setCellValue(roundGames.get(j).getAwayTeam().getName());
                //tmp.createCell(4).setCellValue(roundGames.get(j).getTimeSlot().getArena().getName());
                //tmp.createCell(5).setCellValue(roundGames.get(j).getTimeSlot().getStartDateTime());
            }
        }
    }

    /**
     * Method called to create the workbook file containing schedule info
     */
    public void exportSchedule() throws IOException {
        // Process Rounds
        System.out.println("Number of sheets: " + wb.getNumberOfSheets());
        processRounds();

        // Add round data to each sheet


        // Output the workbook file
        String fp = "Output_Schedule.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        wb.write(out);
        out.close();
    }

    /**
     * Main function
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // Create workbook
        XSSFWorkbook outputSchedule = new XSSFWorkbook();

        // Add sheets
        /*
        for(int i = 0; i < 4; ++i) {
            XSSFSheet sheet = outputSchedule.createSheet("Round " + (i+1));
            System.out.println("Sheet " + (i+1) +" created");
        }
        */

        // Save workbook
        String fp = "Output_Schedule.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        outputSchedule.write(out);
        out.close();
    }

}
