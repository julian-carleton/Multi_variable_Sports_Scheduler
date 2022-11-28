package main.java.Scheduler;

import java.util.ArrayList;

public class Schedule {
	ArrayList<Round> rounds;
	ArrayList<Object> teams;
	int num_rounds;

	/**
	 * Default constructor
	 * */
	public Schedule(ArrayList<Object> teams){//, ArrayList<TimeSlot> timelsots) {
		this.teams = teams;
		this.rounds = new ArrayList<Round>();
	}

	/**
	 *
	 *
	 **/
	public void makeMatchups() {

	}

	/**
	 *
	 *
	 **/
	public void matchRR() {
		//Determine if even or odd # teams
		boolean even = false;
		int num_teams = this.teams.size();
		if (num_teams % 2 == 0) {
			num_rounds = num_teams - 1;
			even = true;
		} else {
			num_rounds = num_teams;
		}

		for (int j = 0; j < num_rounds; j++) {
			//Matching a round
			int curr_x = (0 + j) % num_rounds;
			int curr_y = (num_teams - 1 + j) % num_rounds;
			Round curr_round = new Round();
			for (int i = 0; i < Math.floorDiv(num_teams, 2); i++) {
				Game curr_game = new Game();
				//System.out.print(curr_x);
				//System.out.print(curr_y);
				curr_game.setHomeTeam(teams.get(curr_x));
				if(even && i == 0) {
					curr_game.setAwayTeam(teams.get(num_teams - 1));
				} else {
					curr_game.setAwayTeam(teams.get(curr_y));
				}
				curr_round.add(curr_game);
				curr_x = (curr_x + 1) % num_rounds;
				curr_y --;
				if (curr_y < 0) {
					curr_y += num_rounds;
				}

			}
			this.rounds.add(curr_round);
		}
	}

	public void matchRound() {

	}

	public ArrayList<Round> getRounds() {
		return this.rounds;
	}

	public static void main(String[] args) {
		ArrayList<Object> teams = new ArrayList<Object>();
		teams.add("Team 1");
		teams.add("Team 2");
		teams.add("Team 3");
		teams.add("Team 4");
		teams.add("Team 5");
		teams.add("Team 6");
		teams.add("Team 7");
		teams.add("Team 8");
		teams.add("Team 9");
		teams.add("Team 10");
		Schedule schedule = new Schedule(teams);
		schedule.matchRR();

		boolean even = false;
		int num_teams = teams.size();
		int num_rounds;
		if (num_teams % 2 == 0) {
			num_rounds = num_teams - 1;
			even = true;
		} else {
			num_rounds = num_teams;
		}

		for (int j = 0; j < num_rounds; j++) {
			Round curr_round = schedule.getRounds().get(j);

			for (int i = 0; i <  Math.floorDiv(teams.size(), 2); i++) {
				System.out.print(((Game) curr_round.getGame(i)).getHomeTeam());
				System.out.print(" vs ");
				System.out.print(((Game) curr_round.getGame(i)).getAwayTeam());
				System.out.print("\n");
			}
			System.out.print("This round has: ");
			System.out.print(((ArrayList<Object>)curr_round.getMatchups()).size());
			System.out.print(" matchups\n\n");
		}
	}
}
