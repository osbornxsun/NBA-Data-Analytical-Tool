    package use_case.teamstatsrepresentation;


    import entity.SeasonTeam;
    import entity.SeasonTeamStats;
    import use_case.DataAccessException;

    public class TeamStatsRepInteractor implements TeamStatsRepInputBoundary{

        private final TeamStatsRepOutputBoundary presenter;
        private final TeamStatsRepDataAccessInterface teamStatsDAO;

        public TeamStatsRepInteractor (TeamStatsRepDataAccessInterface teamStatsDAO,
                                      TeamStatsRepOutputBoundary presenter) {
            this.teamStatsDAO = teamStatsDAO;
            this.presenter = presenter;
        }

        private TeamStatsRepOutputData toOutputData(SeasonTeam team) {
            SeasonTeamStats stats = team.getStats();
            return new TeamStatsRepOutputData(
                    team.getName(),
                    team.getSeason(),
                    stats.getGamesPlayed(),
                    stats.getPoints(),
                    stats.getFastBreakPoints(),
                    stats.getPointsInPaint(),
                    stats.getPointsOffTurnovers(),
                    stats.getSecondChancePoints(),
                    stats.getFieldGoalsMade(),
                    stats.getFieldGoalsAttempted(),
                    stats.getFieldGoalPercent(),
                    stats.getFreeThrowsMade(),
                    stats.getFreeThrowsAttempted(),
                    stats.getFreeThrowPercent(),
                    stats.getThreePointersMade(),
                    stats.getThreePointersAttempted(),
                    stats.getThreePointPercent(),
                    stats.getOffensiveRebounds(),
                    stats.getDefensiveRebounds(),
                    stats.getTotalRebounds(),
                    stats.getAssists(),
                    stats.getPersonalFouls(),
                    stats.getSteals(),
                    stats.getTurnovers(),
                    stats.getBlocks(),
                    stats.getPlusMinus(),
                    stats.getBiggestLead(),
                    stats.getLongestRun());
        }

        @Override
        public void execute(TeamStatsRepInputData inputData) {
            String teamName = inputData.getTeamName();
            int season = inputData.getSeason();

            if (teamName == null || teamName.isBlank()) {
                presenter.prepareFailView("Please enter a team name.");
                return;
            }

            try {
                SeasonTeam team = teamStatsDAO.getSeasonTeam(teamName, season);
                if (team == null) {
                    presenter.prepareFailView("Team \"" + teamName + "\" not found.");
                    return;
                }

                TeamStatsRepOutputData outputData = toOutputData(team);
                presenter.prepareSuccessView(outputData);

            } catch (DataAccessException e) {
                presenter.prepareFailView("Could not load data: " + e.getMessage());
            }
        }
    }
