package use_case.predict_match;

import java.util.List;

/** Output boundary: implemented by the presenter. */
public interface PredictMatchOutputBoundary {

  void prepareSuccessView(PredictMatchOutputData outputData);

  void prepareFailView(String errorMessage);

  void prepareHistoryView(List<String> history);
}
