/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.controller;

import java.io.File;
import java.io.IOException;
import mx.infotec.dads.insight.pdes.model.ReportInformation;
import mx.infotec.dads.insight.pdes.model.URLProduct;
import mx.infotec.dads.insight.pdes.model.RoleStatus;
import mx.infotec.dads.insight.pdes.model.RoleDefinition;
import mx.infotec.dads.insight.pdes.model.PlanAccion;
import mx.infotec.dads.insight.pdes.model.InfoPQI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mx.infotec.dads.insight.util.Constants;
import static mx.infotec.dads.insight.util.Constants.PAGE_PLANNING;
import static mx.infotec.dads.insight.util.Constants.PAGE_QUALITY;
import static mx.infotec.dads.insight.util.Constants.PAGE_TASK_PRODUCTS;
import static mx.infotec.dads.insight.util.Constants.PLAN_DE_ACCION;
import mx.infotec.dads.insight.util.ContextUtil;



/**
 * FXML Controller class
 *
 * @author victor.lorenzana
 */
public class WizardController implements Initializable {

            
    @FXML
    private TableColumn columnRoleDesc;
    
    @FXML
    private TableColumn columnResp;
    
    @FXML
    private TableColumn columnStatus;
    
    @FXML
    private TableView tableRoles;
    
    @FXML
    private TableView tablePlanAccionQuality;
    
    @FXML
    private Button btnDeletePlanAccionQuality;
    
    @FXML
    private Button btnAddPlanAccionQuality;
    
    
    
    
    @FXML
    private GridPane gridRoles;
    
    @FXML
    private WebView webViewPlan;
    
    @FXML
    private WebView webViewTasksProducts;
    
    @FXML
    private WebView webViewQuality;
    
    
    @FXML
    private Button btnAddPlanAccion;
    
    @FXML
    private Button save;
    
    
    @FXML
    private Button btnCerrar;
    
    
    
    @FXML
    private Button btnDeletePlanAccion;
    
    @FXML
    private Button btnDeleteRowActionTaks;
    
    @FXML
    private Button btnAddRowActionTaks;
    
            
    @FXML
    private TableView tablePlanAccionTaks;        
    
            
    @FXML
    private TableView tablePlanAccion;        
    
    @FXML
    private TableView tablePQI; 
    
    @FXML
    private TableView tableProductsURL; 
    
            
    
    
    @FXML
    private TableColumn columnProduct;
     @FXML
    private TableColumn columnURL;
    
    @FXML
    private TableColumn tableColumnProductPQI;
    
    @FXML
    private TableColumn tableColumnProductPQIAction;
    
    @FXML
    private TextArea textAreaIntPlan;
    
    @FXML
    private TextArea textAreaComp_Ext;
    
    @FXML
    private TextArea textArea_Hitos;
    
    
    
    @FXML
    private TextArea textArea_IntSize;
    
    
    @FXML
    private TextArea textArea_IntTime;
    
    @FXML
    private TextArea textArea_Quality;
    
    private String pathToReport;
    
    private ReportInformation information;
    private final ObservableList<PlanAccion> dataActionPlanPlanning =
            FXCollections.observableArrayList();
    
     private final ObservableList<PlanAccion> dataTaksActions =
            FXCollections.observableArrayList();
     
    private final ObservableList<PlanAccion> dataQualityActions =
            FXCollections.observableArrayList();
    
    private final ObservableList<InfoPQI> dataPQI =
            FXCollections.observableArrayList();
    
    private final ObservableList<URLProduct> dataProductURL =
            FXCollections.observableArrayList();
    
