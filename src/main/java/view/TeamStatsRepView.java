package view;

import interface_adapter.teamstatsrepresentation.TeamStatsRepController;
import interface_adapter.teamstatsrepresentation.TeamStatsRepState;
import interface_adapter.teamstatsrepresentation.TeamStatsRepViewModel;
import interface_adapter.teamstatsrepresentation.TeamStatsRepViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

/**
 * The View for the Team Stats Representation use case.
 *
 * <p>The user enters a team name and season year; the view shows that team's
 * season stats in a two-column table. It only displays what is in the state and
 * passes user input to the controller; it contains no business logic.</p>
 */
public class TeamStatsRepView extends JPanel implements TeamStatsRepViewModelListener {

    private final String viewName = TeamStatsRepViewModel.VIEW_NAME;
    private final TeamStatsRepViewModel viewModel;
    private TeamStatsRepController controller;

    private final JTextField teamNameField = new JTextField(15);
    private final JTextField seasonField = new JTextField(6);
    private final JButton searchButton = new JButton("Show Stats");

    private final JLabel titleLabel = new JLabel(TeamStatsRepViewModel.TITLE_LABEL);
    private final JLabel errorLabel = new JLabel(" ");

    private final DefaultTableModel tableModel =
            new DefaultTableModel(TeamStatsRepViewModel.TABLE_COLUMNS, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
    private final JTable statsTable = new JTable(tableModel);

    private final JButton exitButton = new JButton(TeamStatsRepViewModel.EXIT_BUTTON_LABEL);

    /**
     * Creates the view and registers it as a listener on the view model.
     *
     * @param viewModel the view model for this view
     */
    public TeamStatsRepView(TeamStatsRepViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(new JScrollPane(statsTable), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        searchButton.addActionListener(e -> onSearch());
        teamNameField.addActionListener(e -> onSearch());
        seasonField.addActionListener(e -> onSearch());
        exitButton.addActionListener(e -> onExit());
    }

    private JPanel buildTopPanel() {
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Team name:"));
        searchRow.add(teamNameField);
        searchRow.add(new JLabel("Season:"));
        searchRow.add(seasonField);
        searchRow.add(searchButton);

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        errorLabel.setForeground(Color.RED);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        searchRow.setAlignmentX(LEFT_ALIGNMENT);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);
        top.add(searchRow);
        top.add(titleLabel);
        top.add(errorLabel);
        return top;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(exitButton);
        return bottom;
    }

    /** Reads the inputs and passes them to the controller. */
    private void onSearch() {
        String teamName = teamNameField.getText().trim();
        String seasonText = seasonField.getText().trim();

        if (teamName.isEmpty() || seasonText.isEmpty()) {
            errorLabel.setText("Please enter a team name and a season year.");
            return;
        }

        int season;
        try {
            season = Integer.parseInt(seasonText);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Season must be a year, e.g. 2023.");
            return;
        }

        controller.execute(teamName, season);
    }

    /** Closes the window this view is in. */
    private void onExit() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    /**
     * Redraws the view from the current state.
     */
    @Override
    public void stateChanged() {
        TeamStatsRepState state = viewModel.getState();

        String error = state.getErrorMessage();
        errorLabel.setText(error == null ? " " : error);

        String title = state.getTitle();
        titleLabel.setText(title == null || title.isEmpty()
                ? TeamStatsRepViewModel.TITLE_LABEL : title);

        tableModel.setRowCount(0);
        for (String[] row : state.getRows()) {
            tableModel.addRow(row);
        }
    }

    public void setController(TeamStatsRepController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return viewName;
    }
}