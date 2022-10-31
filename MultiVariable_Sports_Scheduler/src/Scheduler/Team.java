package Scheduler;

public class Team {
	
	private String name;
	private Division division;
	private Tier tier;
	private League league;
	
	
	/**
	 * Default Constructor
	 * @param name, division, tier, league
	 */
	public Team(String name, Division division, Tier tier, League league) {
		this.name = name;
		this.division = division;
		this.tier = tier;
		this.league = league;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the division
	 */
	public Division getDivision() {
		return division;
	}

	/**
	 * @return the tier
	 */
	public Tier getTier() {
		return tier;
	}

	/**
	 * @return the league
	 */
	public League getLeague() {
		return league;
	}

	
	
}
