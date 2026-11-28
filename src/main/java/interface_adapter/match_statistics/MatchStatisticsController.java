package interface_adapter.match_statistics;

import use_case.match_statistics.MatchStatisticsInputBoundary;
import use_case.match_statistics.MatchStatisticsInputData;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/** Handles requests from the Match Statistics view. */
public final class MatchStatisticsController {
    private final MatchStatisticsInputBoundary interactor;
    private SwingWorker<Void, Void> pending;

    public MatchStatisticsController(MatchStatisticsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void findGames(int teamId, int season) {
        MatchStatisticsInputData inputData = new MatchStatisticsInputData(teamId, season);
        interactor.execute(inputData);
    }

    public void viewStatistics(int gameId) {
        cancelStatistics();
        pending = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                interactor.viewStatistics(gameId, result -> SwingUtilities.invokeLater(() -> {
                    if (pending == this) {
                        result.run();
                    }
                }));
                return null;
            }
        };
        pending.execute();
    }

    /** Called on the Swing thread when the selected game changes. */
    public void cancelStatistics() {
        if (pending != null) {
            pending.cancel(true);
            pending = null;
        }
    }
}
