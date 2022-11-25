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
	public CreateDataStrucs(ArrayList<ArrayList<String>> teamsstr, ArrayList<ArrayList<String>> Time_Exceptions, ArrayList<ArrayList<String>> Date_Exceptions, 
							ArrayList<ArrayList<String>> arenasStr, ArrayList<ArrayList<String>> timeSlotsStr) {
		createDivLegueTeams(teamsstr);
		createExceptions(Time_Exceptions,Date_Exceptions);
		createArenas(arenasStr);
		createTimeSlots(timeSlotsStr);
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
		
		for (int x = 0 ; x < teamsstr.size() ; x++) {
			//String name, float longitude, float latitude,  Division division, Tier tier, League league
			String name = (String) teamsstr.get(x).get(1);
			float longitude = Float.parseFloat( (String) teamsstr.get(x).get(5));
			float latitude = Float.parseFloat( (String) teamsstr.get(x).get(6));
			
			//Assign Division and make division list
			boolean contains = false;
			Division tempdiv = null; // Has to be set look at *1 and *2
			for (int y = 0 ; y < divisions.size() ; y ++) {
				if (divisions.get(y).getName().equals((String) teamsstr.get(x).get(6))) {
					contains = true; //*1
					tempdiv = divisions.get(y);
				}
			}
			if (!contains) { //*2
				tempdiv = new Division((String) teamsstr.get(x).get(3));
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
			
			
			Tier tier = Tier.fromInteger(Integer.parseInt((String) teamsstr.get(x).get(4)));
			Team temp = new Team( name, longitude, latitude, tempdiv, tier);
			teams.add(temp);
		}
		
	}
	
	
	
	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * adds exceptions to the teams created
	 * @param Time_Exceptions
	 * @param Date_Exceptions
	 */
	public void createExceptions(ArrayList<ArrayList<String>> Time_Exceptions, ArrayList<ArrayList<String>> Date_Exceptions) {
		LocalDateTime start;
		LocalDateTime end;
		//LocalDateTime start,LocalDateTime end
		
		
		// Add Time Exceptions
		for (int x = 0 ; x < Time_Exceptions.size() ; x++) {
			Team temp = getTeamFromStr((String)Time_Exceptions.get(x).get(0), (String)Time_Exceptions.get(x).get(1));
			start = generateDateTime(((String)Time_Exceptions.get(x).get(2)), ((String)Time_Exceptions.get(x).get(3)));
			Exception tempException = new Exception(start,start);
			temp.addException(tempException);
		}
		
		
		// Add Date Exceptions
		for (int x = 0 ; x < Date_Exceptions.size() ; x++) {
			Team temp = getTeamFromStr((String)Date_Exceptions.get(x).get(0), (String)Date_Exceptions.get(x).get(1));
			start = generateDateTime(((String)Date_Exceptions.get(x).get(2)), "00:00");
			end = generateDateTime(((String)Date_Exceptions.get(x).get(3)), "24:59");
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
		
		for (ArrayList<String> a : arenasStr) {
			arenas = new ArrayList<Arena>();
			arenas.add(new Arena((String) a.get(0), Float.parseFloat((String) a.get(1)), Float.parseFloat((String) a.get(2) )));
		}
		
	}
	
	
	public void createTimeSlots(ArrayList<ArrayList<String>> timeSlotsStr) {
		
		for (ArrayList<String> t : timeSlotsStr) {
			timeslots = new ArrayList<TimeSlot>();
			//create DateTime
			LocalDateTime dateTime = generateDateTime((String)t.get(1),(String)t.get(2));
			
			//get Arena
			Arena arena = getArena((String)t.get(0));
			
			//get Division
			Division div = getDiv((String)t.get(3));
			
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
		for (League l : leagues) {
			for (Division d : l.getDivisions()) {
				if (d.getName().equals(division)) {
					for (Team t : d.getTeams()) {
						if (t.getName().equals(teamstr)){
							team = t;

							} 
					}
				}
			}
			
		}
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
