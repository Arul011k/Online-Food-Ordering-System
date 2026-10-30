// Parent (base) class: common data for every kind of user
public abstract class User {
    private int userId;
    private String username;
    private String role;

    public User(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    // Abstract method: every child class MUST provide its own version
    public abstract String getWelcomeMessage();
}
