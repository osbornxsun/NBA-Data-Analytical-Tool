package app;

import data_access.ApiClient;
import data_access.ApiTeamDataAccessObject;
import interface_adapter.teamstatsrepresentation.TeamStatsRepController;
import interface_adapter.teamstatsrepresentation.TeamStatsRepPresenter;
import interface_adapter.teamstatsrepresentation.TeamStatsRepViewModel;
import use_case.teamstatsrepresentation.TeamStatsRepDataAccessInterface;
import use_case.teamstatsrepresentation.TeamStatsRepInputBoundary;
import use_case.teamstatsrepresentation.TeamStatsRepInteractor;
import use_case.teamstatsrepresentation.TeamStatsRepOutputBoundary;
import view.TeamStatsRepView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts the NBA Data Analytical Tool.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Data access (outer layer)
            ApiClient apiClient = new ApiClient(System.getenv("API_SPORTS_KEY"));
            TeamStatsRepDataAccessInterface dao = new ApiTeamDataAccessObject(apiClient);

            // Interface adapters
            TeamStatsRepViewModel viewModel = new TeamStatsRepViewModel();
            TeamStatsRepOutputBoundary presenter = new TeamStatsRepPresenter(viewModel);

            // Use case
            TeamStatsRepInputBoundary interactor = new TeamStatsRepInteractor(dao, presenter);

            // View
            TeamStatsRepView view = new TeamStatsRepView(viewModel);
            view.setController(new TeamStatsRepController(interactor));

            JFrame frame = new JFrame(TeamStatsRepViewModel.TITLE_LABEL);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.setSize(500, 650);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}