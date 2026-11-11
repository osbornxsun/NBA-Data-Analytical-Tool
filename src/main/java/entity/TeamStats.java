package entity;

/**
 * Entity that stores relevant team statisitcs.
 * Teams have:
 *     - id: Unique integer id for the team.
 *     - name: Name of the NBA team.
 *     - stats: Pointer to TeamsStats object with statisitcs of the team.
 */
public class TeamStats {
  private final int gamesPlayed;
  private final int pointsPerGame;
  private final int fieldGoalPercent;
  private final int freeThrowPercent;
  private final int threePointPercent;
  private final int reboundsPerGame;
  private final int assistsPerGame;
  private final int stealsPerGame;
  private final int blocksPerGame;
  private final int turnoversPerGame;
  private final int plusMinus;

  public TeamStats(int gamesPlayed, int pointsPerGame,
                   int fieldGoalPercent, int freeThrowPercent,
                   int threePointPercent, int reboundsPerGame,
                   int assistsPerGame, int stealsPerGame, int blocksPerGame,
                   int turnoversPerGame, int plusMinus) {
    this.gamesPlayed = gamesPlayed;
    this.pointsPerGame = pointsPerGame;
    this.fieldGoalPercent = fieldGoalPercent;
    this.freeThrowPercent = freeThrowPercent;
    this.threePointPercent = threePointPercent;
    this.reboundsPerGame = reboundsPerGame;
    this.assistsPerGame = assistsPerGame;
    this.stealsPerGame = stealsPerGame;
    this.blocksPerGame = blocksPerGame;
    this.turnoversPerGame = turnoversPerGame;
    this.plusMinus = plusMinus;
  }

  public int getGamesPlayed() { return gamesPlayed; }
  public int getPointsPerGame() { return pointsPerGame; }
  public int getFieldGoalPercent() { return fieldGoalPercent; }
  public int getFreeThrowPercent() { return freeThrowPercent; }
  public int getThreePointPercent() { return threePointPercent; }
  public int getReboundsPerGame() { return reboundsPerGame; }
  public int getAssistsPerGame() { return assistsPerGame; }
  public int getStealsPerGame() { return stealsPerGame; }
  public int getBlocksPerGame() { return blocksPerGame; }
  public int getTurnoversPerGame() { return turnoversPerGame; }
  public int getPlusMinus() { return plusMinus; }
}
