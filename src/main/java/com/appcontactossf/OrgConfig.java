/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.dialog.DeleteDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.SFOrg;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author ucb40
 */
public class OrgConfig extends VBox{
    ObservableList<SFOrg> orgs;
    
    @FXML ListView<SFOrg> orgList;
    @FXML Button nuevo, edit, delete, importj, exportj, cancel, save;
    Stage myStage;
    
    public OrgConfig(ObservableList<SFOrg> myOrgs, Stage stage){
        orgs = FXCollections.unmodifiableObservableList(myOrgs);
        this.myStage = stage;
        initialize();
    }
    
    public ObservableList<SFOrg> getOrgs(){
        return orgList.getItems();
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
        orgList.setItems(FXCollections.observableArrayList(orgs));
        
        nuevo.setOnAction(e -> {
            openNewOrg();
        });
        
        orgList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        orgList.getSelectionModel().selectedItemProperty().addListener(cl -> {
            SFOrg selected = (SFOrg) orgList.getSelectionModel().getSelectedItem();
           
            edit.setDisable(selected == null);
            delete.setDisable(selected == null);
        });
        
        orgList.setOnMouseClicked(eh -> {
            if (eh.getClickCount() > 1){
                int index = orgList.getSelectionModel().getSelectedIndex();
                openEditOrg(index);
                
            }
            
        });
        
        edit.setOnAction(eh -> {
            int index = orgList.getSelectionModel().getSelectedIndex();
            openEditOrg(index);
            
        });
        
        delete.setOnAction(eh ->{
            SFOrg org = orgList.getSelectionModel().getSelectedItem();
            if (org != null){
                DeleteDialog dialog = new DeleteDialog("Eliminar Org. " + org.getName() + " ?");
                boolean delete = dialog.showAndWait();
                if (delete){
                    orgList.getItems().remove(org);
                }
            }
        });
        
        cancel.setOnAction(e -> {
            orgList.setItems(orgs);
            myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
        save.setOnAction(e -> {
            myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
        importj.setOnAction(eh -> {
            importJson();
        });
        
        exportj.setOnAction(eh -> {
            exportJson();
        });
    }
    
    public void openEditOrg(int index){
        Stage stage = new Stage();
        SFOrg org = orgs.get(index);
        SFOrgDialog dialog = new SFOrgDialog(org, orgs, stage);
        System.out.println("Org size (org): " + org.size());
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(myStage);
        stage.setScene(scene);
        stage.setTitle("Nueva org");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            System.out.println("Org size (org): " + org.size());
            SFOrg newOrg = dialog.getOrg();
            
            if (newOrg == null){
                // Eliminada
                orgList.getItems().remove(index);
            }
            if (newOrg != org){
                orgList.getItems().set(index, newOrg);
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
                // Nueva org creada
                orgList.getItems().add(newOrg);
            }
            
        });
    }
    
    public void exportJson(){
        Gson gson = new Gson();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar archivo JSON");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON","*.json"));
        File json = fileChooser.showSaveDialog(myStage);
        
        
        
        
        if (json != null){
            ArrayList<SFOrg> list = new ArrayList<>();
            for (SFOrg sfo : orgList.getItems()) {
                list.add(sfo);
            }
            
            System.out.println(json.getPath());
            try (FileWriter writer = new FileWriter(json)){
                gson.toJson(list, writer);
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    public void importJson(){
        Gson gson = new Gson();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo JSON");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON", "*.json"));
        File json = fileChooser.showOpenDialog(myStage);

        if (json != null) {
            ArrayList<SFOrg> list = new ArrayList<>();
            /*
            for (SFOrg sfo : orgList.getItems()) {
                list.add(sfo);
            }
            */

            System.out.println(json.getPath());
            try (Reader reader = Files.newBufferedReader(json.toPath())) {
                list = gson.fromJson(reader, new TypeToken<ArrayList<SFOrg>>() {}.getType());
                reader.close();
                
                orgList.getItems().clear();
                for (SFOrg sfo : list) {
                    orgList.getItems().add(sfo);
                }
                
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al importar");
                alert.setHeaderText(null);
                String content = "El archivo no contiene Orgs. de Salesforce";

                alert.initModality(Modality.WINDOW_MODAL);

                alert.setContentText(content);
                alert.showAndWait();
            }
        }
    }

    
}
