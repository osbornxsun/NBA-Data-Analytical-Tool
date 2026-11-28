package interface_adapter.match_statistics;

import use_case.match_statistics.CompletedGameOutputData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Optional;
import entity.BoxScore;

/** Holds the rows and message displayed by the Match Statistics view. */
public final class MatchStatisticsViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private List<CompletedGameOutputData> games = List.of();
    private Optional<BoxScore> boxScore = Optional.empty();
    private String message = "";

    public List<CompletedGameOutputData> getGames() {
        return games;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(List<CompletedGameOutputData> games, String message) {
        boxScore = Optional.empty();
        this.games = List.copyOf(games);
        this.message = message;
        changes.firePropertyChange("result", null, this.games);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public Optional<BoxScore> getBoxScore() {
        return boxScore;
    }

    public void setBoxScore(Optional<BoxScore> score, String message) {
        boxScore = score;
        this.message = message;
        changes.firePropertyChange("boxScore", null, score);
    }
}
