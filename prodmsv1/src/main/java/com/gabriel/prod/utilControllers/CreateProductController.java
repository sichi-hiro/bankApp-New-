package com.gabriel.prod.utilControllers;

import com.gabriel.prod.model.Product;
import com.gabriel.prod.model.Uom;
import com.gabriel.prod.serviceImpl.ProductService;
import com.gabriel.prod.serviceImpl.UomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class CreateProductController implements Initializable {
    @Setter
    Stage stage;
    @Setter
    Scene parentScene;
    @Setter
    ProdManController prodManController;
    @Setter
    ProductService productService;

    @FXML private TextField tfName;
    @FXML private ComboBox<Uom> cbUom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CreateProductController: initialize");
        try {
            // Populate the dropdown with your newly updated PHP & USD currency profiles
            Uom[] uoms = (Uom[]) UomService.getService().getUoms();
            cbUom.getItems().clear();
            cbUom.getItems().addAll(uoms);
        } catch (Exception ex) {
            System.out.println("CreateProductController Init Error: " + ex.getMessage());
        }
    }

    @FXML
    public void onSubmit(ActionEvent actionEvent) throws Exception {
        Product product = new Product();
        product.setName(tfName.getText().trim());
        product.setDescription("Active Banking Account");

        // DYNAMIC FIX: Capture what the admin actually highlighted in the currency list combo box!
        Uom selectedUom = cbUom.getSelectionModel().getSelectedItem();
        if (selectedUom != null) {
            product.setUomId(selectedUom.getId());
            product.setUomName(selectedUom.getName());
        } else {
            // Fallback safety settings to catch the entry we updated in Postman (ID #1)
            product.setUomId(1);
            product.setUomName("PHP (Philippine Peso)");
        }

        try {
            // Send request payload out to your Spring service backend repository layer
            product = productService.create(product);

            // Force the main dashboard view container to refresh its directory list frame rows
            if (prodManController != null) {
                prodManController.refresh();
            }

            onBack(actionEvent);
        } catch (Exception ex) {
            System.out.println("CreateProductController:onSubmit Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        System.out.println("CreateProductController:onBack");
        if (stage != null && parentScene != null) {
            stage.setScene(parentScene);
            stage.setTitle("Apex Trust Bank - Administration Console");
        }
    }

    public void clearControlTexts() {
        tfName.setText("");
        cbUom.getSelectionModel().clearSelection();
    }
}