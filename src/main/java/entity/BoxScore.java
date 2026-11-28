package entity;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/** Away and home totals, with points in period order. */
public record BoxScore(Team away, Team home) {
    public BoxScore {
        if (away.periodScores().size() != home.periodScores().size()) {
            throw new IllegalArgumentException("Teams must have matching periods");
        }
    }

    public record Team(String name, Integer finalScore, List<Integer> periodScores,
                       Integer rebounds, Integer assists, Integer steals) {
        public Team {
            // A null entry preserves the position of an unreported period.
            periodScores = Collections.unmodifiableList(new ArrayList<>(periodScores));
        }
    }
}
