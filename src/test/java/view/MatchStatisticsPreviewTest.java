package view;

import interface_adapter.match_statistics.MatchStatisticsController;
import interface_adapter.match_statistics.MatchStatisticsPresenter;
import interface_adapter.match_statistics.MatchStatisticsViewModel;
import org.junit.jupiter.api.Test;
import use_case.match_statistics.MatchStatisticsInteractor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MatchStatisticsPreviewTest {
    @Test
    void displaysTheClickedGamesScoreAndClearsUnavailableStats() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var model = new MatchStatisticsViewModel();
            var presenter = new MatchStatisticsPresenter(model);
            var controller = new MatchStatisticsController(
                    new MatchStatisticsInteractor(MatchStatisticsPreview.sampleData(), presenter));
            var view = new MatchStatisticsView(controller, model, Map.of("Lakers", 17), new Integer[]{2025});
            var button = components(view).filter(JButton.class::isInstance).map(JButton.class::cast)
                    .filter(b -> "View Statistics".equals(b.getText())).findFirst().orElseThrow();
            var table = components(view).filter(JTable.class::isInstance).map(JTable.class::cast)
                    .findFirst().orElseThrow();

            controller.findGames(17, 2025);
            assertEquals(2, table.getRowCount());
            assertFalse(button.isEnabled());
            table.setAutoCreateRowSorter(true);
            table.getRowSorter().toggleSortOrder(0);
            table.setRowSelectionInterval(0, 0);
            assertTrue(button.isEnabled());
            awaitBoxScore(model, button::doClick);
            assertEquals(List.of("Team", "Suns", "Lakers", "Final score", "98", "105",
                    "Q1", "20", "25", "Q2", "24", "28", "Q3", "25", "22", "Q4", "29", "30",
                    "Rebounds", "38", "42", "Assists", "20", "24", "Steals", "7", "9"), statsLabels(view));

            table.setRowSelectionInterval(1, 1);
            assertTrue(statsLabels(view).isEmpty());
            awaitBoxScore(model, button::doClick);
            assertEquals(List.of("Team", "Lakers", "Warriors", "Final score", "110", "113",
                    "Q1", "30", "27", "Q2", "22", "29", "Q3", "28", "26", "Q4", "30", "31",
                    "Rebounds", "45", "41", "Assists", "26", "30", "Steals", "6", "8"), statsLabels(view));

            awaitBoxScore(model, () -> controller.viewStatistics(999));
            assertTrue(statsLabels(view).isEmpty());
            assertTrue(components(view).filter(JLabel.class::isInstance).map(JLabel.class::cast)
                    .anyMatch(label -> "Statistics unavailable for this game.".equals(label.getText())));
            assertEquals(1, table.getSelectedRow());
        });
    }

    @Test
    void discardsLatePresentationAfterSelectionChanges() throws Exception {
        var delivery = new java.util.concurrent.CompletableFuture<java.util.concurrent.Executor>();
        var displayed = new java.util.concurrent.atomic.AtomicBoolean();
        var controller = new MatchStatisticsController(new use_case.match_statistics.MatchStatisticsInputBoundary() {
            @Override
            public void execute(use_case.match_statistics.MatchStatisticsInputData input) {
                throw new AssertionError("Unexpected game search");
            }

            @Override
            public void viewStatistics(int id, java.util.concurrent.Executor executor) {
                assertFalse(SwingUtilities.isEventDispatchThread());
                delivery.complete(executor);
            }
        });
        SwingUtilities.invokeAndWait(() -> controller.viewStatistics(101));
        var oldDelivery = delivery.get(5, java.util.concurrent.TimeUnit.SECONDS);
        SwingUtilities.invokeAndWait(controller::cancelStatistics);
        oldDelivery.execute(() -> displayed.set(true));
        SwingUtilities.invokeAndWait(() -> assertFalse(displayed.get()));
    }

    private static void awaitBoxScore(MatchStatisticsViewModel model, Runnable request) {
        var loop = java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
        var timeout = new javax.swing.Timer(5000, event -> loop.exit());
        timeout.setRepeats(false);
        model.addPropertyChangeListener(event -> {
            if ("boxScore".equals(event.getPropertyName())
                    && (model.getBoxScore().isPresent() || model.getMessage().startsWith("Statistics unavailable"))) {
                loop.exit();
            }
        });
        request.run();
        timeout.start();
        loop.enter();
        timeout.stop();
    }

    private static List<String> statsLabels(Container view) {
        return components(view).filter(Container.class::isInstance).map(Container.class::cast)
                .filter(c -> c.getLayout() instanceof java.awt.GridLayout)
                .flatMap(c -> Arrays.stream(c.getComponents()))
                .map(JLabel.class::cast).map(JLabel::getText).toList();
    }

    private static Stream<Component> components(Container parent) {
        return Arrays.stream(parent.getComponents()).flatMap(child -> child instanceof Container container
                ? Stream.concat(Stream.of(child), components(container)) : Stream.of(child));
    }
}
