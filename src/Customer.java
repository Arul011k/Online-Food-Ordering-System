// Child class: Customer IS-A User (inheritance)
public class Customer extends User {

    public Customer(int userId, String username) {
        super(userId, username, "CUSTOMER");   // calls the parent constructor
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, " + getUsername() + "! Browse the menu and place your order.";
    }
}