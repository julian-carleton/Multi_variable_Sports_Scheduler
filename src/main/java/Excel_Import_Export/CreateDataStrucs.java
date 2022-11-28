package main.java.Excel_Import_Export;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.Scheduler.*;
import main.java.Scheduler.Exception;

public class CreateDataStrucs {
	private ArrayList<League> leagues;
	private ArrayList<Division> divisions;
	private ArrayList<Team> teams;
	private ArrayList<Arena> arenas;
	private ArrayList<TimeSlot> timeslots;
	
	
	/**
	 * Constructor to convert ArrayList<ArrayList<Object>> list from excel file to their respective Data Types
	 * @param teamsstr
	 * @param Time_Exceptions
	 * @param Date_Exceptions
	 * @param arenasStr
	 * @param timeSlotsStr
	 */
	public CreateDataStrucs(ArrayList<ArrayList<Object>> teamsstr, ArrayList<ArrayList<Object>> Time_Exceptions, ArrayList<ArrayList<Object>> Date_Exceptions, 
							ArrayList<ArrayList<Object>> arenasStr, ArrayList<ArrayList<Object>> timeSlotsStr) {
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
	public void createDivLegueTeams(ArrayList<ArrayList<Object>> teamsstr) {
		
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
			
			contains = false;
			League tempLeague = null; // same as div
			for (int y = 0 ; y < leagues.size() ; y ++) {
				if (leagues.get(y).getName().equals((String) teamsstr.get(x).get(6))) {
					contains = true;
					tempLeague = leagues.get(y);
					if (!tempLeague.getDivisions().contains(tempdiv)) {
						leagues.get(y).addDivision(tempdiv);
					}
				}
			}
			if (!contains) {
				ArrayList<Division> d = new ArrayList<Division>();
				d.add(tempdiv);
				tempLeague = new League((String) teamsstr.get(x).get(3),d);
				leagues.add(tempLeague);
			}
			
			
			Tier tier = Tier.fromInteger(Integer.parseInt((String) teamsstr.get(x).get(4)));
			Team temp = new Team( name, longitude, latitude, tempdiv, tier, tempLeague);
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
	public void createArenas(ArrayList<ArrayList<Object>> arenasStr) {
		
		for (ArrayList<Object> a : arenasStr) {
			arenas = new ArrayList<Arena>();
			arenas.add(new Arena((String) a.get(0), Float.parseFloat((String) a.get(1)), Float.parseFloat((String) a.get(2) )));
		}
		
	}
	
	
	public void createTimeSlots(ArrayList<ArrayList<Object>> timeSlotsStr) {
		
		for (ArrayList<Object> t : timeSlotsStr) {
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

	/**
	 * Method to set location radius for a team that's defined by their given home arenas
	 *
	 * @param teamArenas list of home arenas a team can play at
	 */
	private void makeTeamRadius(ArrayList<ArrayList<Object>> teamArenas) {
		// Iterate over teams
		for (int i = 0; i < teamArenas.get(0).size(); i++) {
			 // Find the potential home arenas for team
			String team = (String) teamArenas.get(0).get(i);
			String division = (String) teamArenas.get(1).get(i);
			Arena a = getArena((String) teamArenas.get(2).get(i));

			// Add arena to team
			getTeamFromStr(division, team).addArena(a);
		}

		for(Team t: teams) {
			ArrayList<Arena> arenas = t.getHomeArenas();
			double lat1, lat2, lat3;
			double lon1, lon2, lon3;
			double dLon, Bx, By;

			switch(arenas.size()) {
				case 1:
					t.setLatitude(arenas.get(0).getLatitude());
					t.setLongitude(arenas.get(0).getLongitude());
				case 2:
					// Convert arena 1 coords to rad
					lat1 = Math.toRadians(arenas.get(0).getLatitude());
					lon1 = Math.toRadians(arenas.get(0).getLongitude());

					// Convert arena 2 coords to rad
					lat2 = Math.toRadians(arenas.get(1).getLatitude());
					lon2 = Math.toRadians(arenas.get(1).getLongitude());

					// Intermediate variables
					dLon = Math.toRadians(lon2 - lon1);
					Bx = Math.cos(lat2) * Math.cos(dLon);
					By = Math.cos(lat2) * Math.sin(dLon);

					// Calculate midpoint
					double latF = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx)*(Math.cos(lat1)+ Bx) + By * By));
					double lonF = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

					// Set midpoint for team
					t.setLatitude((float) Math.toDegrees(latF));
					t.setLongitude((float) Math.toDegrees(lonF));
				case 3:
					lat1 = Math.toRadians(arenas.get(0).getLatitude());
					lon1 = Math.toRadians(arenas.get(0).getLongitude());

					lat2 = Math.toRadians(arenas.get(1).getLatitude());
					lon2 = Math.toRadians(arenas.get(1).getLongitude());

					lat3 = Math.toRadians(arenas.get(2).getLatitude());
					lon3 = Math.toRadians(arenas.get(2).getLongitude());

					// Average of points
					latF = (lat1 + lat2 + lat3) / 3;
					lonF = (lon1 + lon2 + lon3) / 3;

					// Set midpoint for team
					t.setLatitude((float) Math.toDegrees(latF));
					t.setLongitude((float) Math.toDegrees(lonF));
			}
		}
	}

	/**
	 * Temporary method to check if team can play at an arena as a home team
	 *
	 * Note: this should be changed to use location as a check
	 *
	 * @param team
	 * @param arena
	 * @return
	 */
	public boolean canTeamPlay(Team team, Arena arena) {
		// Check if given arena is in list of home arenas
		for(Arena a: team.getHomeArenas()) {
			if(arena != a) {
				return false;
			}
		}
		return true;
	}
}