    private final ObservableList<RoleStatus> role_status =
            FXCollections.observableArrayList();
    
    
    public WizardController()
    {
        
    }
    private void save()
    {
        this.information.intPlan=this.textAreaIntPlan.getText();
        this.information.intComp_Ext=this.textAreaComp_Ext.getText();
        this.information.intHitos=this.textArea_Hitos.getText();
        this.information.actions.clear();
        for(PlanAccion action : dataActionPlanPlanning)
        {
            this.information.actions.add(action.getAccion());
        }
        this.information.actionsTaks.clear();
        for(PlanAccion action : dataTaksActions)
        {
            this.information.actionsTaks.add(action.getAccion());
        }
        
        this.information.url_products.clear();
        for(URLProduct info : this.dataProductURL)
        {
            this.information.url_products.add(info);
        }
        this.information.actionsQuality.clear();
        for(PlanAccion action : dataQualityActions)
        {
            this.information.actionsQuality.add(action.getAccion());
        }
        
        this.information.int_Quality=this.textArea_Quality.getText();
        this.information.pqi.clear();
        for(InfoPQI info : this.dataPQI)
        {
            this.information.pqi.add(info);
        }      
        
        
        this.information.int_Size=this.textArea_IntSize.getText();
        this.information.int_Time=this.textArea_IntTime.getText();
        
        try
        {
            information.loadRoleDefinition();

            Map<String,List<String>> actionsToUpdate=new HashMap<>();
            for(RoleStatus status : role_status)
            {
                final String id=status.getId();
                if(!actionsToUpdate.containsKey(id))
                {
                    actionsToUpdate.put(id, new ArrayList<>());
                }
                actionsToUpdate.get(id).add(status.getStatus());
            }
            
            for(RoleDefinition def : information.definitions)
            {
                if(actionsToUpdate.containsKey(def.getId()))
                {
                    def.setUsed(true);
                    def.setActions(actionsToUpdate.get(def.getId()));
                }
                else
                {
                    def.setUsed(false);
                }
            }
            
            
        }catch(IOException e){
            ContextUtil.saveExceptionToDisk(e, Constants.FILE_ERROR_TXT, new File("."+File.separator));
        }
        
        this.information.save();
        this.webViewPlan.getEngine().reload();
        this.webViewQuality.getEngine().reload();
        this.webViewTasksProducts.getEngine().reload();        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guardar Reporte");
        alert.setContentText("¡El reporte ha sido almacenado!");                                        
        alert.showAndWait();
        List<String> validations=getValidations();
        if(!validations.isEmpty())
        {
            Alert alertValidations = new Alert(Alert.AlertType.INFORMATION);
            alertValidations.setTitle("Reporte de validaciones no cumplidas");
            StringBuilder stringBuilder=new StringBuilder();
            int index=0;
            for(String validation : validations)
            {
                index++;
                stringBuilder.append(index).append(". ").append(validation);
                stringBuilder.append("\r\n");
            }
            alertValidations.setContentText(stringBuilder.toString());                                        
            alertValidations.showAndWait();
        }
    }
    public void setDataToFill(final ReportInformation information)
    {
        this.information=information;        
    }
    public void setPathToReport(final String pathToReport)
    {
        this.pathToReport=pathToReport;
        
    }
    private List<String> getValidations()
    {
        List<String> getValidations=new ArrayList<>();
        validatePlanning(getValidations);
        validateTask(getValidations);
        validateQuality(getValidations);
        validateRoles(getValidations);
        return getValidations;
    }
    
