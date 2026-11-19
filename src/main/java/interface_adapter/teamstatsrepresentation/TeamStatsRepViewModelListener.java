package interface_adapter.teamstatsrepresentation;

/**
 * Listener for changes to the TeamStatsRepViewModel (implemented by the view).
 */
public interface TeamStatsRepViewModelListener {

    /**
     * Called when the view model's state has changed.
     */
    void stateChanged();
}