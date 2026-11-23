package use_case.predict_match;

import java.time.LocalDate;
import java.util.List;

/**
 * Plain data handed to the presenter. Contains no entities, so outer layers
 * never depend on entity classes through this object.
 */
public class PredictMatchOutputData {

  private final String homeTeam;
  private final String awayTeam;
  private final LocalDate matchDate;
  private final int seasonUsed;
  private final double homeWinProbability;
  private final double awayWinProbability;
  private final double expectedHomeMargin;
  private final String modelDescription;
  private final List<String> history;

  public PredictMatchOutputData(String homeTeam, String awayTeam, LocalDate matchDate,
                                int seasonUsed, double homeWinProbability,
                                double awayWinProbability, double expectedHomeMargin,
                                String modelDescription, List<String> history) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.matchDate = matchDate;
    this.seasonUsed = seasonUsed;
    this.homeWinProbability = homeWinProbability;
    this.awayWinProbability = awayWinProbability;
    this.expectedHomeMargin = expectedHomeMargin;
    this.modelDescription = modelDescription;
    this.history = List.copyOf(history);
  }

  public String getHomeTeam() { return homeTeam; }
  public String getAwayTeam() { return awayTeam; }
  public LocalDate getMatchDate() { return matchDate; }
  public int getSeasonUsed() { return seasonUsed; }
  public double getHomeWinProbability() { return homeWinProbability; }
  public double getAwayWinProbability() { return awayWinProbability; }
  public double getExpectedHomeMargin() { return expectedHomeMargin; }
  public String getModelDescription() { return modelDescription; }
  public List<String> getHistory() { return history; }
}
