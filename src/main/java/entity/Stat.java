package entity;

/**
 * The season-average stats shown on a player card, in display order.
 *
 * <p>Each stat knows whether a higher or a lower value is better, or whether it
 * should not be compared at all (games played is context, not a "win").</p>
 */
public enum Stat {
    GAMES_PLAYED(Direction.NOT_COMPARED),
    MINUTES(Direction.HIGHER_IS_BETTER),
    POINTS(Direction.HIGHER_IS_BETTER),
    REBOUNDS(Direction.HIGHER_IS_BETTER),
    ASSISTS(Direction.HIGHER_IS_BETTER),
    STEALS(Direction.HIGHER_IS_BETTER),
    BLOCKS(Direction.HIGHER_IS_BETTER),
    TURNOVERS(Direction.LOWER_IS_BETTER),
    FIELD_GOAL_PERCENT(Direction.HIGHER_IS_BETTER),
    THREE_POINT_PERCENT(Direction.HIGHER_IS_BETTER),
    FREE_THROW_PERCENT(Direction.HIGHER_IS_BETTER);

    /** Which way a stat should be compared. */
    public enum Direction { HIGHER_IS_BETTER, LOWER_IS_BETTER, NOT_COMPARED }

    private final Direction direction;

    Stat(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns true if this stat is a percentage (stored as 0-100).
     *
     * @return whether the stat is a percentage
     */
    public boolean isPercentage() {
        return this == FIELD_GOAL_PERCENT || this == THREE_POINT_PERCENT || this == FREE_THROW_PERCENT;
    }
}
