package main.java.Excel_Import_Export;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.Scheduler.*;
import main.java.Scheduler.Exception;

/**
 * Converts the Excel imported ArrayList to their Respective Data Types
 * 
 * @author Quinn Sondermeyer, Brady Norton, Faris Abo-Mazeed
 */
public class CreateDataStrucs {
	private ArrayList<League> leagues;
	private ArrayList<Division> divisions;
	private ArrayList<Team> teams;
	private ArrayList<Arena> arenas;
	private ArrayList<TimeSlot> timeslots;
	
	
	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * Constructor to convert ArrayList<ArrayList<Object>> list from excel file to their respective Data Types
	 * @param teamsStr
	 * @param timeExceptions
	 * @param dateExceptions
	 * @param arenasStr
	 * @param timeSlotsStr
	 */
	public CreateDataStrucs(ArrayList<ArrayList<String>> teamsStr, ArrayList<ArrayList<Object>> timeExceptions, ArrayList<ArrayList<Object>> dateExceptions, 
							ArrayList<ArrayList<String>> arenasStr, ArrayList<ArrayList<Object>> timeSlotsStr,ArrayList<ArrayList<String>> homeArenas) {
		createDivLegueTeams(teamsStr);
		createExceptions(timeExceptions,dateExceptions);
		createArenas(arenasStr);
		createTimeSlots(timeSlotsStr);
		//teamSetCenterPoint(homeArenas); 		// Add in once Fixed
		System.out.println();
	}

	/**
	 * Creates list of teams, division, league types 
	 * 
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * @param teams
	 */
	private void createDivLegueTeams(ArrayList<ArrayList<String>> teamsStr) {
		
		leagues = new ArrayList<League>();
		divisions = new ArrayList<Division>();
		teams = new ArrayList<Team>();
		
		for (int i = 0; i < teamsStr.get(0).size(); i++) {

			String name = (String) teamsStr.get(1).get(i);
//			float longitude = Float.parseFloat( (String) teamsstr.get(x).get(5));
//			float latitude = Float.parseFloat( (String) teamsstr.get(x).get(6));	will calculate location based on home arena
			
			/*
			 * Assign Division and make division list
			 */
			boolean contains = false;
			Division tempDiv = null; 		// Has to be set look at *1 and *2
			for (int j = 0 ; j < divisions.size() ; j ++) {
				if (divisions.get(j).getName().equals((String) teamsStr.get(3).get(i))) {
					contains = true; 		//*1
					tempDiv = divisions.get(j);
				}
			}
			if (!contains) { //*2
				tempDiv = new Division((String) teamsStr.get(3).get(i));
				divisions.add(tempDiv);
			}
			
			/*
			 * Assign League
			 */
			
//			contains = false;
//			League tempLeague = null; // same as div
//			for (int j = 0 ; j < leagues.size() ; j ++) {
//				if (leagues.get(j).getName().equals((String) teamsStr.get(i).get(6))) {
//					contains = true;
//					tempLeague = leagues.get(j);
//					if (!tempLeague.getDivisions().contains(tempDiv)) {
//						leagues.get(j).addDivision(tempDiv);
//					}
//				}
//			}
//			if (!contains) {
//				ArrayList<Division> d = new ArrayList<Division>();
//				d.add(tempDiv);
//				tempLeague = new League((String) teamsstr.get(i).get(3),d);
//				leagues.add(tempLeague);
//			}
			
			/*
			 * Get Tier
			 */
			String teirNumStr = teamsStr.get(4).get(i);
			Tier tier = Tier.fromInteger((int) Float.parseFloat(teirNumStr));
			
			/*
			 * Create Team and add to divisions and teams
			 */
			Team temp = new Team( name,(float) 0.0, (float) 0.0, tempDiv, tier);
			tempDiv.addTeam(temp);
			teams.add(temp);
		}
		
	}
	
	
	
