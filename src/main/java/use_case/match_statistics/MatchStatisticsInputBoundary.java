package use_case.match_statistics;

/** Allows the controller to request a list of completed games. */
public interface MatchStatisticsInputBoundary {
    void execute(MatchStatisticsInputData inputData);
}