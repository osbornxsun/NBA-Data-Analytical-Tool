package entity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Creates SeasonTeam entities from API-Sports NBA JSON responses.
 */
public class SeasonTeamFactory {

  public SeasonTeam create(JSONObject team, JSONObject stats, int season) {

    if (team == null || stats == null) {
      throw new IllegalArgumentException("Team and statistics cannot be null");
    }

    JSONObject statObject = extractStats(stats);

    int id = team.getInt("id");
    String name = team.getString("name");

    String nickname = team.optString("nickname", null);
    String code = team.optString("code", null);
    String city = team.optString("city", null);

    SeasonTeamStats relevantStats = new SeasonTeamStats(
      readInt(statObject, "games"),
      readInt(statObject, "points"),
      readInt(statObject, "fastBreakPoints"),
      readInt(statObject, "pointsInPaint"),
      readInt(statObject, "pointsOffTurnovers"),
      readInt(statObject, "secondChancePoints"),

      readInt(statObject, "fgm"),
      readInt(statObject, "fga"),
      readDouble(statObject, "fgp"),

      readInt(statObject, "ftm"),
      readInt(statObject, "fta"),
      readDouble(statObject, "ftp"),

      readInt(statObject, "tpm"),
      readInt(statObject, "tpa"),
      readDouble(statObject, "tpp"),

      readInt(statObject, "offReb"),
      readInt(statObject, "defReb"),
      readInt(statObject, "totReb"),

      readInt(statObject, "assists"),
      readInt(statObject, "pFouls"),
      readInt(statObject, "steals"),
      readInt(statObject, "turnovers"),
      readInt(statObject, "blocks"),
      readInt(statObject, "plusMinus"),

      readInt(statObject, "biggestLead"),
      readInt(statObject, "longestRun")
    );

    return new SeasonTeam(
      id, name, city, season, relevantStats
    );
  }

  /**
   * Accepts either the complete API response or an individual response item.
   */
  private JSONObject extractStats(JSONObject stats) {

    if (stats.has("response")) {
      JSONArray response = stats.getJSONArray("response");

      if (response.isEmpty()) {
        throw new IllegalArgumentException(
          "API returned no team statistics"
        );
      }

      return response.getJSONObject(0);
    }

    return stats;
  }

  /**
   * Reads an integer field while preserving missing/null values.
   */
  private Integer readInt(JSONObject json, String key) {
    if (!json.has(key) || json.isNull(key)) {
      return null;
    }

    Object value = json.get(key);

    if (value instanceof Number) {
      return ((Number) value).intValue();
    }

    if (value instanceof String) {
      return (int) Double.parseDouble((String) value);
    }

    return null;
  }

  /**
   * Reads a numeric field that may be returned as either a JSON number
   * or a numeric string.
   */
  private Double readDouble(JSONObject json, String key) {
    if (!json.has(key) || json.isNull(key)) {
      return null;
    }

    Object value = json.get(key);

    if (value instanceof Number) {
      return ((Number) value).doubleValue();
    }

    if (value instanceof String) {
      return Double.parseDouble((String) value);
    }

    return null;
  }
}