	/**
	 * Creates and adds exceptions to a list in the the teams before created
	 * 
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * @param timeExceptions
	 * @param dateExceptions
	 */
	private void createExceptions(ArrayList<ArrayList<Object>> timeExceptions, ArrayList<ArrayList<Object>> dateExceptions) {
		LocalDateTime start;
		LocalDateTime end;
		
		/*
		 *  Add Time Exceptions
		 */
		for (int x = 0 ; x < timeExceptions.get(0).size() ; x++) {
			Team temp = getTeamFromStr((String)timeExceptions.get(0).get(x), (String)timeExceptions.get(1).get(x));
			LocalDateTime startDay = (LocalDateTime)timeExceptions.get(2).get(x);
			String startStr = "" + startDay.getMonthValue() + "/" + startDay.getDayOfMonth() + "/" + startDay.getYear();
			start = generateDateTime(  startStr, ((String)timeExceptions.get(3).get(x)));
			Exception tempException = new Exception(start,start);
			temp.addException(tempException);
		}
		
		
		/*
		 *  Add Date Exceptions
		 */
		for (int x = 0 ; x < dateExceptions.get(0).size() ; x++) {
			Team temp = getTeamFromStr((String)dateExceptions.get(0).get(x), (String)dateExceptions.get(1).get(x));
			LocalDateTime startDay = (LocalDateTime)dateExceptions.get(2).get(x);
			String startStr = "" + startDay.getMonthValue() + "/" + startDay.getDayOfMonth() + "/" + startDay.getYear();
			start = generateDateTime(  startStr, "00:00");
			
			LocalDateTime endDay = (LocalDateTime)dateExceptions.get(3).get(x);
			String endStr = "" + endDay.getMonthValue() + "/" + endDay.getDayOfMonth() + "/" + endDay.getYear();
			end = generateDateTime(endStr, "23:59");
			Exception tempException = new Exception(start,end);
			temp.addException(tempException);
		}

	}
	
	/**
	 * Create Arena Types from list containing
	 * Name, Location X(Lat), Location Y (long)
	 * 
	 * @author Quinn Sondermeyer
	 * @param teamsstr
	 */
	private void createArenas(ArrayList<ArrayList<String>> arenasStr) {
		arenas = new ArrayList<Arena>();
		for (int i = 0 ; i < arenasStr.get(0).size(); i++) {
			arenas.add(new Arena((String) arenasStr.get(0).get(i), Float.parseFloat((String) arenasStr.get(1).get(i)), Float.parseFloat((String) arenasStr.get(2).get(i) )));
		}
		
	}
	
	/**
	 * Creates TimeSlot data types and puts them in a list in the timeSlots variable
	 * 
	 * @author Quinn Sondermeyer
	 * @param timeSlotsStr
	 */
	private void createTimeSlots(ArrayList<ArrayList<Object>> timeSlotsStr) {
		timeslots = new ArrayList<TimeSlot>();
		for (int i = 0 ; i < timeSlotsStr.get(0).size(); i++) {
			
			/*
			 * Create DateTime
			 */
			LocalDateTime Day = (LocalDateTime)timeSlotsStr.get(1).get(i);
			String DayStr = "" + Day.getMonthValue() + "/" + Day.getDayOfMonth() + "/" + Day.getYear();
			LocalDateTime dateTime = generateDateTime(DayStr,(String)timeSlotsStr.get(2).get(i));
			
			/*
			 * Get Arena
			 */
			Arena arena = getArena((String)timeSlotsStr.get(0).get(i));
			
			/*
			 * get Division
			 */
			Division div = getDiv((String)timeSlotsStr.get(3).get(i));
			
			timeslots.add(new TimeSlot(dateTime, arena, div));
		}
		
	}
	




