package entity;

/**
 * A simple entity representing a Team.
 * Teams have:
 *     - id: Unique integer id for the team.
 *     - name: Name of the NBA team.
 *     - stats: Pointer to TeamsStats object with statisitcs of the team.
 */
public class Team {
  private final int id;
  private final String name;
  private final TeamStats stats;

  /**
   * Creates a new user with the given non-empty name and non-empty password.
    * @param id the Team's unique id.
    * @param name the Team's name.
    * @param stats the Team's Stats.
    * @throws IllegalArgumentException if the parameters empty.
   */
  public Team(int id, String name, TeamStats stats) {
    if ("".equals(name)) {
      throw new IllegalArgumentException("Password cannot be empty");
    }

    this.id = id;
    this.name = name;
    this.stats = stats;
  }

  public int getId() { return id; }
  public String getName() { return name; }
  public TeamStats getStats() { return stats; }
}
