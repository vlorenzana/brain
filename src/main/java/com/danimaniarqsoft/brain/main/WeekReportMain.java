package com.danimaniarqsoft.brain.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danimaniarqsoft.brain.controller.ScreensController;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * WeekReportMain class execute the main app.
 * 
 * @author Daniel Cortes Pichardo
 */
public class WeekReportMain extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeekReportMain.class);
	private Stage primaryStage;

	public static String main = "main";
	public static String mainFile = "/fxml/Main.fxml";
	public static String properties = "Properties";
	public static String propertiesFile = "/fxml/Properties.fxml";
	public static final String APPLICATION_ICON = "http://cdn1.iconfinder.com/data/icons/Copenhagen/PNG/32/people.png";
	public static final String SPLASH_IMAGE = "http://fxexperience.com/wp-content/uploads/2010/06/logo.png";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		ScreensController mainContainer = new ScreensController();
		mainContainer.loadScreen(WeekReportMain.main, WeekReportMain.mainFile);
		mainContainer.loadScreen(WeekReportMain.properties, WeekReportMain.propertiesFile);
		mainContainer.setScreen(WeekReportMain.main);
		Group root = new Group();
		root.getChildren().addAll(mainContainer);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Monkey Brain");
		scene.getStylesheets().add("/styles/JMetroLightTheme.css");
		primaryStage.show();
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		LOGGER.debug(primaryStage.toString());
		return primaryStage;
	}
}
