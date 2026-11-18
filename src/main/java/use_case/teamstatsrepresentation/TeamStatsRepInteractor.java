    package use_case.teamstatsrepresentation;



    public class TeamStatsRepInteractor implements TeamStatsRepInputBoundary{

        private TeamStatsRepOutputBoundary presenter;
        private TeamStatsRepDataAccessInterface teamStatsDAO;
        private TeamLookupDataAccessInterface teamLookupDAO;

        public TeamStatsRepInteractor(TeamLookupDataAccessInterface teamLookupDAO,
                                      TeamStatsRepDataAccessInterface teamStatsDAO,
                                      TeamStatsRepOutputBoundary presenter) {
            this.teamLookupDAO = teamLookupDAO;
            this.teamStatsDAO = teamStatsDAO;
            this.presenter = presenter;
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
                Team team = teamLookupDAO.getTeamByName(teamName);
                if (team == null) {
                    presenter.prepareFailView("Team \"" + teamName + "\" not found.");
                    return;
                }

                TeamStats stats = teamStatsDAO.getTeamStats(team.getId(), season);
                if (stats == null) {
                    presenter.prepareFailView("No stats for " + team.getName() + " in " + season + ".");
                    return;
                }

                TeamStatsRepOutputData outputData = new TeamStatsRepOutputData(
                        team.getName(), season,
                        stats.getGamesPlayed(), stats.getWins(), stats.getLosses(),
                        stats.getPointsPerGame());   // add more
                presenter.prepareSuccessView(outputData);

            } catch (DataAccessException e) {   // your own exception, thrown by the API DAO
                presenter.prepareFailView("Could not load data: " + e.getMessage());
            }
        }
    }
