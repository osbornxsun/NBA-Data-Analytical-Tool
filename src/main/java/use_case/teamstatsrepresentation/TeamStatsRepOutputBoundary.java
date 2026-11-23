package use_case.teamstatsrepresentation;

public interface TeamStatsRepOutputBoundary {

    void prepareSuccessView(TeamStatsRepOutputData outputData);

    void prepareFailView(String errorMessage);
}
