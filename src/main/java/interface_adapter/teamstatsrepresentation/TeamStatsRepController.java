package interface_adapter.teamstatsrepresentation;

import use_case.teamstatsrepresentation.TeamStatsRepInputBoundary;
import use_case.teamstatsrepresentation.TeamStatsRepInputData;

public class TeamStatsRepController {

    private final TeamStatsRepInputBoundary interactor;

    public TeamStatsRepController(TeamStatsRepInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String teamName, int season) {
        final TeamStatsRepInputData inputData = new TeamStatsRepInputData(teamName, season);

        interactor.execute(inputData);
    }
}
