package com.gabriel.prod.utilControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Data;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class SplashController implements Initializable {
    @Setter
    Stage stage;
    @javafx.fxml.FXML
    @Setter
    public ImageView productImage;
    @Setter
    Image image;
    @javafx.fxml.FXML
    private Button btnProceed;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image = new Image(getClass().getResourceAsStream("/images/ItemSpecifications.png"));
        productImage.setImage(image);
    }

    @javafx.fxml.FXML
    public void onProceed(ActionEvent actionEvent) {
        System.out.println("SplashApp: Navigating to Role Selection Portal");
        Node node = ((Node) (actionEvent.getSource()));
        Window window = node.getScene().getWindow();
        Stage currentStage = (Stage) window;
        window.hide();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("role-selection.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

            currentStage.setTitle("Apex Trust Bank - Access Portal");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
