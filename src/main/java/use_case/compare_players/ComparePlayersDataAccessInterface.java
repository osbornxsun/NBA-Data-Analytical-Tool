package use_case.compare_players;

import java.util.List;

import entity.Player;
import entity.PlayerGameStats;
import use_case.DataAccessException;

/**
 * Data the Compare Players use case needs.
 */
public interface ComparePlayersDataAccessInterface {

    /**
     * Finds players whose name may match the given name. The result may contain
     * players that are only partial matches; the interactor picks the right one.
     *
     * @param name the name the user typed, e.g. "Stephen Curry" or "Curry"
     * @return the candidate players (empty if none)
     * @throws DataAccessException if the data could not be loaded
     */
    List<Player> searchPlayers(String name) throws DataAccessException;

    /**
     * Returns a player's game lines for one season.
     *
     * @param playerId the player's id
     * @param season   the season start year
     * @return the game lines (empty if the player has none that season)
     * @throws DataAccessException if the data could not be loaded
     */
    List<PlayerGameStats> getSeasonGameStats(int playerId, int season) throws DataAccessException;
}
