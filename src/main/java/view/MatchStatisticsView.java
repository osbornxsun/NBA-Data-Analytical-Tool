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
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Map;

public final class MatchStatisticsView extends JPanel {

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Date", "Away Team", "Home Team"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JLabel messageLabel = new JLabel(" ");

    public MatchStatisticsView(MatchStatisticsController controller,
                               MatchStatisticsViewModel viewModel,
                               Map<String, Integer> teamIds,
                               Integer[] seasons) {
        super(new BorderLayout(8, 8));

        JComboBox<String> teamBox = new JComboBox<>(
                teamIds.keySet().stream().sorted().toArray(String[]::new));
        JComboBox<Integer> seasonBox = new JComboBox<>(seasons);
        JButton findButton = new JButton("Find Games");
        findButton.setEnabled(teamBox.getItemCount() > 0 && seasonBox.getItemCount() > 0);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEADING));
        controls.add(new JLabel("Team"));
        controls.add(teamBox);
        controls.add(new JLabel("Season"));
        controls.add(seasonBox);
        controls.add(findButton);

        JTable gamesTable = new JTable(tableModel);
        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gamesTable.setFillsViewportHeight(true);

        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(gamesTable), BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        findButton.addActionListener(event -> {
            String team = (String) teamBox.getSelectedItem();
            Integer season = (Integer) seasonBox.getSelectedItem();
            if (team != null && season != null) {
                controller.findGames(teamIds.get(team), season);
            }
        });

        viewModel.addPropertyChangeListener(event -> refresh(viewModel));
        refresh(viewModel);
    }

    private void refresh(MatchStatisticsViewModel viewModel) {
        tableModel.setRowCount(0);
        for (CompletedGameOutputData game : viewModel.getGames()) {
            tableModel.addRow(new Object[]{
                    game.date(), game.awayTeamName(), game.homeTeamName()
            });
        }
        String message = viewModel.getMessage();
        messageLabel.setText(message.isEmpty() ? " " : message);
    }
}