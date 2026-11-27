package entity;

/**
 * One player's box-score line for a single game.
 *
 * <p>Any value may be null when the API did not report it. A line counts as a
 * game played only if the player actually took the floor (see {@link #isPlayed()}).</p>
 */
public class PlayerGameStats {

    private final String teamName;
    private final Double minutes;
    private final Integer points;
    private final Integer rebounds;
    private final Integer assists;
    private final Integer steals;
    private final Integer blocks;
    private final Integer turnovers;
    private final Integer fieldGoalsMade;
    private final Integer fieldGoalsAttempted;
    private final Integer threePointersMade;
    private final Integer threePointersAttempted;
    private final Integer freeThrowsMade;
    private final Integer freeThrowsAttempted;

    public PlayerGameStats(String teamName, Double minutes, Integer points, Integer rebounds,
                           Integer assists, Integer steals, Integer blocks, Integer turnovers,
                           Integer fieldGoalsMade, Integer fieldGoalsAttempted,
                           Integer threePointersMade, Integer threePointersAttempted,
                           Integer freeThrowsMade, Integer freeThrowsAttempted) {
        this.teamName = teamName;
        this.minutes = minutes;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.turnovers = turnovers;
        this.fieldGoalsMade = fieldGoalsMade;
        this.fieldGoalsAttempted = fieldGoalsAttempted;
        this.threePointersMade = threePointersMade;
        this.threePointersAttempted = threePointersAttempted;
        this.freeThrowsMade = freeThrowsMade;
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    /**
     * Returns true if the player appeared in this game. When minutes are reported
     * they must be positive; when they are missing, a reported points value is
     * taken as evidence that the player played.
     *
     * @return whether this line counts as a game played
     */
    public boolean isPlayed() {
        if (minutes != null) {
            return minutes > 0;
        }
        return points != null;
    }

    public String getTeamName() { return teamName; }

    public Double getMinutes() { return minutes; }

    public Integer getPoints() { return points; }

    public Integer getRebounds() { return rebounds; }

    public Integer getAssists() { return assists; }

    public Integer getSteals() { return steals; }

    public Integer getBlocks() { return blocks; }

    public Integer getTurnovers() { return turnovers; }

    public Integer getFieldGoalsMade() { return fieldGoalsMade; }

    public Integer getFieldGoalsAttempted() { return fieldGoalsAttempted; }

    public Integer getThreePointersMade() { return threePointersMade; }

    public Integer getThreePointersAttempted() { return threePointersAttempted; }

    public Integer getFreeThrowsMade() { return freeThrowsMade; }

    public Integer getFreeThrowsAttempted() { return freeThrowsAttempted; }
}
