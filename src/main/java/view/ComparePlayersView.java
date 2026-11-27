package view;

import interface_adapter.compare_players.ComparePlayersController;
import interface_adapter.compare_players.ComparePlayersState;
import interface_adapter.compare_players.ComparePlayersViewModel;
import interface_adapter.compare_players.ComparePlayersViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The View for the Player Card Comparison use case.
 *
 * <p>The user enters two player names and a season year; the view shows one card
 * per player with their season averages, and highlights the better value of each
 * stat. It only displays what is in the state and passes user input to the
 * controller; it contains no business logic.</p>
 */
public class ComparePlayersView extends JPanel implements ComparePlayersViewModelListener {

    private static final Color HIGHLIGHT_BACKGROUND = new Color(0xC8E6C9);
    private static final Color HIGHLIGHT_FOREGROUND = new Color(0x1B5E20);
    private static final Color CARD_BORDER = new Color(0xB0BEC5);

    private final String viewName = ComparePlayersViewModel.VIEW_NAME;
    private final ComparePlayersViewModel viewModel;
    private ComparePlayersController controller;

    private final JTextField firstPlayerField = new JTextField(14);
    private final JTextField secondPlayerField = new JTextField(14);
    private final JTextField seasonField = new JTextField(5);
    private final JButton compareButton = new JButton(ComparePlayersViewModel.COMPARE_BUTTON_LABEL);
    private final JButton exitButton = new JButton(ComparePlayersViewModel.EXIT_BUTTON_LABEL);

    private final JLabel titleLabel = new JLabel(ComparePlayersViewModel.TITLE_LABEL);
    private final JLabel errorLabel = new JLabel(" ");
    private final JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 12, 0));

    /**
     * Creates the view and registers it as a listener on the view model.
     *
     * @param viewModel the view model for this view
     */
    public ComparePlayersView(ComparePlayersViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        compareButton.addActionListener(e -> onCompare());
        firstPlayerField.addActionListener(e -> onCompare());
        secondPlayerField.addActionListener(e -> onCompare());
        seasonField.addActionListener(e -> onCompare());
        exitButton.addActionListener(e -> onExit());
    }

    private JPanel buildTopPanel() {
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel(ComparePlayersViewModel.FIRST_PLAYER_LABEL));
        searchRow.add(firstPlayerField);
        searchRow.add(new JLabel(ComparePlayersViewModel.SECOND_PLAYER_LABEL));
        searchRow.add(secondPlayerField);
        searchRow.add(new JLabel(ComparePlayersViewModel.SEASON_LABEL));
        searchRow.add(seasonField);
        searchRow.add(compareButton);

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
        JLabel legend = new JLabel("  Better value  ");
        legend.setOpaque(true);
        legend.setBackground(HIGHLIGHT_BACKGROUND);
        legend.setForeground(HIGHLIGHT_FOREGROUND);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.add(legend);
        left.add(new JLabel("Season averages per game played; lower turnovers is better."));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(exitButton);
        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);
        return bottom;
    }

    /** Reads the inputs and runs the comparison off the Swing event thread. */
    private void onCompare() {
        String first = firstPlayerField.getText().trim();
        String second = secondPlayerField.getText().trim();
        String seasonText = seasonField.getText().trim();

        if (first.isEmpty() || second.isEmpty() || seasonText.isEmpty()) {
            errorLabel.setText("Please enter two player names and a season year.");
            return;
        }

        int season;
        try {
            season = Integer.parseInt(seasonText);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Season must be a year, e.g. 2024 for the 2024-25 season.");
            return;
        }

        compareButton.setEnabled(false);
        errorLabel.setText("Loading...");
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                controller.execute(first, second, season);
                return null;
            }

            @Override
            protected void done() {
                compareButton.setEnabled(true);
            }
        }.execute();
    }

    /** Closes the window this view is in. */
    private void onExit() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    /**
     * Redraws the view from the current state (on the Swing event thread).
     */
    @Override
    public void stateChanged() {
        if (SwingUtilities.isEventDispatchThread()) {
            render();
        } else {
            SwingUtilities.invokeLater(this::render);
        }
    }

    private void render() {
        ComparePlayersState state = viewModel.getState();

        String error = state.getErrorMessage();
        errorLabel.setText(error == null ? " " : error);

        String title = state.getTitle();
        titleLabel.setText(title == null || title.isEmpty() ? ComparePlayersViewModel.TITLE_LABEL : title);

        cardsPanel.removeAll();
        if (state.getFirstCard() != null && state.getSecondCard() != null) {
            cardsPanel.add(buildCard(state, state.getFirstCard(),
                    ComparePlayersState.StatRow::getFirstValue, ComparePlayersState.StatRow::isFirstHighlighted));
            cardsPanel.add(buildCard(state, state.getSecondCard(),
                    ComparePlayersState.StatRow::getSecondValue, ComparePlayersState.StatRow::isSecondHighlighted));
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel buildCard(ComparePlayersState state, ComparePlayersState.CardHeader header,
                             Function<ComparePlayersState.StatRow, String> value,
                             Predicate<ComparePlayersState.StatRow> highlighted) {
        JLabel name = new JLabel(header.getName(), SwingConstants.CENTER);
        name.setFont(name.getFont().deriveFont(Font.BOLD, 18f));
        JLabel subtitle = new JLabel(header.getSubtitle(), SwingConstants.CENTER);
        subtitle.setForeground(Color.DARK_GRAY);

        JPanel head = new JPanel(new GridLayout(2, 1));
        head.setOpaque(false);
        head.add(name);
        head.add(subtitle);

        JPanel stats = new JPanel(new GridLayout(0, 2, 0, 2));
        stats.setOpaque(false);
        for (ComparePlayersState.StatRow row : state.getRows()) {
            JLabel label = new JLabel(" " + row.getLabel());
            JLabel cell = new JLabel(value.apply(row) + " ", SwingConstants.RIGHT);
            if (highlighted.test(row)) {
                cell.setOpaque(true);
                cell.setBackground(HIGHLIGHT_BACKGROUND);
                cell.setForeground(HIGHLIGHT_FOREGROUND);
                cell.setFont(cell.getFont().deriveFont(Font.BOLD));
            }
            stats.add(label);
            stats.add(cell);
        }

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        card.add(head, BorderLayout.NORTH);
        card.add(stats, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(card, BorderLayout.NORTH);   // keep the card at its natural height
        return wrapper;
    }

    public void setController(ComparePlayersController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return viewName;
    }
}
