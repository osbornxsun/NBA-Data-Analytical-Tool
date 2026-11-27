package app;

import data_access.ApiClient;
import data_access.ApiPlayerDataAccessObject;
import interface_adapter.compare_players.ComparePlayersController;
import interface_adapter.compare_players.ComparePlayersPresenter;
import interface_adapter.compare_players.ComparePlayersViewModel;
import use_case.compare_players.ComparePlayersDataAccessInterface;
import use_case.compare_players.ComparePlayersInputBoundary;
import use_case.compare_players.ComparePlayersInteractor;
import use_case.compare_players.ComparePlayersOutputBoundary;
import view.ComparePlayersView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts the Player Card Comparison screen on its own.
 * Requires the API_SPORTS_KEY environment variable.
 */
public class ComparePlayersMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Data access (outer layer)
            ApiClient apiClient = new ApiClient(System.getenv("API_SPORTS_KEY"));
            ComparePlayersDataAccessInterface dao = new ApiPlayerDataAccessObject(apiClient);

            // Interface adapters
            ComparePlayersViewModel viewModel = new ComparePlayersViewModel();
            ComparePlayersOutputBoundary presenter = new ComparePlayersPresenter(viewModel);

            // Use case
            ComparePlayersInputBoundary interactor = new ComparePlayersInteractor(dao, presenter);

            // View
            ComparePlayersView view = new ComparePlayersView(viewModel);
            view.setController(new ComparePlayersController(interactor));

            JFrame frame = new JFrame(ComparePlayersViewModel.TITLE_LABEL);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.setSize(820, 560);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
