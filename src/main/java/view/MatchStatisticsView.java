package view;

import interface_adapter.match_statistics.MatchStatisticsController;
import interface_adapter.match_statistics.MatchStatisticsViewModel;
import use_case.match_statistics.CompletedGameOutputData;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** Lets the user find completed games for a selected team and season. */
public final class MatchStatisticsView extends JPanel {
    private static final int GAME_ID_COLUMN = 3;

    private final MatchStatisticsController controller;
    private final MatchStatisticsViewModel viewModel;
    private final Map<String, Integer> teamIds;

    private final JComboBox<String> teamBox;
    private final JComboBox<Integer> seasonBox;
    private final JButton findButton = new JButton("Find Games");
    private final JLabel messageLabel = new JLabel(" ");
    private final JPanel statsPanel = new JPanel(new GridLayout(0, 3, 12, 2));

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Date", "Away Team", "Home Team", "Game ID"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public MatchStatisticsView(MatchStatisticsController controller,
                               MatchStatisticsViewModel viewModel,
                               Map<String, Integer> teamIds,
                               Integer[] seasons) {
        super(new BorderLayout(8, 8));
        this.controller = controller;
        this.viewModel = viewModel;
        this.teamIds = Map.copyOf(teamIds);

        String[] teamNames = this.teamIds.keySet().stream()
                .sorted()
                .toArray(String[]::new);
        teamBox = new JComboBox<>(teamNames);
        seasonBox = new JComboBox<>(seasons);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEADING));
        controls.add(new JLabel("Team"));
        controls.add(teamBox);
        controls.add(new JLabel("Season"));
        controls.add(seasonBox);
        controls.add(findButton);

        JTable gamesTable = new JTable(tableModel);
        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gamesTable.setFillsViewportHeight(true);
        gamesTable.removeColumn(gamesTable.getColumnModel().getColumn(GAME_ID_COLUMN));

        JButton statsButton = new JButton("View Statistics");
        statsButton.setEnabled(false);
        controls.add(statsButton);
        gamesTable.getSelectionModel().addListSelectionListener(event -> {
            controller.cancelStatistics();
            viewModel.setBoxScore(java.util.Optional.empty(), "");
            statsButton.setEnabled(gamesTable.getSelectedRow() >= 0);
        });
        statsButton.addActionListener(event -> {
            int viewRow = gamesTable.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = gamesTable.convertRowIndexToModel(viewRow);
                int gameId = (Integer) tableModel.getValueAt(modelRow, GAME_ID_COLUMN);
                viewModel.setBoxScore(java.util.Optional.empty(), "Loading statistics...");
                controller.viewStatistics(gameId);
            }
        });

        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(gamesTable), BorderLayout.CENTER);
        JPanel details = new JPanel(new BorderLayout());
        details.add(statsPanel, BorderLayout.CENTER);
        details.add(messageLabel, BorderLayout.SOUTH);
        add(details, BorderLayout.SOUTH);

        findButton.addActionListener(event -> findGames());
        findButton.setEnabled(teamBox.getItemCount() > 0 && seasonBox.getItemCount() > 0);

        // The presenter can update the ViewModel from the lookup worker.
        viewModel.addPropertyChangeListener(event -> {
            Runnable update = "boxScore".equals(event.getPropertyName())
                    ? this::refreshStatistics : this::refresh;
            if (SwingUtilities.isEventDispatchThread()) {
                update.run();
            } else {
                SwingUtilities.invokeLater(update);
            }
        });
        refresh();
    }

    private void findGames() {
        String teamName = (String) teamBox.getSelectedItem();
        Integer season = (Integer) seasonBox.getSelectedItem();
        if (teamName == null || season == null) {
            return;
        }

        int teamId = teamIds.get(teamName);
        setSearchControlsEnabled(false);
        tableModel.setRowCount(0);
        statsPanel.removeAll();
        statsPanel.revalidate();
        statsPanel.repaint();
        messageLabel.setText("Loading games...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                controller.findGames(teamId, season);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    messageLabel.setText("Game lookup was interrupted.");
                } catch (ExecutionException exception) {
                    messageLabel.setText("Could not load games. Please try again.");
                } finally {
                    setSearchControlsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void setSearchControlsEnabled(boolean enabled) {
        teamBox.setEnabled(enabled);
        seasonBox.setEnabled(enabled);
        findButton.setEnabled(enabled);
    }

    private void refresh() {
        tableModel.setRowCount(0);
        for (CompletedGameOutputData game : viewModel.getGames()) {
            tableModel.addRow(new Object[]{
                    game.date(),
                    game.awayTeamName(),
                    game.homeTeamName(),
                    game.gameId()
            });
        }

        refreshStatistics();
    }

    private void refreshStatistics() {
        statsPanel.removeAll();
        viewModel.getBoxScore().ifPresent(score -> {
            var away = score.away();
            var home = score.home();
            addStatRow("Team", away.name(), home.name());
            addStatRow("Final score", away.finalScore(), home.finalScore());
            for (int i = 0; i < away.periodScores().size(); i++) {
                String period = i < 4 ? "Q" + (i + 1) : "OT" + (i - 3);
                addStatRow(period, away.periodScores().get(i), home.periodScores().get(i));
            }
            addStatRow("Rebounds", away.rebounds(), home.rebounds());
            addStatRow("Assists", away.assists(), home.assists());
            addStatRow("Steals", away.steals(), home.steals());
        });
        statsPanel.revalidate();
        statsPanel.repaint();
        String message = viewModel.getMessage();
        messageLabel.setText(message.isEmpty() ? " " : message);
    }

    private void addStatRow(String label, Object away, Object home) {
        statsPanel.add(new JLabel(label));
        statsPanel.add(new JLabel(away == null ? "Unavailable" : away.toString()));
        statsPanel.add(new JLabel(home == null ? "Unavailable" : home.toString()));
    }
}
