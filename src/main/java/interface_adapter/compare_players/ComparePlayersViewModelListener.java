package interface_adapter.compare_players;

/**
 * Listener for changes to the ComparePlayersViewModel (implemented by the view).
 */
public interface ComparePlayersViewModelListener {

    /**
     * Called when the view model's state has changed.
     */
    void stateChanged();
}
