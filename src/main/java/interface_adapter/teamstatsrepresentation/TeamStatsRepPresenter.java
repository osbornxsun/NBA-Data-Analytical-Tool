package interface_adapter.teamstatsrepresentation;

import java.util.ArrayList;
import java.util.List;

import use_case.teamstatsrepresentation.TeamStatsRepOutputBoundary;
import use_case.teamstatsrepresentation.TeamStatsRepOutputData;

/**
 * Presenter for the Team Stats Representation use case.
 *
 * <p>Turns the raw values in {@link TeamStatsRepOutputData} into display-ready text
 * (labels, "N/A" for missing stats, formatted percentages), stores them in the
 * view model's state, and notifies the view.</p>
 */
public class TeamStatsRepPresenter implements TeamStatsRepOutputBoundary {

    private static final String NOT_AVAILABLE = "N/A";

    private final TeamStatsRepViewModel viewModel;

    /**
     * Creates a presenter that updates the given view model.
     *
     * @param viewModel the view model for the team stats view
     */
    public TeamStatsRepPresenter(TeamStatsRepViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @Override
    public void prepareSuccessView(TeamStatsRepOutputData outputData) {
        final TeamStatsRepState state = viewModel.getState();
        state.setTitle(outputData.getTeamName() + " - " + outputData.getSeason() + " Season");
        state.setRows(buildRows(outputData));
        state.setErrorMessage(null);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final TeamStatsRepState state = viewModel.getState();
        state.setTitle("");
        state.setRows(new ArrayList<>());
        state.setErrorMessage(errorMessage);
        viewModel.notifyStateChanged();
    }

    /**
     * Builds the table rows as {label, value} pairs, in display order.
     */
    private List<String[]> buildRows(TeamStatsRepOutputData d) {
        List<String[]> rows = new ArrayList<>();

        addRow(rows, "Games Played", count(d.getGamesPlayed()));

        addRow(rows, "Points", count(d.getPoints()));
        addRow(rows, "Fast Break Points", count(d.getFastBreakPoints()));
        addRow(rows, "Points in Paint", count(d.getPointsInPaint()));
        addRow(rows, "Points off Turnovers", count(d.getPointsOffTurnovers()));
        addRow(rows, "Second Chance Points", count(d.getSecondChancePoints()));

        addRow(rows, "Field Goals Made", count(d.getFieldGoalsMade()));
        addRow(rows, "Field Goals Attempted", count(d.getFieldGoalsAttempted()));
        addRow(rows, "Field Goal %", percent(d.getFieldGoalPercent()));

        addRow(rows, "Free Throws Made", count(d.getFreeThrowsMade()));
        addRow(rows, "Free Throws Attempted", count(d.getFreeThrowsAttempted()));
        addRow(rows, "Free Throw %", percent(d.getFreeThrowPercent()));

        addRow(rows, "Three-Pointers Made", count(d.getThreePointersMade()));
        addRow(rows, "Three-Pointers Attempted", count(d.getThreePointersAttempted()));
        addRow(rows, "Three-Point %", percent(d.getThreePointPercent()));

        addRow(rows, "Offensive Rebounds", count(d.getOffensiveRebounds()));
        addRow(rows, "Defensive Rebounds", count(d.getDefensiveRebounds()));
        addRow(rows, "Total Rebounds", count(d.getTotalRebounds()));

        addRow(rows, "Assists", count(d.getAssists()));
        addRow(rows, "Personal Fouls", count(d.getPersonalFouls()));
        addRow(rows, "Steals", count(d.getSteals()));
        addRow(rows, "Turnovers", count(d.getTurnovers()));
        addRow(rows, "Blocks", count(d.getBlocks()));
        addRow(rows, "Plus/Minus", plusMinus(d.getPlusMinus()));

        addRow(rows, "Biggest Lead", count(d.getBiggestLead()));
        addRow(rows, "Longest Run", count(d.getLongestRun()));

        return rows;
    }

    private void addRow(List<String[]> rows, String label, String value) {
        rows.add(new String[] {label, value});
    }

    /** Formats a counting stat, or "N/A" if it was unavailable. */
    private String count(Integer value) {
        return value == null ? NOT_AVAILABLE : String.valueOf(value);
    }

    /** Formats a percentage stored as e.g. 47.5 to "47.5%", or "N/A". */
    private String percent(Double value) {
        return value == null ? NOT_AVAILABLE : String.format("%.1f%%", value);
    }

    /** Formats plus/minus with an explicit sign, e.g. "+120" or "-35", or "N/A". */
    private String plusMinus(Integer value) {
        if (value == null) {
            return NOT_AVAILABLE;
        }
        return value > 0 ? "+" + value : String.valueOf(value);
    }
}