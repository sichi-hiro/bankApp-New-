package com.gabriel.prod.utilControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class RoleSelectionController {

    // Action execution loop when the System Admin card is clicked
    @FXML
    public void onAdminSelected(MouseEvent event) {
        System.out.println("RoleSelectionController: Admin Access Portal Selected");
        try {
            // 1. Fetch the active application Window Stage path safely
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 2. Load your updated banking-themed main dashboard layout
            // Inside onAdminSelected:
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("prodman-view.fxml"));
            Parent root = fxmlLoader.load();

            // 3. Configure the scene and map your existing workspace stylesheets
            Scene scene = new Scene(root, 720, 575);
            scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

            // 4. Render the Admin Banking dashboard window track
            stage.setTitle("Apex Trust Bank - Administration Console");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error launching Admin Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Action execution loop when the Bank Customer card is clicked
    @FXML
    public void onCustomerSelected(MouseEvent event) {
        System.out.println("RoleSelectionController: Customer Access Portal Selected");
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Redirect to customer-login.fxml instead of directly to customer-view!
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customer-login.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 600, 450);
            scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

            stage.setTitle("Apex Trust Bank - Customer Identification");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error launching Customer Login Portal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}