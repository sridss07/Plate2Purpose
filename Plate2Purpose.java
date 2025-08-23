import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FoodShare extends Application {

    static class User {
        String username;
        String password;
        String role; // Donor or NGO
        String phoneNumber;
        String bikeNumber; // only for NGO

        User(String username, String password, String role, String phoneNumber, String bikeNumber) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.phoneNumber = phoneNumber;
            this.bikeNumber = bikeNumber;
        }
    }

    static class FoodItem {
        String name;
        String quantity;
        String location;
        LocalDateTime timestamp;
        boolean collected;
        String donorPhone;

        FoodItem(String name, String quantity, String location, LocalDateTime timestamp, boolean collected, String donorPhone) {
            this.name = name;
            this.quantity = quantity;
            this.location = location;
            this.timestamp = timestamp;
            this.collected = collected;
            this.donorPhone = donorPhone;
        }
    }

    private final List<User> users = new ArrayList<>();
    private final List<FoodItem> foodList = new ArrayList<>();
    private User currentUser;
    private String currentRole = "";
    private Stage primaryStageRef;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStageRef = primaryStage;

        // Pre-added users
        users.add(new User("donor1", "donorpass", "Donor", "1234567890", null));
        users.add(new User("ngo1", "ngopass", "NGO", "0987654321", "BIKE123"));

        primaryStage.setTitle("FoodShare - Donation Portal");
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage stage) {
        Label title = new Label("FoodShare Login");
        title.setFont(new Font("Arial", 28));

        Button donorBtn = new Button("Login as Donor");
        Button ngoBtn = new Button("Login as NGO");
        Button registerBtn = new Button("New User? Register Here");

        donorBtn.setOnAction(e -> showCredentialScreen(stage, "Donor"));
        ngoBtn.setOnAction(e -> showCredentialScreen(stage, "NGO"));
        registerBtn.setOnAction(e -> showRegistrationScreen(stage));

        VBox layout = new VBox(15, title, donorBtn, ngoBtn, registerBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #fdf6e3;");

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void showCredentialScreen(Stage stage, String role) {
        Label heading = new Label(role + " Login");
        heading.setFont(new Font("Arial", 22));

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label errorLabel = new Label();

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> {
            User user = getUser(role, username.getText().trim(), password.getText().trim());
            if (user != null) {
                currentRole = role;
                currentUser = user;
                if (role.equals("Donor")) showDonorScreen(stage);
                else showNGOScreen(stage);
            } else {
                errorLabel.setText("Invalid credentials. Try again.");
                errorLabel.setTextFill(Color.RED);
            }
        });

        VBox layout = new VBox(10, heading, username, password, loginBtn, errorLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #fdf6e3;");

        stage.setScene(new Scene(layout, 500, 400));
    }

    private void showRegistrationScreen(Stage stage) {
        Label heading = new Label("Register New User");
        heading.setFont(new Font("Arial", 22));

        TextField username = new TextField();
        username.setPromptText("Choose Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Choose Password");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Donor", "NGO");
        roleComboBox.setPromptText("Select Role");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField bikeField = new TextField();
        bikeField.setPromptText("Bike Number (NGO only)");

        Label status = new Label();

        roleComboBox.setOnAction(e -> {
            if ("NGO".equals(roleComboBox.getValue())) {
                bikeField.setDisable(false);
            } else {
                bikeField.clear();
                bikeField.setDisable(true);
            }
        });

        bikeField.setDisable(true);

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();
            String role = roleComboBox.getValue();
            String phone = phoneField.getText().trim();
            String bike = bikeField.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || role == null || phone.isEmpty() || ("NGO".equals(role) && bike.isEmpty())) {
                status.setText("All fields are required (Bike number required for NGO).");
                status.setTextFill(Color.RED);
            } else if (isUsernameTaken(user)) {
                status.setText("Username already exists. Try another.");
                status.setTextFill(Color.RED);
            } else {
                users.add(new User(user, pass, role, phone, bike));
                status.setText("Registration successful! Return to login.");
                status.setTextFill(Color.GREEN);
            }
        });

        Button backBtn = new Button("Back to Login");
        backBtn.setOnAction(e -> showLoginScreen(stage));

        VBox layout = new VBox(10, heading, username, password, roleComboBox, phoneField, bikeField, registerBtn, status, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #fdf6e3;");

        stage.setScene(new Scene(layout, 500, 500));
    }

    private boolean isUsernameTaken(String username) {
        for (User u : users) {
            if (u.username.equals(username)) return true;
        }
        return false;
    }

    private User getUser(String role, String user, String pass) {
        for (User u : users) {
            if (u.role.equals(role) && u.username.equals(user) && u.password.equals(pass)) return u;
        }
        return null;
    }

    private void showDonorScreen(Stage stage) {
        // Background image
        Image bgImage = new Image("file:///C:/Users/sri09/Downloads/FoodDonor.jpg");
        BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));

        Label heading = new Label("Donor - Add Food Details");
        heading.setFont(new Font("Arial", 20));

        TextField foodName = new TextField();
        foodName.setPromptText("Enter food item");

        TextField quantity = new TextField();
        quantity.setPromptText("Enter quantity");

        TextField location = new TextField();
        location.setPromptText("Enter location/address");

        TextField donorPhone = new TextField(currentUser.phoneNumber);
        donorPhone.setPromptText("Phone Number");
        donorPhone.setDisable(true);

        Label msg = new Label();

        // QR Code ImageView fixed right
        ImageView qrView = new ImageView();
        qrView.setFitWidth(180);
        qrView.setFitHeight(180);

        Button addButton = new Button("Donate Food");

        addButton.setOnAction(e -> {
            String name = foodName.getText().trim();
            String qty = quantity.getText().trim();
            String loc = location.getText().trim();
            String phone = donorPhone.getText().trim();
            LocalDateTime dateTime = LocalDateTime.now();

            if (name.isEmpty() || qty.isEmpty() || loc.isEmpty()) {
                msg.setText("All fields are required!");
                msg.setTextFill(Color.RED);
                qrView.setImage(null);
            } else {
                FoodItem item = new FoodItem(name, qty, loc, dateTime, false, phone);
                foodList.add(item);
                msg.setText("Food item added successfully!");
                msg.setTextFill(Color.GREEN);

                foodName.clear();
                quantity.clear();
                location.clear();

                // Generate QR code for this donation
                String qrData = "Food: " + item.name + "\nQty: " + item.quantity + "\nLocation: " + item.location +
                        "\nDonor Phone: " + item.donorPhone + "\nAdded: " + item.timestamp.toLocalDate();
                try {
                    String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=" + URLEncoder.encode(qrData, StandardCharsets.UTF_8.toString());
                    Image qrImage = new Image(qrUrl);
                    qrView.setImage(qrImage);
                } catch (Exception ex) {
                    qrView.setImage(null);
                }
            }
        });

        Button notifyNgoBtn = new Button("Notify NGO via WhatsApp");
        notifyNgoBtn.setOnAction(e -> {
            // Notify all NGO phones via WhatsApp web
            StringBuilder ngoPhones = new StringBuilder();
            for (User u : users) {
                if ("NGO".equals(u.role)) {
                    ngoPhones.append(u.phoneNumber).append(",");
                }
            }
            if (ngoPhones.length() > 0) {
                ngoPhones.deleteCharAt(ngoPhones.length() - 1);
                String msgText = "Food donation available from donor: " + currentUser.username +
                        ". Please check the FoodShare app for details.";
                try {
                    String encodedMsg = URLEncoder.encode(msgText, StandardCharsets.UTF_8.toString());
                    String url = "https://wa.me/?text=" + encodedMsg;
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    msg.setText("Failed to open WhatsApp.");
                    msg.setTextFill(Color.RED);
                }
            } else {
                msg.setText("No NGO phone numbers found.");
                msg.setTextFill(Color.RED);
            }
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            currentRole = "";
            showLoginScreen(primaryStageRef);
        });

        VBox inputs = new VBox(10, heading, foodName, quantity, location, donorPhone, addButton, notifyNgoBtn, logoutBtn, msg);
        inputs.setAlignment(Pos.CENTER_LEFT);
        inputs.setPadding(new Insets(20));
        inputs.setPrefWidth(380);

        VBox qrBox = new VBox(new Label("Donation QR Code:"), qrView);
        qrBox.setAlignment(Pos.TOP_CENTER);
        qrBox.setPadding(new Insets(20));
        qrBox.setStyle("-fx-border-color: gray; -fx-background-color: #fff8dc;");
        qrBox.setPrefWidth(220);

        HBox mainLayout = new HBox(inputs, qrBox);
        mainLayout.setBackground(new Background(bg));
        mainLayout.setStyle("-fx-background-color: #fdf6e3;");

        stage.setScene(new Scene(mainLayout, 650, 450));
    }

    private void showNGOScreen(Stage stage) {
        // Background image for NGO (reuse or change if needed)
        Image bgImage = new Image("file:///C:/Users/sri09/Downloads/FoodDonor.jpg");
        BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));

        Label heading = new Label("NGO Dashboard");
        heading.setFont(new Font("Arial", 22));

        Label bikeLabel = new Label("Bike Number: " + (currentUser.bikeNumber == null ? "N/A" : currentUser.bikeNumber));
        Label phoneLabel = new Label("Phone Number: " + currentUser.phoneNumber);

        TableView<FoodItem> table = new TableView<>();
        table.setPrefWidth(600);

        TableColumn<FoodItem, String> foodCol = new TableColumn<>("Food");
        foodCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().name));

        TableColumn<FoodItem, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().quantity));

        TableColumn<FoodItem, String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().location));

        TableColumn<FoodItem, String> dateCol = new TableColumn<>("Added On");
        dateCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().timestamp.toLocalDate().toString()));

        TableColumn<FoodItem, String> donorPhoneCol = new TableColumn<>("Donor Phone");
        donorPhoneCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().donorPhone));

        TableColumn<FoodItem, Boolean> collectedCol = new TableColumn<>("Collected");
        collectedCol.setCellValueFactory(d -> new javafx.beans.property.SimpleBooleanProperty(d.getValue().collected));
        collectedCol.setCellFactory(tc -> new CheckBoxTableCell<>());

        table.getColumns().addAll(foodCol, qtyCol, locCol, dateCol, donorPhoneCol, collectedCol);
        table.getItems().setAll(foodList);

        Label msg = new Label();

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setOnAction(e -> {
            table.getItems().setAll(foodList);
            msg.setText("List refreshed.");
            msg.setTextFill(Color.GREEN);
        });

        Button notifyDonorBtn = new Button("Notify Donors via WhatsApp");
        notifyDonorBtn.setOnAction(e -> {
            if (foodList.isEmpty()) {
                msg.setText("No donations available to notify.");
                msg.setTextFill(Color.RED);
                return;
            }
            // Notify all donor phones with donation info
            StringBuilder phonesMsg = new StringBuilder();
            for (FoodItem f : foodList) {
                phonesMsg.append("Food: ").append(f.name).append(", Qty: ").append(f.quantity).append(", Location: ").append(f.location).append("\n");
            }
            String finalMsg = "Dear donors, NGO " + currentUser.username + " is available for food collection.\n" + phonesMsg.toString();
            try {
                String encodedMsg = URLEncoder.encode(finalMsg, StandardCharsets.UTF_8.toString());
                String url = "https://wa.me/?text=" + encodedMsg;
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                msg.setText("Failed to open WhatsApp.");
                msg.setTextFill(Color.RED);
            }
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            currentRole = "";
            showLoginScreen(primaryStageRef);
        });

        // Map button (Initially disabled)
        Button openMapBtn = new Button("Open Location in Map");
        openMapBtn.setDisable(true);

        // Enable map button when a food item is selected
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                openMapBtn.setDisable(false);
            } else {
                openMapBtn.setDisable(true);
            }
        });

        // Action to open Google Maps with the selected location
        openMapBtn.setOnAction(e -> {
            FoodItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && selected.location != null && !selected.location.trim().isEmpty()) {
                try {
                    String locEncoded = URLEncoder.encode(selected.location, StandardCharsets.UTF_8.toString());
                    String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + locEncoded;
                    Desktop.getDesktop().browse(new URI(mapsUrl));
                } catch (Exception ex) {
                    msg.setText("Failed to open map.");
                    msg.setTextFill(Color.RED);
                }
            } else {
                msg.setText("No location info available for selected item.");
                msg.setTextFill(Color.RED);
            }
        });

        VBox vbox = new VBox(10, heading, bikeLabel, phoneLabel, table, refreshBtn, notifyDonorBtn, openMapBtn, logoutBtn, msg);
        vbox.setPadding(new Insets(20));
        vbox.setBackground(new Background(bg));

        stage.setScene(new Scene(vbox, 700, 550));
    }
}
