package interface_adapter.compare_players;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the player comparison view: display-ready text and highlight flags only.
 */
public class ComparePlayersState {

    private String title = "";
    private CardHeader firstCard;
    private CardHeader secondCard;
    private List<StatRow> rows = new ArrayList<>();
    private String errorMessage;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    /**
     * Returns the first player's card header, or null when there are no cards to show.
     *
     * @return the header or null
     */
    public CardHeader getFirstCard() { return firstCard; }

    public void setFirstCard(CardHeader firstCard) { this.firstCard = firstCard; }

    public CardHeader getSecondCard() { return secondCard; }

    public void setSecondCard(CardHeader secondCard) { this.secondCard = secondCard; }

    /**
     * Returns the stat rows shared by both cards, in display order.
     *
     * @return the rows
     */
    public List<StatRow> getRows() { return rows; }

    public void setRows(List<StatRow> rows) { this.rows = rows; }

    /**
     * Returns the error message, or null if the last comparison succeeded.
     *
     * @return the error message or null
     */
    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    /** The top of a player card: name and a line such as "Los Angeles Lakers · 70 games". */
    public static class CardHeader {
        private final String name;
        private final String subtitle;

        public CardHeader(String name, String subtitle) {
            this.name = name;
            this.subtitle = subtitle;
        }

        public String getName() { return name; }

        public String getSubtitle() { return subtitle; }
    }

    /** One stat as it appears on both cards. */
    public static class StatRow {
        private final String label;
        private final String firstValue;
        private final String secondValue;
        private final boolean firstHighlighted;
        private final boolean secondHighlighted;

        public StatRow(String label, String firstValue, String secondValue,
                       boolean firstHighlighted, boolean secondHighlighted) {
            this.label = label;
            this.firstValue = firstValue;
            this.secondValue = secondValue;
            this.firstHighlighted = firstHighlighted;
            this.secondHighlighted = secondHighlighted;
        }

        public String getLabel() { return label; }

        public String getFirstValue() { return firstValue; }

        public String getSecondValue() { return secondValue; }

        /** @return true if the first player's value is the better one */
        public boolean isFirstHighlighted() { return firstHighlighted; }

        /** @return true if the second player's value is the better one */
        public boolean isSecondHighlighted() { return secondHighlighted; }
    }
}
