package Optimization;

import Optimization.QualityChecker;
import Scheduler.*;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class QualityCheckerTest {
    private Schedule schedule;

    private ArrayList<Game> games;
    private ArrayList<TimeSlot> timeSlots;
    private ArrayList<Team> teams;

    /**
     * Creates a new Schedule for testing:
     *
     * Teams: Team A, Team B, Team C, Team D
     * TimeSlots: 20 total, randomly selected from 01/01/2023 to 03/31/2023 (3 months)
     * Games: 20 total -> 10 games per team
     *  c @ a
     *  d @ b
     *  a @ c
     *  b @ d
     *  a @ b
     *  c @ d
     *  b @ a
     *  c @ d
     *  c @ b
     *  a @ d
     *  d @ b
     *  a @ c
     *  c @ a
     *  b @ d
     *  a @ b
     *  c @ d
     *  b @ c
     *  a @ d
     *  d @ a
     *  c @ b
     *
     *  Team A:
     *  Home Games = 4
     *  Away Games = 6
     *
     *  Team B:
     *  Home Games = 6
     *  Away Games = 4
     *
     *  Team C:
     *  Home Games = 3
     *  Away Games = 7
     *
     *  Team D:
     *  Home Games = 7
     *  Away Games = 3
     */
    @BeforeEach
    public void setup() {
        // Initialize Lists
        games = new ArrayList<>();
        timeSlots = new ArrayList<>();
        teams = new ArrayList<>();

        // Create Teams
        Division tempTest = new Division("Test");
        ArrayList<Division> test = new ArrayList<Division>();
        test.add(tempTest);

        Team a = new Team("a", 1.0, 1.0, tempTest, Tier.ZERO);
        Team b = new Team("b", 1.0, 1.0, tempTest, Tier.ZERO);
        Team c = new Team("c", 1.0, 1.0, tempTest, Tier.ZERO);
        Team d = new Team("d", 1.0, 1.0, tempTest, Tier.ZERO);

        teams.add(a);
        teams.add(b);
        teams.add(c);
        teams.add(d);

        // Create Arenas
        Arena arenaA = new Arena("Arena A", 1, 1);
        Arena arenaB = new Arena("Arena B", 1, 1);
        Arena arenaC = new Arena("Arena C", 1, 1);
        Arena arenaD = new Arena("Arena D", 1, 1);

        a.addArena(arenaA);
        b.addArena(arenaB);
        c.addArena(arenaC);
        d.addArena(arenaD);

        // Create Games
        games.add(new Game(a,c));
        games.add(new Game(b,d));

        games.add(new Game(c,a));
        games.add(new Game(d,b));

        games.add(new Game(b,a));
        games.add(new Game(d,c));

        games.add(new Game(a,b));
        games.add(new Game(d,c));

        games.add(new Game(b,c));
        games.add(new Game(d,a));

        games.add(new Game(b,d));
        games.add(new Game(c,a));

        games.add(new Game(a,c));
        games.add(new Game(d,b));

        games.add(new Game(b,a));
        games.add(new Game(d,c));

        games.add(new Game(c,b));
        games.add(new Game(d,a));

        games.add(new Game(a,d));
        games.add(new Game(b,c));

        // Create TimeSlots
        long rangeStart = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long rangeEnd = Timestamp.valueOf("2023-03-31 00:58:00").getTime();
        long diff = rangeEnd - rangeStart + 1;

        for(int i = 0; i < 20; i++) {
            Timestamp rand = new Timestamp(rangeStart + (long)(Math.random() * diff));
            TimeSlot t = new TimeSlot(rand.toLocalDateTime(), games.get(i).getHomeTeam().getHomeArenas().get(0), test);
            timeSlots.add(t);
        }

        // Assign TimeSlots to Games
        for(int i = 0; i < games.size(); i++) {
            games.get(i).setTimeSlot(timeSlots.get(i));
            timeSlots.get(i).useTimeslot();

            System.out.println("Matchup: " + games.get(i).getAwayTeam().getName() + " @ " + games.get(i).getHomeTeam().getName());
            System.out.println("TimeSlot: " + games.get(i).getTimeSlot().getStartDateTime().toString());
        }
    }

    @Test
    void checkTimeslotUsage() {
        QualityChecker qc = new QualityChecker(games, timeSlots, teams);

        double penalty = qc.checkTimeslotUsage();

        // Penalty should be zero
        Assertions.assertEquals(0, penalty);
    }

    @Test
    void checkHomeAwayEquality() {
        QualityChecker qc = new QualityChecker(games, timeSlots, teams);

        double penalty = qc.checkHomeAwayEquality();

        /*
        Penalty for Team A:
        Home = 4
        Away = 6
        Imbalance = 1

        Penalty for Team B:
        Home = 6
        Away = 4
        Imbalance = 1

        Penalty for Team C:
        Home = 3
        Away = 7
        Imbalance = 2

        Penalty for Team D:
        Home = 3
        Away = 7
        Imbalance = 2

        Total Imbalance = 1 + 1 + 2 + 2 = 6
         */

        Assertions.assertEquals(6, penalty);
    }

    @Test
    void checkAvgTravelDistance() {
    }

    @Test
    void checkRestDayEquality() {
    }

    @Test
    void checkScheduledMatchEquality() {
        QualityChecker qc = new QualityChecker(games, timeSlots, teams);

        double penalty = qc.checkScheduledMatchEquality();

        Assertions.assertEquals(0, penalty);
    }

    @Test
    void getQuality() {
        QualityChecker qc = new QualityChecker(games, timeSlots, teams);

        double penalty = qc.getQuality();

        double test = qc.checkTimeslotUsage() + qc.checkHomeAwayEquality() + qc.checkScheduledMatchEquality();

        Assertions.assertEquals(penalty, test);
    }
}
