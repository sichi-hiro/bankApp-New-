package com.gabriel.prod.utilControllers;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "com.gabriel.prod",
        exclude = {DataSourceAutoConfiguration.class}
)
public class ProdManApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProdManApplication.class, args);

        Application.launch(SplashApp.class, args);
    }
}