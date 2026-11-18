package data_access;

import data_access.team_stats_rep.ApiTeamDataAccessObject;   // adjust to your package name
import entity.SeasonTeamStats;
import entity.Team;

public class DaoManualTest {
    public static void main(String[] args) throws Exception {
        ApiTeamDataAccessObject dao =
                new ApiTeamDataAccessObject(System.getenv("API_SPORTS_KEY"));

        // Test 1: a real team
        Team team = dao.getTeamByName("Toronto Raptors");
        System.out.println("Team: " + (team == null ? "null" : team.getId() + " " + team.getName()));

        // Test 2: a fake team (should print null)
        System.out.println("Fake team: " + dao.getTeamByName("Fake Team"));

        // Test 3: stats for the real team
        if (team != null) {
            SeasonTeamStats s = dao.getSeasonTeamStats(team.getId(), 2024);
            if (s == null) {
                System.out.println("Stats: null");
            } else {
                System.out.println("Games: " + s.getGamesPlayed());
                System.out.println("Points: " + s.getPoints());
                System.out.println("FG%: " + s.getFieldGoalPercent());
                System.out.println("Rebounds: " + s.getTotalRebounds());
                System.out.println("Longest run: " + s.getLongestRun());
            }
        }
    }
}