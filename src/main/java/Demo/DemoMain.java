package Demo;

import Excel_Import_Export.CreateDataStrucs;
import Excel_Import_Export.ExcelImport;
import Scheduler.League;
import Scheduler.Schedule;

public class DemoMain {
    public static void main(String[] args) {
        ExcelImport excelImport = new ExcelImport();

        /*
         * Import sheets
         */
        excelImport.importData();

        /*
         * Create Data Types
         */
        CreateDataStrucs strucs = new CreateDataStrucs(excelImport.getTeams(), excelImport.getTimeExceptions(),excelImport.getDateExceptions(), excelImport.getArenas(),excelImport.getTimeSlots(),excelImport.getHomeArenas());


        League league = new League("League", strucs.getDivisions(),strucs.getTimeslots(), strucs.getArenas());
        league.generateSchedules();

        for (Schedule s: league.getSchedules()) {
            if (!s.getTimeSlots().isEmpty()) {
                s.createSchedule();
            }
        }
        System.out.print(false);
    }
}
