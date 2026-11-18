package entity;

/**
 * Entitiy that stores a team's statistics for a particular NBA season.
 *  - Counting statistics are stored as season totals.
 *  - Shooting percentages are stored as percentages (e.g. 47.5, not 0.475).
 * <p>
 * Nullable fields represent statistics that were unavailable in the API
 * response, rather than treating missing values as zero.
 */

public class SeasonTeamStats {
  // Scoring
  private final Integer points;
  private final Integer fastBreakPoints;
  private final Integer pointsInPaint;
  private final Integer pointsOffTurnovers;
  private final Integer secondChancePoints;

  // Field goals
  private final Integer fieldGoalsMade;
  private final Integer fieldGoalsAttempted;
  private final Double fieldGoalPercent;

  // Free throws
  private final Integer freeThrowsMade;
  private final Integer freeThrowsAttempted;
  private final Double freeThrowPercent;

  // Three-pointers
  private final Integer threePointersMade;
  private final Integer threePointersAttempted;
  private final Double threePointPercent;

  // Rebounding
  private final Integer offensiveRebounds;
  private final Integer defensiveRebounds;
  private final Integer totalRebounds;

  // Other statistics
  private final Integer assists;
  private final Integer personalFouls;
  private final Integer steals;
  private final Integer turnovers;
  private final Integer blocks;
  private final Integer plusMinus;

  // Additional contextual statistics
  private final Integer gamesPlayed;
  private final Integer biggestLead;
  private final Integer longestRun;

  public SeasonTeamStats(
    Integer gamesPlayed,
    Integer points,
    Integer fastBreakPoints,
    Integer pointsInPaint,
    Integer pointsOffTurnovers,
    Integer secondChancePoints,
    Integer fieldGoalsMade,
    Integer fieldGoalsAttempted,
    Double fieldGoalPercent,
    Integer freeThrowsMade,
    Integer freeThrowsAttempted,
    Double freeThrowPercent,
    Integer threePointersMade,
    Integer threePointersAttempted,
    Double threePointPercent,
    Integer offensiveRebounds,
    Integer defensiveRebounds,
    Integer totalRebounds,
    Integer assists,
    Integer personalFouls,
    Integer steals,
    Integer turnovers,
    Integer blocks,
    Integer plusMinus,
    Integer biggestLead,
    Integer longestRun) {

    this.gamesPlayed = gamesPlayed;
    this.points = points;
    this.fastBreakPoints = fastBreakPoints;
    this.pointsInPaint = pointsInPaint;
    this.pointsOffTurnovers = pointsOffTurnovers;
    this.secondChancePoints = secondChancePoints;
    this.fieldGoalsMade = fieldGoalsMade;
    this.fieldGoalsAttempted = fieldGoalsAttempted;
    this.fieldGoalPercent = fieldGoalPercent;
    this.freeThrowsMade = freeThrowsMade;
    this.freeThrowsAttempted = freeThrowsAttempted;
    this.freeThrowPercent = freeThrowPercent;
    this.threePointersMade = threePointersMade;
    this.threePointersAttempted = threePointersAttempted;
    this.threePointPercent = threePointPercent;
    this.offensiveRebounds = offensiveRebounds;
    this.defensiveRebounds = defensiveRebounds;
    this.totalRebounds = totalRebounds;
    this.assists = assists;
    this.personalFouls = personalFouls;
    this.steals = steals;
    this.turnovers = turnovers;
    this.blocks = blocks;
    this.plusMinus = plusMinus;
    this.biggestLead = biggestLead;
    this.longestRun = longestRun;
  }

  public Integer getGamesPlayed() { return gamesPlayed; }

  public Integer getPoints() { return points; }
  public Integer getFastBreakPoints() { return fastBreakPoints; }
  public Integer getPointsInPaint() { return pointsInPaint; }
  public Integer getPointsOffTurnovers() { return pointsOffTurnovers; }
  public Integer getSecondChancePoints() { return secondChancePoints; }

  public Integer getFieldGoalsMade() { return fieldGoalsMade; }
  public Integer getFieldGoalsAttempted() { return fieldGoalsAttempted; }
  public Double getFieldGoalPercent() { return fieldGoalPercent; }

  public Integer getFreeThrowsMade() { return freeThrowsMade; }
  public Integer getFreeThrowsAttempted() { return freeThrowsAttempted; }
  public Double getFreeThrowPercent() { return freeThrowPercent; }

  public Integer getThreePointersMade() { return threePointersMade; }
  public Integer getThreePointersAttempted() { return threePointersAttempted; }
  public Double getThreePointPercent() { return threePointPercent; }

  public Integer getOffensiveRebounds() { return offensiveRebounds; }
  public Integer getDefensiveRebounds() { return defensiveRebounds; }
  public Integer getTotalRebounds() { return totalRebounds; }

  public Integer getAssists() { return assists; }
  public Integer getPersonalFouls() { return personalFouls; }
  public Integer getSteals() { return steals; }
  public Integer getTurnovers() { return turnovers; }
  public Integer getBlocks() { return blocks; }
  public Integer getPlusMinus() { return plusMinus; }

  public Integer getBiggestLead() { return biggestLead; }
  public Integer getLongestRun() { return longestRun; }

  /**
   * Returns a per-game average, or null if the required data is unavailable.
   */
  private Double perGame(Integer total) {
    if (gamesPlayed == 0 || total == null || gamesPlayed == null) {
      return null;
    }
    return (double) (total / gamesPlayed);
  }

}
