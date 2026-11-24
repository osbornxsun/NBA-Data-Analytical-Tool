package data_access;


import entity.SeasonTeam;
import entity.SeasonTeamStats;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.predict_match.PredictMatchDataAccessInterface;
import use_case.teamstatsrepresentation.TeamStatsRepDataAccessInterface;


public class ApiTeamDataAccessObject implements TeamStatsRepDataAccessInterface, PredictMatchDataAccessInterface {

    private final ApiClient api;

    public ApiTeamDataAccessObject(ApiClient api) {
        this.api = api;
    }

    @Override
    public SeasonTeam getSeasonTeam(String teamName, int season) throws DataAccessException {

        // get team info by teamName
        String name = teamName.replace(" ", "%20");
        JSONArray teams = api.get("/teams?name=" + name);


        if (teams.isEmpty()) {
            return null;
        }
        //get team id using what the api returns
        JSONObject team = teams.getJSONObject(0);
        int id = team.getInt("id");

        //build SeasonTeam entity which holds SeasonTeamStats
        JSONArray statsArray = api.get("/teams/statistics?id=" + id + "&season=" + season);
        if (statsArray.isEmpty()) {
            return null;
        }
        SeasonTeamStats stats = parseStats(statsArray.getJSONObject(0));

        return new SeasonTeam(id, team.getString("name"), team.optString("city", null), season, stats);

    }

    private static SeasonTeamStats parseStats(JSONObject s) {
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

    // Returns the value as an Integer, or null if the key is missing or null.
    private static Integer intOrNull(JSONObject o, String key) {
        if (o.isNull(key)) {
            return null;
        }
        return o.getInt(key);
    }

    // Returns the value as a Double, or null if the key is missing or null.
    private static Double doubleOrNull(JSONObject o, String key) {
        if (o.isNull(key)) {
            return null;
        }
        return o.getDouble(key);
    }

}