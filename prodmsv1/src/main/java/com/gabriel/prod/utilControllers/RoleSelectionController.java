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

    @FXML
    public void onAdminSelected(MouseEvent event) {
        System.out.println("RoleSelectionController: Admin Access Portal Selected");
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Inside onAdminSelected:
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("prodman-view.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 720, 575);
            scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

            stage.setTitle("Apex Trust Bank - Administration Console");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error launching Admin Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCustomerSelected(MouseEvent event) {
        System.out.println("RoleSelectionController: Customer Access Portal Selected");
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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