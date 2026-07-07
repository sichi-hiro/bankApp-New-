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
public class UpdateProductController implements Initializable {
    @Setter
    Stage stage;
    @Setter
    Scene parentScene;
    @Setter
    ProdManController controller;

    @FXML private TextField tfName;
    @FXML private ComboBox<Uom> cbUom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("UpdateProductController: initialize");
        try {
            refresh();
        }
        catch(Exception ex){
            System.out.println("UpdateProductController Initialization Error: " + ex.getMessage());
        }
    }

    public void refresh() throws Exception {
        Product product = ProdManController.product;
        if (product != null) {
            tfName.setText(product.getName());

            cbUom.getItems().clear();
            Uom[] uoms = (Uom[]) UomService.getService().getUoms();
            cbUom.getItems().addAll(uoms);

            cbUom.getSelectionModel().select(UomService.getService().getUom(product.getUomId()));
        }
    }

    @FXML
    public void onSubmit(ActionEvent actionEvent) {
        Product target = ProdManController.product;
        if (target == null) return;

        target.setName(tfName.getText().trim());
        target.setDescription("Active Banking Account");

        Uom uom = cbUom.getSelectionModel().getSelectedItem();
        if (uom != null) {
            target.setUomId(uom.getId());
            target.setUomName(uom.getName());
        }

        try {
            target = ProductService.getService().update(target);

            controller.refresh();
            controller.setControlTexts(target);
            onBack(actionEvent);
        }
        catch(Exception ex) {
            System.out.println("UpdateProductController:onSubmit Error: " + ex.getMessage());
        }
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        System.out.println("UpdateProductController:onBack");
        // Seamlessly switch back without using window.hide() to prevent accidental shutdown exits
        if (stage != null && parentScene != null) {
            stage.setScene(parentScene);
            stage.setTitle("Bank Application - Administration Console");
        }
    }
}