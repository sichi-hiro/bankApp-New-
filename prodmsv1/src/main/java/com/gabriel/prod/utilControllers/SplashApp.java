package com.gabriel.prod.utilControllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SplashApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("SplashApp: Booting System Entry Point");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("role-selection.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/css/splash.css").toExternalForm());

        stage.setTitle("Bank Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}