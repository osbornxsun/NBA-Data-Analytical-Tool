package use_case.teamstatsrepresentation;

public class TeamStatsRepInputData {

    private final String teamName;
    private final int season;

    public TeamStatsRepInputData(String teamName, int season) {
        this.teamName = teamName;
        this.season = season;
    }

    String getTeamName() {
        return teamName;
    }

    int getSeason() {
        return season;
    }

}
