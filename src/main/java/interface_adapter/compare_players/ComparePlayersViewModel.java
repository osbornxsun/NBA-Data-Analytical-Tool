package interface_adapter.compare_players;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for the player comparison view.
 */
public class ComparePlayersViewModel {

    public static final String TITLE_LABEL = "Player Card Comparison";
    public static final String FIRST_PLAYER_LABEL = "Player 1:";
    public static final String SECOND_PLAYER_LABEL = "Player 2:";
    public static final String SEASON_LABEL = "Season:";
    public static final String COMPARE_BUTTON_LABEL = "Compare";
    public static final String EXIT_BUTTON_LABEL = "Exit";

    public static final String VIEW_NAME = "compare players";

    private final List<ComparePlayersViewModelListener> listeners = new ArrayList<>();

    private ComparePlayersState state = new ComparePlayersState();

    public ComparePlayersState getState() {
        return state;
    }

    public void setState(ComparePlayersState state) {
        this.state = state;
    }

    public void addListener(ComparePlayersViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (ComparePlayersViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
