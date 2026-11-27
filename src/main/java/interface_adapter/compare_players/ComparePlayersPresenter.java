package interface_adapter.compare_players;

import java.util.ArrayList;
import java.util.List;

import entity.Stat;
import use_case.compare_players.ComparePlayersOutputBoundary;
import use_case.compare_players.ComparePlayersOutputData;
import use_case.compare_players.ComparePlayersOutputData.Leader;
import use_case.compare_players.ComparePlayersOutputData.PlayerCard;
import use_case.compare_players.ComparePlayersOutputData.StatComparison;

/**
 * Turns the comparison into display-ready text (labels, one decimal place,
 * "N/A" for missing values) and highlight flags, stores them in the view
 * model's state, and notifies the view.
 */
public class ComparePlayersPresenter implements ComparePlayersOutputBoundary {

    static final String NOT_AVAILABLE = "N/A";

    private final ComparePlayersViewModel viewModel;

    public ComparePlayersPresenter(ComparePlayersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ComparePlayersOutputData outputData) {
        ComparePlayersState state = viewModel.getState();
        state.setTitle(outputData.getFirstPlayer().getName() + " vs " + outputData.getSecondPlayer().getName()
                + " - " + seasonLabel(outputData.getSeason()) + " Season Averages");
        state.setFirstCard(header(outputData.getFirstPlayer()));
        state.setSecondCard(header(outputData.getSecondPlayer()));
        state.setRows(buildRows(outputData.getComparisons()));
        state.setErrorMessage(null);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        ComparePlayersState state = viewModel.getState();
        state.setTitle("");
        state.setFirstCard(null);
        state.setSecondCard(null);
        state.setRows(new ArrayList<>());
        state.setErrorMessage(errorMessage);
        viewModel.notifyStateChanged();
    }

    private static ComparePlayersState.CardHeader header(PlayerCard card) {
        String games = card.getGamesPlayed() + (card.getGamesPlayed() == 1 ? " game" : " games");
        String subtitle = card.getTeamName() == null ? games : card.getTeamName() + " · " + games;
        return new ComparePlayersState.CardHeader(card.getName(), subtitle);
    }

    private static List<ComparePlayersState.StatRow> buildRows(List<StatComparison> comparisons) {
        List<ComparePlayersState.StatRow> rows = new ArrayList<>();
        for (StatComparison c : comparisons) {
            if (c.getStat() == Stat.GAMES_PLAYED) {
                continue;   // already shown in each card's header
            }
            rows.add(new ComparePlayersState.StatRow(
                    label(c.getStat()),
                    format(c.getStat(), c.getFirstValue()),
                    format(c.getStat(), c.getSecondValue()),
                    c.getLeader() == Leader.FIRST,
                    c.getLeader() == Leader.SECOND));
        }
        return rows;
    }

    static String label(Stat stat) {
        switch (stat) {
            case GAMES_PLAYED: return "Games Played";
            case MINUTES: return "Minutes";
            case POINTS: return "Points";
            case REBOUNDS: return "Rebounds";
            case ASSISTS: return "Assists";
            case STEALS: return "Steals";
            case BLOCKS: return "Blocks";
            case TURNOVERS: return "Turnovers";
            case FIELD_GOAL_PERCENT: return "Field Goal %";
            case THREE_POINT_PERCENT: return "Three-Point %";
            case FREE_THROW_PERCENT: return "Free Throw %";
            default: return stat.name();
        }
    }

    /** Formats a value to one decimal place ("27.3" or "51.2%"), or "N/A". */
    static String format(Stat stat, Double value) {
        if (value == null) {
            return NOT_AVAILABLE;
        }
        String number = String.format("%.1f", value);
        return stat.isPercentage() ? number + "%" : number;
    }

    /** 2024 -> "2024-25". */
    static String seasonLabel(int season) {
        return season + "-" + String.format("%02d", (season + 1) % 100);
    }
}
