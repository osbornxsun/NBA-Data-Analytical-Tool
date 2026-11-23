package entity.predictionDecorators;

import entity.SeasonTeamStats;

/**
 * Concrete component: the innermost object every decorator chain wraps.
 * With no factors selected the teams are considered equal (50 / 50).
 */
public class BaselineModel implements MatchupModel {

  @Override
  public double predictMargin(SeasonTeamStats home, SeasonTeamStats away) {
    return 0.0;
  }

  @Override
  public String describe() {
    return "Baseline";
  }
}
