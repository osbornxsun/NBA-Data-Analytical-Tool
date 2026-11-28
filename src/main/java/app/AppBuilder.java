package app;

// ===== SHARED imports =====
import data_access.ApiClient;
import data_access.ApiTeamDataAccessObject;
import interface_adapter.ViewManagerModel;
import view.ViewManager;

// ===== Use case 1: Team Stats Representation (Osborn) =====
import interface_adapter.teamstatsrepresentation.TeamStatsRepController;
import interface_adapter.teamstatsrepresentation.TeamStatsRepPresenter;
import interface_adapter.teamstatsrepresentation.TeamStatsRepViewModel;
import use_case.teamstatsrepresentation.TeamStatsRepInputBoundary;
import use_case.teamstatsrepresentation.TeamStatsRepInteractor;
import use_case.teamstatsrepresentation.TeamStatsRepOutputBoundary;
import view.TeamStatsRepView;

// ===== Use case 2..5: add your imports here, in your own block =====

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;

/**
 * Builds the whole app (Builder pattern), following the CA login example.
 *
 * TEAM RULES
 *  1. Each use case owns its own fields + addXView() + addXUseCase(). Edit only your block.
 *  2. Main calls ALL addXView() BEFORE any addXUseCase().
 *  3. Use the shared viewManagerModel and the shared nbaDataAccessObject. Never create another.
 *     The DAO implements each use case's DataAccessInterface.
 *  4. Each ViewModel has a public static final String VIEW_NAME (like LoginViewModel.VIEW_NAME).
 *  5. Use cases 2-5 are commented out so main compiles. Uncomment yours when your classes are merged.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // ===================== SHARED =====================
    // ONE ViewManagerModel for the whole app. Pass it ONLY to Presenters that switch screens.
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    // Listens to viewManagerModel and flips the CardLayout to the active view.
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // ONE DAO for the NBA API, shared by all five use cases.
    ApiClient apiClient = new ApiClient(System.getenv("API_SPORTS_KEY"));
    final ApiTeamDataAccessObject nbaDataAccessObject = new ApiTeamDataAccessObject(apiClient);

    // ===================== FIELDS (one block per use case) =====================
    // Use case 1: Team Stats Representation (Osborn)
    private TeamStatsRepViewModel teamStatsRepViewModel;
    private TeamStatsRepView teamStatsRepView;

    // Use case 2: <name> (<owner>)
    // private UseCase2ViewModel useCase2ViewModel;
    // private UseCase2View useCase2View;

    // Use case 3: <name> (<owner>)
    // private UseCase3ViewModel useCase3ViewModel;
    // private UseCase3View useCase3View;

    // Use case 4: <name> (<owner>)
    // private UseCase4ViewModel useCase4ViewModel;
    // private UseCase4View useCase4View;

    // Use case 5: <name> (<owner>)
    // private UseCase5ViewModel useCase5ViewModel;
    // private UseCase5View useCase5View;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // ===================== VIEWS =====================

    /** Use case 1: Team Stats Representation view (Osborn). */
    public AppBuilder addTeamStatsRepView() {
        teamStatsRepViewModel = new TeamStatsRepViewModel();
        teamStatsRepView = new TeamStatsRepView(teamStatsRepViewModel);
        cardPanel.add(teamStatsRepView, TeamStatsRepViewModel.VIEW_NAME);
        return this;
    }

    /** Use case 2 view. */
    public AppBuilder addUseCase2View() {
        // useCase2ViewModel = new UseCase2ViewModel();
        // useCase2View = new UseCase2View(useCase2ViewModel);
        // cardPanel.add(useCase2View, UseCase2ViewModel.VIEW_NAME);
        return this;
    }

    /** Use case 3 view. */
    public AppBuilder addUseCase3View() {
        // useCase3ViewModel = new UseCase3ViewModel();
        // useCase3View = new UseCase3View(useCase3ViewModel);
        // cardPanel.add(useCase3View, UseCase3ViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Use case 4 view.
     * If this use case shares a screen with another one (like ChangePassword and
     * Logout both live on LoggedInView in the example), delete this method and set
     * the controller on that existing view in addUseCase4UseCase() instead.
     */
    public AppBuilder addUseCase4View() {
        // useCase4ViewModel = new UseCase4ViewModel();
        // useCase4View = new UseCase4View(useCase4ViewModel);
        // cardPanel.add(useCase4View, UseCase4ViewModel.VIEW_NAME);
        return this;
    }

    /** Use case 5 view. */
    public AppBuilder addUseCase5View() {
        // useCase5ViewModel = new UseCase5ViewModel();
        // useCase5View = new UseCase5View(useCase5ViewModel);
        // cardPanel.add(useCase5View, UseCase5ViewModel.VIEW_NAME);
        return this;
    }

    // ===================== USE CASES =====================

    /** Use case 1: Team Stats Representation (Osborn). */
    public AppBuilder addTeamStatsRepUseCase() {
        final TeamStatsRepOutputBoundary teamStatsRepOutputBoundary =
                new TeamStatsRepPresenter(teamStatsRepViewModel);
        final TeamStatsRepInputBoundary teamStatsRepInteractor =
                new TeamStatsRepInteractor(nbaDataAccessObject, teamStatsRepOutputBoundary);

        final TeamStatsRepController teamStatsRepController =
                new TeamStatsRepController(teamStatsRepInteractor);
        teamStatsRepView.setController(teamStatsRepController);
        return this;
    }

    /** Use case 2. */
    public AppBuilder addUseCase2UseCase() {
        // final UseCase2OutputBoundary useCase2OutputBoundary =
        //         new UseCase2Presenter(viewManagerModel, useCase2ViewModel);
        // final UseCase2InputBoundary useCase2Interactor =
        //         new UseCase2Interactor(nbaDataAccessObject, useCase2OutputBoundary);
        //
        // final UseCase2Controller useCase2Controller = new UseCase2Controller(useCase2Interactor);
        // useCase2View.setUseCase2Controller(useCase2Controller);
        return this;
    }

    /** Use case 3. */
    public AppBuilder addUseCase3UseCase() {
        // final UseCase3OutputBoundary useCase3OutputBoundary =
        //         new UseCase3Presenter(viewManagerModel, useCase3ViewModel);
        // final UseCase3InputBoundary useCase3Interactor =
        //         new UseCase3Interactor(nbaDataAccessObject, useCase3OutputBoundary);
        //
        // final UseCase3Controller useCase3Controller = new UseCase3Controller(useCase3Interactor);
        // useCase3View.setUseCase3Controller(useCase3Controller);
        return this;
    }

    /** Use case 4. */
    public AppBuilder addUseCase4UseCase() {
        // final UseCase4OutputBoundary useCase4OutputBoundary =
        //         new UseCase4Presenter(viewManagerModel, useCase4ViewModel);
        // final UseCase4InputBoundary useCase4Interactor =
        //         new UseCase4Interactor(nbaDataAccessObject, useCase4OutputBoundary);
        //
        // final UseCase4Controller useCase4Controller = new UseCase4Controller(useCase4Interactor);
        // useCase4View.setUseCase4Controller(useCase4Controller);
        return this;
    }

    /** Use case 5. */
    public AppBuilder addUseCase5UseCase() {
        // final UseCase5OutputBoundary useCase5OutputBoundary =
        //         new UseCase5Presenter(viewManagerModel, useCase5ViewModel);
        // final UseCase5InputBoundary useCase5Interactor =
        //         new UseCase5Interactor(nbaDataAccessObject, useCase5OutputBoundary);
        //
        // final UseCase5Controller useCase5Controller = new UseCase5Controller(useCase5Interactor);
        // useCase5View.setUseCase5Controller(useCase5Controller);
        return this;
    }

    // ===================== BUILD =====================

    public JFrame build() {
        final JFrame application = new JFrame("NBA Data Analytical Tool");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // First screen shown when the app opens (team decides which one):
        // First screen shown when the app opens:
        viewManagerModel.setState(TeamStatsRepViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
