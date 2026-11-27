package use_case.compare_players;

/**
 * Input boundary for the Compare Players use case.
 */
public interface ComparePlayersInputBoundary {

    /**
     * Compares the two players' season averages.
     *
     * @param inputData the two player names and the season
     */
    void execute(ComparePlayersInputData inputData);
}
