package interface_adapter.compare_players;

import use_case.compare_players.ComparePlayersInputBoundary;
import use_case.compare_players.ComparePlayersInputData;

/**
 * Passes the user's input from the view to the Compare Players use case.
 */
public class ComparePlayersController {

    private final ComparePlayersInputBoundary interactor;

    public ComparePlayersController(ComparePlayersInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Runs the comparison.
     *
     * @param firstPlayerName  the first player's name
     * @param secondPlayerName the second player's name
     * @param season           the season start year, e.g. 2024 for 2024-25
     */
    public void execute(String firstPlayerName, String secondPlayerName, int season) {
        interactor.execute(new ComparePlayersInputData(firstPlayerName, secondPlayerName, season));
    }
}
