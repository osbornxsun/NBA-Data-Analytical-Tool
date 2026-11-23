package entity.predictionDecorators;

import entity.SeasonTeamStats;

/**
 * Component interface of the Decorator pattern.
 * <p>
 * A MatchupModel estimates the home team's expected winning margin (in points)
 * against the away team. Positive iff home team expected to win.
 * Concrete decorators each add the influence of one statistic, so the user can
 * build a model from exactly the stats they care about at runtime.
 */
public interface MatchupModel {

  /** Expected home-team margin in points. */
  double predictMargin(SeasonTeamStats home, SeasonTeamStats away);

  /** Human-readable list of the factors this model uses, e.g. "Baseline + FG% + Turnovers". */
  String describe();
}
