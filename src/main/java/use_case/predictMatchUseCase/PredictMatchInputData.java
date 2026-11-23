package use_case.predictMatchUseCase;

import entity.predictionDecorators.StatFactor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** What the user asked for: two teams, a date, and which factors the model should use. */
public class PredictMatchInputData {

  private final String homeTeamName;
  private final String awayTeamName;
  private final LocalDate matchDate;
  private final Set<StatFactor> factors;

  public PredictMatchInputData(String homeTeamName, String awayTeamName,
                               LocalDate matchDate, Set<StatFactor> factors) {
    this.homeTeamName = homeTeamName;
    this.awayTeamName = awayTeamName;
    this.matchDate = matchDate;
    this.factors = factors == null || factors.isEmpty()
      ? Collections.emptySet()
      : Collections.unmodifiableSet(EnumSet.copyOf(factors));
  }

  public String getHomeTeamName() { return homeTeamName; }
  public String getAwayTeamName() { return awayTeamName; }
  public LocalDate getMatchDate() { return matchDate; }
  public Set<StatFactor> getFactors() { return factors; }
}
