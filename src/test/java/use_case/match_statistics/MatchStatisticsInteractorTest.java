package use_case.match_statistics;

import entity.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MatchStatisticsInteractorTest {

    private static final int TEAM_ID = 17;
    private static final int SEASON = 2025;
    private static final LocalDate OCTOBER_3 = LocalDate.of(2025, 10, 3);

    private final RecordingPresenter presenter = new RecordingPresenter();

    @Test
    void presentsOnlyFinishedGamesWithNewestFirst() {
        Game older = new Game(101, OCTOBER_3, "Lakers", "Suns", "Finished");
        Game newer = new Game(102, OCTOBER_3.plusDays(2), "Warriors", "Lakers", "Finished");
        Game scheduled = new Game(103, OCTOBER_3.plusDays(4), "Lakers", "Bulls", "Scheduled");

        MatchStatisticsDataAccessInterface dataAccess = (teamId, season) -> {
            assertEquals(TEAM_ID, teamId);
            assertEquals(SEASON, season);
            return List.of(older, scheduled, newer);
        };

        MatchStatisticsInteractor interactor =
                new MatchStatisticsInteractor(dataAccess, presenter);
        interactor.execute(new MatchStatisticsInputData(TEAM_ID, SEASON));

        assertEquals(List.of(
                new CompletedGameOutputData(102, OCTOBER_3.plusDays(2), "Warriors", "Lakers"),
                new CompletedGameOutputData(101, OCTOBER_3, "Lakers", "Suns")
        ), presenter.completedGames);
        assertNull(presenter.errorMessage);
    }

    @Test
    void presentsEmptyListWhenNoGamesAreFinished() {
        Game scheduled = new Game(103, OCTOBER_3, "Lakers", "Bulls", "Scheduled");
        MatchStatisticsDataAccessInterface dataAccess =
                (teamId, season) -> List.of(scheduled);

        MatchStatisticsInteractor interactor =
                new MatchStatisticsInteractor(dataAccess, presenter);
        interactor.execute(new MatchStatisticsInputData(TEAM_ID, SEASON));

        assertEquals(List.of(), presenter.completedGames);
        assertNull(presenter.errorMessage);
    }

    @Test
    void presentsErrorWhenGameLookupFails() {
        MatchStatisticsDataAccessInterface dataAccess = (teamId, season) -> {
            throw new IOException("Connection failed");
        };

        MatchStatisticsInteractor interactor =
                new MatchStatisticsInteractor(dataAccess, presenter);
        interactor.execute(new MatchStatisticsInputData(TEAM_ID, SEASON));

        assertEquals("Could not load games. Please try again.", presenter.errorMessage);
        assertNull(presenter.completedGames);
    }

    /** Records which output the interactor sends during a test. */
    private static final class RecordingPresenter implements MatchStatisticsOutputBoundary {
        private List<CompletedGameOutputData> completedGames;
        private String errorMessage;

        @Override
        public void prepareSuccessView(List<CompletedGameOutputData> games) {
            completedGames = games;
        }

        @Override
        public void prepareFailView(String message) {
            errorMessage = message;
        }
    }
}
