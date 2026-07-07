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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML private Label lblBalance;
    @FXML private Label lblWelcome; // Tied directly to fx:id="lblWelcome" in FXML
    @FXML private TextField tfAmount;
    @FXML private Button btnDeposit;
    @FXML private Button btnWithdraw;
    @FXML private Button btnLogout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Dynamically set personalized greeting banner text
        if (ProdManController.product != null) {
            lblWelcome.setText("Welcome back, " + ProdManController.product.getName() + "!");
        } else {
            lblWelcome.setText("Customer Transaction Terminal");
        }

        updateBalanceDisplay();
    }

    private String getCurrencySymbol() {
        if (ProdManController.product != null) {
            String currencyProfile = ProdManController.product.getUomName();
            if (currencyProfile != null) {
                String profileUpper = currencyProfile.toUpperCase();
                if (profileUpper.contains("DOLLAR") || profileUpper.contains("USD")) {
                    return "$";
                } else if (profileUpper.contains("EURO") || profileUpper.contains("EUR")) {
                    return "€";
                } else if (profileUpper.contains("YEN") || profileUpper.contains("JPY")) {
                    return "¥";
                }
            }
        }
        return "₱";
    }

    private void updateBalanceDisplay() {
        if (ProdManController.product != null) {
            lblBalance.setText(String.format("%s %,.2f", getCurrencySymbol(), (double) ProdManController.product.getBalance()));
        } else {
            lblBalance.setText("₱ 0.00");
        }
    }

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

            int currentBalance = ProdManController.product.getBalance();
            ProdManController.product.setBalance(currentBalance + (int) amount);

            ProductService.getService().update(ProdManController.product);

            updateBalanceDisplay();
            showNotification(Alert.AlertType.INFORMATION, "Transaction Successful", String.format("Successfully deposited %s%,.2f", getCurrencySymbol(), amount));
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

            ProdManController.product.setBalance(currentBalance - (int) amount);

            ProductService.getService().update(ProdManController.product);

            updateBalanceDisplay();
            showNotification(Alert.AlertType.INFORMATION, "Transaction Successful", String.format("Successfully withdrew %s%,.2f", getCurrencySymbol(), amount));
            tfAmount.clear();

        } catch (NumberFormatException e) {
            showNotification(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric transaction amount.");
        } catch (Exception e) {
            showNotification(Alert.AlertType.ERROR, "Database Error", "Failed to update balance: " + e.getMessage());
        }
    }

    @FXML
    public void onLogout(ActionEvent event) {
        try {
            Stage stage;
            if (event.getSource() instanceof Node) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else if (event.getSource() instanceof MenuItem) {
                stage = (Stage) btnLogout.getScene().getWindow();
            } else {
                return;
            }

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

            stage.setTitle("Bank Application - Role Selection Portal");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error navigating back to login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onDepositMenuItem(ActionEvent event) {
        onDeposit(new ActionEvent(btnDeposit, btnDeposit));
    }

    @FXML
    public void onWithdrawMenuItem(ActionEvent event) {
        onWithdraw(new ActionEvent(btnWithdraw, btnWithdraw));
    }

    @FXML
    public void onAboutMenuItem(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Bank Application");
        alert.setContentText("This is a Product Management Application template developed by Rijai Consulting, modified into a Bank Application.\n\n"
                + "Modified by:\n"
                + "• Marc Dei Niel Bides\n"
                + "• Chloie May Broñola\n"
                + "• Naveen Pablo\n"
                + "• Karl Evlan Robasto");
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(480.0);

        alert.showAndWait();
    }

    private void showNotification(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}