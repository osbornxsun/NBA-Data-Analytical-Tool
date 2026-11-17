import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiTeamIDTest {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("API_SPORTS_KEY");
        String teamName = "Atlanta Hawks";   // change to any full team name

        String encodedName = URLEncoder.encode(teamName, StandardCharsets.UTF_8).replace("+", "%20");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v2.nba.api-sports.io/teams?name=" + encodedName))
                .header("x-apisports-key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP status: " + response.statusCode());
        JSONObject json = new JSONObject(response.body());
        System.out.println(json.toString(2)); // pretty-printed

        if (json.getJSONArray("response").isEmpty()) {
            System.out.println("No team found for: " + teamName);
        } else {
            int teamId = json.getJSONArray("response").getJSONObject(0).getInt("id");
            System.out.println(teamName + " → id " + teamId);
        }
    }
}