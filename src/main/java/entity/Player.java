package entity;

/**
 * An NBA player as identified by the API.
 */
public class Player {

    private final int id;
    private final String firstName;
    private final String lastName;

    public Player(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName == null ? "" : firstName.trim();
        this.lastName = lastName == null ? "" : lastName.trim();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Returns "First Last", or whichever part is present.
     *
     * @return the player's full name
     */
    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
