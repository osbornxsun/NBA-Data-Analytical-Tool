package interface_adapter.match_statistics;

import use_case.match_statistics.MatchStatisticsInputBoundary;
import use_case.match_statistics.MatchStatisticsInputData;

/** Handles requests from the Match Statistics view. */
public final class MatchStatisticsController {
    private final MatchStatisticsInputBoundary interactor;

    public MatchStatisticsController(MatchStatisticsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void findGames(int teamId, int season) {
        MatchStatisticsInputData inputData = new MatchStatisticsInputData(teamId, season);
        interactor.execute(inputData);
    }
}