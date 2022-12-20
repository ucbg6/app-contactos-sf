/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.login.SFOrg;
import com.login.SFUser;
import java.io.IOException;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ucb40
 */
public class SFUserDialog extends VBox {
    
    @FXML TextField name, id;
    @FXML PasswordField token;
    @FXML Button save, cancel;
    @FXML Label orgName, namel, tokenl, status;
    @FXML CheckBox editToken;
    
    Stage stage;
    
    SFUser user;
    String org;
    
    ObservableList<SFUser> users = FXCollections.observableArrayList();
    
    StringProperty statusValue = new SimpleStringProperty();

    public SFUserDialog(SFUser sfu, String nameOrg, Stage stage){
        user = sfu;
        this.org = nameOrg;
        this.stage = stage;
        initialize();
    }
    
    public SFUser getUser(){
        return user;
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/SFUserView.fxml");
        System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        if (user != null){
            vistaEdit();
        } else {
            vistaNuevo();
        }
        
        eventListeners();

        cancel.setOnAction(e -> {
            user = null;
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
        name.textProperty().addListener((obv, oval, nval) -> {
            if (!nval.isEmpty()){
                SFUser sfu = buscarPorUsername(nval);
                if (sfu != null){
                    token.setText(sfu.getSecurityToken());
                }
            }
        });
        
        save.setOnAction(e -> {
            if (buscarPorUsername(name.getText()) != null && !name.getText().equals(user.getUsername())){
                statusValue.set("Este usuario ya fue registrado");
            } else {
                user.setUsername(name.getText());
                user.setSecurityToken(token.getText());
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
            
        });
        
    }
    
    public void vistaNuevo(){
        user = new SFUser();
        orgName.setText(org + " -  Nuevo usuario");
        editToken.setVisible(false);
    }
    
    public void vistaEdit(){
        orgName.setText(org + " - " + user.getUsername());
        editToken.setVisible(true);
        token.setDisable(true);
        name.setText(user.getUsername());
        token.setText(user.getSecurityToken());
    }
    
    public SFUser buscarPorUsername(String user){
        for (SFUser sfu : users){
            if (sfu.getUsername().equals(user)){
                return sfu;
            }
        }
        return null;
    }
    
    public void eventListeners(){
        
        statusValue.addListener(cl -> {
            if (statusValue.get().isEmpty()){
                status.setVisible(false);
            } else {
                status.setText(statusValue.get());
                status.setVisible(true);
            }
        });
        
        editToken.selectedProperty().addListener((obv, oval, nval) -> {
            token.setDisable(!nval);
        });
    } 
}
