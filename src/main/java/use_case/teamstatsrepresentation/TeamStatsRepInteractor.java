    package use_case.teamstatsrepresentation;


    import entity.SeasonTeam;
    import entity.SeasonTeamStats;

    public class TeamStatsRepInteractor implements TeamStatsRepInputBoundary{

        private final TeamStatsRepOutputBoundary presenter;
        private final TeamStatsRepDataAccessInterface teamStatsDAO;
        private final TeamLookupDataAccessInterface teamLookupDAO;

        public TeamStatsRepInteractor(TeamLookupDataAccessInterface teamLookupDAO,
                                      TeamStatsRepDataAccessInterface teamStatsDAO,
                                      TeamStatsRepOutputBoundary presenter) {
            this.teamLookupDAO = teamLookupDAO;
            this.teamStatsDAO = teamStatsDAO;
            this.presenter = presenter;
        }

        private TeamStatsRepOutputData toOutputData(SeasonTeam team, SeasonTeamStats stats, int season) {
            return new TeamStatsRepOutputData(
                    team.getName(),
                    season,
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
                SeasonTeam team = teamLookupDAO.getTeamByName(teamName);
                if (team == null) {
                    presenter.prepareFailView("Team \"" + teamName + "\" not found.");
                    return;
                }

                SeasonTeamStats stats = teamStatsDAO.getSeasonTeamStats(team.getId(), season);
                if (stats == null) {
                    presenter.prepareFailView("No stats for " + team.getName() + " in " + season + ".");
                    return;
                }

                TeamStatsRepOutputData outputData = toOutputData(team, stats, season);
                presenter.prepareSuccessView(outputData);

            } catch (DataAccessException e) {   // your own exception, thrown by the API DAO
                presenter.prepareFailView("Could not load data: " + e.getMessage());
            }
        }
    }
