package com.danimaniarqsoft.brain.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

  public static void main(String[] args) throws Exception {
    LOGGER.info("main");

    launch(args);
  }

  public void start(Stage stage) throws Exception {
    LOGGER.info("Starting Brain");
    String fxmlFile = "/fxml/main.fxml";
    FXMLLoader loader = new FXMLLoader();
    Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
    Scene scene = new Scene(rootNode, 400, 200);
    scene.getStylesheets().add("/styles/styles.css");
    stage.setTitle("Brain");
    stage.setScene(scene);
    stage.show();
  }
}
