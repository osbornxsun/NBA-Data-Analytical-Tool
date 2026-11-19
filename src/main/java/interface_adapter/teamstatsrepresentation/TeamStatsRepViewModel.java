package interface_adapter.teamstatsrepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Team Stats View.
 */
public class TeamStatsRepViewModel {

    public static final String TITLE_LABEL = "Team Season Stats";
    public static final String[] TABLE_COLUMNS = {"Stat", "Value"};
    public static final String EXIT_BUTTON_LABEL = "Exit";

    public static final String VIEW_NAME = "team stats";

    private final List<TeamStatsRepViewModelListener> listeners = new ArrayList<>();

    private TeamStatsRepState state = new TeamStatsRepState();

    public TeamStatsRepState getState() {
        return this.state;
    }

    public void setState(TeamStatsRepState state) {
        this.state = state;
    }

    public void addListener(TeamStatsRepViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (TeamStatsRepViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}