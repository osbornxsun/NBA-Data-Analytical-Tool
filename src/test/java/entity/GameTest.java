package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    private static final LocalDate GAME_DATE = LocalDate.of(2025, 10, 4);

    @Test
    void finishedGameIsCompleted() {
        Game game = new Game(1, GAME_DATE, "Lakers", "Warriors", "Finished");

        assertTrue(game.isCompleted());
    }

    @Test
    void scheduledGameIsNotCompleted() {
        Game game = new Game(2, GAME_DATE, "Lakers", "Warriors", "Scheduled");

        assertFalse(game.isCompleted());
    }

    @Test
    void missingStatusIsNotCompleted() {
        Game game = new Game(3, GAME_DATE, "Lakers", "Warriors", null);

        assertFalse(game.isCompleted());
    }
}
