package interface_adapter.predict_match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the Match Prediction view's state and notifies listeners when it changes
 * (Observer pattern). Similar to TeamStatsRepViewModel so both views work the same way.
 */
public class PredictMatchViewModel {

  public static final String VIEW_NAME = "match prediction";
  public static final String TITLE_LABEL = "Predict Match Outcome";
  public static final String PREDICT_BUTTON_LABEL = "Predict";
  public static final String TEAM_NAME_HINT = "Full team name, e.g. Boston Celtics";

  private final List<PredictMatchViewModelListener> listeners = new ArrayList<>();
  private final Map<String, String> factorOptions;
  private PredictMatchState state = new PredictMatchState();

  /**
   * @param factorOptions factor key -> checkbox label. Supplied by the app builder,
   *                      so the view never needs to import the entity enum.
   *                      Map is made immutable in order to prevent changes down the line.
   */
  public PredictMatchViewModel(Map<String, String> factorOptions) {
    this.factorOptions = Collections.unmodifiableMap(new LinkedHashMap<>(factorOptions));
  }

  public Map<String, String> getFactorOptions() { return factorOptions; }

  public PredictMatchState getState() { return new PredictMatchState(state); }

  public void setState(PredictMatchState state) { this.state = new PredictMatchState(state); }

  public void addListener(PredictMatchViewModelListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners() {
    for (PredictMatchViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
