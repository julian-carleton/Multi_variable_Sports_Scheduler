package main.java.Scheduler;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Schedule {
	ArrayList<Round> rounds;
	ArrayList<Team> teams;
	int num_rounds;

	/**
	 * Default constructor
	 * */
	public Schedule(ArrayList<Team> teams){//, ArrayList<TimeSlot> timelsots) {
		this.teams = teams;
		this.rounds = new ArrayList<Round>();
		matchRR();
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

	/**
	 * Orders the rounds so that the games with matchups with most exceptions are ordered first.
	 * 
	 * */
	public void orderExceptionNumber() {
		ArrayList<Round> new_rounds = new ArrayList<Round>();
		
		//Iterating through the rounds
		for (int i = 0; i < this.rounds.size(); i++) {
			//Creating the new round
			Round new_round = new Round();
			Round curr_round = rounds.get(i);
			for (int j = 0; j < curr_round.getMatchups().size(); j++) {
				Game curr_game = curr_round.getGame(j);
				int curr_game_excps_number = curr_game.getExceptionsNumber();
				ArrayList<Game> new_round_matchups = new_round.getMatchups();
				if (new_round_matchups.size() == 0) {
					new_round_matchups.add(curr_game);
				} else {
					for (int k = 0; k < new_round_matchups.size(); k++) {
						int next_game_excps_number = new_round_matchups.get(k).getExceptionsNumber();
						if (curr_game_excps_number > next_game_excps_number) {
							new_round_matchups.add(k, curr_game);
							break;
						}
					}
				}
			}
			new_rounds.add(new_round);
		}
		this.rounds = new_rounds;
	}
	
	public ArrayList<Round> getRounds() {
		return this.rounds;
	}

	public static void main(String[] args) {
		ArrayList<Team> teams = new ArrayList<Team>();

		teams.add(new Team("Team 1"));
		teams.add(new Team("Team 2"));
		teams.add(new Team("Team 3"));
		teams.add(new Team("Team 4"));
		teams.add(new Team("Team 5"));
		teams.add(new Team("Team 6"));
		teams.add(new Team("Team 7"));
		teams.add(new Team("Team 8"));
		teams.add(new Team("Team 9"));
		teams.add(new Team("Team 10"));
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
				System.out.print(((Game) curr_round.getGame(i)).getHomeTeam().getName());
				System.out.print(" vs ");
				System.out.print(((Game) curr_round.getGame(i)).getAwayTeam().getName());
				System.out.print("\n");
			}
			System.out.print("This round has: ");
			System.out.print(((ArrayList<Game>)curr_round.getMatchups()).size());
			System.out.print(" matchups\n\n");
		}
	}
}
