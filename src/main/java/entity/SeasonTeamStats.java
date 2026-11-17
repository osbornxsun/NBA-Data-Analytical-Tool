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
  private final int points;
  private final int fastBreakPoints;
  private final int pointsInPaint;
  private final int pointsOffTurnovers;
  private final int secondChancePoints;

  // Field goals
  private final int fieldGoalsMade;
  private final int fieldGoalsAttempted;
  private final double fieldGoalPercent;

  // Free throws
  private final int freeThrowsMade;
  private final int freeThrowsAttempted;
  private final double freeThrowPercent;

  // Three-pointers
  private final int threePointersMade;
  private final int threePointersAttempted;
  private final double threePointPercent;

  // Rebounding
  private final int offensiveRebounds;
  private final int defensiveRebounds;
  private final int totalRebounds;

  // Other statistics
  private final int assists;
  private final int personalFouls;
  private final int steals;
  private final int turnovers;
  private final int blocks;
  private final int plusMinus;

  // Additional contextual statistics
  private final int gamesPlayed;
  private final int biggestLead;
  private final int longestRun;

  public SeasonTeamStats(
    int gamesPlayed,
    int points,
    int fastBreakPoints,
    int pointsInPaint,
    int pointsOffTurnovers,
    int secondChancePoints,
    int fieldGoalsMade,
    int fieldGoalsAttempted,
    double fieldGoalPercent,
    int freeThrowsMade,
    int freeThrowsAttempted,
    double freeThrowPercent,
    int threePointersMade,
    int threePointersAttempted,
    double threePointPercent,
    int offensiveRebounds,
    int defensiveRebounds,
    int totalRebounds,
    int assists,
    int personalFouls,
    int steals,
    int turnovers,
    int blocks,
    int plusMinus,
    int biggestLead,
    int longestRun) {

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

  public int getGamesPlayed() { return gamesPlayed; }

  public int getPoints() { return points; }
  public int getFastBreakPoints() { return fastBreakPoints; }
  public int getPointsInPaint() { return pointsInPaint; }
  public int getPointsOffTurnovers() { return pointsOffTurnovers; }
  public int getSecondChancePoints() { return secondChancePoints; }

  public int getFieldGoalsMade() { return fieldGoalsMade; }
  public int getFieldGoalsAttempted() { return fieldGoalsAttempted; }
  public double getFieldGoalPercent() { return fieldGoalPercent; }

  public int getFreeThrowsMade() { return freeThrowsMade; }
  public int getFreeThrowsAttempted() { return freeThrowsAttempted; }
  public double getFreeThrowPercent() { return freeThrowPercent; }

  public int getThreePointersMade() { return threePointersMade; }
  public int getThreePointersAttempted() { return threePointersAttempted; }
  public double getThreePointPercent() { return threePointPercent; }

  public int getOffensiveRebounds() { return offensiveRebounds; }
  public int getDefensiveRebounds() { return defensiveRebounds; }
  public int getTotalRebounds() { return totalRebounds; }

  public int getAssists() { return assists; }
  public int getPersonalFouls() { return personalFouls; }
  public int getSteals() { return steals; }
  public int getTurnovers() { return turnovers; }
  public int getBlocks() { return blocks; }
  public int getPlusMinus() { return plusMinus; }

  public int getBiggestLead() { return biggestLead; }
  public int getLongestRun() { return longestRun; }

  /**
   * Returns a per-game average, or null if the required data is unavailable.
   */
  private double perGame(int total) {
    if (gamesPlayed == 0) {
      return 0;
    }
    return (double) total / gamesPlayed;
  }

}
