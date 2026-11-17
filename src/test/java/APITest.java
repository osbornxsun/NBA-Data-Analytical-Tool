import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import entity.SeasonTeam;
import entity.SeasonTeamFactory;
import entity.SeasonTeamStats;
import org.json.JSONObject;

public class APITest {

  public static void main(String[] args) throws Exception {
    String apiKey = System.getenv("API_SPORTS_KEY");

    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("API_SPORTS_KEY is not set");
    }

    HttpClient client = HttpClient.newHttpClient();

    // Example: Los Angeles Lakers, for the 2025 season
    String url =
      "https://v2.nba.api-sports.io/teams/statistics?id=17&season=2024";

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("x-apisports-key", apiKey)
      .GET()
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    System.out.println("HTTP Status: " + response.statusCode());

    JSONObject json = new JSONObject(response.body());

    System.out.println("API errors: " + json.opt("errors"));
    System.out.println("Results: " + json.opt("results"));

    // Inspect the complete response before parsing it.
    System.out.println(json.toString(2));

    JSONObject team = new JSONObject()
      .put("id", 17)
      .put("name", "Los Angeles Lakers")
      .put("city", "Los Angeles");

    SeasonTeamFactory factory = new SeasonTeamFactory();

    SeasonTeam result = factory.create(team, json, 2024);

    System.out.println("Team: " + result.getName());
    System.out.println("Season: " + result.getSeason());

    SeasonTeamStats s = result.getStats();

    System.out.println("Games: " + s.getGamesPlayed());
    System.out.println("Points: " + s.getPoints());
    System.out.println("FG%: " + s.getFieldGoalPercent());
    System.out.println("Rebounds: " + s.getTotalRebounds());
  }
}
