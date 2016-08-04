/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.infotec.dads.insight.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author victor.lorenzana
 */
public class WizardController implements Initializable {

    @FXML
    private Button save;
    
    @FXML
    private TreeView<String> treeview;
    
    @FXML
    private BorderPane panelContainerDocument;
    
    private Parent panelStatus=null;
    
    private ImageView statusNode=new ImageView(new Image(getClass().getResourceAsStream("/images/doc.png")));
    public WizardController()
    {
        
    }
    @FXML
    private void onSaveAction(ActionEvent e)
    {
        Stage stage = (Stage) save.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        try
        {
            panelStatus = FXMLLoader.load(this.getClass().getResource("/fxml/panelStatus.fxml"));
        }
        catch(Exception e)
        {
            
        }
        ImageView rootNode=new ImageView(new Image(getClass().getResourceAsStream("/images/doc.png")));
        TreeItem<String> rootItem = new TreeItem<String> ("Reporte", rootNode);
        rootItem.setExpanded(true);
        treeview.setRoot(rootItem);
                
        
        TreeItem<String> stausReport = new TreeItem<String> ("Estado del Reporte", statusNode);
        rootItem.getChildren().add(stausReport);
        
        treeview.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {            
                if(mouseEvent.getClickCount() == 1)
                {
                    TreeItem<String> item = treeview.getSelectionModel().getSelectedItem();
                    if(item.equals(stausReport))
                    {
                        
                        panelContainerDocument.setCenter(panelStatus);                        
                    }
                }
            }
        });
        
    }    
    private void mouseEventHandler(MouseEvent e)
    {
        Node node=e.getPickResult().getIntersectedNode();
    }

    
    
}
