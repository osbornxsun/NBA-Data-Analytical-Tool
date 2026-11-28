package interface_adapter.match_statistics;

import use_case.match_statistics.CompletedGameOutputData;
import use_case.match_statistics.MatchStatisticsOutputBoundary;

import java.util.List;
import java.util.Optional;
import entity.BoxScore;

/** Converts game-search outcomes into updates for the view. */
public final class MatchStatisticsPresenter implements MatchStatisticsOutputBoundary {
    private final MatchStatisticsViewModel viewModel;

    public MatchStatisticsPresenter(MatchStatisticsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(List<CompletedGameOutputData> completedGames) {
        String message = completedGames.isEmpty() ? "No completed games found." : "";
        viewModel.setResult(completedGames, message);
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setResult(List.of(), message);
    }

    @Override
    public void presentBoxScore(Optional<BoxScore> score) {
        String message = score.isEmpty() ? "Statistics unavailable for this game." : "";
        viewModel.setBoxScore(score, message);
    }
}
