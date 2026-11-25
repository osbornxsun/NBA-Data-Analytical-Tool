package interface_adapter.predict_match;

import java.util.ArrayList;
import java.util.List;

/** Everything the Match Prediction view needs to draw itself, already formatted. */
public class PredictMatchState {

  private String homeTeam = "";
  private String awayTeam = "";
  private String homeProbabilityText = "";
  private String awayProbabilityText = "";
  private int homeProbabilityPercent = 50;
  private String summary = "";
  private String modelDescription = "";
  private String error;
  private List<String> history = new ArrayList<>();

  public PredictMatchState() { }

  public PredictMatchState(PredictMatchState copy) {
    homeTeam = copy.homeTeam;
    awayTeam = copy.awayTeam;
    homeProbabilityText = copy.homeProbabilityText;
    awayProbabilityText = copy.awayProbabilityText;
    homeProbabilityPercent = copy.homeProbabilityPercent;
    summary = copy.summary;
    modelDescription = copy.modelDescription;
    error = copy.error;
    history = new ArrayList<>(copy.history);
  }

  public String getHomeTeam() { return homeTeam; }
  public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
  public String getAwayTeam() { return awayTeam; }
  public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
  public String getHomeProbabilityText() { return homeProbabilityText; }
  public void setHomeProbabilityText(String t) { this.homeProbabilityText = t; }
  public String getAwayProbabilityText() { return awayProbabilityText; }
  public void setAwayProbabilityText(String t) { this.awayProbabilityText = t; }
  public int getHomeProbabilityPercent() { return homeProbabilityPercent; }
  public void setHomeProbabilityPercent(int p) { this.homeProbabilityPercent = p; }
  public String getSummary() { return summary; }
  public void setSummary(String summary) { this.summary = summary; }
  public String getModelDescription() { return modelDescription; }
  public void setModelDescription(String d) { this.modelDescription = d; }
  public String getError() { return error; }
  public void setError(String error) { this.error = error; }
  public List<String> getHistory() { return history; }
  public void setHistory(List<String> history) { this.history = new ArrayList<>(history); }
}