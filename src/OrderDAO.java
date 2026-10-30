import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAO {

    // Saves an order together with its payment details
    public boolean placeOrder(String foodSummary, int totalQuantity, double totalAmount,
                              String paymentMethod, String paymentStatus) {
        String sql = "INSERT INTO ORDERS (food_name, quantity, total_amount, payment_method, payment_status) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, foodSummary);
            ps.setInt(2, totalQuantity);
            ps.setDouble(3, totalAmount);
            ps.setString(4, paymentMethod);
            ps.setString(5, paymentStatus);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error placing order: " + e.getMessage());
            return false;
        }
    }

    // Used by the Admin screen: reads every order from MySQL
    public ArrayList<String> getAllOrders() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT * FROM ORDERS ORDER BY order_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String method = rs.getString("payment_method");
                String status = rs.getString("payment_status");
                if (method == null) method = "-";
                if (status == null) status = "-";

                list.add("#" + rs.getInt("order_id")
                        + " | " + rs.getString("food_name")
                        + " | Qty: " + rs.getInt("quantity")
                        + " | Rs." + rs.getDouble("total_amount")
                        + " | " + method + " (" + status + ")"
                        + " | " + rs.getTimestamp("order_date"));
            }
        } catch (SQLException e) {
            System.out.println("Error reading orders: " + e.getMessage());
        }
        return list;
    }
}