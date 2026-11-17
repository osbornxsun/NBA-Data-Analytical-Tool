package entity;

/**
 * Represents an NBA team in a particular season.
 */
public class SeasonTeam {

  private final int id;
  private final String name;
  private final String city;
  private final int season;
  private final SeasonTeamStats stats;

  public SeasonTeam(
    int id,
    String name,
    String city,
    int season,
    SeasonTeamStats stats) {

    if (id <= 0) {
      throw new IllegalArgumentException("Team ID must be positive");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Team name cannot be empty");
    }
    if (season <= 0) {
      throw new IllegalArgumentException("Season must be positive");
    }
    if (stats == null) {
      throw new IllegalArgumentException("Team statistics cannot be null");
    }

    this.id = id;
    this.name = name;
    this.city = city;
    this.season = season;
    this.stats = stats;
  }
  
  public int getId() { return id; }
  public String getName() { return name; }
  public String getCity() { return city; }
  public int getSeason() { return season; }
  public SeasonTeamStats getStats() { return stats; }
}
