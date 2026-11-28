package data_access;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ApiMatchStatisticsDataAccessObjectTest {
    private JSONArray fixture(String name) throws IOException {
        try (var input = getClass().getResourceAsStream("/match_statistics/" + name + ".json")) {
            assertNotNull(input);
            return new JSONArray(new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Test
    void joinsAwayFirstByIdAndIncludesOvertime() throws IOException {
        var score = ApiMatchStatisticsDataAccessObject.parseBoxScore(50, fixture("game"),
                fixture("away-first")).orElseThrow();
        assertEquals("Suns", score.away().name());
        assertEquals(105, score.away().finalScore());
        assertEquals(44, score.away().rebounds());
        assertEquals(0, score.away().steals());
        assertEquals(17, score.home().assists());
        assertEquals(8, score.home().periodScores().get(4));
    }

    @Test
    void preservesMissingFieldsPeriodsAndEntireTeamTotals() throws IOException {
        var games = fixture("game");
        var home = games.getJSONObject(0).getJSONObject("scores").getJSONObject("home");
        home.remove("points");
        home.getJSONArray("linescore").remove(4);
        var score = ApiMatchStatisticsDataAccessObject.parseBoxScore(50, games,
                fixture("missing")).orElseThrow();
        assertNull(score.home().finalScore());
        assertNull(score.home().periodScores().get(4));
        assertNull(score.away().rebounds());
        assertNull(score.home().rebounds());
        assertEquals(0, score.home().assists());
        assertNull(score.home().steals());
    }
}
