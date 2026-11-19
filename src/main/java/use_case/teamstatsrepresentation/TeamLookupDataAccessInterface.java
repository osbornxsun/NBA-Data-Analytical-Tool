package use_case.teamstatsrepresentation;

import entity.SeasonTeam;

public interface TeamLookupDataAccessInterface {


    SeasonTeam getTeamByName(String teamName) throws DataAccessException;
}
