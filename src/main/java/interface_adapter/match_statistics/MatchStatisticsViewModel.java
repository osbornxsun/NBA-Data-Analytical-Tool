package interface_adapter.match_statistics;

import use_case.match_statistics.CompletedGameOutputData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Holds the rows and message displayed by the Match Statistics view. */
public final class MatchStatisticsViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private List<CompletedGameOutputData> games = List.of();
    private String message = "";

    public List<CompletedGameOutputData> getGames() {
        return games;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(List<CompletedGameOutputData> games, String message) {
        this.games = List.copyOf(games);
        this.message = message;
        changes.firePropertyChange("result", null, this.games);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }
}
