package Excel_Import_Export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlgraphics.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import Scheduler.Team;
import Scheduler.Exception;

import static org.apache.batik.svggen.SVGStylingAttributes.set;


public class Excel_Import {
    // Arrays to hold excel info
    ArrayList<String> headers;
    ArrayList<String> league;
    ArrayList<String> name;
    ArrayList<String> org;
    ArrayList<String> division;
    ArrayList<String> tier;

    // 2D Arraylists for each sheet
    List<List<String>> teams = new ArrayList<List<String>>();
    List<List<String>> timeExceptions =  new ArrayList<List<String>>();
    ArrayList[] dateExceptions;
    ArrayList[] arenas;
    ArrayList[] timeSlots;
    ArrayList[] homeArenas;
    ArrayList[] other;

    // Excel Variables
    XSSFWorkbook wb;
    XSSFSheet teamsSheet;
    XSSFSheet timeExceptionsSheet;
    XSSFSheet dataExceptionsSheet;
    XSSFSheet arenasSheet;
    XSSFSheet timeSlotsSheet;
    XSSFSheet homeArenasSheet;
    XSSFSheet otherSheet;

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
            dataExceptionsSheet = wb.getSheetAt(2);
            arenasSheet = wb.getSheetAt(3);
            timeSlotsSheet = wb.getSheetAt(4);
            homeArenasSheet = wb.getSheetAt(5);
            otherSheet = wb.getSheetAt(6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    public void importData() throws IOException {
        // Initialize Arrays
        headers = new ArrayList<>();
        league = new ArrayList<>();
        name = new ArrayList<>();
        org = new ArrayList<>();
        division = new ArrayList<>();
        tier = new ArrayList<>();

        // Extract Excel File Data
        try {
            // InputStream
            InputStream schedulingData = Excel_Import.class.getResourceAsStream("/Input_Proposal.xlsx");

            // Input_Proposal Workbook
            XSSFWorkbook wb = new XSSFWorkbook(schedulingData);

            // Sheets
            XSSFSheet teamsSheet = wb.getSheetAt(0);
            XSSFSheet timeExceptionsSheet = wb.getSheetAt(1);
            XSSFSheet dataExceptionsSheet = wb.getSheetAt(2);
            XSSFSheet arenasSheet = wb.getSheetAt(3);
            XSSFSheet timeSlotsSheet = wb.getSheetAt(4);
            XSSFSheet homeArenasSheet = wb.getSheetAt(5);
            XSSFSheet otherSheet = wb.getSheetAt(6);

            // Iterators (just iterating the first sheet for now)
            Iterator<Row> rowIterator1 = teamsSheet.rowIterator();

            while (rowIterator1.hasNext()) {
                // Next row
                Row row = rowIterator1.next();

                // Iterate over cells in row
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    // Next cell
                    Cell cell = cellIterator.next();

                    if (row.getRowNum() == 0) {
                        headers.add(cell.getStringCellValue());
                    } else {
                        if (cell.getColumnIndex() == 0) {
                            league.add(cell.getStringCellValue());
                        } else if (cell.getColumnIndex() == 1) {
                            name.add(cell.getStringCellValue());
                        } else if (cell.getColumnIndex() == 2) {
                            org.add(cell.getStringCellValue());
                        } else if (cell.getColumnIndex() == 3) {
                            division.add(cell.getStringCellValue());
                        } else if (cell.getColumnIndex() == 4) {
                            tier.add(String.valueOf(cell.getNumericCellValue()));
                        }
                    }
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        // Print team names
        for (int i = 0; i < name.size(); i++) {
            System.out.println("Team " + i + ": " + name.get(i));
        }
    }
     **/

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

        // Add internal arrays to 2D array
        teams.add(league);
        teams.add(name);
        teams.add(org);
        teams.add(division);
        teams.add(tier);

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
                        teams.get(0).add(cell.getStringCellValue());

                    } else if (cell.getColumnIndex() == 1) {
                        name.add(cell.getStringCellValue());
                    } else if (cell.getColumnIndex() == 2) {
                        org.add(cell.getStringCellValue());
                    } else if (cell.getColumnIndex() == 3) {
                        division.add(cell.getStringCellValue());
                    } else if (cell.getColumnIndex() == 4) {
                        tier.add(String.valueOf(cell.getNumericCellValue()));
                    }
                }
            }
        }
    }

    /**
     * Import data from sheet 1 (Time Exceptions)
     */
    public void importTimeExceptions() {
        // 2D array
        //timeExceptions = new ArrayList[7];

        //

        // Iterator
        Iterator<Row> rowIterator = timeExceptionsSheet.rowIterator();

        // Iterate over data
        while(rowIterator.hasNext()) {
            // Next row
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while(cellIterator.hasNext()) {

            }
        }
    }

    /**
     * Import data from sheet 2 (Date Exceptions)
     */
    public void importDateExceptions() {
        // 2D list
        dateExceptions = new ArrayList[4];

    }

    /**
     * Import data from sheet 3 (Arenas)
     */
    public void importArenas() {
        // 2D list
        arenas = new ArrayList[3];

    }

    /**
     * Import data from sheet 4 (Time Slots)
     */
    public void importTimeSlots() {
        // 2D list
        timeSlots = new ArrayList[4];

    }

    /**
     * Import data from sheet 5 (Home Arena)
     */
    public void importHomeArenas() {
        // 2D list
        homeArenas = new ArrayList[4];
    }

    /**
     * Import data from sheet 6 (Other)
     */
    public void importOtherData() {

    }

    public void printSheetData(List<List<String>> sheetArray) {
        // Size of internal lists
        int totalLength = sheetArray.get(0).size();

        for(int i = 0; i < totalLength; i++) {
            System.out.println("\n" + "Team: " + i);
            System.out.println("League: " + sheetArray.get(0).get(i));
            System.out.println("Name: " + sheetArray.get(1).get(i));
            System.out.println("Organization: " + sheetArray.get(2).get(i));
            System.out.println("Division: " + sheetArray.get(3).get(i));
            System.out.println("Tier: " + sheetArray.get(4).get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        // Create test run
        Excel_Import test = new Excel_Import();
        test.importTeams();
        test.printSheetData(test.teams);
    }
}
