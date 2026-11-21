package entity;

import java.time.LocalDate;

public record Game(int id, LocalDate date, String homeTeamName,
                   String awayTeamName, String status) {

    public boolean isCompleted() {
        return "Finished".equals(status);
    }
}