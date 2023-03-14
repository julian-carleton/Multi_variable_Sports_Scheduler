package Excel_Import_Export;


import Optimization.TabuSearch;
import Scheduler.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.apache.commons.math3.util.Precision.round;

/**
 * Exports the games into Excel
 * 
 * @author Brady Norton
 *
 */
public class ExcelExport {
    // 2D Arrays
    private ArrayList<Round> rounds;
    private ArrayList<Team> teams;
    private ArrayList<TimeSlot> timeSlots;

    // Excel Variables (Output_Schedule)
    private XSSFWorkbook wb;
    private CellStyle style;
    private CellStyle headers;

    // Excel Variables (Optimization_Stats)
    private  XSSFWorkbook statsBook;

    // Scheduling Variables
    private Runner runner;

    // Demo mode
    boolean demo = false;

    /**
     *  Constructor for Excel_Export class
     */
    public ExcelExport(Runner runner) {
        // Initialize the league to be exported
        this.runner = runner;

        // Create workbook
        wb = new XSSFWorkbook();
    }

    /**
     * Prints important league data (name/divisions in league/amount of schedules)
     */
    public void printLeagueInfo() {
        System.out.println("League Name: " + runner.getName());
        System.out.println("Number of Divisions: " + runner.getDivisions().size());
        System.out.println("Number of Schedules: " + (runner.getSchedules().size() - 1));
    }

    /**
     * Adds new sheet to workbook for each schedule created by league
     */
    public void processLeague() {
        // Get the amount of schedules for the league
        int scheduleNum = runner.getSchedules().size();
        System.out.println("Total rounds scheduled: " + rounds);

        // Iterate over each schedule (starts at 1 since schedule @ index 0 is an empty test schedule)
        for(int i = 1; i < scheduleNum; ++i) {
            // Current schedule info
            Schedule schedule = runner.getSchedules().get(i);

            // Create Sheet
            XSSFSheet sheet = wb.createSheet("Schedule " + (i));
            sheet.setColumnWidth(0, 30);
            //System.out.println("Sheet " + (i) +" created");

            // Create sheet headers
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Tier");
            row.createCell(1).setCellValue("Division");
            row.createCell(2).setCellValue("Home Team");
            row.createCell(3).setCellValue("Away Team");
            row.createCell(4).setCellValue("Arena");
            row.createCell(5).setCellValue("Time");
            row.createCell(6).setCellValue("Date");

            // Get games in current schedule
            //ArrayList<Game> games = league.getSchedules().get(i).getGames();
            ArrayList<Game> games = sortMatches(i);

            // Add match-ups to sheet
            for(int j = 0; j < games.size(); ++j) {
                // Create game row
                Row tmp = sheet.createRow(j + 1);

                // Set Tier
                tmp.createCell(0).setCellValue(games.get(j).getHomeTeam().getTier().ordinal());

                // Set Division
                tmp.createCell(1).setCellValue(games.get(j).getHomeTeam().getDivision().getName());

                // Set Home Team
                tmp.createCell(2).setCellValue(games.get(j).getHomeTeam().getName());

                // Set Away Team
                tmp.createCell(3).setCellValue(games.get(j).getAwayTeam().getName());

                // Setting Arena and Date/Time cells for matches with an assigned timeslot
                if(games.get(j).getTimeSlot() != null) {
                    // Set Arena
                    tmp.createCell(4).setCellValue(games.get(j).getTimeSlot().getArena().getName());

                    // Set time
                    LocalDateTime ldt = games.get(j).getTimeSlot().getStartDateTime();
                    String time = (ldt.format(DateTimeFormatter.ofPattern("HH:mm")));
                    tmp.createCell(5).setCellValue(time);

                    // Set Date
                    int day = games.get(j).getTimeSlot().getStartDateTime().getDayOfMonth();
                    int month = games.get(j).getTimeSlot().getStartDateTime().getMonthValue();
                    int year = games.get(j).getTimeSlot().getStartDateTime().getYear();
                    tmp.createCell(6).setCellValue(day + "/" + month + "/" + year);
                }
            }
        }
    }

