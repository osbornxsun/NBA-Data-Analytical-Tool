package use_case.TeamStatsRepresentation;

public interface TeamStatsRepDataAccessInterface {

    TeamStats getTeamStats(int teamId, int season) throws DataAccessException;


}
