package interface_adapter.teamstatsrepresentation;

import use_case.teamstatsrepresentation.TeamStatsRepOutputBoundary;
import use_case.teamstatsrepresentation.TeamStatsRepOutputData;

public class TeamStatsRepPresenter implements TeamStatsRepOutputBoundary {


    @Override
    public void prepareSuccessView(TeamStatsRepOutputData outputData) {

    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
