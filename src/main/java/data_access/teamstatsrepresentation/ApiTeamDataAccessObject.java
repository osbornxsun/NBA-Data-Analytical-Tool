package data_access.teamstatsrepresentation;


import entity.Team;
import entity.SeasonTeamStats;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.teamstatsrepresentation.DataAccessException;
import use_case.teamstatsrepresentation.TeamLookupDataAccessInterface;
import use_case.teamstatsrepresentation.TeamStatsRepDataAccessInterface;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiTeamDataAccessObject implements TeamStatsRepDataAccessInterface, TeamLookupDataAccessInterface {

    private static final String BASE_URL = "https://v2.nba.api-sports.io";
    private final String apikey;

    public ApiTeamDataAccessObject(String apikey) {
        this.apikey = apikey;
    }

    // Sends a GET request and returns the "response" array.
    private JSONArray get(String path) throws DataAccessException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("x-apisports-key", apikey)
                    .build();
            String body = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
            return new JSONObject(body).getJSONArray("response");
        } catch (Exception e) {
            throw new DataAccessException("Could not load data from API.", e);
        }
    }

    @Override
    public Team getTeamByName(String teamName) throws DataAccessException {
        String name = URLEncoder.encode(teamName, StandardCharsets.UTF_8);
        JSONArray teams = get("/teams?name=" + name);
        if (teams.isEmpty()) {
            return null;
        }
        JSONObject t = teams.getJSONObject(0);
        return new Team(t.getInt("id"), t.getString("name"));
    }

    @Override
    public SeasonTeamStats getSeasonTeamStats(int teamId, int season) throws DataAccessException {
        JSONArray arr = get("/teams/statistics?id=" + teamId + "&season=" + season);
        if (arr.isEmpty()) {
            return null;
        }
        JSONObject s = arr.getJSONObject(0);

        return new SeasonTeamStats(
                intOrNull(s, "games"),
                intOrNull(s, "points"),
                intOrNull(s, "fastBreakPoints"),
                intOrNull(s, "pointsInPaint"),
                intOrNull(s, "pointsOffTurnovers"),
                intOrNull(s, "secondChancePoints"),
                intOrNull(s, "fgm"),
                intOrNull(s, "fga"),
                doubleOrNull(s, "fgp"),
                intOrNull(s, "ftm"),
                intOrNull(s, "fta"),
                doubleOrNull(s, "ftp"),
                intOrNull(s, "tpm"),
                intOrNull(s, "tpa"),
                doubleOrNull(s, "tpp"),
                intOrNull(s, "offReb"),
                intOrNull(s, "defReb"),
                intOrNull(s, "totReb"),
                intOrNull(s, "assists"),
                intOrNull(s, "pFouls"),
                intOrNull(s, "steals"),
                intOrNull(s, "turnovers"),
                intOrNull(s, "blocks"),
                intOrNull(s, "plusMinus"),
                intOrNull(s, "biggestLead"),
                intOrNull(s, "longestRun"));
    }

    // Returns the value as an Interger, or null if the key is missing or its value is null.
    private static Integer intOrNull(JSONObject o, String key) {
        if (o.isNull(key)) {
            return null;
        } else {
            return o.optInt(key);
        }
    }

    // Returns the value as a Double, or null if the key is missing or its value is null.
    private static Double doubleOrNull(JSONObject o, String key) {
        if (o.isNull(key)) {
            return null;
        } else {
            return o.optDouble(key);
        }
    }
}