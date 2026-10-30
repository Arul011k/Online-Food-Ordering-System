import java.util.ArrayList;

public class TestFoodDAO {
    public static void main(String[] args) {
        FoodDAO dao = new FoodDAO();
        ArrayList<Food> foods = dao.getAllFood();

        for (Food food : foods) {
            System.out.println(food);
        }
    }
}