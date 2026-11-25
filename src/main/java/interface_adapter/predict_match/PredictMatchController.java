package interface_adapter.predict_match;

import entity.predictionDecorators.StatFactor;
import use_case.predict_match.PredictMatchInputBoundary;
import use_case.predict_match.PredictMatchInputData;

import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 * Turns raw view input (strings, a date, factor keys) into InputData.
 */
public class PredictMatchController {

  private final PredictMatchInputBoundary interactor;

  public PredictMatchController(PredictMatchInputBoundary interactor) {
    this.interactor = interactor;
  }

  public void execute(String homeTeam, String awayTeam, LocalDate matchDate,
                      Collection<String> selectedFactorKeys) {
    Set<StatFactor> factors = EnumSet.noneOf(StatFactor.class);
    for (String key : selectedFactorKeys) {
      factors.add(StatFactor.valueOf(key));
    }
    interactor.execute(new PredictMatchInputData(homeTeam, awayTeam, matchDate, factors));
  }

  public void loadHistory() {
    interactor.loadHistory();
  }
}
