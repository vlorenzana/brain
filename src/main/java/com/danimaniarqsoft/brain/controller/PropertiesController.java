package com.danimaniarqsoft.brain.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danimaniarqsoft.brain.main.WeekReportMain;
import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;
import com.danimaniarqsoft.brain.util.Constants;
import com.danimaniarqsoft.brain.util.PropertyFileUtils;
import com.danimaniarqsoft.brain.util.UrlPd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PropertiesController implements Initializable, ControlledScreen {
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesController.class);
  ScreensController           myController;
  @FXML
  private TextField           projectName;
  @FXML
  private TextField           port;
  @FXML
  private Label               messageLabel;
  @FXML
  private Button              btnReportId;
  @FXML
  private Button              btnSaveProperties;
  private UrlPd               urlPd;

  @FXML
  public void saveProperties(ActionEvent event) {
    Properties props = new Properties();
    props.setProperty(Constants.PROPERTY_PORT, port.getText());
    props.setProperty(Constants.PROPERTY_PROJECT, projectName.getText());
    PropertyFileUtils.saveProperties(props);
    messageLabel.setText("Datos Guardados!");
  }

  public void goMainScreen(ActionEvent event) {
    myController.loadScreen(WeekReportMain.main, WeekReportMain.mainFile);
    myController.setScreen(WeekReportMain.main);
  }

  @Override
  public void setScreenParent(ScreensController screenPage) {
    myController = screenPage;
  }

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   * 
   * @throws IOException
   * @throws ReportException
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    LOGGER.debug("initialize PropertiesController");
    urlPd = PropertyFileUtils.loadUrlContext();
    projectName.setText(urlPd.getProjectName());
    port.setText(urlPd.getPort());
  }
}
