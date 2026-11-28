package view;

import entity.Game;
import interface_adapter.match_statistics.MatchStatisticsController;
import interface_adapter.match_statistics.MatchStatisticsPresenter;
import interface_adapter.match_statistics.MatchStatisticsViewModel;
import use_case.match_statistics.MatchStatisticsDataAccessInterface;
import use_case.match_statistics.MatchStatisticsInteractor;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import entity.BoxScore;
import java.util.Map;

public final class MatchStatisticsPreview {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Map<String, Integer> teamIds = Map.of("Lakers", 17);

            MatchStatisticsDataAccessInterface sampleData = sampleData();

            var viewModel = new MatchStatisticsViewModel();
            var presenter = new MatchStatisticsPresenter(viewModel);
            var interactor = new MatchStatisticsInteractor(sampleData, presenter);
            var controller = new MatchStatisticsController(interactor);
            var view = new MatchStatisticsView(
                    controller, viewModel, teamIds, new Integer[]{2025, 2024});

            JFrame frame = new JFrame("Match Statistics");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    static MatchStatisticsDataAccessInterface sampleData() {
        Map<Integer, BoxScore> scores = Map.of(
                101, new BoxScore(
                        new BoxScore.Team("Suns", 98, List.of(20, 24, 25, 29), 38, 20, 7),
                        new BoxScore.Team("Lakers", 105, List.of(25, 28, 22, 30), 42, 24, 9)),
                102, new BoxScore(
                        new BoxScore.Team("Lakers", 110, List.of(30, 22, 28, 30), 45, 26, 6),
                        new BoxScore.Team("Warriors", 113, List.of(27, 29, 26, 31), 41, 30, 8)));
        return new MatchStatisticsDataAccessInterface() {
            @Override
            public List<Game> getGamesForTeam(int teamId, int season) {
                if (teamId != 17 || season != 2025) {
                    return List.of();
                }
                return List.of(
                        new Game(101, LocalDate.of(2025, 10, 3),
                                "Lakers", "Suns", "Finished"),
                        new Game(102, LocalDate.of(2025, 10, 5),
                                "Warriors", "Lakers", "Finished"),
                        new Game(103, LocalDate.of(2025, 10, 7),
                                "Lakers", "Bulls", "Scheduled")
                );
            }

            @Override
            public Optional<BoxScore> getBoxScore(int gameId) {
                return Optional.ofNullable(scores.get(gameId));
            }
        };
    }
}
