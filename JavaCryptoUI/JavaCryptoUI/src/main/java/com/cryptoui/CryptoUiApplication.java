package com.cryptoui;

import com.cryptoui.controller.CryptoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX application class for the Crypto UI
 */
public class CryptoUiApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            
            // Get the controller
            CryptoController controller = loader.getController();
            
            // Set up the scene
            Scene scene = new Scene(root);
            
            // Set up the stage
            primaryStage.setTitle("Cryptocurrency Trading Simulator");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            
            // Set up application shutdown handling
            primaryStage.setOnCloseRequest(event -> {
                controller.shutdown();
            });
            
            // Show the stage
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * The main method is only needed for the IDE with limited JavaFX support.
     * It is not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
} 