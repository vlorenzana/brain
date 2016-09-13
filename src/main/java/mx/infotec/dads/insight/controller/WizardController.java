/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.controller;

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
import static mx.infotec.dads.insight.util.Constants.PAGE_PLANNING;
import static mx.infotec.dads.insight.util.Constants.PAGE_QUALITY;
import static mx.infotec.dads.insight.util.Constants.PAGE_TASK_PRODUCTS;



/**
 * FXML Controller class
 *
 * @author victor.lorenzana
 */
public class WizardController implements Initializable {

    
    @FXML
    private TableColumn columnPlanAccionTaks;
    
            
    @FXML
    private TableColumn columnRoleDesc;
    
    @FXML
    private TableColumn columnResp;
    
    @FXML
    private TableColumn columnStatus;
    
    @FXML
    private TableView tableRoles;
    
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
    private TableColumn columnPlanAccion;
    
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
    private final ObservableList<PlanAccion> data =
            FXCollections.observableArrayList();
    
     private final ObservableList<PlanAccion> dataTaksActions =
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
        for(PlanAccion action : data)
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
                    actionsToUpdate.put(id, new ArrayList<String>());
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
            
            
        }catch(IOException e){}
        
        this.information.save();
        this.webViewPlan.getEngine().reload();
        this.webViewQuality.getEngine().reload();
        this.webViewTasksProducts.getEngine().reload();
    }
    public void setDataToFill(ReportInformation information)
    {
        this.information=information;        
    }
    public void setPathToReport(String pathToReport)
    {
        this.pathToReport=pathToReport;
        
    }
    private boolean noHasFinished()
    {
        return false;
    }
    
    public void onSave()
    {
        save();
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
        tablePlanAccionTaks.setEditable(true);        
        for(String action : information.actionsTaks)
        {
            dataTaksActions.add(new PlanAccion(action.replace("\r\n", "").trim()));
        }
        tablePlanAccionTaks.setItems(dataTaksActions);
        tablePlanAccionTaks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnPlanAccionTaks.setCellValueFactory(new PropertyValueFactory<PlanAccion, String>("accion"));        
        columnPlanAccionTaks.setCellFactory(TextFieldTableCell.forTableColumn());        
        columnPlanAccionTaks.setOnEditCommit(
            new EventHandler<CellEditEvent<PlanAccion, String>>() {
                @Override
                public void handle(CellEditEvent<PlanAccion, String> t) {
                    ((PlanAccion) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setAccion(t.getNewValue());
                }
            }
        );
        
        
        tableRoles.setItems(role_status);
        tableRoles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnResp.setCellValueFactory(new PropertyValueFactory<RoleStatus, String>("resp"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<RoleStatus, String>("status"));
        columnRoleDesc.setCellValueFactory(new PropertyValueFactory<RoleStatus, String>("desc"));
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
        columnProduct.setCellValueFactory(new PropertyValueFactory<URLProduct, String>("product"));
        columnURL.setCellValueFactory(new PropertyValueFactory<URLProduct, String>("url"));
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
        tablePlanAccion.setEditable(true);        
        tablePlanAccion.setItems(data);
        tablePlanAccion.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        columnPlanAccion.setOnEditCommit(
            new EventHandler<CellEditEvent<InfoPQI, String>>() {
                @Override
                public void handle(CellEditEvent<InfoPQI, String> t) {
                    ((InfoPQI) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setAccion(t.getNewValue());
                }
            }
        );
        
        
        
        
        columnPlanAccion.setCellValueFactory(new PropertyValueFactory<PlanAccion, String>("accion"));        
        columnPlanAccion.setCellFactory(TextFieldTableCell.forTableColumn());        
        columnPlanAccion.setOnEditCommit(
            new EventHandler<CellEditEvent<PlanAccion, String>>() {
                @Override
                public void handle(CellEditEvent<PlanAccion, String> t) {
                    ((PlanAccion) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setAccion(t.getNewValue());
                }
            }
        );
        
        
        textAreaIntPlan.setText(this.information.intPlan);
        textAreaComp_Ext.setText(this.information.intComp_Ext);
        textArea_Hitos.setText(this.information.intHitos);
        for(String action : information.actions)
        {
            data.add(new PlanAccion(action.replace("\r\n", "").trim()));
        }
        
        textArea_IntSize.setText(this.information.int_Size);
        textArea_IntTime.setText(this.information.int_Time);
        textArea_Quality.setText(this.information.int_Quality);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               
       
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
    
    
    

    
    
}
