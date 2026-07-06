package com.gabriel.prod.utilControllers;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// This explicitly shuts off the database url tracking requirement for this layout module
@SpringBootApplication(
        scanBasePackages = "com.gabriel.prod",
        exclude = {DataSourceAutoConfiguration.class}
)
public class ProdManApplication {
    public static void main(String[] args) {
        // 1. Fire up the Spring Boot environment safely
        SpringApplication.run(ProdManApplication.class, args);

        // 2. Launch the JavaFX Application pipeline using SplashApp
        Application.launch(SplashApp.class, args);
    }
}