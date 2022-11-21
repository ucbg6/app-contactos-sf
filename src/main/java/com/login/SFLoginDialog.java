/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import com.appcontactossf.AppContactos;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.apache.http.HttpStatus;

/**
 * FXML Controller class
 *
 * @author ucb40
 */
public class SFLoginDialog extends BorderPane {
    
    @FXML Button login, settings, offline, exit;
    @FXML ComboBox<SFOrg> pickOrg;
    @FXML ComboBox<SFUser> pickUser;
    @FXML PasswordField pass;
    @FXML Label title, slabel;
    @FXML VBox loginBox;
    
    Stage primaryStage;

    ObservableList<SFOrg> orgList = FXCollections.observableArrayList();
    
    StringProperty status = new SimpleStringProperty("");

    public SFLoginDialog(Stage stage){
        initialize();
        primaryStage = stage;
    }
    
    public ObservableList<SFOrg> getOrgs(){
        return orgList;
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/LoginSFView.fxml");
        System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        pickOrg.setItems(orgList);
        eventListeners();
    }
    
    public void eventListeners(){
        status.addListener((obv, oval, nval) -> {
            if (nval.isEmpty()){
                slabel.setVisible(false);
            } else {
                slabel.setVisible(true);
                slabel.setText(nval);
            }
        });
        
        pickOrg.valueProperty().addListener(cl -> {
            pickUser.setItems(pickOrg.getValue().getUsers());
        });
        
        pickUser.setConverter(new StringConverter<SFUser>(){
            @Override
            public String toString(SFUser t) {
                if (t != null){
                    return t.username;
                }
                return "";
            }

            @Override
            public SFUser fromString(String string) {
                if (string.isEmpty()){
                        return new SFUser();
                    }
                
                for (SFUser user : pickOrg.getValue().getUsers()){
                    if (user.username.equals(string)){
                        return user;
                    }
                }
                return new SFUser();
            }
        });
        
        login.setOnAction(e ->{
            if (!pass.getText().isEmpty()){
                
                SFLogin sfLogin = new SFLogin(pickOrg.getValue(), pickUser.getValue(),pass.getText(), login);
                sfLogin.statusCode.addListener((obv, oval, nval) -> {
                    switch ((int)nval){
                        case 1:{
                            
                            status.set("Iniciando sesión");
                            pass.setText("");
                            break;
                        }
                        case HttpStatus.SC_OK: {
                            login.setStyle("-fx-background-color: darkgreen;");
                            login.setDisable(true);
                            pass.setDisable(true);
                            status.set("Sesión iniciada");
                            // openContactos(sfLogin);
                            break;
                        }
                        default: {
                            status.set("Error al iniciar sesión");
                            login.setDisable(false);
                            break;
                        }
                    }
                });
                sfLogin.login();
                
                
            }
        });
        
        settings.setOnAction(e ->{
            openOrgsConfig();
        });
        
        exit.setOnAction(e -> {
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
        offline.setOnAction(e -> {
            openContactosOff();
        });
        
        
        
        
        
        
    }
    
    public void openContactosOff(){
        Stage stage = new Stage();
        AppContactos app = new AppContactos(stage);
        Scene scene = new Scene(app);
        // stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("AppContactosSF");
        // stage.show();
       
        primaryStage.setOnHidden(h -> {
            stage.show();
        });
        
        primaryStage.hide();

        stage.setOnCloseRequest(cl -> {
            primaryStage.show();
        });
    }
    
    public void openOrgsConfig(){
        Stage stage = new Stage();
        OrgConfig config = new OrgConfig(orgList, stage);
        Scene scene = new Scene(config);
        // stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("Orgs");
        // stage.show();
       
        primaryStage.setOnHidden(h -> {
            stage.show();
        });
        
        primaryStage.hide();

        stage.setOnCloseRequest(cl -> {
            orgList = config.getOrgs();
            primaryStage.show();
        });
    }
    /* 
    
    public void newProfileWindow(){
        Stage stage = new Stage();
        SFUserDialog dialog = new SFUserDialog(stage,orgList,profileList);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("Crear nuevo perfil");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            SFUser toSpf = dialog.getProfile();  
            if (toSpf != null) {
                    System.out.println(toSpf);
                    // pickProfile.setItems(profileList);
                    pickProfile.getSelectionModel().select(toSpf);  
                }
                
        });
    }
    
    public void editProfileWindow(SFUser spf){
        Stage stage = new Stage();
        SFUserDialog dialog = new SFUserDialog(stage,spf,orgList,profileList);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle("[" + spf.id + "] " + spf.name);
        stage.show();

        stage.setOnCloseRequest(eh -> {
            SFUser toSpf = dialog.getProfile();  
            if (toSpf != null) {
                    System.out.println(toSpf);
                    pickProfile.getSelectionModel().select(toSpf);  
                }
                
        });
            
        dialog.loadProfile();
    }
    */
}
