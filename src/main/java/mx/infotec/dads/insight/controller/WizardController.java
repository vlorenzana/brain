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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import mx.infotec.dads.insight.pdes.model.Validation;
import mx.infotec.dads.insight.util.Constants;
import static mx.infotec.dads.insight.util.Constants.PAGE_PLANNING;
import static mx.infotec.dads.insight.util.Constants.PAGE_QUALITY;
import static mx.infotec.dads.insight.util.Constants.PAGE_TASK_PRODUCTS;
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
                String id=status.id;
                if(!actionsToUpdate.containsKey(id))
                {
                    actionsToUpdate.put(id, new ArrayList<>());
                }
                actionsToUpdate.get(id).add(status.status);
            }
            
            for(RoleDefinition def : information.definitions)
            {
                if(actionsToUpdate.containsKey(def.id))
                {
                    def.used=true;
                    def.actions=actionsToUpdate.get(def.id);
                }
                else
                {
                    def.used=false;
                }
            }
            
            
        }catch(IOException e){
            ContextUtil.saveExceptionToDisk(e, Constants.FILE_ERROR_TXT, new File("./"));
        }
        
        this.information.save();
        this.webViewPlan.getEngine().reload();
        this.webViewQuality.getEngine().reload();
        this.webViewTasksProducts.getEngine().reload();        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guardar Reporte");
        alert.setContentText("¡El reporte ha sido almacenado!");                                        
        alert.showAndWait();
        List<Validation> validations=getValidations();
        if(!validations.isEmpty())
        {
            Alert alertValidations = new Alert(Alert.AlertType.INFORMATION);
            alertValidations.setTitle("Reporte de validaciones no cumplidas");
            StringBuilder sb=new StringBuilder();
            int index=0;
            for(Validation validation : validations)
            {
                index++;
                sb.append(index).append(". ").append(validation.name);
                sb.append("\r\n");
            }
            alertValidations.setContentText(sb.toString());                                        
            alertValidations.showAndWait();
        }
    }
    public void setDataToFill(ReportInformation information)
    {
        this.information=information;        
    }
    public void setPathToReport(String pathToReport)
    {
        this.pathToReport=pathToReport;
        
    }
    private List<Validation> getValidations()
    {
        List<Validation> getValidations=new ArrayList<>();
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
    private void initTablePlanAction(List<String> initialData,ObservableList model,TableView table)
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
                public void handle(CellEditEvent<PlanAccion, String> t) {
                    ((PlanAccion) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setAccion(t.getNewValue());
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
        information.load();        
        String url_Report="file:///"+pathToReport;
        
        
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
                public void handle(CellEditEvent<RoleStatus, String> t) {
                    ((RoleStatus) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setStatus(t.getNewValue());
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
                public void handle(CellEditEvent<URLProduct, String> t) {
                    ((URLProduct) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setUrl(t.getNewValue());
                }
            }
        );
        
        
        dataPQI.addAll(information.pqi);
        tablePQI.setItems(dataPQI);
        tablePQI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableColumnProductPQI.setCellValueFactory(new PropertyValueFactory<InfoPQI, String>("product"));
        tableColumnProductPQIAction.setCellValueFactory(new PropertyValueFactory<InfoPQI, String>("accion"));
        tableColumnProductPQIAction.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnProductPQIAction.setOnEditCommit(
            new EventHandler<CellEditEvent<InfoPQI, String>>() {
                @Override
                public void handle(CellEditEvent<InfoPQI, String> t) {
                    ((InfoPQI) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setAccion(t.getNewValue());
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
            CheckBox chk=new CheckBox(def.title);
            chk.setId(def.id);
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
        
        
        webViewPlan.getEngine().load(url_Report+"/"+PAGE_PLANNING);
        webViewTasksProducts.getEngine().load(url_Report+"/"+PAGE_TASK_PRODUCTS);
        webViewQuality.getEngine().load(url_Report+"/"+PAGE_QUALITY);
        
        initTablePlanForPlanning();
        
        textAreaIntPlan.setText(this.information.intPlan);
        textAreaComp_Ext.setText(this.information.intComp_Ext);
        textArea_Hitos.setText(this.information.intHitos);        
        
        textArea_IntSize.setText(this.information.int_Size);
        textArea_IntTime.setText(this.information.int_Time);
        textArea_Quality.setText(this.information.int_Quality);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
             
        
        btnAddPlanAccionQuality.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tablePlanAccionQuality.getItems().add(new PlanAccion("Plan de acción"));
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
                tablePlanAccion.getItems().add(new PlanAccion("Plan de acción"));
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
                tablePlanAccionTaks.getItems().add(new PlanAccion("Plan de acción"));
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
    
    private void onCheckBox(CheckBox chk)
    {
        String id=chk.getId();
        for(RoleDefinition def : information.definitions)
        {
            if(id.equals(def.id))
            {
                if(chk.isSelected())
                {
                    // adds the lines
                    def.used=true;
                    for(int i=0;i<def.responsabilities.size();i++)
                    {
                        String resp=def.responsabilities.get(i);
                        String action=def.actions.get(i);
                        RoleStatus roleStatus=new RoleStatus();
                        roleStatus.status=action;
                        roleStatus.resp=resp;
                        roleStatus.id=id;
                        roleStatus.desc=def.title;
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
                        if(id.equals(status.id))
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

    private void validatePlanning(List<Validation> validations) {
        
        if(textAreaIntPlan.getText().trim().isEmpty())
        {
            validations.add(new Validation("Hace falta la Interpretación Plan Calendario."));
        }
        if(dataActionPlanPlanning.isEmpty())
        {
            validations.add(new Validation("No existe ningún plan de acción para el Plan Calendario."));
        }
        if(textAreaComp_Ext.getText().trim().isEmpty())
        {
            validations.add(new Validation("Hace falta la Interpretación de Compromisos Externos."));
        }
        if(textArea_Hitos.getText().trim().isEmpty())
        {
            validations.add(new Validation("Hace falta la Interpretación de Hitos."));
        }
    }

    private void validateTask(List<Validation> validations) {
        
        if(dataTaksActions.isEmpty())
        {
            validations.add(new Validation("No existe ningún plan de acción para Productos y Tareas."));
        }
        for(URLProduct product : dataProductURL)
        {
            if(product.getUrl()==null || product.getUrl().trim().isEmpty())
            {
               validations.add(new Validation("No se describió la URL para el producto "+product.product+".")); 
            }
        }
        if(textArea_IntSize.getText().trim().isEmpty())
        {
            validations.add(new Validation("Hace falta la Interpretación de la Tabla de Tamaños."));
        }
        if(textArea_IntTime.getText().trim().isEmpty())
        {
            validations.add(new Validation("Hace falta la Interpretación de la Tabla de Tiempos en fase."));
        }
    }

    private void validateQuality(List<Validation> validations) {
        if(textArea_Quality.getText().trim().isEmpty())
        {
            validations.add(new Validation("Hace falta la Interpretación del estado de la Calidad."));
        }
        if(dataQualityActions.isEmpty())
        {
            validations.add(new Validation("No existe ningún plan de acción para el estado de la Calidad."));
        }
        for(InfoPQI pqi : dataPQI)
        {
            if(pqi.pqiActual<=0.4)
            {
                validations.add(new Validation("No existe ninguna acción definida por PQI menor o igual a 0.4 para el producto "+pqi.product+"."));
            }
        }
    }

    private void validateRoles(List<Validation> validations) {
        
        if(role_status.isEmpty())
        {
            validations.add(new Validation("¡Cuidado! No se tiene reportado ningún Rol Manager."));
        }
        for(RoleStatus status : role_status)
        {
            if(status.status==null || status.status.trim().isEmpty())
            {
                validations.add(new Validation(status.desc+": No existe ninguna actividad ejercida y estado para la actividad '"+status.resp+"'."));
            }
        }
    }
}
