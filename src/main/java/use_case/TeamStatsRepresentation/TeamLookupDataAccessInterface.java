package use_case.TeamStatsRepresentation;

public interface TeamLookupDataAccessInterface {


    Team getTeamByName(String teamName) throws DataAccessException;
}
