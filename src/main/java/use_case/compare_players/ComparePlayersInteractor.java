package use_case.compare_players;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import entity.Player;
import entity.PlayerGameStats;
import entity.PlayerSeasonAverages;
import entity.Stat;
import use_case.DataAccessException;
import use_case.compare_players.ComparePlayersOutputData.Leader;
import use_case.compare_players.ComparePlayersOutputData.PlayerCard;
import use_case.compare_players.ComparePlayersOutputData.StatComparison;

/**
 * Compares two players' season averages stat by stat.
 *
 * <p>Steps: validate the input, find each player by name, load each player's
 * game lines for the season, compute season averages, and decide for every
 * stat which player is better.</p>
 */
public class ComparePlayersInteractor implements ComparePlayersInputBoundary {

    /** Most names to list when a name matches several players. */
    private static final int MAX_SUGGESTIONS = 5;

    private final ComparePlayersDataAccessInterface dataAccess;
    private final ComparePlayersOutputBoundary presenter;

    public ComparePlayersInteractor(ComparePlayersDataAccessInterface dataAccess,
                                    ComparePlayersOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ComparePlayersInputData inputData) {
        String firstName = inputData.getFirstPlayerName();
        String secondName = inputData.getSecondPlayerName();
        int season = inputData.getSeason();

        if (isBlank(firstName) || isBlank(secondName)) {
            presenter.prepareFailView("Please enter the names of two players.");
            return;
        }

        try {
            Player first = findPlayer(firstName.trim());
            Player second = findPlayer(secondName.trim());
            if (first.getId() == second.getId()) {
                presenter.prepareFailView("Please enter two different players.");
                return;
            }

            PlayerSeasonAverages firstAverages = loadAverages(first, season);
            PlayerSeasonAverages secondAverages = loadAverages(second, season);

            presenter.prepareSuccessView(compare(season, firstAverages, secondAverages));
        } catch (LookupFailedException e) {
            presenter.prepareFailView(e.getMessage());
        } catch (DataAccessException e) {
            presenter.prepareFailView("Could not load data: " + e.getMessage());
        }
    }

    /**
     * Picks the one player the user meant. An exact full-name match wins;
     * otherwise a single player whose name contains every word typed is used.
     */
    private Player findPlayer(String name) throws DataAccessException, LookupFailedException {
        List<Player> candidates = dataAccess.searchPlayers(name);
        String wanted = normalize(name);

        List<Player> exact = candidates.stream()
                .filter(p -> normalize(p.getFullName()).equals(wanted))
                .collect(Collectors.toList());
        if (exact.size() == 1) {
            return exact.get(0);
        }
        if (exact.size() > 1) {
            throw ambiguous(name, exact);
        }

        Set<String> wantedWords = new HashSet<>(Arrays.asList(wanted.split(" ")));
        List<Player> partial = candidates.stream()
                .filter(p -> new HashSet<>(Arrays.asList(normalize(p.getFullName()).split(" ")))
                        .containsAll(wantedWords))
                .collect(Collectors.toList());
        if (partial.size() == 1) {
            return partial.get(0);
        }
        if (partial.isEmpty()) {
            throw new LookupFailedException("Player \"" + name + "\" not found.");
        }
        throw ambiguous(name, partial);
    }

    private PlayerSeasonAverages loadAverages(Player player, int season)
            throws DataAccessException, LookupFailedException {
        List<PlayerGameStats> games = dataAccess.getSeasonGameStats(player.getId(), season);
        PlayerSeasonAverages averages = PlayerSeasonAverages.fromGames(player, season, games);
        if (averages.getGamesPlayed() == 0) {
            throw new LookupFailedException(player.getFullName() + " did not play in the "
                    + seasonLabel(season) + " season.");
        }
        return averages;
    }

    private static ComparePlayersOutputData compare(int season, PlayerSeasonAverages first,
                                                    PlayerSeasonAverages second) {
        List<StatComparison> comparisons = new ArrayList<>();
        for (Stat stat : Stat.values()) {
            Double a = first.get(stat);
            Double b = second.get(stat);
            comparisons.add(new StatComparison(stat, a, b, leader(stat, a, b)));
        }
        return new ComparePlayersOutputData(season, card(first), card(second), comparisons);
    }

    /**
     * Decides which value is better. Values are compared as displayed (one
     * decimal place), so two values that look the same are a tie.
     */
    static Leader leader(Stat stat, Double first, Double second) {
        if (stat.getDirection() == Stat.Direction.NOT_COMPARED || first == null || second == null) {
            return Leader.NONE;
        }
        int cmp = Long.compare(Math.round(first * 10), Math.round(second * 10));
        if (cmp == 0) {
            return Leader.TIE;
        }
        boolean firstHigher = cmp > 0;
        boolean higherIsBetter = stat.getDirection() == Stat.Direction.HIGHER_IS_BETTER;
        return firstHigher == higherIsBetter ? Leader.FIRST : Leader.SECOND;
    }

    private static PlayerCard card(PlayerSeasonAverages averages) {
        return new PlayerCard(averages.getPlayer().getFullName(), averages.getTeamName(),
                averages.getGamesPlayed());
    }

    private static LookupFailedException ambiguous(String name, List<Player> matches) {
        String names = matches.stream()
                .limit(MAX_SUGGESTIONS)
                .map(Player::getFullName)
                .collect(Collectors.joining(", "));
        if (matches.size() > MAX_SUGGESTIONS) {
            names += ", ...";
        }
        return new LookupFailedException("\"" + name + "\" matches several players: " + names
                + ". Please enter the full name.");
    }

    /** Lower-cases, strips accents and punctuation, and collapses spaces ("Dončić" -> "doncic"). */
    static String normalize(String name) {
        String noAccents = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return noAccents.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
    }

    private static String seasonLabel(int season) {
        return season + "-" + String.format("%02d", (season + 1) % 100);
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    /** A lookup problem that should be shown to the user as-is. */
    private static final class LookupFailedException extends Exception {
        LookupFailedException(String message) {
            super(message);
        }
    }
}
