package com.gabriel.prod.utilControllers;

import com.gabriel.prod.model.Product;
import com.gabriel.prod.serviceImpl.ProductService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

public class DeleteProductController {
    @Setter private ProdManController controller;
    @Setter private Stage stage;
    @Setter private Scene parentScene;

    @FXML private Label lblWarningText;

    public void refresh() {
        Product target = ProdManController.product;
        if (target != null) {
            lblWarningText.setText("Are you sure you want to completely terminate the account registry for:\n\n"
                    + target.getName() + " (Account ID: #" + target.getId() + ")?");
        }
    }

    @FXML
    public void onSubmit(ActionEvent event) {
        try {
            Product target = ProdManController.product;
            if (target != null) {
                // Fire delete method out to server repo connection
                ProductService.getService().delete(target.getId());
                controller.refresh();
                controller.clearControlTexts();
            }
            onBack(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBack(ActionEvent event) {
        if (stage != null && parentScene != null) {
            stage.setScene(parentScene);
            stage.setTitle("Apex Trust Bank - Administration Console");
        }
    }
}