    /**
     * Creates new Workbook for optimization stats and populates it with pre-optimization data
     */
    public void initializeLeagueStats() {
        // Create Workbook
        statsBook = new XSSFWorkbook();

        // Create Sheet
        XSSFSheet sheet = statsBook.createSheet("Optimization Statistics");
        sheet.autoSizeColumn(0);

        // Create sheet headers
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Schedule (Pre-Optimization)");
        row.createCell(1).setCellValue("Total Games");
        row.createCell(2).setCellValue("Scheduled Games");
        row.createCell(3).setCellValue("Unscheduled Games");
        row.createCell(4).setCellValue("Total TimeSlots");
        row.createCell(5).setCellValue("Used TimeSlots");
        row.createCell(6).setCellValue("Unused TimeSlots");
        row.createCell(7).setCellValue("Scheduling Success %");

        // Add Stats
        for(int i = 1; i < runner.getSchedules().size(); ++i) {
            Schedule s = runner.getSchedules().get(i);
            ArrayList<Game> scheduledGames = new ArrayList<>();
            ArrayList<TimeSlot> unusedTimeslots = new ArrayList<>();

            // Finding all games with an assigned timeslot
            for(Game g : s.getGames()) {
                if(g.getTimeSlot() != null) {
                    scheduledGames.add(g);
                }
            }

            // Finding all remaining available timeslots
            for(TimeSlot t : s.getTimeSlots()) {
                if(t.isAvailable()) {
                    unusedTimeslots.add(t);
                }
            }

            double tg = s.getGames().size(); // total games
            double tsg = scheduledGames.size(); // total scheduled games
            double ssr = (tsg / tg) * 100; // scheduling success rate
            double tsTotal = s.getTimeSlots().size(); // total allotted timeslots
            double atsTotal = unusedTimeslots.size(); // total available timeslots (unused remaining timeslots)

            // Create Row for Schedule stats
            Row statsRow = sheet.createRow(i);

            // Add cell data
            statsRow.createCell(0).setCellValue(s.getScheduleName());
            statsRow.createCell(1).setCellValue(s.getGames().size());
            statsRow.createCell(2).setCellValue(scheduledGames.size());
            statsRow.createCell(3).setCellValue((s.getGames().size() - scheduledGames.size()));
            statsRow.createCell(4).setCellValue(s.getTimeSlots().size());
            statsRow.createCell(5).setCellValue((s.getTimeSlots().size() - unusedTimeslots.size()));
            statsRow.createCell(6).setCellValue(unusedTimeslots.size());
            statsRow.createCell(7).setCellValue(round(ssr,2) + "%");
        }
    }

    /**
     *
     * @throws IOException
     */
    public void updateLeagueStats() throws IOException {
        Sheet sheet = statsBook.getSheetAt(0);

        // Re-create sheet headers
        Row row = sheet.createRow(14);
        row.createCell(0).setCellValue("Schedule (Post-Optimization)");
        row.createCell(1).setCellValue("Total Games");
        row.createCell(2).setCellValue("Scheduled Games");
        row.createCell(3).setCellValue("Unscheduled Games");
        row.createCell(4).setCellValue("Total TimeSlots");
        row.createCell(5).setCellValue("Used TimeSlots");
        row.createCell(6).setCellValue("Unused TimeSlots");
        row.createCell(7).setCellValue("Scheduling Success %");

        // Add Stats
        for(int i = 1; i < runner.getSchedules().size(); ++i) {
            Schedule s = runner.getSchedules().get(i);
            ArrayList<Game> scheduledGames = new ArrayList<>();
            ArrayList<TimeSlot> unusedTimeslots = new ArrayList<>();

            // Finding all games with an assigned timeslot
            for(Game g : s.getGames()) {
                if(g.getTimeSlot() != null) {
                    scheduledGames.add(g);
                }
            }

            // Finding all remaining available timeslots
            for(TimeSlot t : s.getTimeSlots()) {
                if(t.isAvailable()) {
                    unusedTimeslots.add(t);
                }
            }

            double tg = s.getGames().size(); // total games
            double tsg = scheduledGames.size(); // total scheduled games
            double ssr = (tsg / tg) * 100; // scheduling success rate
            double tsTotal = s.getTimeSlots().size(); // total allotted timeslots
            double atsTotal = unusedTimeslots.size(); // total available timeslots (unused remaining timeslots)

            // Create Row for Schedule stats
            Row statsRow = sheet.createRow(i + 14);

            // Add cell data
            statsRow.createCell(0).setCellValue(s.getScheduleName());
            statsRow.createCell(1).setCellValue(s.getGames().size());
            statsRow.createCell(2).setCellValue(scheduledGames.size());
            statsRow.createCell(3).setCellValue((s.getGames().size() - scheduledGames.size()));
            statsRow.createCell(4).setCellValue(s.getTimeSlots().size());
            statsRow.createCell(5).setCellValue((s.getTimeSlots().size() - unusedTimeslots.size()));
            statsRow.createCell(6).setCellValue(unusedTimeslots.size());
            statsRow.createCell(7).setCellValue(round(ssr,2) + "%");
        }
    }

