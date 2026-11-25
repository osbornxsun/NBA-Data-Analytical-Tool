package interface_adapter.predict_match;

import use_case.predict_match.PredictMatchOutputBoundary;
import use_case.predict_match.PredictMatchOutputData;

import java.util.List;

/** Formats output for display,=.
 */
public class PredictMatchPresenter implements PredictMatchOutputBoundary {

  private final PredictMatchViewModel viewModel;

  public PredictMatchPresenter(PredictMatchViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(PredictMatchOutputData out) {
    PredictMatchState state = viewModel.getState();
    state.setError(null);
    state.setHomeTeam(out.getHomeTeam());
    state.setAwayTeam(out.getAwayTeam());
    state.setHomeProbabilityText(String.format("%.1f%%", out.getHomeWinProbability() * 100));
    state.setAwayProbabilityText(String.format("%.1f%%", out.getAwayWinProbability() * 100));
    state.setHomeProbabilityPercent((int) Math.round(out.getHomeWinProbability() * 100));

    double margin = out.getExpectedHomeMargin();
    String favourite = margin >= 0 ? out.getHomeTeam() : out.getAwayTeam();
    state.setSummary(String.format("%s favoured by %.1f pts on %s (using %d-%02d season stats)",
      favourite, Math.abs(margin), out.getMatchDate(),
      out.getSeasonUsed(), (out.getSeasonUsed() + 1) % 100));
    state.setModelDescription("Model: " + out.getModelDescription());
    state.setHistory(out.getHistory());

    viewModel.setState(state);
    viewModel.notifyListeners();
  }

  @Override
  public void prepareFailView(String errorMessage) {
    PredictMatchState state = viewModel.getState();
    state.setError(errorMessage);
    viewModel.setState(state);
    viewModel.notifyListeners();
  }

  @Override
  public void prepareHistoryView(List<String> history) {
    PredictMatchState state = viewModel.getState();
    state.setError(null);
    state.setHistory(history);
    viewModel.setState(state);
    viewModel.notifyListeners();
  }
}
