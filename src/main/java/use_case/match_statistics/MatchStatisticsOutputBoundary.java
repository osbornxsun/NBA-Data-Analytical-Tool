package use_case.match_statistics;

import java.util.List;

/** Allows the interactor to report the outcome of a game search. */
public interface MatchStatisticsOutputBoundary {

    void prepareSuccessView(List<CompletedGameOutputData> completedGames);

    void prepareFailView(String message);
}