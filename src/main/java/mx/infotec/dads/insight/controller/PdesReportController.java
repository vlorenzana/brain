package mx.infotec.dads.insight.controller;

import java.io.File;
import mx.infotec.dads.insight.pdes.model.ReportInformation;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import mx.infotec.dads.insight.main.WeekReportMain;
import mx.infotec.dads.insight.pdes.exceptions.ReportException;
import mx.infotec.dads.insight.pdes.model.TipoReporte;
import mx.infotec.dads.insight.tasks.GenerateReportTask;
import mx.infotec.dads.insight.util.Constants;
import mx.infotec.dads.insight.util.PropertyFileUtils;
import mx.infotec.dads.insight.util.UrlPd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import mx.infotec.dads.insight.pdes.service.WeekReportService;
import mx.infotec.dads.insight.pdes.service.context.ReportContext;
import mx.infotec.dads.insight.util.DateUtils;

/**
 * PdesReportController, Controlador principal de la aplicaciÃ³n
 * 
 * @author Daniel Cortes Pichardo
 *
 */
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
    private static Date getDateReport(UrlPd UrlPd) throws IOException,URISyntaxException,ReportException
    {
        return WeekReportService.getStartReport(UrlPd);
    }
    @Override
    public void setScreenParent(ScreensController screenPage) {
	myController = screenPage;
    }
    private void showEditReport(String path) throws IOException
    {
        Stage stage = new Stage();
        WizardController controler=new WizardController();
        FXMLLoader loader=new FXMLLoader(WizardController.class.getResource("/fxml/wizard.fxml"));            
        loader.setController(controler);
        Parent root = loader.load();            
        controler=loader.getController();
        controler.setPathToReport(path);
        controler.setDataToFill(new ReportInformation(path));            
        controler.init();
        stage.setScene(new Scene(root));
        stage.setTitle("Análisis de reporte");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
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
        
	btnReportId.setOnAction(event -> {
            
            
            try
            {
                if (cbxTipoReportId.getSelectionModel().getSelectedIndex() == Constants.FIST_SELECTION) {
                    DateUtils.setEn(false);
                } else {
                    DateUtils.setEn(true);
                }
                Date endReport=getDateReport(urlPd);
                ReportContext context = new ReportContext(endReport);
                File dirToReport=context.getOutputFile();
                File index=new File(dirToReport,"index.html");
                boolean edit=false;
                if(index.exists())
                {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Editar Reporte");
                    String dateToShow=DateUtils.convertDateToString(endReport);
                    alert.setHeaderText("Ya existe un reporte anterior para la semana que termina el "+dateToShow);
                    alert.setContentText("Desea editar el reporte actual creado para el "+dateToShow+"?");                                        
                    ButtonType editButton = new ButtonType("Editar");
                    ButtonType generateButton = new ButtonType("Sobreescribir reporte");
                    ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(editButton, generateButton, buttonTypeCancel);
                    Optional<ButtonType> result = alert.showAndWait();                    
                    if (result.get() == editButton)
                    {
                        edit=true;
                    }
                    else if (result.get() == generateButton)
                    {
                        Alert alertconfirm = new Alert(AlertType.CONFIRMATION);
                        alertconfirm.setTitle("PDES Reporter");
                        alertconfirm.setHeaderText("Reporte Semanal");
                        alertconfirm.setContentText("¡Esta opción borrará el reporte existente!");
                        Optional<ButtonType> resultConfirm =alertconfirm.showAndWait();
                        if (resultConfirm.get() == ButtonType.OK)
                        {
                            edit=false;
                        }
                        else
                        {
                            return;
                        }
                    }
                    else
                    {
                        return;
                    }
                }
            if(edit)
            {
                showEditReport(context.getOutputFile().getAbsolutePath());
                return;
            }
            }
            catch(IOException | URISyntaxException | ReportException e)
            {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("PDES Reporter");
                alert.setHeaderText("Reporte Semanal");
                alert.setContentText("¡No se puede conectar con el Dashboard, favor de verificar si esta abierto e intente de nuevo!");
                alert.showAndWait();
                return;
            }
            
            
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("PDES Reporter");
	    alert.setHeaderText("Reporte Semanal");
	    alert.setContentText("Generando...");
	    Task<Boolean> task = new GenerateReportTask(cbxTipoReportId.getSelectionModel().getSelectedIndex(), urlPd);
	    task.setOnRunning(e -> alert.show());
	    task.setOnSucceeded(e -> {
		alert.hide();
		alert.close();
                String path=((GenerateReportTask)task).getPathToReport();
                try {
                    showEditReport(path);
                } catch (IOException ex) {
                    
                }
                
	    });
	    task.setOnFailed(e -> {
		alert.hide();
		alert.close();
	    });
	    new Thread(task).start();
	    messageLabel.setText("¡Gracias por Utilizar Brain!");
	});
    }
}