	/*
	 *  Helper Functions
	 */
	/**
	 * Searches for a Team with the name and division
	 * 
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * @param division
	 * @param teamStr
	 * @return Team from given division with given name
	 */
	private Team getTeamFromStr(String division, String teamStr) {
		Team team = null;
		//for (League l : leagues) {// one league change
			for (Division d : divisions) { 
				if (d.getName().equals(division)) {
					for (Team t : d.getTeams()) {
						if (t.getName().equals(teamStr)){
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
	 * 
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * @param date "MM,DD,YYYY"
	 * @param time "HH:MM"
	 * @return LocalDateTime value from given parameters
	 */
	private LocalDateTime generateDateTime(String date, String time) {
		String[] dateStr = date.split("/");
		String[] timeStr = time.split(":");
		return LocalDateTime.of((Integer.parseInt(dateStr[2])), (Integer.parseInt(dateStr[0])), (Integer.parseInt(dateStr[1])), (Integer.parseInt(timeStr[0])),(Integer.parseInt(timeStr[1]))) ;  
		
	}
	
	/**
	 * Gets The Arena from the given name
	 * 
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


	/**
	 * Method to set location radius for a team that's defined by their given home arenas
	 *
	 * @author Brady Norton
	 * @param teamArenas list of home arenas a team can play at
	 */
	private void teamSetCenterPoint(ArrayList<ArrayList<String>> teamArenas) {
		/*
		 * Iterate over teams
		 */
		for (int i = 0; i < teamArenas.get(0).size(); i++) {
			 /*
			  * Find the potential home arenas for team
			  */
			String team =  teamArenas.get(0).get(i);
			String division = teamArenas.get(1).get(i);
			Arena a = getArena( teamArenas.get(2).get(i));

			/*
			 * Add arena to team
			 */
			getTeamFromStr(division, team).addArena(a);
		}

		for(Team t: teams) {
			ArrayList<Arena> arenas = t.getHomeArenas();
			double lat1, lat2, lat3;
			double lon1, lon2, lon3;
			double dLon, bX, bY;

			switch(arenas.size()) {
				case 1:
					t.setLatitude(arenas.get(0).getLatitude());
					t.setLongitude(arenas.get(0).getLongitude());
				case 2:
					
					/*
					 *  Convert arena 1 coords to rad
					 */
					lat1 = Math.toRadians(arenas.get(0).getLatitude());
					lon1 = Math.toRadians(arenas.get(0).getLongitude());

					/*
					 *  Convert arena 2 coords to rad
					 */
					lat2 = Math.toRadians(arenas.get(1).getLatitude());
					lon2 = Math.toRadians(arenas.get(1).getLongitude());

					/*
					 *  Intermediate variables
					 */
					dLon = Math.toRadians(lon2 - lon1);
					bX = Math.cos(lat2) * Math.cos(dLon);
					bY = Math.cos(lat2) * Math.sin(dLon);

					/*
					 *  Calculate midpoint
					 */
					double latF = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + bX)*(Math.cos(lat1)+ bX) + bY * bY));
					double lonF = lon1 + Math.atan2(bY, Math.cos(lat1) + bX);

					/*
					 *  Set midpoint for team
					 */
					t.setLatitude( Math.toDegrees(latF));
					t.setLongitude( Math.toDegrees(lonF));
				case 3:
					lat1 = Math.toRadians(arenas.get(0).getLatitude());
					lon1 = Math.toRadians(arenas.get(0).getLongitude());

					lat2 = Math.toRadians(arenas.get(1).getLatitude());
					lon2 = Math.toRadians(arenas.get(1).getLongitude());

					lat3 = Math.toRadians(arenas.get(2).getLatitude());
					lon3 = Math.toRadians(arenas.get(2).getLongitude());

					/*
					 *  Average of points
					 */
					latF = (lat1 + lat2 + lat3) / 3;
					lonF = (lon1 + lon2 + lon3) / 3;

					/*
					 *  Set midpoint for team
					 */
					t.setLatitude( Math.toDegrees(latF));
					t.setLongitude( Math.toDegrees(lonF));
			}
		}
	}
	
	/*
	 * Getters and Setters
	 * @author Quinn Sondermeyer
	 */

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
