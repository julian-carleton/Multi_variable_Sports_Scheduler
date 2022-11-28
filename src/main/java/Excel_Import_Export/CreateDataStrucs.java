package main.java.Excel_Import_Export;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.Scheduler.*;
import main.java.Scheduler.Exception;

public class CreateDataStrucs {
	private ArrayList<League> leagues;
	private ArrayList<Division> divisions;
	private ArrayList<Team> teams;
	private ArrayList<Arena> arenas;
	private ArrayList<TimeSlot> timeslots;
	
	
	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * Constructor to convert ArrayList<ArrayList<Object>> list from excel file to their respective Data Types
	 * @param teamsstr
	 * @param Time_Exceptions
	 * @param Date_Exceptions
	 * @param arenasStr
	 * @param timeSlotsStr
	 */
	public CreateDataStrucs(ArrayList<ArrayList<String>> teamsstr, ArrayList<ArrayList<Object>> Time_Exceptions, ArrayList<ArrayList<Object>> Date_Exceptions, 
							ArrayList<ArrayList<String>> arenasStr, ArrayList<ArrayList<Object>> timeSlotsStr) {
		createDivLegueTeams(teamsstr);
		createExceptions(Time_Exceptions,Date_Exceptions);
		createArenas(arenasStr);
		createTimeSlots(timeSlotsStr);
		System.out.println();
	}

	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * Creates list of teams, division, league types 
	 * @param teams
	 */
	public void createDivLegueTeams(ArrayList<ArrayList<String>> teamsstr) {
		
		leagues = new ArrayList<League>();
		divisions = new ArrayList<Division>();
		teams = new ArrayList<Team>();
		
		for (int x = 0 ; x < teamsstr.get(0).size() ; x++) {
			//String name, float longitude, float latitude,  Division division, Tier tier, League league
			String name = (String) teamsstr.get(1).get(x);
//			float longitude = Float.parseFloat( (String) teamsstr.get(x).get(5));
//			float latitude = Float.parseFloat( (String) teamsstr.get(x).get(6));	will calculate location based on home arena
			
			//Assign Division and make division list
			boolean contains = false;
			Division tempdiv = null; // Has to be set look at *1 and *2
			for (int y = 0 ; y < divisions.size() ; y ++) {
				if (divisions.get(y).getName().equals((String) teamsstr.get(3).get(x))) {
					contains = true; //*1
					tempdiv = divisions.get(y);
				}
			}
			if (!contains) { //*2
				tempdiv = new Division((String) teamsstr.get(3).get(x));
				divisions.add(tempdiv);
			}
			
//			contains = false;
//			League tempLeague = null; // same as div
//			for (int y = 0 ; y < leagues.size() ; y ++) {
//				if (leagues.get(y).getName().equals((String) teamsstr.get(x).get(6))) {
//					contains = true;
//					tempLeague = leagues.get(y);
//					if (!tempLeague.getDivisions().contains(tempdiv)) {
//						leagues.get(y).addDivision(tempdiv);
//					}
//				}
//			}
//			if (!contains) {
//				ArrayList<Division> d = new ArrayList<Division>();
//				d.add(tempdiv);
//				tempLeague = new League((String) teamsstr.get(x).get(3),d);
//				leagues.add(tempLeague);
//			}
			String teirnumStr = teamsstr.get(4).get(x);
			
			Tier tier = Tier.fromInteger((int) Float.parseFloat(teirnumStr));
			Team temp = new Team( name,(float) 0.0, (float) 0.0, tempdiv, tier);
			tempdiv.addTeam(temp);
			teams.add(temp);
		}
		
	}
	
	
	
	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * adds exceptions to the teams created
	 * @param Time_Exceptions
	 * @param Date_Exceptions
	 */
	public void createExceptions(ArrayList<ArrayList<Object>> Time_Exceptions, ArrayList<ArrayList<Object>> Date_Exceptions) {
		LocalDateTime start;
		LocalDateTime end;
		//LocalDateTime start,LocalDateTime end
		
		
		// Add Time Exceptions
		for (int x = 0 ; x < Time_Exceptions.get(0).size() ; x++) {
			Team temp = getTeamFromStr((String)Time_Exceptions.get(0).get(x), (String)Time_Exceptions.get(1).get(x));
			LocalDateTime startDay = (LocalDateTime)Time_Exceptions.get(2).get(x);
			String startStr = "" + startDay.getMonthValue() + "/" + startDay.getDayOfMonth() + "/" + startDay.getYear();
			start = generateDateTime(  startStr, ((String)Time_Exceptions.get(3).get(x)));
			Exception tempException = new Exception(start,start);
			temp.addException(tempException);
		}
		
		
		// Add Date Exceptions
		for (int x = 0 ; x < Date_Exceptions.get(0).size() ; x++) {
			Team temp = getTeamFromStr((String)Date_Exceptions.get(0).get(x), (String)Date_Exceptions.get(1).get(x));
			LocalDateTime startDay = (LocalDateTime)Date_Exceptions.get(2).get(x);
			String startStr = "" + startDay.getMonthValue() + "/" + startDay.getDayOfMonth() + "/" + startDay.getYear();
			start = generateDateTime(  startStr, "00:00");
			
			LocalDateTime endDay = (LocalDateTime)Date_Exceptions.get(3).get(x);
			String endStr = "" + endDay.getMonthValue() + "/" + endDay.getDayOfMonth() + "/" + endDay.getYear();
			end = generateDateTime(endStr, "23:59");
			Exception tempException = new Exception(start,end);
			temp.addException(tempException);
		}
		
		
	}
	
