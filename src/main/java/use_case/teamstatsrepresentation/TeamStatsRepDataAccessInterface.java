package use_case.teamstatsrepresentation;

import entity.SeasonTeamStats;

public interface TeamStatsRepDataAccessInterface {

    SeasonTeamStats getSeasonTeamStats(int teamId, int season) throws DataAccessException;


}
