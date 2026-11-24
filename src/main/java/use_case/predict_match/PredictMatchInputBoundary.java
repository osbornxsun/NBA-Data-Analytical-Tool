package use_case.predict_match;

/** Input boundary for the Predict Match Outcome use case. */
public interface PredictMatchInputBoundary {

  void execute(PredictMatchInputData inputData);

  /** Shows previously saved predictions (called once when the app starts). */
  void loadHistory();
}
