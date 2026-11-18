package use_case.teamstatsrepresentation;

public interface TeamLookupDataAccessInterface {


    Team getTeamByName(String teamName) throws DataAccessException;
}
