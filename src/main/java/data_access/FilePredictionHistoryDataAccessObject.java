package data_access;

import entity.MatchPrediction;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.predict_match.PredictionHistoryDataAccessInterface;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Stores predictions in a JSON file so the history survives restarts.
 */
public class FilePredictionHistoryDataAccessObject implements PredictionHistoryDataAccessInterface {

  private final Path file;
  private final List<MatchPrediction> predictions = new ArrayList<>();

  public FilePredictionHistoryDataAccessObject(Path file) {
    this.file = file;
    load();
  }

  @Override
  public synchronized void save(MatchPrediction prediction) {
    predictions.add(prediction);
    JSONArray array = new JSONArray();
    for (MatchPrediction p : predictions) {
      array.put(new JSONObject()
        .put("home", p.getHomeTeam())
        .put("away", p.getAwayTeam())
        .put("date", p.getMatchDate().toString())
        .put("homeProbability", p.getHomeWinProbability())
        .put("model", p.getModelDescription()));
    }
    try {
      if (file.getParent() != null) {
        Files.createDirectories(file.getParent());
      }
      Files.writeString(file, array.toString(2), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException("Could not save prediction history", e);
    }
  }

  @Override
  public synchronized List<MatchPrediction> loadRecent(int limit) {
    List<MatchPrediction> recent = new ArrayList<>();
    for (int i = predictions.size() - 1; i >= 0 && recent.size() < limit; i--) {
      recent.add(predictions.get(i));
    }
    return recent;
  }

  private void load() {
    if (!Files.exists(file)) {
      return;
    }
    try {
      JSONArray array = new JSONArray(Files.readString(file, StandardCharsets.UTF_8));
      for (int i = 0; i < array.length(); i++) {
        JSONObject o = array.getJSONObject(i);
        predictions.add(new MatchPrediction(o.getString("home"), o.getString("away"),
          LocalDate.parse(o.getString("date")), o.getDouble("homeProbability"),
          o.optString("model", "")));
      }
    } catch (IOException | RuntimeException e) {
      // A corrupt history file shouldn't stop the app so we start with an empty history.
      predictions.clear();
    }
  }
}
