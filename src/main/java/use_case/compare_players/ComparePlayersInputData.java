package use_case.compare_players;

/**
 * Input for the Compare Players use case: two player names and a season.
 */
public class ComparePlayersInputData {

    private final String firstPlayerName;
    private final String secondPlayerName;
    private final int season;

    /**
     * Creates the input data.
     *
     * @param firstPlayerName  the first player's name, e.g. "LeBron James"
     * @param secondPlayerName the second player's name
     * @param season           the season start year, e.g. 2024 for the 2024-25 season
     */
    public ComparePlayersInputData(String firstPlayerName, String secondPlayerName, int season) {
        this.firstPlayerName = firstPlayerName;
        this.secondPlayerName = secondPlayerName;
        this.season = season;
    }

    String getFirstPlayerName() {
        return firstPlayerName;
    }

    String getSecondPlayerName() {
        return secondPlayerName;
    }

    int getSeason() {
        return season;
    }
}