	/**
	 * @author Quinn Sondermeyer
	 * Create Arena Types from list containing
	 * Name, Location X(Lat), Location Y (long)
	 * 
	 * @param teamsstr
	 */
	public void createArenas(ArrayList<ArrayList<String>> arenasStr) {
		arenas = new ArrayList<Arena>();
		for (int i = 0 ; i < arenasStr.get(0).size(); i++) {
			arenas.add(new Arena((String) arenasStr.get(0).get(i), Float.parseFloat((String) arenasStr.get(1).get(i)), Float.parseFloat((String) arenasStr.get(2).get(i) )));
		}
		
	}
	
	
	public void createTimeSlots(ArrayList<ArrayList<Object>> timeSlotsStr) {
		timeslots = new ArrayList<TimeSlot>();
		for (int i = 0 ; i < timeSlotsStr.get(0).size(); i++) {
			
			//create DateTime
			LocalDateTime Day = (LocalDateTime)timeSlotsStr.get(1).get(i);
			String DayStr = "" + Day.getMonthValue() + "/" + Day.getDayOfMonth() + "/" + Day.getYear();
			LocalDateTime dateTime = generateDateTime(DayStr,(String)timeSlotsStr.get(2).get(i));
			
			//get Arena
			Arena arena = getArena((String)timeSlotsStr.get(0).get(i));
			
			//get Division
			Division div = getDiv((String)timeSlotsStr.get(3).get(i));
			
			timeslots.add(new TimeSlot(dateTime, arena, div));
		}
		
	}
	




	// Helper Functions
	
	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * @param division
	 * @param teamstr
	 * @return Team from given division with given name
	 */
	private Team getTeamFromStr(String division, String teamstr) {
		Team team = null;
		//for (League l : leagues) {// one league change
			for (Division d : divisions) { 
				if (d.getName().equals(division)) {
					for (Team t : d.getTeams()) {
						if (t.getName().equals(teamstr)){
							team = t;

							} 
					}
				}
			}
			
		//}
		return team;
	}
	
	
	/**
	 * Generate LocalDateTime Type given String
	 * @param date "MM,DD,YYYY"
	 * @param time "HH:MM"
	 * @return LocalDateTime value from given parameters
	 */
	private LocalDateTime generateDateTime(String date, String time) {
		String[] datestr = date.split("/");
		String[] timestr = time.split(":");
		return LocalDateTime.of((Integer.parseInt(datestr[2])), (Integer.parseInt(datestr[0])), (Integer.parseInt(datestr[1])), (Integer.parseInt(timestr[0])),(Integer.parseInt(timestr[1]))) ;  
		
	}
	
	/**
	 * @author Quinn Sondermeyer
	 * @param name
	 * @return Arena with given name
	 */
	private Arena getArena(String name) {
		Arena temp = null;
		for (Arena a:arenas) {
			if (a.getName() == name) {
				temp = a;
			}
		}
		return temp;
	}
	
	/**
	 * @author Quinn Sondermeyer
	 * @param string
	 * @return Division from name provided
	 */
	private Division getDiv(String name) {
		Division temp = null;
		for (Division d:divisions) {
			if (d.getName() == name) {
				temp = d;
			}
		}
		return temp;
	}

	
	//Getters and setters
	public ArrayList<League> getLeagues() {
		return leagues;
	}

	public ArrayList<Division> getDivisions() {
		return divisions;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public ArrayList<TimeSlot> getTimeslots() {
		return timeslots;
	}
	
	
	
	
	
}
