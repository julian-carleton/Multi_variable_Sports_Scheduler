package Excel_Import_Export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Imports the Data From Excel
 * 
 * @author Brady Norton
 *
 */
public class ExcelImport {
	
	private ArrayList<ArrayList<ArrayList<Object>>> sheets; // imported Data in 3D ArrayList
	
	private XSSFWorkbook wb;		//	Excel workbook

    /**
     * Constructor for Excel_Import class
     */
    public ExcelImport(InputStream schedulingData) {
        try {
            InputStream stream = schedulingData;	// InputStream

            wb = new XSSFWorkbook(stream); 	// Input_Proposal Workbook
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Runs all the functions to import data from excel sheet
     * 
     * @author Quinn Sondermeyer
     */
    public void importData() {
    	
    	Iterator<Sheet> sheetIterator = wb.sheetIterator();
    	sheets = new ArrayList<ArrayList<ArrayList<Object>>>();	// create list to be added to
    	
    	int sheetCount = 0;
    	int rowCount = 0;
    	
    	while (sheetIterator.hasNext()) { 	// Iterate over data
    		
    		sheets.add(new ArrayList<ArrayList<Object>>());		// create list to be added to
			Sheet sheet = sheetIterator.next();					// Next Row
			Iterator<Row> rowIterator = sheet.rowIterator(); 	// Iterator
			rowCount = 0;
	    	
			while (rowIterator.hasNext()) { 	// Iterate over data
				sheets.get(sheetCount).add(new ArrayList<Object>()); 	// create list to be added to
			
				Row row = rowIterator.next();	// Next Row
				
				Iterator<Cell> cellIterator = row.cellIterator();	// Iterate over cells in row
				
			    while (cellIterator.hasNext()) {
			    	Cell cell = cellIterator.next();
			    	
			    	switch (cell.getCellType()) {					// Based on type import Data add to 3D sheet list
				    	case BOOLEAN:
				    		sheets.get(sheetCount).get(rowCount).add(cell.getBooleanCellValue());
				            break;
				        	
				        case NUMERIC:
				        	if(DateUtil.isCellDateFormatted(cell)){
				        		sheets.get(sheetCount).get(rowCount).add(cell.getLocalDateTimeCellValue());
			        			break;
				        	}else {
			        			sheets.get(sheetCount).get(rowCount).add(cell.getNumericCellValue());
			        			break;
				        	}				        	
				        case STRING:
				        	sheets.get(sheetCount).get(rowCount).add(cell.getStringCellValue());
				            break;
					default:
						break;
			    	}
			    }
			    rowCount++;
			}
			sheetCount++;
    	}
    }
  
    
    /*
     *  Getters and setters
     */
    
	public ArrayList<ArrayList<ArrayList<Object>>> getSheets() {
		return sheets;
	}

	
	


}
