package view;

import entity.SeasonTeam;
import entity.SeasonTeamStats;
import interface_adapter.teamstatsrepresentation.TeamStatsRepController;
import interface_adapter.teamstatsrepresentation.TeamStatsRepPresenter;
import interface_adapter.teamstatsrepresentation.TeamStatsRepViewModel;
import use_case.teamstatsrepresentation.DataAccessException;
import use_case.teamstatsrepresentation.TeamLookupDataAccessInterface;
import use_case.teamstatsrepresentation.TeamStatsRepDataAccessInterface;
import use_case.teamstatsrepresentation.TeamStatsRepInteractor;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Opens the Team Stats view with a fake DAO, for testing the GUI by hand
 * without calling the real API.
 *
 * <p>Try these inputs:</p>
 * <ul>
 *   <li>"Toronto Raptors", 2023 - success; Fast Break Points shows "N/A"</li>
 *   <li>"Toronto Raptors", 1990 - no stats for that season</li>
 *   <li>"Fake Team", 2023 - team not found</li>
 *   <li>"error", 2023 - simulated API failure</li>
 *   <li>empty boxes, or season "abc" - input error shown by the view</li>
 * </ul>
 */
public class TeamStatsRepManualTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TeamStatsRepViewModel viewModel = new TeamStatsRepViewModel();
            TeamStatsRepView view = new TeamStatsRepView(viewModel);

            TeamStatsRepPresenter presenter = new TeamStatsRepPresenter(viewModel);
            TeamStatsRepInteractor interactor = new TeamStatsRepInteractor(new FakeDao(), new FakeDao(), presenter);
            view.setController(new TeamStatsRepController(interactor));

            JFrame frame = new JFrame(TeamStatsRepViewModel.TITLE_LABEL);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(view);
            frame.setSize(500, 650);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /** Returns made-up data instead of calling the API. */
    /**
     * Returns made-up data instead of calling the API.
     */
    private static class FakeDao implements TeamLookupDataAccessInterface,
            TeamStatsRepDataAccessInterface {

        private static final int RAPTORS_ID = 28;

        @Override
        public SeasonTeam getTeamByName(String teamName) throws DataAccessException {
            if (teamName.equalsIgnoreCase("error")) {
                throw new DataAccessException("Simulated API failure", null);
            }
            if (!teamName.equalsIgnoreCase("Toronto Raptors")) {
                return null;
            }
            return new SeasonTeam(RAPTORS_ID, "Toronto Raptors", "Toronto", 2023, makeStats());
        }

        @Override
        public SeasonTeamStats getSeasonTeamStats(int teamId, int season) throws DataAccessException {
            if (teamId != RAPTORS_ID || season != 2023) {
                return null;
            }
            return makeStats();
        }

        private SeasonTeamStats makeStats() {
            return new SeasonTeamStats(
                    82, 9500, null, 4200, 1400, 1100,   // games, points, fast break (N/A), paint, off TO, 2nd chance
                    3500, 7500, 46.7,                   // field goals
                    1500, 1900, 78.9,                   // free throws
                    1000, 2900, 34.5,                   // three-pointers
                    800, 2700, 3500,                    // rebounds
                    2100, 1600, 600, 1100, 400, -120,   // ast, fouls, stl, tov, blk, +/-
                    25, 14);                            // biggest lead, longest run
        }
    }
}