package data_access;

import org.json.JSONArray;
import use_case.DataAccessException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

/**
 * Decorator: wraps any ApiGateway and saves each response to a JSON file.
 * While a file is fresh, the request is answered from disk without calling the API.
 * <ul>
 *   <li>Protects the free plan's 100 requests/day.</li>
 *   <li>Makes repeat searches instant, for every use case that shares the gateway.</li>
 *   <li>Lets the demo run without network once the cache is setup (data persistence).</li>
 * </ul>
 * Failed requests throw before anything is written, so errors are never cached.
 */
public class CachingApiGateway implements ApiGateway {

  private final ApiGateway wrapped;
  private final Path cacheDirectory;
  private final Duration timetoLive;

  public CachingApiGateway(ApiGateway wrapped, Path cacheDirectory, Duration timetoLive) {
    this.wrapped = wrapped;
    this.cacheDirectory = cacheDirectory;
    this.timetoLive = timetoLive;
  }

  @Override
  public JSONArray get(String path) throws DataAccessException {
    Path file = cacheDirectory.resolve(fileNameFor(path));
    JSONArray cached = readIfFreshAndJuicy(file);
    if (cached != null) {
      return cached;
    }
    JSONArray response = wrapped.get(path);
    write(file, response);
    return response;
  }

  private JSONArray readIfFreshAndJuicy(Path file) {
    try {
      if (!Files.exists(file)) {
        return null;
      }
      Instant modified = Files.getLastModifiedTime(file).toInstant();
      if (modified.plus(timetoLive).isBefore(Instant.now())) {
        return null;
      }
      return new JSONArray(Files.readString(file, StandardCharsets.UTF_8));
    } catch (IOException | RuntimeException e) {
      return null; // unreadable cache entry: fall through to the real API
    }
  }

  private void write(Path file, JSONArray response) {
    try {
      Files.createDirectories(cacheDirectory);
      Files.writeString(file, response.toString(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      // Caching is an optimisation, thus failing to write must not break the request.
    }
  }

  /** "/teams/statistics?id=17&season=2025" -> "teams_statistics_id_17_season_2025.json" */
  static String fileNameFor(String path) {
    return path.replaceAll("[^A-Za-z0-9]+", "_").replaceAll("^_|_$", "") + ".json";
  }
}
