<<<<<<< Updated upstream
package main.java.Excel_Import_Export;
=======
package Excel_Import_Export;
>>>>>>> Stashed changes

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
<<<<<<< Updated upstream

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

=======
import org.apache.xmlbeans.ResourceLoader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import Scheduler.Team;
import Scheduler.Exception;

/**
 * 
 * @author Brady Norton
 *
 */
>>>>>>> Stashed changes
public class Excel_Import {
    // Arrays to hold excel info
    ArrayList<String> headers;
    ArrayList<String> league;
    ArrayList<String> name;
    ArrayList<String> org;
    ArrayList<String> division;
    ArrayList<String> tier;

<<<<<<< Updated upstream
    public static final String EXCEL_INPUTS = "Input_Proposal.xlsx";
=======
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
>>>>>>> Stashed changes

    /**
     * Constructor for Excel_Import class
     */
    public Excel_Import() {
<<<<<<< Updated upstream
    }

    public void importData() {
=======
        try {
            // InputStream
            InputStream schedulingData = Excel_Import.class.getResourceAsStream("./Input_Proposal.xlsx");

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
>>>>>>> Stashed changes
        // Initialize Arrays
        headers = new ArrayList<>();
        league = new ArrayList<>();
        name = new ArrayList<>();
        org = new ArrayList<>();
        division = new ArrayList<>();
        tier = new ArrayList<>();

<<<<<<< Updated upstream
        File file = new File("C:\\Users\\Brady\\Desktop\\Multi-variable-Sports-Scheduler\\src\\main\\resources\\Input_Proposal.xlsx");

        // Extract Excel File Data
        try {
            // InputStream
            FileInputStream schedulingData = new FileInputStream(file);
=======
        // Extract Excel File Data
        try {
            // InputStream
            InputStream schedulingData = Excel_Import.class.getResourceAsStream("/Input_Proposal.xlsx");
>>>>>>> Stashed changes

            // Input_Proposal Workbook
            XSSFWorkbook wb = new XSSFWorkbook(schedulingData);

            // Sheets
            XSSFSheet teamsSheet = wb.getSheetAt(0);
            XSSFSheet timeExceptionsSheet = wb.getSheetAt(1);
            XSSFSheet dataExceptionsSheet = wb.getSheetAt(2);
<<<<<<< Updated upstream
            XSSFSheet dateExceptionsSheet = wb.getSheetAt(3);
            XSSFSheet arenasSheet = wb.getSheetAt(4);
            XSSFSheet timeSlotsSheet = wb.getSheetAt(5);
            XSSFSheet homeArenasSheet = wb.getSheetAt(6);
            //XSSFSheet otherSheet = wb.getSheetAt(7);
=======
            XSSFSheet arenasSheet = wb.getSheetAt(3);
            XSSFSheet timeSlotsSheet = wb.getSheetAt(4);
            XSSFSheet homeArenasSheet = wb.getSheetAt(5);
            XSSFSheet otherSheet = wb.getSheetAt(6);
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
                            tier.add(cell.getStringCellValue());
=======
                            tier.add(String.valueOf(cell.getNumericCellValue()));
>>>>>>> Stashed changes
                        }
                    }
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        // Print team names
        for (int i = 0; i < name.size(); i++) {
<<<<<<< Updated upstream
            System.out.println("Team: " + i + name.get(i));
        }
    }

    /**
     * Creates an InputStreamReader for a file with the specified name.
     *
     * @param name the name of the file (and optionally, its folder)
     * @return inputStreamReader an InputStreamReader for the file
     */
    private InputStreamReader createInputStreamReader(String name) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
            assert inputStream != null;
            // Specify CharSet as UTF-8
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println(name + " was not found.");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Excel_Import test = new Excel_Import();

        test.importData();
=======
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
>>>>>>> Stashed changes
    }
}
