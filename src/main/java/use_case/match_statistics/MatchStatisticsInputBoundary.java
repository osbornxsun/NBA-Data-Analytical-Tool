package use_case.match_statistics;

import java.util.concurrent.Executor;

/** Requests completed games and presents box scores on the supplied delivery executor. */
public interface MatchStatisticsInputBoundary {
    void execute(MatchStatisticsInputData inputData);

    void viewStatistics(int gameId, Executor delivery);
}
