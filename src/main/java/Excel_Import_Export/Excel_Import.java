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

public class Excel_Import {
    // Arrays to hold excel info
    ArrayList<String> headers;
    ArrayList<String> league;
    ArrayList<String> name;
    ArrayList<String> org;
    ArrayList<String> division;
    ArrayList<String> tier;

    /**
     * Constructor for Excel_Import class
     */
    public Excel_Import() {

    }

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

    /**
     *
     */
    public void importTeams() {

    }

    public void importTimeExceptions() {

    }

    public void importDateExceptions() {

    }

    public void importArenas() {

    }

    public void importTimeSlots() {

    }

    public void importHomeArenas() {

    }

    public void importOtherData() {

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

    public static void main(String[] args) throws IOException {
        // Create test run
        Excel_Import test = new Excel_Import();
        test.importData();
    }
}
