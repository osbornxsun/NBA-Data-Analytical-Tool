package data_access;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import entity.Player;
import entity.PlayerGameStats;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.compare_players.ComparePlayersDataAccessInterface;

/**
 * Loads players and their game statistics from API-NBA.
 *
 * <p>Endpoints used:</p>
 * <ul>
 *   <li>{@code GET /players?search=...} to find candidate players by name</li>
 *   <li>{@code GET /players/statistics?id=...&season=...} for one box-score line per game</li>
 * </ul>
 */
public class ApiPlayerDataAccessObject implements ComparePlayersDataAccessInterface {

    /** The API's search parameter needs at least this many characters. */
    private static final int MIN_SEARCH_LENGTH = 3;

    private final ApiClient api;

    public ApiPlayerDataAccessObject(ApiClient api) {
        this.api = api;
    }

    /**
     * Searches by the last word of the name (usually the surname), then by the
     * other words if that finds nobody. The interactor narrows the results down.
     */
    @Override
    public List<Player> searchPlayers(String name) throws DataAccessException {
        List<String> words = new ArrayList<>(Arrays.asList(name.trim().split("\\s+")));
        Collections.reverse(words);   // surname first

        for (String word : words) {
            String term = word.replaceAll("[^\\p{L}\\p{N}'-]", "");
            if (term.length() < MIN_SEARCH_LENGTH) {
                continue;
            }
            JSONArray response = api.get("/players?search=" + encode(term));
            List<Player> players = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject p = response.getJSONObject(i);
                players.add(new Player(p.getInt("id"), p.optString("firstname", ""),
                        p.optString("lastname", "")));
            }
            if (!players.isEmpty()) {
                return players;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<PlayerGameStats> getSeasonGameStats(int playerId, int season) throws DataAccessException {
        JSONArray response = api.get("/players/statistics?id=" + playerId + "&season=" + season);

        List<JSONObject> lines = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            lines.add(response.getJSONObject(i));
        }
        // Game ids increase over the season, so this puts the games in date order.
        lines.sort(Comparator.comparingInt(ApiPlayerDataAccessObject::gameId));

        List<PlayerGameStats> games = new ArrayList<>();
        for (JSONObject line : lines) {
            games.add(parseGame(line));
        }
        return games;
    }

    static PlayerGameStats parseGame(JSONObject s) {
        JSONObject team = s.optJSONObject("team");
        String teamName = team == null ? null : team.optString("name", null);
        return new PlayerGameStats(
                teamName,
                parseMinutes(s.opt("min")),
                intOrNull(s, "points"),
                intOrNull(s, "totReb"),
                intOrNull(s, "assists"),
                intOrNull(s, "steals"),
                intOrNull(s, "blocks"),
                intOrNull(s, "turnovers"),
                intOrNull(s, "fgm"),
                intOrNull(s, "fga"),
                intOrNull(s, "tpm"),
                intOrNull(s, "tpa"),
                intOrNull(s, "ftm"),
                intOrNull(s, "fta"));
    }

    /**
     * Reads minutes played, which the API reports as "34", "34:12", "--" or null.
     *
     * @return the minutes as a decimal number, or null if not reported
     */
    static Double parseMinutes(Object raw) {
        if (raw == null || JSONObject.NULL.equals(raw)) {
            return null;
        }
        if (raw instanceof Number) {
            return ((Number) raw).doubleValue();
        }
        String text = raw.toString().trim();
        try {
            if (text.contains(":")) {
                String[] parts = text.split(":");
                return Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) / 60.0;
            }
            return Double.parseDouble(text);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    // Returns the value as an Integer, or null if it is missing, null or not a number.
    private static Integer intOrNull(JSONObject o, String key) {
        Object raw = o.opt(key);
        if (raw == null || JSONObject.NULL.equals(raw)) {
            return null;
        }
        if (raw instanceof Number) {
            return ((Number) raw).intValue();
        }
        try {
            return Integer.parseInt(raw.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int gameId(JSONObject line) {
        JSONObject game = line.optJSONObject("game");
        return game == null ? 0 : game.optInt("id", 0);
    }

    private static String encode(String term) {
        return URLEncoder.encode(term, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
