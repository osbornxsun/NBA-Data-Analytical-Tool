package view;

import interface_adapter.predict_match.PredictMatchController;
import interface_adapter.predict_match.PredictMatchState;
import interface_adapter.predict_match.PredictMatchViewModel;
import interface_adapter.predict_match.PredictMatchViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The View for the Predict Match Outcome use case.
 * <p>
 * The user enters two teams, a date and the factors to consider. The view shows
 * each team's win probability and the saved prediction history. It only displays
 * what is in the state and passes user input to the controller.
 */
public class PredictMatchView extends JPanel implements PredictMatchViewModelListener {

  private final String viewName = PredictMatchViewModel.VIEW_NAME;
  private final PredictMatchViewModel viewModel;
  private PredictMatchController controller;

  private final JTextField homeField = new JTextField(15);
  private final JTextField awayField = new JTextField(15);
  private final JSpinner dateSpinner = new JSpinner(
    new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
  private final Map<String, JCheckBox> factorBoxes = new LinkedHashMap<>();
  private final JButton predictButton = new JButton(PredictMatchViewModel.PREDICT_BUTTON_LABEL);

  private final JLabel errorLabel = new JLabel(" ");
  private final JLabel homeLabel = new JLabel(" ");
  private final JLabel awayLabel = new JLabel(" ");
  private final JProgressBar probabilityBar = new JProgressBar(0, 100);
  private final JLabel summaryLabel = new JLabel(" ");
  private final JLabel modelLabel = new JLabel(" ");
  private final DefaultListModel<String> historyModel = new DefaultListModel<>();

  /**
   * Creates the view and registers it as a listener on the view model.
   *
   * @param viewModel the view model for this view
   */
  public PredictMatchView(PredictMatchViewModel viewModel) {
    this.viewModel = viewModel;
    this.viewModel.addListener(this);

    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(buildInputPanel(), BorderLayout.NORTH);
    add(buildResultPanel(), BorderLayout.CENTER);
    add(buildHistoryPanel(), BorderLayout.SOUTH);

    predictButton.addActionListener(e -> onPredict());
  }

  private JPanel buildInputPanel() {
    JLabel title = new JLabel(PredictMatchViewModel.TITLE_LABEL);
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

    JPanel teams = new JPanel(new GridLayout(3, 2, 6, 6));
    teams.add(new JLabel("Home team:"));
    teams.add(homeField);
    teams.add(new JLabel("Away team:"));
    teams.add(awayField);
    teams.add(new JLabel("Match date:"));
    dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
    teams.add(dateSpinner);
    homeField.setToolTipText(PredictMatchViewModel.TEAM_NAME_HINT);
    awayField.setToolTipText(PredictMatchViewModel.TEAM_NAME_HINT);

    JPanel factors = new JPanel(new GridLayout(0, 2));
    factors.setBorder(BorderFactory.createTitledBorder("Factors the model should consider"));
    for (Map.Entry<String, String> option : viewModel.getFactorOptions().entrySet()) {
      JCheckBox box = new JCheckBox(option.getValue(), true);
      factorBoxes.put(option.getKey(), box);
      factors.add(box);
    }

    JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonRow.add(predictButton);
    errorLabel.setForeground(Color.RED);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    for (javax.swing.JComponent c : new javax.swing.JComponent[] {
      title, teams, factors, buttonRow, errorLabel}) {
      c.setAlignmentX(LEFT_ALIGNMENT);
      panel.add(c);
    }
    return panel;
  }

  private JPanel buildResultPanel() {
    JPanel panel = new JPanel(new GridLayout(4, 1, 4, 4));
    panel.setBorder(BorderFactory.createTitledBorder("Prediction"));
    JPanel names = new JPanel(new BorderLayout());
    names.add(homeLabel, BorderLayout.WEST);
    names.add(awayLabel, BorderLayout.EAST);
    probabilityBar.setStringPainted(true);
    probabilityBar.setString("");
    probabilityBar.setValue(50);
    panel.add(names);
    panel.add(probabilityBar);
    panel.add(summaryLabel);
    panel.add(modelLabel);
    return panel;
  }

  private JScrollPane buildHistoryPanel() {
    JScrollPane scroll = new JScrollPane(new JList<>(historyModel));
    scroll.setBorder(BorderFactory.createTitledBorder("Saved predictions (most recent first)"));
    scroll.setPreferredSize(new Dimension(620, 140));
    return scroll;
  }

  /** Reads the inputs and passes them to the controller on a background thread. */
  private void onPredict() {
    String home = homeField.getText();
    String away = awayField.getText();
    LocalDate date = ((Date) dateSpinner.getValue()).toInstant()
      .atZone(ZoneId.systemDefault()).toLocalDate();
    List<String> selected = new ArrayList<>();
    factorBoxes.forEach((key, box) -> {
      if (box.isSelected()) {
        selected.add(key);
      }
    });

    // Two teams = up to four API calls; keep them off the Event Dispatch Thread.
    predictButton.setEnabled(false);
    errorLabel.setText("Loading...");
    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        controller.execute(home, away, date, selected);
        return null;
      }

      @Override
      protected void done() {
        predictButton.setEnabled(true);
      }
    }.execute();
  }

  /** Redraws the view from the current state (always on the EDT). */
  @Override
  public void stateChanged() {
    PredictMatchState state = viewModel.getState();
    SwingUtilities.invokeLater(() -> render(state));
  }

  private void render(PredictMatchState state) {
    String error = state.getError();
    errorLabel.setText(error == null ? " " : error);

    if (!state.getHomeTeam().isEmpty()) {
      homeLabel.setText(state.getHomeTeam() + "  " + state.getHomeProbabilityText());
      awayLabel.setText(state.getAwayProbabilityText() + "  " + state.getAwayTeam());
      probabilityBar.setValue(state.getHomeProbabilityPercent());
      probabilityBar.setString(state.getHomeProbabilityText() + " home win");
      summaryLabel.setText(state.getSummary());
      modelLabel.setText(state.getModelDescription());
    }

    historyModel.clear();
    state.getHistory().forEach(historyModel::addElement);
  }

  public void setController(PredictMatchController controller) {
    this.controller = controller;
  }

  public String getViewName() {
    return viewName;
  }
}
