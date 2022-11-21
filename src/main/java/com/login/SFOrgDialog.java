/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import java.io.IOException;
import java.net.URL;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SFOrgDialog extends VBox{
    ObservableList<SFOrg> orgs;
    SFOrg org;
    Stage stage;
    
    @FXML Label namel, loginl, keyl, secretl, slabel;
    @FXML Button createuser, edituser, deluser, test, cancel, save, delete;
    @FXML TextField name, domain, login, key, secret;
    @FXML CheckBox check;
    @FXML VBox consumerbox;
    @FXML ListView<SFUser> users;
    
    StringProperty status = new SimpleStringProperty();
    
    BooleanProperty valid = new SimpleBooleanProperty();
    
    /*
    Condiciones para guardar Org
     - Tiene nombre (no duplicado) (textProperty listener)
     - Tiene login
     - Tiene Key y Secret
     - Tiene al menos un usuario
     - Todos los usuarios han sido validados
    */
    
    public SFOrgDialog(SFOrg sforg, ObservableList<SFOrg> orgList, Stage myStage){
        org = sforg;
        orgs = orgList;
        stage = myStage;
        initialize();
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/SFOrgView.fxml");
        // System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        
        
        valid.addListener((obv, oval, nval) -> {
            if (nval){
                save.setDisable(false);
            } else {
                save.setDisable(true);
            }
        });
        
        status.addListener((obv,oval,nval) -> {
            if (nval.isEmpty()){
                slabel.setVisible(false);
            } else {
                slabel.setText(nval);
                slabel.setVisible(true);
            }
        });
        
        check.selectedProperty().addListener((obv, oval ,nval) -> {
            consumerbox.setDisable(!nval);
        });
        
        name.textProperty().addListener((obv, oval, nval) -> {
            if (buscarPorNombre(nval) != null){
                status.set("Ya existe una org con este nombre.");
                valid.set(false);
            } else {
                status.set("");
            }
            
            if (nval.isEmpty()){
                status.set("Introduzca un nombre para su Org.");
            }
            System.out.println(nval);
        });
        
        test.setOnAction(e -> {
            boolean isValid = validarDatos();
            if (isValid){
                // Realizar test de conexión
            }
        });
        
        if (org == null){
            vistaNuevo();
        } else {
            vistaEdit();
        }
        
        cancel.setOnAction(e -> {
            org = null;
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
    }
    
    public SFOrg getOrg(){
        return org;
    }
    
    public boolean validarDatos(){
        boolean isValid = true;
        
        if (org.users.isEmpty()){
            status.set("Añada al menos un usuario a su org antes de validar");
            isValid = false;
        }
        if (key.getText().isEmpty() || secret.getText().isEmpty()){
            status.set("Compruebe la clave y el secreto de su App Conectada");
            isValid = false;
        }
        if (!validarLogin()){
            status.set("Introduzca una URL válida para login");
            isValid = false;
        }
        if (name.getText().isEmpty()){
            status.set("Introduzca un nombre para su Org.");
            isValid = false;
        }
        return isValid;
    }
    
    public boolean validarLogin(){
        if (login.getText().isEmpty()){
            return false;
        }
        if (!login.getText().contains("https://")){
            login.setText("https://" + login.getText());
        }
        return true;        
    }
    
    public void vistaNuevo(){
        org = new SFOrg();
        check.setVisible(false);
        consumerbox.setDisable(false);
    }
    
    public void vistaEdit(){
        cargarDatos();
    }
    
    public void cargarDatos(){
        name.setText(org.name);
        domain.setText(org.domain);
        login.setText(org.loginUrl);
        key.setText(org.clientId);
        secret.setText(org.clientSecret);
        users.setItems(org.getUsers());
    }
    
    
    public SFOrg buscarPorNombre(String st){
        for (SFOrg o : orgs){
            if (o.name.equals(st)){
                System.out.println("found");
                return o;
            }
        }
        return null;
    }
}
