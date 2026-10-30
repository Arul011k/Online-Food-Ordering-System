import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodDAO {

    public ArrayList<Food> getAllFood() {
        ArrayList<Food> foodList = new ArrayList<>();
        String sql = "SELECT * FROM FOOD";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("food_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                foodList.add(new Food(id, name, price));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching food: " + e.getMessage());
        }

        return foodList;
    }
}
