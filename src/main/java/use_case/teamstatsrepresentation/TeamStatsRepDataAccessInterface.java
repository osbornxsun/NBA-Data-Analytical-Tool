package use_case.teamstatsrepresentation;

public interface TeamStatsRepDataAccessInterface {

    SeasonTeamStats getSeasonTeamStats(int teamId, int season) throws DataAccessException;


}
