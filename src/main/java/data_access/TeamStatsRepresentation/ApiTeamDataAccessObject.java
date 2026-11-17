package data_access.TeamStatsRepresentation;

import use_case.TeamStatsRepresentation.TeamLookupDataAccessInterface;
import use_case.TeamStatsRepresentation.TeamStatsRepDataAccessInterface;

public class ApiTeamDataAccessObject implements TeamStatsRepDataAccessInterface, TeamLookupDataAccessInterface {

    private final String apikey;

    public ApiTeamDataAccessObject(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public Team getTeamByName(String teamName) {
    }

    @Override
    public TeamStats getTeamStats(int teamid, int season){

    }
}
