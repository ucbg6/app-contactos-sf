/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

import java.io.IOException;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ucb40
 */
public class LoginSFDialog extends BorderPane {
    
    @FXML Button login, edit;
    @FXML ComboBox<SFProfile> pickProfile;
    @FXML PasswordField pass;
    @FXML Label nameLabel, status;
    
    Stage primaryStage;
    
    private ObservableList<SFProfile> profileList = FXCollections.observableArrayList();
    ObservableList<SFOrg> orgList = FXCollections.observableArrayList();
    
    StringProperty username = new SimpleStringProperty("");

    public LoginSFDialog(Stage stage){
        initialize();
        primaryStage = stage;
    }
    
    public ObservableList<SFProfile> getProfileList(){
        return profileList;
    }
    
    public ObservableList<SFOrg> getOrgs(){
        return orgList;
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/LoginSF.fxml");
        System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        pickProfile.setItems(profileList);
        eventListeners();
        profileList.add(new SFProfile());
        orgList.add(new SFOrg());
    }
    
    public void eventListeners(){
        pickProfile.valueProperty().addListener(cl -> {
            if (pickProfile.getValue().toString().equals("Nuevo")) {
                username.set("");
                edit.setVisible(false);
                newProfileWindow();
            } else {
                edit.setVisible(true);
                username.set(pickProfile.getValue().username);

            }
        });
        
        
        edit.setOnAction(e -> {
            editProfileWindow(pickProfile.getValue());
        });
        
        username.addListener(cl -> {
            nameLabel.setText(username.get());
        });
        
        
        
        
        
        
    }
    
    public void newProfileWindow(){
        Stage stage = new Stage();
        SFProfileDialog dialog = new SFProfileDialog(stage,orgList,profileList);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("Crear nuevo perfil");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            SFProfile toSpf = dialog.getProfile();  
            if (toSpf != null) {
                    System.out.println(toSpf);
                    // pickProfile.setItems(profileList);
                    pickProfile.getSelectionModel().select(toSpf);  
                }
                
        });
    }
    
    public void editProfileWindow(SFProfile spf){
        Stage stage = new Stage();
        SFProfileDialog dialog = new SFProfileDialog(stage,spf,orgList,profileList);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("[" + spf.id + "] " + spf.name);
        stage.show();

        stage.setOnCloseRequest(eh -> {
            SFProfile toSpf = dialog.getProfile();  
            if (toSpf != null) {
                    System.out.println(toSpf);
                    pickProfile.getSelectionModel().select(toSpf);  
                }
                
        });
            
        dialog.loadProfile();
    }
}
