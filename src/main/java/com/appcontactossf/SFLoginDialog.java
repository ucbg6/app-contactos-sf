/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.appcontactossf.AppContactos;
import com.login.SFLogin;
import com.login.SFOrg;
import com.login.SFUser;
import java.io.IOException;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

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
        
        pickOrg.valueProperty().addListener((obv, oval, nval) -> {
            if (nval != null){
                pickUser.setItems(pickOrg.getValue().getUsers());
            } else {
                pickUser.getItems().clear();
            }
            
        });
        
        pickUser.setConverter(new StringConverter<SFUser>(){
            @Override
            public String toString(SFUser t) {
                if (t != null){
                    return t.getUsername();
                }
                return "";
            }

            @Override
            public SFUser fromString(String string) {
                if (string.isEmpty()){
                        return new SFUser();
                    }
                
                for (SFUser user : pickOrg.getValue().getUsers()){
                    if (user.getUsername().equals(string)){
                        return user;
                    }
                }
                return new SFUser();
            }
        });
        
        login.setOnAction(e ->{
            login();
        });
        
        this.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.ENTER){
                login();
            }
        });
        
        settings.setOnAction(e ->{
            openOrgsConfig();
        });
        
        exit.setOnAction(e -> {
            primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }
    
    public void login(){
        if (!pass.getText().isEmpty()) {
            status.set("Iniciando sesión...");
            SFLogin sfLogin = new SFLogin(pickOrg.getValue(), pickUser.getValue(), pass.getText());
            sfLogin.setOnSuccess(() -> {
                pass.setText("");
                login.setStyle("-fx-background-color: darkgreen;");
                login.setDisable(true);
                pass.setDisable(true);
                status.set("Sesión iniciada");

                openContactos(sfLogin);
            });

            sfLogin.setOnError(() -> {
                status.set("Error al iniciar sesión");
                login.setDisable(false);
            });

            sfLogin.connect(true);

        }
    }
    
    public void logout(SFLogin sfl){
        status.set("Cerrando sesión...");
        sfl.setOnSuccess(() -> {
            login.setDisable(false);
            pass.setDisable(false);
            login.setStyle("");
            login.setText("Iniciar sesión");
            status.set("Sesión cerrada correctamente");
        });

        sfl.setOnError(() -> {

        });

        sfl.revoke();
    }
    
    public void openContactos(SFLogin sfl){
        Stage stage = new Stage();
        AppContactos app = new AppContactos(sfl,stage);
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
            logout(sfl);
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
            pickOrg.setItems(orgList);
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
