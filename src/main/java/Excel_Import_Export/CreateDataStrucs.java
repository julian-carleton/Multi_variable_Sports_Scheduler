package Excel_Import_Export;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Scheduler.*;
import Scheduler.Exception;

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
	
	private ArrayList<ArrayList<Object>> modes;
	private ArrayList<ArrayList<Object>> matchups;
	
	
	
	/**
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * Constructor to convert ArrayList<ArrayList<Object>> list from excel file to their respective Data Types
	 * @param teamsStr
	 * @param timeExceptions
	 * @param dateExceptions
	 * @param arenasStr
	 * @param timeSlotsStr
	 */
	public CreateDataStrucs(ArrayList<ArrayList<ArrayList<Object>>> sheets) {
		
		ArrayList<ArrayList<Object>> teamsStr = sheets.get(0);
		teamsStr.remove(0);
		ArrayList<ArrayList<Object>> timeExceptions = sheets.get(1);
		timeExceptions.remove(0);
		ArrayList<ArrayList<Object>> dateExceptions = sheets.get(2);
		dateExceptions.remove(0);
		ArrayList<ArrayList<Object>> arenasStr = sheets.get(3);
		arenasStr.remove(0);
		ArrayList<ArrayList<Object>> timeSlotsStr = sheets.get(4);
		timeSlotsStr.remove(0);
		ArrayList<ArrayList<Object>> homeArena = sheets.get(5);
		homeArena.remove(0);
		ArrayList<ArrayList<Object>> matchups = sheets.get(6);
		matchups.remove(0);
		modes = sheets.get(7);
		
		
		
		createDivLegueTeams(teamsStr);
		createExceptions(timeExceptions,dateExceptions);
		createArenas(arenasStr);
		createTimeSlots(timeSlotsStr);
		addHomeArenas(homeArena);
		this.matchups = new ArrayList<ArrayList<Object>>();
		teamSetMatchups(matchups);
		teamSetCenterPoint();
		
	}

	/**
	 * Creates list of teams, division, league types 
	 * 
	 * @author Faris Abo-Mazeed & Quinn Sondermeyer
	 * @param teams
	 */
	private void createDivLegueTeams(ArrayList<ArrayList<Object>> teamsStr) {
		
		leagues = new ArrayList<League>();
		divisions = new ArrayList<Division>();
		teams = new ArrayList<Team>();
		
		for (int i = 0; i < teamsStr.size(); i++) {

			String name = (String) teamsStr.get(i).get(1);
			
			/*
			 * Assign Division and make division list
			 */
			boolean contains = false;
			Division tempDiv = null; 		// Forced to be set -- look at *1 and *2
			for (int j = 0 ; j < divisions.size() ; j ++) {
				if (divisions.get(j).getName().equals((String) teamsStr.get(i).get(3))) {
					contains = true; 		//*1
					tempDiv = divisions.get(j);
				}
			}
			if (!contains) { //*2
				tempDiv = new Division((String) teamsStr.get(i).get(3));
				divisions.add(tempDiv);
			}
			
			/*
			 * Assign League
			 */
			
			contains = false;
			League tempLeague = null; // same as div
			for (int j = 0 ; j < leagues.size() ; j ++) {
				if (leagues.get(j).getName().equals( (String) teamsStr.get(i).get(0))) {
					contains = true;
					tempLeague = leagues.get(j);
					if (!tempLeague.getDivisions().contains(tempDiv)) {
						leagues.get(j).addDivision(tempDiv);
					}
				}
			}
			if (!contains) {
				ArrayList<Division> d = new ArrayList<Division>();
				d.add(tempDiv);
				tempLeague = new League((String) teamsStr.get(i).get(0),d);
				leagues.add(tempLeague);
			}
			
			/*
			 * Get Tier
			 */
			double teirNumDouble = (double) teamsStr.get(i).get(4);
			int teirNumint = (int) teirNumDouble;
			Tier tier = Tier.fromInteger(teirNumint);
			
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
		for (int x = 0 ; x < timeExceptions.size() ; x++) {
			Team temp = getTeamFromStr((String)timeExceptions.get(x).get(0), (String)timeExceptions.get(x).get(1));
			LocalDateTime day = (LocalDateTime)timeExceptions.get(x).get(2);
			LocalDateTime startTime = (LocalDateTime) timeExceptions.get(x).get(3);
			LocalDateTime endTime = (LocalDateTime) timeExceptions.get(x).get(4);
			
			
			LocalDateTime startDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), startTime.getHour(), startTime.getMinute());
			LocalDateTime endDay = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), endTime.getHour(), endTime.getMinute());
			
			
			Exception tempException = new Exception(startDay,endDay);
			temp.addException(tempException);
		}
		
		
		/*
		 *  Add Date Exceptions
		 */
		for (int x = 0 ; x < dateExceptions.size() ; x++) {
			Team temp = getTeamFromStr((String)dateExceptions.get(x).get(0), (String)dateExceptions.get(x).get(1));
			LocalDateTime startDay = (LocalDateTime)dateExceptions.get(x).get(2);
			String startStr = "" + startDay.getMonthValue() + "/" + startDay.getDayOfMonth() + "/" + startDay.getYear();
			start = generateDateTime(  startStr, "00:00");
			
			LocalDateTime endDay = (LocalDateTime)dateExceptions.get(x).get(3);
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
	private void createArenas(ArrayList<ArrayList<Object>> arenasStr) {
		arenas = new ArrayList<Arena>();
		for (int i = 0 ; i < arenasStr.size(); i++) {
			double lat = (double) arenasStr.get(i).get(1);
			double lon = (double) arenasStr.get(i).get(2);
			
			float latF = (float) lat;
			float lonF = (float) lon;
			arenas.add(new Arena((String) arenasStr.get(i).get(0), latF, lonF ));
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
		for (int i = 0 ; i < timeSlotsStr.size(); i++) {
			
			/*
			 * Create DateTime
			 */
			LocalDateTime day = (LocalDateTime)timeSlotsStr.get(i).get(1);
			LocalDateTime time = (LocalDateTime)timeSlotsStr.get(i).get(2);
			
			LocalDateTime slotDatTime = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(), time.getHour(), time.getMinute());
			
			/*
			 * Get Arena
			 */
			Arena arena = getArena((String)timeSlotsStr.get(i).get(0));
			
			/*
			 * get Division
			 */
			ArrayList<Division> divs = getDiv((String)timeSlotsStr.get(i).get(3));
			
			timeslots.add(new TimeSlot(slotDatTime, arena, divs));
		}
		
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
	 * @author Quinn Sondermeyer
	 * 
	 * @param homeArenas
	 */
	private void addHomeArenas(ArrayList<ArrayList<Object>> homeArenas) {
		for (int i = 0; i < homeArenas.size(); i++) {
			Arena tempArena = getArena((String) homeArenas.get(i).get(2));
			for (Team t : teams) {
				Team tempTeam = getTeamFromStr((String) homeArenas.get(i).get(1), (String) homeArenas.get(i).get(0));
				if (tempTeam.equals(t)) {
					t.addArena(tempArena);
				}
			}
		}
	}
	
	/**
	 * Adds a Hash map of matchups of teams and their rank to each team object
	 * @param matchups
	 */
	private void teamSetMatchups(ArrayList<ArrayList<Object>> matchups) {
		for (Team t: this.teams) {							// Make List for each team
			for (int i = 0; i < matchups.size();i++) {		//Go through matchup list and find suitable matchups
				if (t.equals(getTeamFromStr( (String) matchups.get(i).get(0), (String) matchups.get(i).get(1) ))) {
					Team temp = getTeamFromStr( (String) matchups.get(i).get(0), (String) matchups.get(i).get(2) );
					double rank = (double) matchups.get(i).get(3);
					int rankInt = (int) rank;						// convert double to int as double is default number import type for apachie
					t.addMatchup(getTeamFromStr( (String) matchups.get(i).get(0), (String) matchups.get(i).get(2) ), rankInt);
				}
			}
		}
		for (int i = 0; i < matchups.size() ; i++) {
			this.matchups.add(new ArrayList<Object>());
			for (int j = 0; j < matchups.get(i).size() ; j++) {
				if (1 <= j && j <= 2) {
					this.matchups.get(i).add( getTeamFromStr( (String) matchups.get(i).get(0), (String) matchups.get(i).get(j)));
				}else {
					this.matchups.get(i).add(j);
				}
			}
		}
	}


	/**
	 * Method to set location radius for a team that's defined by their given home arenas
	 *
	 * @author Brady Norton
	 * @param teamArenas list of home arenas a team can play at
	 */
	private void teamSetCenterPoint() {
		
		for(Team t: teams) {
			double lat = 0;
			double lon = 0;
			for (Arena a: t.getHomeArenas()) {
				lat = lat + a.getLatitude();
				lon = lon + a.getLongitude();
			}
			lat = lat/t.getHomeArenas().size();		//get average value
			lon = lon/t.getHomeArenas().size();
			
			t.setLatitude( lat);
			t.setLongitude( lon);
			
			t.generateRad();		// generate rad in teams
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
		if (teamStr.equals("Exhibition")) {
			return this.divisions.get(0).getTeams().get(0);
		}
		
		for (Division d : divisions) { 
			if (d.getName().equals(division)) {
				for (Team t : d.getTeams()) {
					if (t.getName().equals(teamStr)){
						team = t;

						} 
				}
			}
		}
			
		return team;
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
	private ArrayList<Division> getDiv(String name) {
		ArrayList<Division> temp = new ArrayList<Division>();
		String[] divs = name.split(",");
		ArrayList<String> divsArrLst = new ArrayList<String>();
		for (int i = 0; i < divs.length; i ++) {
			divsArrLst.add(divs[i]);
		}
		for (Division d:divisions) {
			if (divsArrLst.contains(d.getName())) {
				temp.add(d);
			}
		}
		return temp;
	}
	
	

	/**
	 * Temporary method to check if team can play at an arena as a home team
	 *
	 * Note: this should be changed to use location as a check
	 * @author Brady Norton
	 * @param team
	 * @param arena
	 * @return
	 */
	public boolean canTeamPlay(Team team, Arena arena) {
		/*
		 * Check if given arena is in list of home arenas
		 */
		for(Arena a: team.getHomeArenas()) {
			if(arena != a) {
				return false;
			}
		}
		return true;
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
	
	public ArrayList<ArrayList<Object>> getModes() {
		return modes;
	}

	public ArrayList<ArrayList<Object>> getMatchups() {
		return matchups;
	}

	
	

}
