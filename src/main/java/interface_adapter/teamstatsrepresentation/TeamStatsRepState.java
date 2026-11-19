package interface_adapter.teamstatsrepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the team stats view: display-ready text only.
 */
public class TeamStatsRepState {

    private String title = "";
    private List<String[]> rows = new ArrayList<>();
    private String errorMessage;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    /**
     * Returns the table rows; each row is {label, value}.
     *
     * @return the rows in display order
     */
    public List<String[]> getRows() { return rows; }

    public void setRows(List<String[]> rows) { this.rows = rows; }

    /**
     * Returns the error message, or null if the last lookup succeeded.
     *
     * @return the error message or null
     */
    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
