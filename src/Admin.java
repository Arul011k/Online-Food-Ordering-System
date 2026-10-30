// Child class: Admin IS-A User (inheritance)
public class Admin extends User {

    public Admin(int userId, String username) {
        super(userId, username, "ADMIN");
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, Admin " + getUsername() + "! You can view all customer orders.";
    }
}