package use_case.predict_match;

import entity.MatchPrediction;

import java.util.List;

/** Saving and loading past predictions (data persistence requirement). */
public interface PredictionHistoryDataAccessInterface {

  void save(MatchPrediction prediction);

  /** Most recent first. */
  List<MatchPrediction> loadRecent(int limit);
}
