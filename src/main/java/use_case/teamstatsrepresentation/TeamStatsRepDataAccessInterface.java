package use_case.teamstatsrepresentation;

import entity.SeasonTeam;
import use_case.DataAccessException;

public interface TeamStatsRepDataAccessInterface {

    SeasonTeam getSeasonTeam(String teamName, int season) throws DataAccessException;


}
