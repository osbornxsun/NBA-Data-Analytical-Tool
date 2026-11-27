package use_case.compare_players;

import java.util.List;

import entity.Stat;

/**
 * Result of the Compare Players use case: one card per player and, for each
 * stat, both values and which player is better.
 */
public class ComparePlayersOutputData {

    /** Which player has the better value of a stat. */
    public enum Leader {
        FIRST,
        SECOND,
        /** Both values are equal (as displayed). */
        TIE,
        /** The stat is not compared, or a value is missing. */
        NONE
    }

    private final int season;
    private final PlayerCard firstPlayer;
    private final PlayerCard secondPlayer;
    private final List<StatComparison> comparisons;

    public ComparePlayersOutputData(int season, PlayerCard firstPlayer, PlayerCard secondPlayer,
                                    List<StatComparison> comparisons) {
        this.season = season;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.comparisons = List.copyOf(comparisons);
    }

    public int getSeason() { return season; }

    public PlayerCard getFirstPlayer() { return firstPlayer; }

    public PlayerCard getSecondPlayer() { return secondPlayer; }

    /**
     * Returns one comparison per stat, in display order.
     *
     * @return the stat comparisons
     */
    public List<StatComparison> getComparisons() { return comparisons; }

    /** The header of one player card. */
    public static class PlayerCard {
        private final String name;
        private final String teamName;
        private final int gamesPlayed;

        public PlayerCard(String name, String teamName, int gamesPlayed) {
            this.name = name;
            this.teamName = teamName;
            this.gamesPlayed = gamesPlayed;
        }

        public String getName() { return name; }

        /**
         * Returns the player's team that season, or null if unknown.
         *
         * @return the team name or null
         */
        public String getTeamName() { return teamName; }

        public int getGamesPlayed() { return gamesPlayed; }
    }

    /** Both players' values for one stat. Values are null when unavailable. */
    public static class StatComparison {
        private final Stat stat;
        private final Double firstValue;
        private final Double secondValue;
        private final Leader leader;

        public StatComparison(Stat stat, Double firstValue, Double secondValue, Leader leader) {
            this.stat = stat;
            this.firstValue = firstValue;
            this.secondValue = secondValue;
            this.leader = leader;
        }

        public Stat getStat() { return stat; }

        public Double getFirstValue() { return firstValue; }

        public Double getSecondValue() { return secondValue; }

        public Leader getLeader() { return leader; }
    }
}
