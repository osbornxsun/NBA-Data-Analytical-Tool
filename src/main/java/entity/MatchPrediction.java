package entity.prediction;

import java.time.LocalDate;

/**
 * The predicted outcome of a match between two NBA teams.
 * Probabilities are stored in [0, 1] and must sum to 1.
 */
public class MatchPrediction {

  private static final double TOLERANCE = 1e-6;

  private final String homeTeam;
  private final String awayTeam;
  private final LocalDate matchDate;
  private final double homeWinProbability;
  private final double awayWinProbability;
  private final String modelDescription;

  public MatchPrediction(String homeTeam, String awayTeam, LocalDate matchDate,
                         double homeWinProbability, String modelDescription) {
    if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) {
      throw new IllegalArgumentException("Both team names are required");
    }
    if (homeTeam.equalsIgnoreCase(awayTeam)) {
      throw new IllegalArgumentException("A team cannot play itself");
    }
    if (matchDate == null) {
      throw new IllegalArgumentException("Match date is required");
    }
    if (homeWinProbability < 0 || homeWinProbability > 1 || Double.isNaN(homeWinProbability)) {
      throw new IllegalArgumentException("Probability must be between 0 and 1");
    }

    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.matchDate = matchDate;
    this.homeWinProbability = homeWinProbability;
    this.awayWinProbability = 1.0 - homeWinProbability;
    this.modelDescription = modelDescription == null ? "" : modelDescription;

    assert Math.abs(this.homeWinProbability + this.awayWinProbability - 1.0) < TOLERANCE;
  }

  public String getHomeTeam() { return homeTeam; }
  public String getAwayTeam() { return awayTeam; }
  public LocalDate getMatchDate() { return matchDate; }
  public double getHomeWinProbability() { return homeWinProbability; }
  public double getAwayWinProbability() { return awayWinProbability; }
  public String getModelDescription() { return modelDescription; }

  /** Name of the team favoured to win (home team on an exact tie). */
  public String getFavourite() {
    return homeWinProbability >= awayWinProbability ? homeTeam : awayTeam;
  }
}
