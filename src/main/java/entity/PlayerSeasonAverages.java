package entity;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A player's per-game averages over one season.
 *
 * <p>Averages are taken over the games the player actually played. Shooting
 * percentages are computed from season totals (made / attempted), not by
 * averaging each game's percentage, and are null when there were no attempts.</p>
 */
public class PlayerSeasonAverages {

    private final Player player;
    private final int season;
    private final String teamName;
    private final Map<Stat, Double> values;

    private PlayerSeasonAverages(Player player, int season, String teamName, Map<Stat, Double> values) {
        this.player = player;
        this.season = season;
        this.teamName = teamName;
        this.values = values;
    }

    /**
     * Builds season averages from a player's game lines.
     *
     * @param player the player
     * @param season the season start year, e.g. 2024 for 2024-25
     * @param games  the player's game lines, oldest first (games not played are ignored)
     * @return the season averages; games played is 0 if no game was played
     */
    public static PlayerSeasonAverages fromGames(Player player, int season, List<PlayerGameStats> games) {
        Map<Stat, Double> values = new EnumMap<>(Stat.class);

        Totals minutes = new Totals();
        Totals points = new Totals();
        Totals rebounds = new Totals();
        Totals assists = new Totals();
        Totals steals = new Totals();
        Totals blocks = new Totals();
        Totals turnovers = new Totals();
        Totals fgm = new Totals();
        Totals fga = new Totals();
        Totals tpm = new Totals();
        Totals tpa = new Totals();
        Totals ftm = new Totals();
        Totals fta = new Totals();

        int played = 0;
        String teamName = null;
        for (PlayerGameStats g : games) {
            if (!g.isPlayed()) {
                continue;
            }
            played++;
            if (g.getTeamName() != null && !g.getTeamName().isBlank()) {
                teamName = g.getTeamName();   // the latest team the player appeared for
            }
            minutes.add(g.getMinutes());
            points.add(g.getPoints());
            rebounds.add(g.getRebounds());
            assists.add(g.getAssists());
            steals.add(g.getSteals());
            blocks.add(g.getBlocks());
            turnovers.add(g.getTurnovers());
            fgm.add(g.getFieldGoalsMade());
            fga.add(g.getFieldGoalsAttempted());
            tpm.add(g.getThreePointersMade());
            tpa.add(g.getThreePointersAttempted());
            ftm.add(g.getFreeThrowsMade());
            fta.add(g.getFreeThrowsAttempted());
        }

        values.put(Stat.GAMES_PLAYED, (double) played);
        values.put(Stat.MINUTES, minutes.average());
        values.put(Stat.POINTS, points.average());
        values.put(Stat.REBOUNDS, rebounds.average());
        values.put(Stat.ASSISTS, assists.average());
        values.put(Stat.STEALS, steals.average());
        values.put(Stat.BLOCKS, blocks.average());
        values.put(Stat.TURNOVERS, turnovers.average());
        values.put(Stat.FIELD_GOAL_PERCENT, percent(fgm, fga));
        values.put(Stat.THREE_POINT_PERCENT, percent(tpm, tpa));
        values.put(Stat.FREE_THROW_PERCENT, percent(ftm, fta));

        return new PlayerSeasonAverages(player, season, teamName, values);
    }

    public Player getPlayer() { return player; }

    public int getSeason() { return season; }

    /**
     * Returns the team the player most recently played for this season, or null.
     *
     * @return the team name or null
     */
    public String getTeamName() { return teamName; }

    public int getGamesPlayed() {
        return values.get(Stat.GAMES_PLAYED).intValue();
    }

    /**
     * Returns the value of a stat, or null if it is not available.
     *
     * @param stat the stat
     * @return the per-game average (or percentage, 0-100), or null
     */
    public Double get(Stat stat) {
        return values.get(stat);
    }

    private static Double percent(Totals made, Totals attempted) {
        if (made.count == 0 || attempted.count == 0 || attempted.sum == 0) {
            return null;
        }
        return 100.0 * made.sum / attempted.sum;
    }

    /** Running sum of the non-null values of one stat. */
    private static final class Totals {
        private double sum;
        private int count;

        void add(Number value) {
            if (value != null) {
                sum += value.doubleValue();
                count++;
            }
        }

        Double average() {
            return count == 0 ? null : sum / count;
        }
    }
}
