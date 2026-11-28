package view;

import data_access.ApiMatchStatisticsDataAccessObject;
import interface_adapter.match_statistics.MatchStatisticsController;
import interface_adapter.match_statistics.MatchStatisticsPresenter;
import interface_adapter.match_statistics.MatchStatisticsViewModel;
import use_case.match_statistics.MatchStatisticsDataAccessInterface;
import use_case.match_statistics.MatchStatisticsInteractor;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.Map;

public final class MatchStatisticsLivePreview {
    // API-NBA v2 /teams?league=standard, restricted to NBA franchises.
    private static final Map<String, Integer> TEAM_IDS = Map.ofEntries(
            Map.entry("Atlanta Hawks", 1),
            Map.entry("Boston Celtics", 2),
            Map.entry("Brooklyn Nets", 4),
            Map.entry("Charlotte Hornets", 5),
            Map.entry("Chicago Bulls", 6),
            Map.entry("Cleveland Cavaliers", 7),
            Map.entry("Dallas Mavericks", 8),
            Map.entry("Denver Nuggets", 9),
            Map.entry("Detroit Pistons", 10),
            Map.entry("Golden State Warriors", 11),
            Map.entry("Houston Rockets", 14),
            Map.entry("Indiana Pacers", 15),
            Map.entry("LA Clippers", 16),
            Map.entry("Los Angeles Lakers", 17),
            Map.entry("Memphis Grizzlies", 19),
            Map.entry("Miami Heat", 20),
            Map.entry("Milwaukee Bucks", 21),
            Map.entry("Minnesota Timberwolves", 22),
            Map.entry("New Orleans Pelicans", 23),
            Map.entry("New York Knicks", 24),
            Map.entry("Oklahoma City Thunder", 25),
            Map.entry("Orlando Magic", 26),
            Map.entry("Philadelphia 76ers", 27),
            Map.entry("Phoenix Suns", 28),
            Map.entry("Portland Trail Blazers", 29),
            Map.entry("Sacramento Kings", 30),
            Map.entry("San Antonio Spurs", 31),
            Map.entry("Toronto Raptors", 38),
            Map.entry("Utah Jazz", 40),
            Map.entry("Washington Wizards", 41));

    private MatchStatisticsLivePreview() {
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("API_SPORTS_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println(
                    "Set API_SPORTS_KEY in the IntelliJ run configuration, then run again.");
            return;
        }

        MatchStatisticsDataAccessInterface liveData =
                new ApiMatchStatisticsDataAccessObject(apiKey);

        SwingUtilities.invokeLater(() -> {
            MatchStatisticsViewModel viewModel = new MatchStatisticsViewModel();
            MatchStatisticsPresenter presenter = new MatchStatisticsPresenter(viewModel);
            MatchStatisticsInteractor interactor =
                    new MatchStatisticsInteractor(liveData, presenter);
            MatchStatisticsController controller = new MatchStatisticsController(interactor);

            MatchStatisticsView view = new MatchStatisticsView(
                    controller,
                    viewModel,
                    TEAM_IDS,
                    new Integer[]{2024, 2023, 2022}
            );

            JFrame frame = new JFrame("Match Statistics – Live API");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.setSize(760, 450);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
