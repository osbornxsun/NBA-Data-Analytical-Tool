package use_case.compare_players;

/**
 * Output boundary for the Compare Players use case (implemented by the presenter).
 */
public interface ComparePlayersOutputBoundary {

    /**
     * Shows both player cards with the better value of each stat marked.
     *
     * @param outputData the comparison
     */
    void prepareSuccessView(ComparePlayersOutputData outputData);

    /**
     * Shows an error instead of the cards.
     *
     * @param errorMessage what went wrong, in words for the user
     */
    void prepareFailView(String errorMessage);
}