    /**
     * Method to sort scheduled games by date
     *
     * NOTE: under development
     *
     * @param scheduleNum the schedule to be sorted
     */
    public ArrayList<Game> sortMatches(int scheduleNum) {
        ArrayList<Game> games = runner.getSchedules().get(scheduleNum).getGames();
        ArrayList<Game> scheduledGames = new ArrayList<>();

        // Create list of matches with timeslots
        for(Game g : games) {
            if(g.getTimeSlot() != null) {
                scheduledGames.add(g);
            }
        }

        Collections.sort(scheduledGames, Comparator.comparing(x -> x.getTimeSlot().getStartDateTime()));

        /*
        // Print dates in order
        for(Game g : scheduledGames) {
            System.out.println("Date: " + g.getTimeSlot().getStartDateTime().toString());
        }
         */

        return scheduledGames;
    }

    /**
     * Applies cell styles to each sheet in workbook
     */
    private void formatSheet(Workbook workbook) {
        style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        headers = workbook.createCellStyle();
        headers.setVerticalAlignment(VerticalAlignment.CENTER);
        headers.setAlignment(HorizontalAlignment.CENTER);
        headers.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        headers.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for(int i = 0; i < workbook.getNumberOfSheets(); ++i) {
            Sheet sheet = workbook.getSheetAt(i);
            sheet.autoSizeColumn(i);
            for(Row r: sheet){
                for(Cell c: r){
                    if(r.getRowNum() == 0 || r.getRowNum() == 14) {
                        c.setCellStyle(headers);
                    }
                    else{
                        sheet.setColumnWidth(c.getColumnIndex(), 20 * 256);
                        c.setCellStyle(style);
                    }
                }
            }
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Prints the total games, scheduled games, allotted timeslots, remaining timeslots, and scheduling success rate
     * for each division/tier schedule
     */
    public void printLeagueData() {
        ArrayList<Schedule> schedules = runner.getSchedules();
        double leagueTimeslots = 0;
        double leagueUsedTimeslots = 0;

        for(int i = 1; i < schedules.size(); ++i) {
            Schedule s = schedules.get(i);
            ArrayList<Game> scheduledGames = new ArrayList<>();
            ArrayList<TimeSlot> unusedTimeslots = new ArrayList<>();

            String division = s.getTeams().get(1).getDivision().getName();
            int tier = s.getTeams().get(1).getTier().ordinal();

            // Finding all games with an assigned timeslot
            for(Game g : s.getGames()) {
                if(g.getTimeSlot() != null) {
                    scheduledGames.add(g);
                }
            }

            // Finding all remaining available timeslots
            for(TimeSlot t : s.getTimeSlots()) {
                if(t.isAvailable()) {
                    unusedTimeslots.add(t);
                }
            }

            double tg = s.getGames().size(); // total games
            double tsg = scheduledGames.size(); // total scheduled games
            double ssr = (tsg / tg) * 100; // scheduling success rate
            double tsTotal = s.getTimeSlots().size(); // total allotted timeslots
            double atsTotal = unusedTimeslots.size(); // total available timeslots (unused remaining timeslots)

            leagueTimeslots = leagueTimeslots + tsTotal;
            leagueUsedTimeslots = leagueUsedTimeslots + (tsTotal - atsTotal);

            System.out.println();
            System.out.println("Schedule: " + division + " (Tier " + tier + ")");
            System.out.println("Total Games: " + tg);
            System.out.println("Total Scheduled Games: " + tsg);
            System.out.println("Total allotted timeslots: " + tsTotal);
            System.out.println("Remaining available timeslots: " + atsTotal);
            System.out.println("Scheduling Success Rate: " + round(ssr,2) + "%");

        }
        System.out.println("Total timeslots allotted to league: " + leagueTimeslots);
        System.out.println("Total timeslots used by league: " + leagueUsedTimeslots);
        System.out.println("Remaining timeslots: " + (leagueTimeslots - leagueUsedTimeslots));
        System.out.println("Program Scheduling Rate: " + round(((leagueUsedTimeslots/leagueTimeslots) * 100),2) + "%\n");
    }


    /**
     * Method called to create the workbook file containing schedule info
     * 
     * @param fileLocation
     * @throws IOException
     */
    public void exportSchedule(String fileLocation) throws IOException {
        // Print league info
        printLeagueInfo();

        // Process Rounds
        processLeague();
        
        FileOutputStream out = new FileOutputStream(fileLocation);
        wb.write(out);
        out.close();
	}
    
    public void printSpecificLeagueData(Schedule s) {
        double leagueTimeslots = 0;
        double leagueUsedTimeslots = 0;

        ArrayList<Game> scheduledGames = new ArrayList<>();
        ArrayList<TimeSlot> unusedTimeslots = new ArrayList<>();

        String division = s.getTeams().get(1).getDivision().getName();
        int tier = s.getTeams().get(1).getTier().ordinal();

        // Finding all games with an assigned timeslot
        for(Game g : s.getGames()) {
            if(g.getTimeSlot() != null) {
                scheduledGames.add(g);
            }
        }

        // Finding all remaining available timeslots
        for(TimeSlot t : s.getTimeSlots()) {
            if(t.isAvailable()) {
                unusedTimeslots.add(t);
            }
        }

        double tg = s.getGames().size(); // total games
        double tsg = scheduledGames.size(); // total scheduled games
        double ssr = (tsg / tg) * 100; // scheduling success rate
        double tsTotal = s.getTimeSlots().size(); // total allotted timeslots
        double atsTotal = unusedTimeslots.size(); // total available timeslots (unused remaining timeslots)

        leagueTimeslots = leagueTimeslots + tsTotal;
        leagueUsedTimeslots = leagueUsedTimeslots + (tsTotal - atsTotal);

        System.out.println();
        System.out.println("Schedule: " + division + " (Tier " + tier + ")");
        System.out.println("Total Games: " + tg);
        System.out.println("Total Scheduled Games: " + tsg);
        System.out.println("Total allotted timeslots: " + tsTotal);
        System.out.println("Remaining available timeslots: " + atsTotal);
        System.out.println("Scheduling Success Rate: " + round(ssr,2) + "%");

    }

    /**
     * Prints the schedule data for a specific division and tier
     *
     */
    public void printScheduleData(Schedule schedule, double initialQuality) {
        int scheduledGameCount = 0;
        for(Game g : schedule.getGames()) {
            if(g.getTimeSlot() != null) {
                scheduledGameCount++;
            }
        }
        double schedulingRate = round((100.0* scheduledGameCount / schedule.getGames().size()),2);

        System.out.println("\n"+schedule.getScheduleName() + " Stats (pre-optimization)");
        System.out.println("TimeSlots: " + schedule.getTimeSlots().size());
        System.out.println("Total Potential Matchups: " + schedule.getGames().size());
        System.out.println("Scheduled Games: " + scheduledGameCount);
        System.out.println("Unscheduled Games: " + (schedule.getGames().size() - scheduledGameCount));
        System.out.println("Scheduling Rate: " + schedulingRate + "%");
        System.out.println("Quality: " + initialQuality);
    }


    /**
     * Prints the following stats for all Schedules post-optimization:
     *  - Initial Quality (pre-optimization)
     *  - Final Quality (post-optimization)
     *  - Total Quality increase
     *  - Total Moves Attempted
     *  - Total Moves deemed Tabu
     *  - Total Moves made to Schedule
     *  - Initial amount of Scheduled Games
     *  - Final amount of Scheduled Games
     *  - Total Scheduled Games added by Optimization
     *  - Total Optimization time
     *
     *  TO-DO: Update method to print these stats to an Excel sheet instead of printing to console
     *
     * @param
     */
        public void getStats(TabuSearch ts) {
        int scheduledGames = ts.getFinalGamesScheduled();
        int remainingGames = ts.getRemainingGames();
        int potentialGames = ts.getCurrentSchedule().size();
        double schedulingRate = round(100.0 * scheduledGames / potentialGames, 2);

        int totalTimeslots = ts.getTimeSlots().size();
        int remainingTimeSlots = ts.getRemainingTimeSlots();
        int usedTimeslots = totalTimeslots - remainingTimeSlots;
        double timeslotRate = round(100.0 * usedTimeslots / totalTimeslots, 2);

        // Printing
        System.out.println("\n" + ts.getScheduleName());

        //System.out.println("Initial Quality: " + ts.getInitialQuality());
        System.out.println("Final Quality: " + ts.getFinalQuality() + " (increased by: " + round(ts.getInitialQuality() - ts.getFinalQuality(), 2) + ")");

        System.out.println("Total Games: " + ts.getCurrentSchedule().size());
        //System.out.println("Initial Scheduled Games: " + ts.getInitialGamesScheduled());
        System.out.println("Final Scheduled Games: " + ts.getFinalGamesScheduled() + " (added: " + (ts.getFinalGamesScheduled() - ts.getInitialGamesScheduled()) + " games)");
        System.out.println("Unscheduled Games: " + remainingGames);
        System.out.println("Scheduling Rate: " + schedulingRate + "%");

        System.out.println("Total Allotted TimeSlots: " + totalTimeslots);
        System.out.println("TimeSlots Used: " + usedTimeslots + " (remaining timeslots: " + remainingTimeSlots +")" );
        System.out.println("TimeSlot Usage Rate: " + timeslotRate + "%");

        System.out.println("Total Moves: " + ts.getTotalMoves());
        System.out.println("Tabu Moves: " + ts.getTabuMovesTotal());

        System.out.println("Total Optimization Time: " + ts.executionTimeToString());
    }

    /**
     * Method called to create the workbook file containing schedule info
     */
    public void exportSchedule() throws IOException {
        // Print league info
        printLeagueInfo();

        // Process the League
        processLeague();

        // Format workbook sheets
        formatSheet(wb);

        // Output the workbook file
        String fp = "Output_Schedule.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        wb.write(out);
        out.close();
    }

    /**
     * Exports the Optimization stats to an Excel File (Different sheet for each Schedule)
     *
     * @throws IOException handles I/O Exception for Optimization_Stats.xlsx
     */
    public void exportStats() throws IOException {
        // Print Optimization Stats
        printLeagueData();

        // Format Workbook
        formatSheet(statsBook);

        // Export Workbook to Excel file
        String fp = "Optimization_Stats.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        statsBook.write(out);
        out.close();
    }

    /**
     * Exports the Tabu Search stats to Excel File (1 sheet for all Schedules)
     *
     * @throws IOException handles I/O Exception for Compact_Optimization_Stats.xlsx
     */
    public void exportTabuStats() throws IOException {
        // Process Stats


        // Format Workbook


        // Export Workbook

    }
}
