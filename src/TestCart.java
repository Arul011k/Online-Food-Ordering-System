public class TestCart {
    public static void main(String[] args) {
        // create some food items
        Food burger = new Food(1, "Veg Burger", 99.00);
        Food pizza = new Food(2, "Cheese Pizza", 199.00);

        // wrap them into cart items with quantities
        CartItem item1 = new CartItem(burger, 2);   // 2 burgers
        CartItem item2 = new CartItem(pizza, 1);    // 1 pizza

        // create a cart and add items
        Cart cart = new Cart();
        cart.addItem(item1);
        cart.addItem(item2);

        // print each item and the total
        for (CartItem item : cart.getItems()) {
            System.out.println(item.getFood().getName()
                + " x" + item.getQuantity()
                + " = Rs." + item.getSubtotal());
        }

        System.out.println("Total: Rs." + cart.getTotal());
    }
}
