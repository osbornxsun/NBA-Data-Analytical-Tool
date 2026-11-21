package use_case.match_statistics;

import entity.Game;

import java.io.IOException;
import java.util.List;

/** Provides games for the requested team and season. */
public interface MatchStatisticsDataAccessInterface {

    /**
     * Returns the team's games for the season, including unfinished games.
     *
     * @throws IOException if the games cannot be retrieved
     */
    List<Game> getGamesForTeam(int teamId, int season) throws IOException;
}