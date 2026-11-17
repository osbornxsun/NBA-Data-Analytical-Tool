import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;



public class ApiTest {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("API_SPORTS_KEY");
        int teamId = 2;      // 2 = Celtics; change to any team id
        int season = 2023 ;    // season start year

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v2.nba.api-sports.io/teams/statistics?id="
                        + teamId + "&season=" + season))
                .header("x-apisports-key", apiKey)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP status: " + response.statusCode());
        System.out.println(new JSONObject(response.body()).toString(2)); // pretty-printed
    }
}
