package use_case.predictMatchUseCase;

import entity.SeasonTeam;
import use_case.DataAccessException;

/**
 * The signature is identical to TeamStatsRepDataAccessInterface on purpose, so
 * ApiTeamDataAccessObject can implement both interfaces with one method.
 * Each use case still owns its own interface (Interface Segregation), and neither
 * use case depends on the other.
 */
public interface PredictMatchDataAccessInterface {

  /**
   * @return the team's season, or null if no team has that name
   * @throws DataAccessException if the API could not be reached or reported an error
   */
  SeasonTeam getSeasonTeam(String teamName, int season) throws DataAccessException;
}
