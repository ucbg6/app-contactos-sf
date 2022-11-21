/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import java.io.IOException;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ucb40
 */
public class SFUserDialog extends VBox {
    
    @FXML TextField name, login, user, id;
    @FXML PasswordField pass, token, secret;
    @FXML ComboBox<SFOrg> pickLogin;
    @FXML Button test, save, delete, cancel;
    @FXML Label nameLabel, status;
    
    Stage stage;
    
    SFUser profile;
    ObservableList<SFOrg> orgs = FXCollections.observableArrayList();
    ObservableList<SFUser> profileList = FXCollections.observableArrayList();
    
    StringProperty statusValue = new SimpleStringProperty();
    
    public SFUserDialog(Stage stage, ObservableList<SFOrg> orgl, ObservableList<SFUser> plist){
        orgs = orgl;
        profileList = plist;
        this.stage = stage;
        initialize();
    }
    
    public SFUser getProfile(){
        return profile;
    }
    
    public SFUserDialog(Stage stage, SFUser spf ,ObservableList<SFOrg> orgl, ObservableList<SFUser> plist){
        orgs = orgl;
        profileList = plist;
        this.stage = stage;
        profile = spf;
        initialize();
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/SFProfileDialogView.fxml");
        System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        eventListeners();
        
        /*
            Condiciones para la validación
            - La org nueva no existe (esto se podrá prevenir)
            - El perfil no es idéntico a uno ya existente
            - El SFLogin ha funcionado exitosamente
        
            PK(SFOrg, username)
        */
        
        pickLogin.valueProperty().addListener(cl -> {
            if (pickLogin.getValue().toString().equals("Nueva Org.")){
                name.setText("");
                id.setDisable(false);
                id.setText("");
                token.setDisable(false);
                login.setText("");
                secret.setDisable(false);
                secret.setText("");
            } else {
                name.setText(pickLogin.getValue().toString());
                login.setText(pickLogin.getValue().loginUrl);
                id.setDisable(true);
                id.setText(pickLogin.getValue().clientId);
                secret.setDisable(true);
                secret.setText(pickLogin.getValue().clientSecret);
            }
        });

        cancel.setOnAction(e -> {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
        user.focusedProperty().addListener((obv, oval, nval) -> {
            if (!nval){
                SFUser spf = buscarPorUsername(user.getText());
                if (spf != null){
                    token.setText(spf.securityToken);
                }
            }
        });
        /* 
        save.setOnAction(e -> {
            SFOrg org = new SFOrg(-1,name.getText(),login.getText(), id.getText(), secret.getText());
            SFUser toSpf = new SFUser(-1,name.getText() + " - " + user.getText(),org,user.getText(),token.getText());
            if (validation(toSpf)){
                // Modificar

                if (profile != null){
                    toSpf.id = profile.id;
                    System.out.println("Profile ID: " + profile.id);
                    profileList.set(profile.id,toSpf);
                    profile = toSpf;
                } else {
                    toSpf.id = profileList.size();
                    profileList.add(toSpf);
                    profile = toSpf;
                }
                
                SFOrg orgBefore = buscarOrg(org.name);
                if (orgBefore != null){
                    org.oid = orgBefore.oid;
                    orgs.set(org.oid, org);
                } else {
                    org.oid = orgs.size();
                    orgs.add(org);
                }
                
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                
            }
            
            
        }); */
        
        pickLogin.setItems(orgs);

        
        
    }
    
    
    public SFUser createNew(){
        return null;
    }
    
    public SFUser buscarPorUsername(String user){
        for (SFUser spf : profileList){
            if (spf.username.equals(user)){
                return spf;
            }
        }
        return null;
    }
    
    public boolean validation(SFUser toSpf){
        return (validateOrgName() && validateProfileChanged(toSpf));
    }
    
    
    public void eventListeners(){
        name.focusedProperty().addListener((obv, oval, nval) -> {
            if (!nval){
                System.out.println("Name is not focused");
                validateOrgName();
            } else {
                nameLabel.setTextFill(Color.BLACK);
                statusValue.set("");
                System.out.println("Name is focused");
            }
        });
        
        statusValue.addListener(cl -> {
            if (statusValue.get().isEmpty()){
                status.setVisible(false);
            } else {
                status.setText(statusValue.get());
                status.setVisible(true);
            }
        });
    } 
    
    public void loadProfile(){
        pickLogin.getSelectionModel().select(profile.org);
        name.setText(profile.org.name);
        login.setText(profile.org.loginUrl);
        user.setText(profile.username);
        token.setText(profile.securityToken);
        token.setDisable(true);
        id.setText(profile.org.clientId);
        id.setDisable(true);
        secret.setText(profile.org.clientSecret);
        secret.setDisable(true);
        save.setText("Guardar");
        delete.setVisible(true);
    }
    
    public SFOrg buscarOrg(String name){
        for (SFOrg org : orgs){
            if (org.name.equals(name)){
                return org;
            }
        }
        return null;
    }
    
    public boolean validateOrgName(){
        if (buscarOrg(name.getText()) != null && pickLogin.getValue().toString().equals("Nueva Org.")){
            nameLabel.setTextFill(Color.RED);
            test.setDisable(true);
            statusValue.set("Ya existe una Org. con este nombre. Elige uno diferente");
            return false;
        }
        return true;
    }
    
    public boolean validateProfileChanged(SFUser toSpf){
        if (toSpf == profile){
            System.out.println("No se han realizado cambios en el perfil");
            return false;
        }     
        return true;
    }
    
   
}
