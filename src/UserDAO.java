import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Returns a Customer or an Admin object, or null if login fails
    public User login(String username, String password) {
        String sql = "SELECT * FROM USER WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String name = rs.getString("username");
                    String role = rs.getString("role");

                    if ("ADMIN".equals(role)) {
                        return new Admin(id, name);
                    } else {
                        return new Customer(id, name);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }
}