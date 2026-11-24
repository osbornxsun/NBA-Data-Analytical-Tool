package use_case.predict_match;

import entity.MatchPrediction;
import entity.SeasonTeam;
import entity.predictionDecorators.MatchupModel;
import entity.predictionDecorators.StatFactor;
import entity.predictionDecorators.WinProbability;
import use_case.DataAccessException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * Predict Match Outcome use case.
 * <ol>
 *   <li>Validate the request.</li>
 *   <li>Work out which NBA season the date falls in.</li>
 *   <li>Fetch both teams' season stats through the data access interface.</li>
 *   <li>Build a decorated MatchupModel from the user's chosen factors.</li>
 *   <li>Turn the expected margin into win probabilities.</li>
 *   <li>Save the prediction and send the result to the presenter.</li>
 * </ol>
 */
public class PredictMatchInteractor implements PredictMatchInputBoundary {

  static final int HISTORY_SIZE = 10;

  private final PredictMatchDataAccessInterface teamDataAccess;
  private final PredictionHistoryDataAccessInterface historyDataAccess;
  private final PredictMatchOutputBoundary presenter;

  public PredictMatchInteractor(PredictMatchDataAccessInterface teamDataAccess,
                                PredictionHistoryDataAccessInterface historyDataAccess,
                                PredictMatchOutputBoundary presenter) {
    this.teamDataAccess = teamDataAccess;
    this.historyDataAccess = historyDataAccess;
    this.presenter = presenter;
  }

  @Override
  public void execute(PredictMatchInputData input) {
    String error = validate(input);
    if (error != null) {
      presenter.prepareFailView(error);
      return;
    }

    int season = seasonFor(input.getMatchDate());
    String homeName = input.getHomeTeamName().trim();
    String awayName = input.getAwayTeamName().trim();
    SeasonTeam home;
    SeasonTeam away;
    try {
      home = teamDataAccess.getSeasonTeam(homeName, season);
      away = teamDataAccess.getSeasonTeam(awayName, season);
    } catch (DataAccessException e) {
      presenter.prepareFailView(e.getMessage());
      return;
    }
    if (home == null || away == null) {
      presenter.prepareFailView(notFoundMessage(home == null ? homeName : awayName, season));
      return;
    }
    if (home.getId() == away.getId()) {
      presenter.prepareFailView("Both names refer to the same team: " + home.getName());
      return;
    }

    MatchupModel model = StatFactor.buildModel(input.getFactors());
    double margin = model.predictMargin(home.getStats(), away.getStats());
    double homeProbability = WinProbability.fromMargin(margin);

    MatchPrediction prediction = new MatchPrediction(
      home.getName(), away.getName(), input.getMatchDate(),
      homeProbability, model.describe());
    historyDataAccess.save(prediction);

    presenter.prepareSuccessView(new PredictMatchOutputData(
      prediction.getHomeTeam(), prediction.getAwayTeam(), prediction.getMatchDate(),
      season, prediction.getHomeWinProbability(), prediction.getAwayWinProbability(),
      margin, prediction.getModelDescription(), summarise(historyDataAccess.loadRecent(HISTORY_SIZE))));
  }

  @Override
  public void loadHistory() {
    presenter.prepareHistoryView(summarise(historyDataAccess.loadRecent(HISTORY_SIZE)));
  }

  private static String notFoundMessage(String teamName, int season) {
    return String.format("No stats found for \"%s\" in the %d-%02d season. "
      + "Use the full team name, e.g. \"Boston Celtics\".", teamName, season, (season + 1) % 100);
  }

  private String validate(PredictMatchInputData input) {
    if (isBlank(input.getHomeTeamName()) || isBlank(input.getAwayTeamName())) {
      return "Please enter both team names.";
    }
    if (input.getHomeTeamName().trim().equalsIgnoreCase(input.getAwayTeamName().trim())) {
      return "Please choose two different teams.";
    }
    if (input.getMatchDate() == null) {
      return "Please choose a match date.";
    }
    if (input.getFactors().isEmpty()) {
      return "Select at least one factor for the model to consider.";
    }
    return null;
  }

  /**
   * API-Sports labels a season by the year it starts, i.e. 2025 = 2025-26.
   * Games from October to December belong to that year's season while January to September belong to
   * the season that started the previous year.
   */
  static int seasonFor(LocalDate date) {
    return date.getMonthValue() >= Month.OCTOBER.getValue() ? date.getYear() : date.getYear() - 1;
  }

  private static List<String> summarise(List<MatchPrediction> predictions) {
    List<String> lines = new ArrayList<>();
    for (MatchPrediction p : predictions) {
      lines.add(String.format("%s: %s %.0f%% vs %s %.0f%%  [%s]",
        p.getMatchDate(), p.getHomeTeam(), p.getHomeWinProbability() * 100,
        p.getAwayTeam(), p.getAwayWinProbability() * 100, p.getModelDescription()));
    }
    return lines;
  }

  private static boolean isBlank(String s) {
    return s == null || s.isBlank();
  }
}