    public void onSave()
    {
        save();
    }
    private void initTablePlanForPlanning()
    {
        initTablePlanAction(this.information.actions, dataActionPlanPlanning, tablePlanAccion);
    }
    private void initTablePlanQuality()
    {
        initTablePlanAction(this.information.actionsQuality, dataQualityActions, tablePlanAccionQuality);
    }
    private void initTablePlanTaks()
    {
        initTablePlanAction(this.information.actionsTaks, dataTaksActions, tablePlanAccionTaks);
    }
    private void initTablePlanAction(final List<String> initialData,final ObservableList model,final TableView table)
    {
        for(String action : initialData)
        {
            model.add(new PlanAccion(action.replace("\r\n", "").trim()));
        }
        table.setEditable(true);
        table.setItems(model);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn column=((TableColumn)table.getColumns().get(0));
        column.setCellValueFactory(new PropertyValueFactory<>("accion"));        
        column.setCellFactory(TextFieldTableCell.forTableColumn());        
        column.setOnEditCommit(
            new EventHandler<CellEditEvent<PlanAccion, String>>() {
                @Override
                public void handle(CellEditEvent<PlanAccion, String> cell) {
                    ((PlanAccion) cell.getTableView().getItems().get(
                        cell.getTablePosition().getRow())
                        ).setAccion(cell.getNewValue());
                }
            }
        );
    }
    public void init()
    {
        save.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onSave();
        }
        });
        
        btnCerrar.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cerrar ventana");
                alert.setContentText("¿Desea cerrar la ventana sin guardar los cambios?");                                                        
                Optional<ButtonType> result = alert.showAndWait();  
                if(result.get()==ButtonType.OK)
                {
                    Stage stage = (Stage) btnCerrar.getScene().getWindow();                
                    stage.close();
                }
        }
        });
        
        information.load();        
        String urlReport="file:///"+pathToReport;
        
        
        initTablePlanQuality();
        initTablePlanTaks();
        
        
        
        
        
        tableRoles.setItems(role_status);
        tableRoles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnResp.setCellValueFactory(new PropertyValueFactory<>("resp"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnRoleDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        columnStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        columnStatus.setOnEditCommit(
            new EventHandler<CellEditEvent<RoleStatus, String>>() {
                @Override
                public void handle(CellEditEvent<RoleStatus, String> cell) {                   
                    ((RoleStatus) cell.getTableView().getItems().get(
                        cell.getTablePosition().getRow())
                        ).setStatus(cell.getNewValue());
                }
            }
        );
        
        dataProductURL.addAll(information.url_products);
        tableProductsURL.setItems(dataProductURL);
        tableProductsURL.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        columnURL.setCellValueFactory(new PropertyValueFactory<>("url"));
        columnURL.setCellFactory(TextFieldTableCell.forTableColumn());
        columnURL.setOnEditCommit(
            new EventHandler<CellEditEvent<URLProduct, String>>() {
                @Override
                public void handle(CellEditEvent<URLProduct, String> cell) {
                    ((URLProduct) cell.getTableView().getItems().get(
                        cell.getTablePosition().getRow())
                        ).setUrl(cell.getNewValue());
                }
            }
        );
        
        
        dataPQI.addAll(information.pqi);
        tablePQI.setItems(dataPQI);
        tablePQI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableColumnProductPQI.setCellValueFactory(new PropertyValueFactory<>("product"));
        tableColumnProductPQIAction.setCellValueFactory(new PropertyValueFactory<>("accion"));
        tableColumnProductPQIAction.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnProductPQIAction.setOnEditCommit(
            new EventHandler<CellEditEvent<InfoPQI, String>>() {
                @Override
                public void handle(CellEditEvent<InfoPQI, String> cell) {
                    ((InfoPQI) cell.getTableView().getItems().get(
                        cell.getTablePosition().getRow())
                        ).setAccion(cell.getNewValue());
                }
            }
        );
         
        int row=-1;
        int col=0;
        for(RoleDefinition def : information.definitions)
        {
            row++;
            if(row>2)
            {
                row=0;
                col++;
            }
            CheckBox chk=new CheckBox(def.getTitle());
            chk.setId(def.getId());
            gridRoles.add(chk, col, row);        
            chk.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CheckBox chk=(CheckBox)event.getSource();
                    onCheckBox(chk);                    
                }
            });
            if(def.used)
            {                
                chk.setSelected(true);
                onCheckBox(chk);                
            }
            
        }
        
        
        webViewPlan.getEngine().load(urlReport+File.separator+PAGE_PLANNING);
        webViewTasksProducts.getEngine().load(urlReport+File.separator+PAGE_TASK_PRODUCTS);
        webViewQuality.getEngine().load(urlReport+File.separator+PAGE_QUALITY);
        
        initTablePlanForPlanning();
        
        textAreaIntPlan.setText(this.information.intPlan);
        textAreaComp_Ext.setText(this.information.intComp_Ext);
        textArea_Hitos.setText(this.information.intHitos);        
        
        textArea_IntSize.setText(this.information.int_Size);
        textArea_IntTime.setText(this.information.int_Time);
        textArea_Quality.setText(this.information.int_Quality);
    }
    
    
    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
             
        
        btnAddPlanAccionQuality.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tablePlanAccionQuality.getItems().add(new PlanAccion(PLAN_DE_ACCION));
        }
        });  
        btnDeletePlanAccionQuality.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index=tablePlanAccionQuality.getSelectionModel().getSelectedIndex();
                if(index>-1)
                {
                    tablePlanAccionQuality.getItems().remove(index);
                            
                }
        }
        }); 
       
        btnAddPlanAccion.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tablePlanAccion.getItems().add(new PlanAccion(PLAN_DE_ACCION));
        }
        });  
        btnDeletePlanAccion.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index=tablePlanAccion.getSelectionModel().getSelectedIndex();
                if(index>-1)
                {
                    tablePlanAccion.getItems().remove(index);
                            
                }
        }
        }); 
        
        btnAddRowActionTaks.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tablePlanAccionTaks.getItems().add(new PlanAccion(PLAN_DE_ACCION));
        }
        });  
        btnDeleteRowActionTaks.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index=tablePlanAccionTaks.getSelectionModel().getSelectedIndex();
                if(index>-1)
                {
                    tablePlanAccionTaks.getItems().remove(index);
                            
                }
        }
        }); 
        
    }    
    
    
    private void onCheckBox(final CheckBox checkbox)
    {
        String id=checkbox.getId();
        for(RoleDefinition def : information.definitions)
        {
            if(id.equals(def.getId()))
            {
                if(checkbox.isSelected())
                {
                    // adds the lines
                    def.used=true;
                    for(int index=0;index<def.getResponsabilities().size();index++)
                    {
                        String responsability=def.getResponsabilities().get(index);
                        String action=def.getActions().get(index);
                        RoleStatus roleStatus=new RoleStatus();
                        roleStatus.setStatus(action);
                        roleStatus.setResponsability(responsability);
                        roleStatus.setId(id);
                        roleStatus.setDescription(def.getTitle());
                        role_status.add(roleStatus);

                    }
                }
                else
                {
                    def.used=false;
                    // removes the lines
                    List<RoleStatus> delete=new ArrayList<>();
                    for(RoleStatus status : role_status)
                    {
                        if(id.equals(status.getId()))
                        {
                            delete.add(status);
                        }
                    }
                    for(RoleStatus status : delete)
                    {
                        role_status.remove(status);
                    }
                }
            }
        }        
    }

    private void validatePlanning(final List<String> validations) {
        
        if(textAreaIntPlan.getText().trim().isEmpty())
        {
            validations.add("Hace falta la Interpretación plan de calendario.");
        }
        if(dataActionPlanPlanning.isEmpty())
        {
            validations.add("No existe ningún plan de acción para el plan de calendario.");
        }
        if(textAreaComp_Ext.getText().trim().isEmpty())
        {
            validations.add("Hace falta la interpretación de compromisos externos.");
        }
        if(textArea_Hitos.getText().trim().isEmpty())
        {
            validations.add("Hace falta la interpretación de hitos.");
        }
    }

    private void validateTask(final List<String> validations) {
        
        if(dataTaksActions.isEmpty())
        {
            validations.add("No existe ningún plan de acción para productos y tareas.");
        }
        for(URLProduct product : dataProductURL)
        {
            if(product.getUrl()==null || product.getUrl().trim().isEmpty())
            {
               validations.add("No se describió la URL para el producto "+product.product+"."); 
            }
        }
        if(textArea_IntSize.getText().trim().isEmpty())
        {
            validations.add("Hace falta la interpretación de la tabla de tamaños.");
        }
        if(textArea_IntTime.getText().trim().isEmpty())
        {
            validations.add("Hace falta la interpretación de la tabla de tiempos en fase.");
        }
    }

    private void validateQuality(final List<String> validations) {
        if(textArea_Quality.getText().trim().isEmpty())
        {
            validations.add("Hace falta la interpretación del estado de la calidad.");
        }
        if(dataQualityActions.isEmpty())
        {
            validations.add("No existe ningún plan de acción para el estado de la calidad.");
        }
        for(InfoPQI pqi : dataPQI)
        {
            if(pqi.getPqiActual()<=0.4)
            {
                validations.add("No existe ninguna acción definida por PQI menor o igual a 0.4 para el producto "+pqi.getProduct()+".");
            }
        }
    }

    private void validateRoles(final List<String> validations) {
        
        if(role_status.isEmpty())
        {
            validations.add("¡Cuidado! No se tiene reportado ningún rol manager.");
        }
        for(RoleStatus status : role_status)
        {
            if(status.getStatus()==null || status.getStatus().trim().isEmpty())
            {
                validations.add(status.getDescription()+": No existe ninguna actividad ejercida y estado para la actividad '"+status.getResponsability()+"'.");
            }
        }
    }
}