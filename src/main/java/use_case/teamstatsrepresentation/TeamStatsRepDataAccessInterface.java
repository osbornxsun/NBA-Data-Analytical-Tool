package use_case.teamstatsrepresentation;

import entity.SeasonTeam;

public interface TeamStatsRepDataAccessInterface {

    SeasonTeam getSeasonTeam(String teamName, int season) throws DataAccessException;


}
