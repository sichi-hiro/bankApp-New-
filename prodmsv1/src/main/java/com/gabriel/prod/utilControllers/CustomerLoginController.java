package com.gabriel.prod.utilControllers;

import com.gabriel.prod.model.Product;
import com.gabriel.prod.serviceImpl.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerLoginController {

    @FXML private TextField txtAccountNumber;
    @FXML private Label lblError;

    @FXML
    public void onVerifyAccount(ActionEvent event) {
        String inputAcc = txtAccountNumber.getText().trim();

        if (inputAcc.isEmpty()) {
            showErrorMessage("Account number field cannot be empty.");
            return;
        }

        if (!inputAcc.matches("\\d+")) {
            showErrorMessage("Invalid account format. Digits only.");
            return;
        }

        try {
            System.out.println("Querying backend system for Account ID: " + inputAcc);
            int lookupId = Integer.parseInt(inputAcc);

            ProductService productService = ProductService.getService();
            Product[] systemAccounts = productService.getProducts();

            Product matchedAccount = null;

            for (Product p : systemAccounts) {
                if (p.getId() == lookupId) {
                    matchedAccount = p;
                    break;
                }
            }

            if (matchedAccount != null) {
                System.out.println("Access Granted! Found Account Holder: " + matchedAccount.getName());

                ProdManController.product = matchedAccount;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customer-view.fxml"));
                Parent root = fxmlLoader.load();

                Scene scene = new Scene(root, 720, 575);
                scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setTitle("Banking App - Customer: " + matchedAccount.getName() + " (#" + lookupId + ")");
                stage.setScene(scene);
                stage.show();
            } else {
                showErrorMessage("Account number #" + inputAcc + " not found in system records.");
            }

        } catch (Exception e) {
            showErrorMessage("Connection error: Unable to reach verification server.");
            System.out.println("Customer login service failure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("role-selection.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setTitle("Banking App - Access Portal");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }
}