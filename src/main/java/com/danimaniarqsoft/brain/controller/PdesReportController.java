package com.danimaniarqsoft.brain.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.danimaniarqsoft.brain.main.WeekReportMain;
import com.danimaniarqsoft.brain.pdes.exceptions.ReportException;
import com.danimaniarqsoft.brain.pdes.model.TipoReporte;
import com.danimaniarqsoft.brain.tasks.GenerateReportTask;
import com.danimaniarqsoft.brain.util.Constants;
import com.danimaniarqsoft.brain.util.PropertyFileUtils;
import com.danimaniarqsoft.brain.util.UrlPd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PdesReportController implements Initializable, ControlledScreen {

	ScreensController myController;
	@FXML
	private Label projectName;
	@FXML
	private Label port;
	@FXML
	private Label messageLabel;
	@FXML
	private Button btnReportId;
	@FXML
	private Button btnSaveProperties;
	@FXML
	private ComboBox<TipoReporte> cbxTipoReportId;
	@FXML
	private ObservableList<TipoReporte> tipoReportList = FXCollections.observableArrayList();

	private UrlPd urlPd;

	public void goPropertiesScreen(ActionEvent event) {
		myController.loadScreen(WeekReportMain.PROPERTIES, WeekReportMain.PROPERTIES_FILE);
		myController.setScreen(WeekReportMain.PROPERTIES);
	}

	public static Popup createPopup(final String message) {
		final Popup popup = new Popup();
		popup.setAutoFix(true);
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		Label label = new Label(message);
		label.setOnMouseReleased(handle -> popup.hide());
		label.getStyleClass().add("popup");
		popup.getContent().add(label);
		return popup;
	}

	public static void showPopupMessage(final String message, final Stage stage) {
		final Popup popup = createPopup(message);
		popup.setOnShown(handle -> {
			popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
			popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
		});
		popup.show(stage);
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 * 
	 * @throws IOException
	 * @throws ReportException
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tipoReportList.add(new TipoReporte(Constants.FIST_SELECTION, "ES"));
		tipoReportList.add(new TipoReporte(Constants.SECOND_SELECTION, "EN"));
		cbxTipoReportId.setItems(tipoReportList);
		cbxTipoReportId.getSelectionModel().selectFirst();
		urlPd = PropertyFileUtils.loadUrlContext();
		projectName.setText(urlPd.getProjectName());
		port.setText(urlPd.getPort());
		btnReportId.setOnAction((event) -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("PDES Reporter");
			alert.setHeaderText("Reporte Semanal");
			alert.setContentText("Generando...");
			Task<Boolean> task = new GenerateReportTask(cbxTipoReportId.getSelectionModel().getSelectedIndex(), urlPd);
			task.setOnRunning((e) -> alert.show());
			task.setOnSucceeded((e) -> {
				alert.hide();
				alert.close();
			});
			task.setOnFailed((e) -> {
				alert.hide();
				alert.close();
			});
			new Thread(task).start();
			messageLabel.setText("¡Gracias por Utilizar Brain!");
		});
	}
}
