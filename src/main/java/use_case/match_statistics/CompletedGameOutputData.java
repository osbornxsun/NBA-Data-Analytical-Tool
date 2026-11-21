package use_case.match_statistics;

import java.time.LocalDate;

/** The information needed to display and select one completed game. */
public record CompletedGameOutputData(
        int gameId,
        LocalDate date,
        String homeTeamName,
        String awayTeamName) {
}