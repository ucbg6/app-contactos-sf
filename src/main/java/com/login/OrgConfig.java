/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import java.io.IOException;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author ucb40
 */
public class OrgConfig extends VBox{
    ObservableList<SFOrg> orgs;
    
    @FXML ListView orgList;
    @FXML Button nuevo, edit, delete, importj, exportj, cancel, save;
    Stage myStage;
    
    public OrgConfig(ObservableList<SFOrg> myOrgs, Stage stage){
        orgs = myOrgs;
        this.myStage = stage;
        initialize();
    }
    
    public ObservableList<SFOrg> getOrgs(){
        return orgs;
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/OrgConfigView.fxml");
        // System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        orgList.setItems(orgs);
        
        nuevo.setOnAction(e -> {
            openNewOrg();
        });
        
        orgList.getSelectionModel().selectedItemProperty().addListener(cl -> {
            SFOrg selected = (SFOrg) orgList.getSelectionModel().getSelectedItem();
           
            edit.setDisable(selected == null);
            delete.setDisable(selected == null);
        });
        
        orgList.setOnMouseClicked(eh -> {
            if (eh.getClickCount() > 1){
                SFOrg selected = (SFOrg) orgList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openEditOrg(selected);
                }
            }
            
        });
        
        edit.setOnAction(eh -> {
            SFOrg selected = (SFOrg) orgList.getSelectionModel().getSelectedItem();
            if (selected != null){
                openEditOrg(selected);
            }
        });
        
        cancel.setOnAction(e -> {
            myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
    
    public void openEditOrg(SFOrg org){
        Stage stage = new Stage();
        SFOrgDialog dialog = new SFOrgDialog(org, orgs, stage);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(myStage);
        stage.setScene(scene);
        stage.setTitle("Nueva org");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            SFOrg newOrg = dialog.getOrg();
            if (newOrg != null){
                orgs.set(newOrg.id,newOrg);
            } else {
                orgs.remove(newOrg);
            }
            
        });
    }
    
    public void openNewOrg(){
        Stage stage = new Stage();
        SFOrgDialog dialog = new SFOrgDialog(null, orgs, stage);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(myStage);
        stage.setScene(scene);
        stage.setTitle("Nueva org");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            SFOrg newOrg = dialog.getOrg();
            if (newOrg != null){
                orgs.add(newOrg);
            }
            
        });
    }
    
}
