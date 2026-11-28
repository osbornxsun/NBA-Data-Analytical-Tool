package use_case.match_statistics;

import entity.Game;
import entity.BoxScore;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

/** Finds completed games for a team and season, ordered newest first. */
public final class MatchStatisticsInteractor implements MatchStatisticsInputBoundary {

    private final MatchStatisticsDataAccessInterface dataAccess;
    private final MatchStatisticsOutputBoundary presenter;

    public MatchStatisticsInteractor(MatchStatisticsDataAccessInterface dataAccess,
                                     MatchStatisticsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(MatchStatisticsInputData inputData) {
        List<Game> games;
        try {
            games = dataAccess.getGamesForTeam(inputData.teamId(), inputData.season());
        } catch (IOException exception) {
            presenter.prepareFailView("Could not load games. Please try again.");
            return;
        }

        List<CompletedGameOutputData> completedGames = games.stream()
                .filter(Game::isCompleted)
                .sorted(Comparator.comparing(Game::date).reversed())
                .map(game -> new CompletedGameOutputData(
                        game.id(), game.date(), game.homeTeamName(), game.awayTeamName()))
                .toList();

        presenter.prepareSuccessView(completedGames);
    }

    @Override
    public void viewStatistics(int gameId, Executor delivery) {
        Optional<BoxScore> score;
        try {
            score = dataAccess.getBoxScore(gameId);
        } catch (IOException exception) {
            score = Optional.empty();
        }
        Optional<BoxScore> result = score;
        delivery.execute(() -> presenter.presentBoxScore(result));
    }
}
