package com.gabriel.prod.utilControllers;

import com.gabriel.prod.model.Product;
import com.gabriel.prod.serviceImpl.ProductService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Data;
import lombok.Setter;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class ProdManController implements Initializable {
    @Setter
    Stage stage;
    @Setter
    Scene createViewScene;
    @Setter
    Scene updateViewScene;
    @Setter
    Scene deleteViewScene;

    public TextField tfId;
    public TextField tfName;
    public TextField tfDesc;
    public ImageView productImage;
    public BorderPane prodman;
    public TextField tfUom;

    public TextField tfBalance;

    Image puffy;
    Image wink;

    @FXML
    public Button createButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button closeButton;

    public static Product product;
    @FXML
    private ListView<Product> lvProducts;

    UpdateProductController updateProductController;
    DeleteProductController deleteProductController;
    CreateProductController createProductController;
    ProductService productService;

    void refresh() throws Exception{
        productService = ProductService.getService();
        Product[] products = productService.getProducts();
        lvProducts.getItems().clear();
        lvProducts.getItems().addAll(products);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ProdManController: initialize");
        disableControls();

        try {
            refresh();
            try {
                puffy = new Image(getClass().getResourceAsStream("/images/puffy.gif"));
                wink = new Image(getClass().getResourceAsStream("/images/wink.gif"));
                productImage.setImage(puffy);
            }
            catch(Exception ex){
                System.out.println("Error with image: " + ex.getMessage());
            }
        }
        catch (Exception ex){
            showErrorDialog("Message: " + ex.getMessage());
        }
    }

    public void disableControls(){
        tfId.editableProperty().set(false);
        tfName.editableProperty().set(false);
        tfDesc.editableProperty().set(false);
        tfUom.editableProperty().set(false);
        tfBalance.editableProperty().set(false);
    }

    public void setControlTexts(Product product){
        tfName.setText(product.getName());
        tfDesc.setText(product.getDescription());
        tfUom.setText(product.getUomName());
        tfBalance.setText(Integer.toString(product.getBalance()));
    }

    public void clearControlTexts(){
        tfId.setText("");
        tfName.setText("");
        tfDesc.setText("");
        tfUom.setText("");
        // Added clean reset loop parameters
        tfBalance.setText("");
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        Product selectedItem = lvProducts.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        try {
            refresh();

            for (Product account : lvProducts.getItems()) {
                if (account.getId() == selectedItem.getId()) {
                    product = account;
                    ProdManController.product = account;
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Directory synchronization failed: " + ex.getMessage());
            product = selectedItem;
        }

        tfId.setText(Integer.toString(product.getId()));
        setControlTexts(product);
        System.out.println("Admin clicked and synchronized: " + product);
    }

    public void onCreate(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onNewProduct");
        Scene currentScene;

        if (actionEvent.getSource() instanceof Node) {
            currentScene = ((Node) actionEvent.getSource()).getScene();
        } else if (actionEvent.getSource() instanceof MenuItem) {
            currentScene = prodman.getScene();
        } else {
            return;
        }

        Window activeWindow = currentScene.getWindow();
        if (activeWindow instanceof Stage) {
            this.stage = (Stage) activeWindow;
        }

        try {
            if (createViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-product.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                createProductController = fxmlLoader.getController();

                createProductController.setStage(this.stage);
                createProductController.setParentScene(currentScene);
                createProductController.setProductService(this.productService);
                createProductController.setProdManController(this);

                createViewScene = new Scene(root, 750, 550);
                createViewScene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());
            }

            if (this.stage != null) {
                this.stage.setTitle("Banking App - Open New Account");
                this.stage.setScene(createViewScene);
                this.stage.show();
            } else {
                System.out.println("Error: Main Stage context could not be resolved.");
            }

            if (createProductController != null) {
                createProductController.clearControlTexts();
            }
            clearControlTexts();

        } catch(Exception ex) {
            System.out.println("ProdmanController onCreate Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void onUpdate(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onUpdate ");
        Scene currentScene = prodman.getScene();

        if (this.stage == null) {
            this.stage = (Stage) currentScene.getWindow();
        }

        try {
            if (product == null) {
                showErrorDialog("Please select an active account from the directory list first.");
                return;
            }

            if(updateViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("update-product.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                updateProductController = fxmlLoader.getController();
                updateProductController.setController(this);
                updateProductController.setStage(this.stage);
                updateProductController.setParentScene(currentScene);
                updateViewScene = new Scene(root, 750, 550);
                updateViewScene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());
            }

            updateProductController.refresh();
            stage.setTitle("Banking App - Edit Account");
            stage.setScene(updateViewScene);
            stage.show();
        }
        catch(Exception ex){
            System.out.println("ProdmanController update navigation failed: "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onDelete ");
        Scene currentScene = prodman.getScene();

        if (this.stage == null) {
            this.stage = (Stage) currentScene.getWindow();
        }

        try {
            if (product == null) {
                showErrorDialog("Please select an active account from the directory list first.");
                return;
            }

            if(deleteViewScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("delete-product.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                deleteProductController = fxmlLoader.getController();
                deleteProductController.setController(this);
                deleteProductController.setStage(this.stage);
                deleteProductController.setParentScene(currentScene);
                deleteViewScene = new Scene(root, 750, 550);
                deleteViewScene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());
            }

            deleteProductController.refresh();
            stage.setTitle("Banking App - Close Account");
            stage.setScene(deleteViewScene);
            stage.show();
        }
        catch(Exception ex){
            System.out.println("ProdmanController delete navigation failed: "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void onAbout(ActionEvent actionEvent) {
        System.out.println("ProdmanController:onAbout");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Product Management System");
        alert.setContentText("This is a Product Management Application template developed by Rijai Consulting.\n\nModified by Chloie Broñola");

        alert.showAndWait();
    }

    @FXML
    public void onClose(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Return to Role Selection Screen?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Log Out Confirmation");
        alert.setHeaderText("Disconnect from Administration Console");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("role-selection.fxml"));
                Parent roleSelectionRoot = fxmlLoader.load();

                Scene roleSelectionScene = new Scene(roleSelectionRoot, 750, 550);

                if (getClass().getResource("/css/splash.css") != null) {
                    roleSelectionScene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());
                }

                if (this.stage == null) {
                    this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                }

                this.stage.setTitle("Banking App - Role Selection Portal");
                this.stage.setScene(roleSelectionScene);
                this.stage.show();

            } catch (Exception ex) {
                System.out.println("Error navigating back to Role Selection: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void addItem(Product product){
        lvProducts.getItems().add(product);
    }
}