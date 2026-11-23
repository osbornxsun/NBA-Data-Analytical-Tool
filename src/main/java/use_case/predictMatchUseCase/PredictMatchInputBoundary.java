package use_case.predictMatchUseCase;

/** Input boundary for the Predict Match Outcome use case. */
public interface PredictMatchInputBoundary {

  void execute(PredictMatchInputData inputData);

  /** Shows previously saved predictions (called once when the app starts). */
  void loadHistory();
}
