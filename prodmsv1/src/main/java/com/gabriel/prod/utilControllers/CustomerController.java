package com.gabriel.prod.utilControllers;

import com.gabriel.prod.model.Product;
import com.gabriel.prod.serviceImpl.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML private Label lblBalance;
    @FXML private TextField tfAmount;
    @FXML private Button btnDeposit;
    @FXML private Button btnWithdraw;
    @FXML private Button btnLogout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBalanceDisplay();
    }

    // Functionality 1: Balance Inquiry (Helper to keep the display perfectly synced)
    private void updateBalanceDisplay() {
        if (ProdManController.product != null) {
            lblBalance.setText(String.format("₱ %,.2f", (double) ProdManController.product.getBalance()));
        } else {
            lblBalance.setText("₱ 0.00");
        }
    }

    // Functionality 2: Deposit handling operation loop
    // Replace the core transaction blocks inside CustomerController.java with these:

    @FXML
    public void onDeposit(ActionEvent event) {
        try {
            if (ProdManController.product == null) {
                showNotification(Alert.AlertType.ERROR, "Account Error", "No active account profile detected.");
                return;
            }

            double amount = Double.parseDouble(tfAmount.getText().trim());
            if (amount <= 0) {
                showNotification(Alert.AlertType.WARNING, "Invalid Amount", "Please enter a deposit amount greater than zero.");
                return;
            }

            // Add directly to the model instance variable
            int currentBalance = ProdManController.product.getBalance();
            ProdManController.product.setBalance(currentBalance + (int) amount);

            // Commit transaction data downstream to the local storage wrapper service
            ProductService.getService().update(ProdManController.product);

            updateBalanceDisplay();
            showNotification(Alert.AlertType.INFORMATION, "Transaction Successful", String.format("Successfully deposited ₱%,.2f", amount));
            tfAmount.clear();

        } catch (NumberFormatException e) {
            showNotification(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric transaction amount.");
        } catch (Exception e) {
            showNotification(Alert.AlertType.ERROR, "Database Error", "Failed to update balance: " + e.getMessage());
        }
    }

    @FXML
    public void onWithdraw(ActionEvent event) {
        try {
            if (ProdManController.product == null) {
                showNotification(Alert.AlertType.ERROR, "Account Error", "No active account profile detected.");
                return;
            }

            double amount = Double.parseDouble(tfAmount.getText().trim());
            if (amount <= 0) {
                showNotification(Alert.AlertType.WARNING, "Invalid Amount", "Please enter a withdrawal amount greater than zero.");
                return;
            }

            int currentBalance = ProdManController.product.getBalance();
            if (amount > currentBalance) {
                showNotification(Alert.AlertType.ERROR, "Insufficient Funds", "You do not have enough money to complete this withdrawal.");
                return;
            }

            // Deduct from the live instance variable
            ProdManController.product.setBalance(currentBalance - (int) amount);

            // Commit transaction data downstream
            ProductService.getService().update(ProdManController.product);

            updateBalanceDisplay();
            showNotification(Alert.AlertType.INFORMATION, "Transaction Successful", String.format("Successfully withdrew ₱%,.2f", amount));
            tfAmount.clear();

        } catch (NumberFormatException e) {
            showNotification(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric transaction amount.");
        } catch (Exception e) {
            showNotification(Alert.AlertType.ERROR, "Database Error", "Failed to update balance: " + e.getMessage());
        }
    }

    // Navigation Session Exit: Returns back to the Role Selection Panel
    @FXML
    public void onLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            URL resourceUrl = getClass().getResource("role-selection.fxml");
            if (resourceUrl == null) {
                resourceUrl = getClass().getResource("/com/gabriel/prod/role-selection.fxml");
            }

            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 750, 550);
            if (getClass().getResource("/css/splash.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());
            }

            // FIXED TEXT: Updated naming constraint to modern universal tag
            stage.setTitle("Banking App - Role Selection Portal");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error navigating back to login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Global alert template helper method
    private void showNotification(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}