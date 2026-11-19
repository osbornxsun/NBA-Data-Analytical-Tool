package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.teamstatsrepresentation.DataAccessException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * Sends requests to API-NBA (api-sports.io). Shared by all DAOs that use this API.
 */
public class ApiClient {

    private static final String BASE_URL = "https://v2.nba.api-sports.io";

    private final String apiKey;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Sends a GET request and returns the "response" array.
     *
     * @param path the endpoint and query, e.g. "/teams?name=Toronto%20Raptors"
     * @return the "response" array (empty if nothing matched)
     * @throws DataAccessException if the API could not be reached or reported an error
     */
    public JSONArray get(String path) throws DataAccessException {
        JSONObject json;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("x-apisports-key", apiKey)
                    .build();
            String body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            json = new JSONObject(body);
        } catch (Exception e) {
            throw new DataAccessException("Could not load data from the API.", e);   // (1)
        }

        if (json.optJSONObject("errors") != null) {
            throw new DataAccessException("The API returned an error: " + json.get("errors"), null);   // (2)
        }
        return json.getJSONArray("response");
    }
}