package data_access;

import entity.BoxScore;
import entity.Game;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.match_statistics.MatchStatisticsDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public final class ApiMatchStatisticsDataAccessObject
        implements MatchStatisticsDataAccessInterface {

    private static final String GAMES_URL = "https://v2.nba.api-sports.io/games";
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private final String apiKey;
    private final HttpClient client;

    public ApiMatchStatisticsDataAccessObject(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("An API-Sports key is required.");
        }
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    @Override
    public List<Game> getGamesForTeam(int teamId, int season) throws IOException {
        URI uri = URI.create(GAMES_URL + "?team=" + teamId
                + "&season=" + season + "&league=standard");

        return parseGames(fetch(uri));
    }

    @Override
    public Optional<BoxScore> getBoxScore(int gameId) throws IOException {
        JSONArray games = fetch(URI.create(GAMES_URL + "?id=" + gameId));
        if (games.isEmpty()) {
            return Optional.empty();
        }
        JSONArray statistics = fetch(URI.create(GAMES_URL + "/statistics?id=" + gameId));
        return parseBoxScore(gameId, games, statistics);
    }

    private JSONArray fetch(URI uri) throws IOException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(TIMEOUT)
                .header("x-apisports-key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("The game lookup was interrupted.", exception);
        }

        if (response.statusCode() != 200) {
            throw new IOException("Game lookup returned HTTP " + response.statusCode());
        }


        try {
            JSONObject result = new JSONObject(response.body());
            Object errors = result.get("errors");
            boolean noErrors = (errors instanceof JSONArray array && array.isEmpty())
                    || (errors instanceof JSONObject object && object.isEmpty());
            if (!noErrors) {
                throw new IOException("API-Sports reported an error during game lookup.");
            }

            return result.getJSONArray("response");
        } catch (JSONException exception) {
            throw new IOException("Could not read the NBA response.", exception);
        }
    }

    static Optional<BoxScore> parseBoxScore(int gameId, JSONArray games, JSONArray statistics)
            throws IOException {
        try {
            JSONObject game = games.optJSONObject(0);
            if (game == null || game.getInt("id") != gameId) {
                return Optional.empty();
            }
            Map<Integer, JSONObject> totals = new HashMap<>();
            for (int i = 0; i < statistics.length(); i++) {
                JSONObject row = statistics.getJSONObject(i);
                JSONArray entries = row.optJSONArray("statistics");
                if (entries != null && entries.optJSONObject(0) != null) {
                    totals.put(row.getJSONObject("team").getInt("id"), entries.getJSONObject(0));
                }
            }
            JSONObject teams = game.getJSONObject("teams");
            JSONObject scores = game.optJSONObject("scores", new JSONObject());
            JSONObject away = scores.optJSONObject("visitors", new JSONObject());
            JSONObject home = scores.optJSONObject("home", new JSONObject());
            JSONArray awayLine = away.optJSONArray("linescore", new JSONArray());
            JSONArray homeLine = home.optJSONArray("linescore", new JSONArray());
            int periods = Math.max(4, Math.max(awayLine.length(), homeLine.length()));
            periods = Math.max(periods, game.optJSONObject("periods", new JSONObject()).optInt("current", 0));
            return Optional.of(new BoxScore(
                    teamScore(teams.getJSONObject("visitors"), away, awayLine, totals, periods),
                    teamScore(teams.getJSONObject("home"), home, homeLine, totals, periods)));
        } catch (JSONException exception) {
            throw new IOException("Could not read the NBA box score.", exception);
        }
    }

    private static BoxScore.Team teamScore(JSONObject team, JSONObject score, JSONArray line,
                                          Map<Integer, JSONObject> totals, int periods) {
        JSONObject stats = totals.getOrDefault(team.getInt("id"), new JSONObject());
        List<Integer> points = new ArrayList<>();
        for (int period = 0; period < periods; period++) {
            points.add(line.optIntegerObject(period, null));
        }
        return new BoxScore.Team(team.optString("name", "Unavailable"),
                score.optIntegerObject("points", null), points,
                stats.optIntegerObject("totReb", null), stats.optIntegerObject("assists", null),
                stats.optIntegerObject("steals", null));
    }

    private static List<Game> parseGames(JSONArray rows) throws IOException {
        try {
            List<Game> games = new ArrayList<>();
            for (int index = 0; index < rows.length(); index++) {
                JSONObject row = rows.getJSONObject(index);
                JSONObject teams = row.getJSONObject("teams");
                String start = row.getJSONObject("date").getString("start");

                games.add(new Game(
                        row.getInt("id"),
                        OffsetDateTime.parse(start).toLocalDate(),
                        teams.getJSONObject("home").getString("name"),
                        teams.getJSONObject("visitors").getString("name"),
                        row.getJSONObject("status").getString("long")
                ));
            }
            return games;
        } catch (JSONException | DateTimeParseException exception) {
            throw new IOException("Could not read the NBA game data.", exception);
        }
    }
}
