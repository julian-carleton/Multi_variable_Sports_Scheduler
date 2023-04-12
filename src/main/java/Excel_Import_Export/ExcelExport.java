package Excel_Import_Export;


import Optimization.Move;
import Optimization.QualityChecker;
import Optimization.TabuSearch;
import Scheduler.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Math.floor;
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
    private XSSFWorkbook statsBook;
    private XSSFWorkbook tsBook;

    private CellStyle style;
    private CellStyle headers;
    private CellStyle sums;

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
        for(int i = 0; i < scheduleNum; ++i) {
            // Current schedule info
            Schedule schedule = runner.getSchedules().get(i);

            // Create Sheet
            XSSFSheet sheet = wb.createSheet("Schedule " + (i));
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
        row.createCell(5).setCellValue("Unused TimeSlots");
        row.createCell(6).setCellValue("TimeSlot Usage %");
        row.createCell(7).setCellValue("Scheduling Success %");
        row.createCell(8).setCellValue("Teams");
        row.createCell(9).setCellValue("Games per Team");
        row.createCell(10).setCellValue("Quality");

        // Add Stats
        for(int i = 0; i < runner.getSchedules().size(); ++i) {
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

            double tps = 1.0 * scheduledGames.size() / s.getTeams().size();
            double timeslotMaximization = (tsTotal - atsTotal) / tsTotal;

            QualityChecker qc = new QualityChecker(s.getGames(), s.getTimeSlots(), s.getTeams());

            // Create Row for Schedule stats
            Row statsRow = sheet.createRow(i+1);

            // Add cell data
            statsRow.createCell(0).setCellValue(s.getScheduleName());
            statsRow.createCell(1).setCellValue(s.getGames().size());
            statsRow.createCell(2).setCellValue(scheduledGames.size());
            statsRow.createCell(3).setCellValue((s.getGames().size() - scheduledGames.size()));
            statsRow.createCell(4).setCellValue(s.getTimeSlots().size());
            statsRow.createCell(5).setCellValue(atsTotal);
            statsRow.createCell(6).setCellValue(round(timeslotMaximization * 100, 2)+"%");
            statsRow.createCell(7).setCellValue(round(ssr,2) + "%");
            statsRow.createCell(8).setCellValue(s.getTeams().size());
            statsRow.createCell(9).setCellValue(floor(tps));
            statsRow.createCell(10).setCellValue(qc.getQuality());
        }
    }

    /**
     *	Updates League stats after running entire league optimization
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
        row.createCell(5).setCellValue("Unused TimeSlots");
        row.createCell(6).setCellValue("TimeSlot Usage %");
        row.createCell(7).setCellValue("Scheduling Success %");
        row.createCell(8).setCellValue("Teams");
        row.createCell(9).setCellValue("Games per Team");
        row.createCell(10).setCellValue("Quality");

        // Add Stats
        for(int i = 0; i < runner.getSchedules().size(); ++i) {
            Schedule s = runner.getSchedules().get(i);

            // Finding all games with an assigned timeslot
            ArrayList<Game> scheduledGames = new ArrayList<>();
            for(Game g : s.getGames()) {
                if(g.getTimeSlot() != null) {
                    scheduledGames.add(g);
                }
            }

            // Finding all remaining available timeslots
            ArrayList<TimeSlot> unusedTimeslots = new ArrayList<>();
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

            double tps = 1.0 * scheduledGames.size() / s.getTeams().size();
            double timeslotMaximization = (tsTotal - atsTotal) / tsTotal;

            QualityChecker qc = new QualityChecker(s.getGames(), s.getTimeSlots(), s.getTeams());

            // Create Row for Schedule stats
            Row statsRow = sheet.createRow(i+15); // offset 14 to give 2 row spacing between prev. stats (ended @ row 12)

            // Add cell data
            statsRow.createCell(0).setCellValue(s.getScheduleName());
            statsRow.createCell(1).setCellValue(s.getGames().size());
            statsRow.createCell(2).setCellValue(scheduledGames.size());
            statsRow.createCell(3).setCellValue((s.getGames().size() - scheduledGames.size()));
            statsRow.createCell(4).setCellValue(s.getTimeSlots().size());
            statsRow.createCell(5).setCellValue(atsTotal);
            statsRow.createCell(6).setCellValue(round(timeslotMaximization * 100, 2)+"%");
            statsRow.createCell(7).setCellValue(round(ssr,2) + "%");
            statsRow.createCell(8).setCellValue(s.getTeams().size());
            statsRow.createCell(9).setCellValue(floor(tps));
            statsRow.createCell(10).setCellValue(qc.getQuality());
        }

        Row optimiziationTime = sheet.createRow(28); // keep 2 row spacing between stat sets (last set stopped @ row 26)
        optimiziationTime.createCell(0).setCellValue("Optimization Time");

        Row tmp = sheet.createRow(29);
        tmp.createCell(0).setCellValue(runner.getOptimizationTime());
    }

    public void initializeTabuStats(ArrayList<TabuSearch> tabuSearches) {
        // Initialize Workbook and Sheet
        tsBook = new XSSFWorkbook();
        Sheet sheet = tsBook.createSheet("Tabu Stats");
        // Create sheet headers
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Schedule");
        row.createCell(1).setCellValue("Quality");
        row.createCell(2).setCellValue("Attempted Moves");
        row.createCell(3).setCellValue("Accepted Moves");
        row.createCell(4).setCellValue("Tabu Moves");
        row.createCell(5).setCellValue("Optimization Time");

        for(int i = 0; i < tabuSearches.size(); ++i) {
            // Get Tabu Search for Schedule
            TabuSearch ts = tabuSearches.get(i);

            // Create Row
            Row tsRow = sheet.createRow(i+1); // offset by 1 accounts for headers

            tsRow.createCell(0).setCellValue(ts.getScheduleName());
            tsRow.createCell(1).setCellValue(ts.getFinalQuality());
            tsRow.createCell(2).setCellValue(ts.getAttemptedMoves().size());
            tsRow.createCell(3).setCellValue(ts.getAcceptedMoves().size());
            tsRow.createCell(4).setCellValue(ts.getTabuList().size());
            tsRow.createCell(5).setCellValue(ts.executionTimeToString());
        }

        // Sum stats of Schedules before entire League optimization (all 11 Schedules optimized w/full list of timeslots)
        Row sumStatsBeforeCombinedTS = sheet.createRow(12);

        String sumQuality = "SUM(B2:B12)";
        String sumAttemptedMoves = "SUM(C2:C12)";
        String sumAcceptedMoves = "SUM(D2:D12)";
        String sumTabuMoves = "SUM(E2:E12)";

        sumStatsBeforeCombinedTS.createCell(0).setCellValue("Sums:");
        sumStatsBeforeCombinedTS.createCell(1).setCellFormula(sumQuality);
        sumStatsBeforeCombinedTS.createCell(2).setCellFormula(sumAttemptedMoves);
        sumStatsBeforeCombinedTS.createCell(3).setCellFormula(sumAcceptedMoves);
        sumStatsBeforeCombinedTS.createCell(4).setCellFormula(sumTabuMoves);

        // Add stats from entire league TS
        TabuSearch ts = runner.getLeagueTS();

        Row leagueHeaders = sheet.createRow(14);
        leagueHeaders.createCell(0).setCellValue("Schedule");
        leagueHeaders.createCell(1).setCellValue("Quality");
        leagueHeaders.createCell(2).setCellValue("Attempted Moves");
        leagueHeaders.createCell(3).setCellValue("Accepted Moves");
        leagueHeaders.createCell(4).setCellValue("Tabu Moves");

        for(int i = 0; i < this.runner.getSchedules().size(); ++i) {
            TabuSearch tmpTS = tabuSearches.get(i);
            Row tmpRow = sheet.createRow(15+i);
            Division tmpDiv = tmpTS.getTeams().get(tmpTS.getTeams().size() - 1).getDivision();
            Tier tmpTier = tmpTS.getTeams().get(tmpTS.getTeams().size() - 1).getTier();
            QualityChecker tmpQC = new QualityChecker(tmpTS.getCurrentSchedule(), tmpTS.getTimeSlots(), tmpTS.getTeams());

            // Get list of all Attempted Moves for specific Div/Tier
            ArrayList<Move> tmpAttemptedMoves = new ArrayList<>();
            for(Move m : ts.getAttemptedMoves()) {
                if(m.getGame().getHomeTeam().getDivision() == tmpDiv &&
                        m.getGame().getHomeTeam().getTier() == tmpTier) {
                    tmpAttemptedMoves.add(m);
                }
            }

            // Get list of all Accepted Moves for specific Div/Tier
            ArrayList<Move> tmpAcceptedMoves = new ArrayList<>();
            for(Move m : ts.getAcceptedMoves()) {
                if(m.getGame().getHomeTeam().getDivision() == tmpDiv &&
                        m.getGame().getHomeTeam().getTier() == tmpTier) {
                    tmpAcceptedMoves.add(m);
                }
            }

            // Get list of all Tabu Moves for specific Div/Tier
            ArrayList<Move> tmpTabuList = new ArrayList<>();
            for(Move m : ts.getTabuList()) {
                if(m.getGame().getHomeTeam().getDivision() == tmpDiv &&
                        m.getGame().getHomeTeam().getTier() == tmpTier) {
                    tmpTabuList.add(m);
                }
            }

            tmpRow.createCell(0).setCellValue("Division " + tmpDiv.getName() + " (tier: " + tmpTier.ordinal() + ")");
            tmpRow.createCell(1).setCellValue(tmpQC.getQuality());
            tmpRow.createCell(2).setCellValue(tmpAttemptedMoves.size());
            tmpRow.createCell(3).setCellValue(tmpAcceptedMoves.size());
            tmpRow.createCell(4).setCellValue(tmpTabuList.size());

        }

        // Sum each of the Tabu Stat columns
        Row sumsAfterCombinedTS = sheet.createRow(26);

        String sumQualityAfter = "SUM(B16:B26)";
        String sumAttemptedMovesAfter = "SUM(C16:C26)";
        String sumAcceptedMovesAfter = "SUM(D16:D26)";
        String sumTabuMovesAfter = "SUM(E16:E26)";

        sumsAfterCombinedTS.createCell(0).setCellValue("Sums:");
        sumsAfterCombinedTS.createCell(1).setCellFormula(sumQualityAfter);
        sumsAfterCombinedTS.createCell(2).setCellFormula(sumAttemptedMovesAfter);
        sumsAfterCombinedTS.createCell(3).setCellFormula(sumAcceptedMovesAfter);
        sumsAfterCombinedTS.createCell(4).setCellFormula(sumTabuMovesAfter);

        // Entire League TS Combined Stats
        Row overallHeaders = sheet.createRow(28);

        QualityChecker overallQC = new QualityChecker(ts.getCurrentSchedule(), ts.getTimeSlots(), ts.getTeams());

        overallHeaders.createCell(0).setCellValue("Schedule");
        overallHeaders.createCell(1).setCellValue("Quality");
        overallHeaders.createCell(2).setCellValue("Attempted Moves");
        overallHeaders.createCell(3).setCellValue("Accepted Moves");
        overallHeaders.createCell(4).setCellValue("Tabu Moves");
        overallHeaders.createCell(5).setCellValue("Optimization Time");
        overallHeaders.createCell(6).setCellValue("Iteration Limit");
        overallHeaders.createCell(7).setCellValue("Quality Threshold");

        Row overallStats = sheet.createRow(29);

        overallStats.createCell(0).setCellValue("Entire League");
        overallStats.createCell(1).setCellValue(overallQC.getQuality());
        overallStats.createCell(2).setCellValue(ts.getAttemptedMoves().size());
        overallStats.createCell(3).setCellValue(ts.getAcceptedMoves().size());
        overallStats.createCell(4).setCellValue(ts.getTabuList().size());
        overallStats.createCell(5).setCellValue(runner.getOptimizationTime());
        overallStats.createCell(6).setCellValue(ts.getIterationLimit() * ts.getNewMoveLimit());
        overallStats.createCell(7).setCellValue(ts.getQualityThreshold());

        for(TabuSearch tmp:tabuSearches) {
            System.out.println(tmp.getScheduleName() + " current Accepted Moves: " + tmp.getAcceptedMoves().size());
            System.out.println(tmp.getScheduleName() + " current Tabu Moves: " + tmp.getTabuList().size());
            System.out.println(tmp.getScheduleName() + " current Attempted Moves: " + tmp.getAttemptedMoves().size());
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

        return scheduledGames;
    }

    /**
     * Applies cell styles to each sheet in workbook
     */
    private void formatSheet(Workbook workbook) {
        // Custom Stats Colors
        byte[] blueHeaderRGB = new byte[3];
        blueHeaderRGB[0] = (byte) 142;
        blueHeaderRGB[1] = (byte) 169;
        blueHeaderRGB[2] = (byte) 219;
        XSSFColor blueHeader = new XSSFColor(blueHeaderRGB);

        byte[] blueHighlighterRGB = new byte[3];
        blueHighlighterRGB[0] = (byte) 217;
        blueHighlighterRGB[1] = (byte) 225;
        blueHighlighterRGB[2] = (byte) 242;
        XSSFColor blueHighlighter = new XSSFColor(blueHighlighterRGB);

        byte[] orangeHeaderRGB = new byte[3];
        orangeHeaderRGB[0] = (byte) 244;
        orangeHeaderRGB[1] = (byte) 176;
        orangeHeaderRGB[2] = (byte) 132;
        XSSFColor orangeHeader = new XSSFColor(orangeHeaderRGB);

        byte[] orangeHighlighterRGB = new byte[3];
        orangeHighlighterRGB[0] = (byte) 248;
        orangeHighlighterRGB[1] = (byte) 203;
        orangeHighlighterRGB[2] = (byte) 173;
        XSSFColor orangeHighlighter = new XSSFColor(orangeHighlighterRGB);

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
                    if(r.getRowNum() == 0 || r.getRowNum() == 14 || r.getRowNum() == 28) {
                        c.setCellStyle(headers);
                    }
                    else if(r.getRowNum() == 12 && sheet.getSheetName().equals("Tabu Stats")) {
                        if(c.getColumnIndex() == 0) {
                            CellStyle blueHeaderStyle = workbook.createCellStyle();
                            blueHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            blueHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
                            blueHeaderStyle.setFillForegroundColor(blueHeader);
                            blueHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            c.setCellStyle(blueHeaderStyle);
                        } else {
                            CellStyle blueHighlighterStyle = workbook.createCellStyle();
                            blueHighlighterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            blueHighlighterStyle.setAlignment(HorizontalAlignment.CENTER);
                            blueHighlighterStyle.setFillForegroundColor(blueHighlighter);
                            blueHighlighterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            c.setCellStyle(blueHighlighterStyle);
                        }
                    }
                    else if(r.getRowNum() == 26 && sheet.getSheetName().equals("Tabu Stats")) {
                        if(c.getColumnIndex() == 0) {
                            CellStyle orangeHeaderStyle = workbook.createCellStyle();
                            orangeHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            orangeHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
                            orangeHeaderStyle.setFillForegroundColor(orangeHeader);
                            orangeHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            c.setCellStyle(orangeHeaderStyle);
                        } else {
                            CellStyle orangeHighlighterStyle = workbook.createCellStyle();
                            orangeHighlighterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            orangeHighlighterStyle.setAlignment(HorizontalAlignment.CENTER);
                            orangeHighlighterStyle.setFillForegroundColor(orangeHighlighter);
                            orangeHighlighterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            c.setCellStyle(orangeHighlighterStyle);
                        }
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

        for(int i = 0; i < schedules.size(); ++i) {
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
     * Method called to create the workbook file containing schedule info
     */
    public void exportSchedule(String exportLocation) throws IOException {
        // Print league info
        printLeagueInfo();

        // Process the League
        processLeague();

        // Format workbook sheets
        formatSheet(wb);

        // Output the workbook file
        String fp = exportLocation + "Output_Schedule.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        wb.write(out);
        out.close();
    }

    /**
     * Exports the Optimization stats to an Excel File (Different sheet for each Schedule)
     *
     * @throws IOException handles I/O Exception for Optimization_Stats.xlsx
     */
    public void exportStats(String exportLocation) throws IOException {
        // Print Optimization Stats
        printLeagueData();

        // Format Workbook
        formatSheet(statsBook);

        // Export Workbook to Excel file
        String fp = exportLocation + "Optimization_Stats.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        statsBook.write(out);
        out.close();
    }

    /**
     * Exports the Tabu Search stats to Excel File (1 sheet for all Schedules)
     *
     * @throws IOException handles I/O Exception for Compact_Optimization_Stats.xlsx
     */
    public void exportTabuStats(ArrayList<TabuSearch> tabuSearches, String exportLocation) throws IOException {
        // Process Stats
        initializeTabuStats(tabuSearches);

        // Format Workbook
        formatSheet(tsBook);

        // Export Workbook
        String fp = exportLocation + "TabuSearch_Stats.xlsx";
        FileOutputStream out = new FileOutputStream(fp);
        tsBook.write(out);
        out.close();
    }
}
