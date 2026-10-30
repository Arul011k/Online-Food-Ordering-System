import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class MenuScreen extends Application {

    private Cart cart = new Cart();
    private Label totalLabel = new Label("Total: Rs.0.0");
    private double discountPercent = 0.0;

    // all food loaded once from MySQL
    private ArrayList<Food> allFood = new ArrayList<>();

    // HashMap: coupon code -> discount percentage (fast lookup by key)
    private HashMap<String, Double> coupons = new HashMap<>();

    private VBox menuBox = new VBox(10);
    private TextField searchField = new TextField();
    private ComboBox<String> sortBox = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {
        FoodDAO foodDao = new FoodDAO();
        OrderDAO orderDao = new OrderDAO();
        allFood = foodDao.getAllFood();

        coupons.put("SAVE10", 0.10);
        coupons.put("SAVE20", 0.20);
        coupons.put("WELCOME5", 0.05);

        // ----- top bar: search, sort, logout -----
        searchField.setPromptText("Search food...");
        sortBox.getItems().addAll("Default", "Price: Low to High", "Price: High to Low", "Name: A to Z");
        sortBox.setValue("Default");

        searchField.textProperty().addListener((obs, oldText, newText) -> refreshMenu());
        sortBox.setOnAction(e -> refreshMenu());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            new LoginScreen().start(new Stage());
            primaryStage.close();
        });

        HBox topBar = new HBox(10, searchField, sortBox, spacer, logoutButton);
        topBar.setPadding(new Insets(10));

        menuBox.setPadding(new Insets(15));
        refreshMenu();

        ScrollPane scrollPane = new ScrollPane(menuBox);
        scrollPane.setFitToWidth(true);

        // ----- coupon and order controls -----
        TextField couponField = new TextField();
        couponField.setPromptText("Coupon code");

        Button applyCouponButton = new Button("Apply Coupon");
        applyCouponButton.setOnAction(e -> {
            String code = couponField.getText().trim().toUpperCase();
            Double percent = coupons.get(code);   // HashMap lookup
            if (percent == null) {
                showAlert(AlertType.WARNING, "Invalid coupon code.");
            } else {
                discountPercent = percent;
                updateTotalLabel();
                showAlert(AlertType.INFORMATION, "Coupon applied: " + (int) (percent * 100) + "% off");
            }
        });

        Button placeOrderButton = new Button("Pay & Place Order");
        placeOrderButton.setOnAction(e -> {
            if (cart.getItems().isEmpty()) {
                showAlert(AlertType.WARNING, "Cart is empty! Add something first.");
                return;
            }

            StringBuilder summary = new StringBuilder();
            int totalQty = 0;
            for (CartItem item : cart.getItems()) {
                summary.append(item.getFood().getName())
                       .append(" x").append(item.getQuantity()).append(", ");
                totalQty += item.getQuantity();
            }
            double finalTotal = cart.getTotal() * (1 - discountPercent);

            // PAYMENT first: the order is saved only if payment succeeds
            String[] payment = processPayment(finalTotal);
            if (payment == null) {
                showAlert(AlertType.ERROR, "Payment failed or cancelled. Order NOT placed.");
                return;
            }

            boolean success = orderDao.placeOrder(summary.toString(), totalQty, finalTotal,
                                                  payment[0], payment[1]);

            if (success) {
                showAlert(AlertType.INFORMATION, "Order placed successfully!\n"
                        + "Total: Rs." + String.format("%.2f", finalTotal) + "\n"
                        + "Payment: " + payment[0] + " (" + payment[1] + ")");
                cart = new Cart();
                discountPercent = 0.0;
                couponField.clear();
                updateTotalLabel();
            } else {
                showAlert(AlertType.ERROR, "Something went wrong saving your order.");
            }
        });

        HBox actionRow = new HBox(10, couponField, applyCouponButton, placeOrderButton);
        actionRow.setPadding(new Insets(10));

        VBox bottomBox = new VBox(5, totalLabel, actionRow);
        bottomBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 640, 560);
        primaryStage.setTitle("Online Food Ordering System - Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Returns {method, status} if payment succeeded, or null if it failed / was cancelled
    private String[] processPayment(double amount) {
        String amountText = String.format("%.2f", amount);

        ChoiceDialog<String> choice = new ChoiceDialog<>("Cash on Delivery",
                "Cash on Delivery", "UPI", "Card");
        choice.setTitle("Payment");
        choice.setHeaderText("Amount to pay: Rs." + amountText);
        choice.setContentText("Choose payment method:");
        Optional<String> result = choice.showAndWait();

        if (result.isEmpty()) {
            return null;                                   // user cancelled
        }
        String method = result.get();

        if (method.equals("Cash on Delivery")) {
            return new String[]{method, "PENDING"};        // pay when food arrives
        }

        if (method.equals("UPI")) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("UPI Payment");
            dialog.setHeaderText("Pay Rs." + amountText + " via UPI");
            dialog.setContentText("Enter UPI ID (example: name@upi):");
            Optional<String> upi = dialog.showAndWait();

            if (upi.isEmpty() || !upi.get().trim().matches("[A-Za-z0-9._-]+@[A-Za-z0-9]+")) {
                return null;                               // cancelled or invalid ID
            }
            return new String[]{method, "PAID"};
        }

        // Card
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Card Payment");
        dialog.setHeaderText("Pay Rs." + amountText + " by card");
        dialog.setContentText("Enter 16-digit card number:");
        Optional<String> card = dialog.showAndWait();

        if (card.isEmpty() || !card.get().trim().matches("\\d{16}")) {
            return null;                                   // cancelled or invalid number
        }
        return new String[]{method, "PAID"};
    }

    // Rebuilds the menu: SEARCH (linear search), then SORT (Comparator)
    private void refreshMenu() {
        String keyword = searchField.getText().trim().toLowerCase();

        ArrayList<Food> shown = new ArrayList<>();
        for (Food f : allFood) {
            if (f.getName().toLowerCase().contains(keyword)) {
                shown.add(f);
            }
        }

        String sort = sortBox.getValue();
        if ("Price: Low to High".equals(sort)) {
            shown.sort(Comparator.comparingDouble(Food::getPrice));
        } else if ("Price: High to Low".equals(sort)) {
            shown.sort(Comparator.comparingDouble(Food::getPrice).reversed());
        } else if ("Name: A to Z".equals(sort)) {
            shown.sort(Comparator.comparing(Food::getName));
        }

        menuBox.getChildren().clear();

        if (shown.isEmpty()) {
            menuBox.getChildren().add(new Label("No food found."));
            return;
        }

        for (Food food : shown) {
            HBox row = new HBox(15);
            Label nameLabel = new Label(food.getName() + " - Rs." + food.getPrice());
            Button addButton = new Button("Add to Cart");

            addButton.setOnAction(e -> {
                cart.addItem(new CartItem(food, 1));
                updateTotalLabel();
            });

            row.getChildren().addAll(nameLabel, addButton);
            menuBox.getChildren().add(row);
        }
    }

    private void updateTotalLabel() {
        double total = cart.getTotal() * (1 - discountPercent);
        totalLabel.setText("Total: Rs." + String.format("%.2f", total));
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}