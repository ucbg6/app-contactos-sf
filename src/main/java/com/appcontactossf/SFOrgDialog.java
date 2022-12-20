/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.dialog.PasswordDialog;
import com.login.SFLogin;
import com.login.SFOrg;
import com.login.SFUser;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class SFOrgDialog extends VBox{
    final ObservableList<SFOrg> orgs;
    final SFOrg org;
    SFOrg newOrg;
    
    Stage myStage;
    
    @FXML Label namel, loginl, keyl, secretl, slabel, validStatus, orgName;
    @FXML Button createuser, edituser, deluser, test, cancel, save;
    @FXML TextField name, domain, login, key, secret;
    @FXML CheckBox check;
    @FXML VBox consumerbox, userbox;
    @FXML ListView<SFUser> users;
    @FXML ImageView imgValid;
    
    StringProperty status = new SimpleStringProperty();
    
    BooleanProperty valid = new SimpleBooleanProperty();
    
    /*
    Condiciones para guardar Org
     - Tiene nombre (no duplicado) (textProperty listener)
     - Tiene login
     - Tiene Key y Secret
     - Tiene al menos un usuario válido
    */
    
    public SFOrgDialog(SFOrg sforg, ObservableList<SFOrg> orgList, Stage stage){
        org = sforg;
        orgs = orgList;
        myStage = stage;
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
        
        if (org == null){
            vistaNuevo();
        } else {
            vistaEdit();
            // saveOriginal();
        }

        valid.addListener((obv, oval, nval) -> {
            if (nval){
                imgValid.setVisible(true);
            } else {
                imgValid.setVisible(false);
            }
            System.out.println("Valid has changed to " + nval);
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
            if (buscarPorNombre(nval) != null && !nval.equals(org.getName())){
                status.set("Ya existe una org con este nombre.");
                valid.set(false);
            } else {
                status.set("");
            }
            
            if (nval.isEmpty()){
                status.set("Introduzca un nombre para su Org.");
                orgName.setText("Nombre Org");
            } else {
                orgName.setText(name.getText());
                status.set("");
            }
        });
        
        login.textProperty().addListener((obv, oval, nval) -> {
            if (org != null){
                if (!login.getText().equals(org.getLoginUrl())){
                    valid.set(!isChanged());
                } 
            }
            
        });
        
        key.textProperty().addListener((obv, oval, nval) -> {
            if (org != null){
                if (!key.getText().equals(org.getClientId())) {
                    valid.set(!isChanged());
                }
            }
            
        });
        
        secret.textProperty().addListener((obv, oval, nval) -> {
            if (org != null){
                if (!secret.getText().equals(org.getClientSecret())) {
                    valid.set(!isChanged());
                }
            }
            
        });

        test.setOnAction(e -> {
            if (validarDatos()){
                SFUser user = users.getSelectionModel().getSelectedItem();
                int index = users.getSelectionModel().getSelectedIndex();
                String pass = passwordDialog(org, user);
                
                if (!pass.isEmpty()){
                    SFLogin sfl = new SFLogin(new SFOrg(name.getText(),login.getText(),key.getText(),secret.getText()), user, pass);
                    userbox.setDisable(true);
                    
                    test.setText("Validando...");
                    sfl.setOnSuccess(() -> {
                        user.setValid(true);
                        users.getItems().set(index, user);
                        domain.setText(sfl.getInstanceURL());
                        test.setText("Usuario validado!");
                        userbox.setDisable(false);
                        test.setDisable(true);
                        validarOrg();
                    });
                    sfl.setOnError(() -> {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error al validar");
                        alert.setHeaderText("Ha ocurrido el siguiente error de validación:");
                        String content = sfl.getResult();
                        if (content == null || content.isEmpty()){
                            content = "Tiempo de respuesta del servidor agotado. Compruebe su conexión a Internet y datos de acceso";
                        }
                        alert.initModality(Modality.WINDOW_MODAL);
                        
                        alert.setContentText(content);
                        alert.showAndWait();
                        
                        test.setText("Validar usuario");
                        userbox.setDisable(false);
                        test.setDisable(false);
                    });
                    sfl.connect(false);
                            
                    
                    
                } else {
                    test.setText("Validar usuario");
                    test.setDisable(false);
                }
            } else {
                test.setText("Validar usuario");
                test.setDisable(false);
            }
        });
        
        users.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        users.getSelectionModel().selectedItemProperty().addListener(cl -> {
            SFUser user = users.getSelectionModel().getSelectedItem();
            if (user != null){
                edituser.setDisable(false);
                deluser.setDisable(false);
                if (!user.isValid()){
                    test.setDisable(false);
                    test.setText("Validar usuario");
                } else {
                    test.setDisable(true);
                    test.setText("Usuario validado");
                }
                
            } else {
                edituser.setDisable(true);
                deluser.setDisable(true);
            }
            
        });
        
        users.setCellFactory(new javafx.util.Callback<ListView<SFUser>, ListCell<SFUser>>(){
            @Override
            public ListCell<SFUser> call(ListView<SFUser> p) {
                ListCell<SFUser> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(SFUser u, boolean up){
                        super.updateItem(u,up);
                        if (u != null){
                            if (!u.isValid()){
                                setText(u.getUsername() + " [VALIDAR]");
                            } else {
                                setText(u.getUsername());
                            }
                            
                        }
                    }
                };
                
                return cell;
                    
            }
        });
        
        users.setOnMouseClicked(me -> {
            if (me.getClickCount() > 1){
                
            }
        });
        
        users.getItems().addListener(new ListChangeListener<SFUser>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends SFUser> change) {
                validStatus.setText("Usuarios validados (" + getValidUsers() + "/1)");
            }
        });
        
        cancel.setOnAction(e -> {
            myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
        createuser.setOnAction(e -> {
            openCreateUser();
        });
        
        edituser.setOnAction(e -> {
            openEditUser(users.getSelectionModel().getSelectedIndex());
        });
        
        save.setOnAction(e -> {
            if (!valid.get()){
                saveWarning();
            } else {
                save();
                myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
            
        });
        
        
        
    }
    
    public void saveWarning(){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        String content = "";
        if (isChanged()){
            content = "Cambios importantes realizados en la Org. Debe volver a validar antes de guardar.";
        } else {
            content = "Para guardar esta Org. debe validar por lo menos un Usuario.";
        }
        alert.setContentText(content);
        alert.showAndWait();
        update();
    }
    
    public boolean isChanged(){
        if (org != null){
            String original = org.getLoginUrl() + org.getClientId() + org.getClientSecret();
            String newData = login.getText() + key.getText() + secret.getText();
            
            return (!original.equals(newData));
        }
        return false;
    }

    public void update(){
        boolean changed = isChanged();
        if (changed) {
            for (int i = 0; i < users.getItems().size(); i++) {
                    SFUser sfu = users.getItems().get(i);
                    sfu.setValid(false);
                    users.getItems().set(i, sfu);
                }
        } else {
            if (!org.getUsers().isEmpty()){
                for (int i = 0; i < org.getUsers().size(); i++) {
                    users.getItems().set(i, org.getUsers().get(i));
                }
            }
            
        }  
    }
    
    public int getValidUsers(){
        int count = 0;
        
        for (SFUser sfu : users.getItems()){
            if (sfu.isValid()){
                count++;
            }
        }
        return count;
    }
    
    public int getSize(){
        return users.getItems().size();
    }
    
    public SFOrg getOrg(){
        return newOrg;
    } 
    
    public void save(){
        newOrg = new SFOrg();
        newOrg.setName(name.getText());
        newOrg.setDomain(domain.getText());
        newOrg.setLoginUrl(login.getText());
        newOrg.setClientId(key.getText());
        newOrg.setClientSecret(secret.getText());
        newOrg.setUsers(users.getItems());
        newOrg.setValid(true);
        myStage.fireEvent(new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        
    }
    
    public String passwordDialog(SFOrg org, SFUser user){
        PasswordDialog dialog = new PasswordDialog(user);
        Optional<String> result = dialog.showAndWait();
        return result.get();
    }
    
    public void openCreateUser(){
        Stage stage = new Stage();
        SFUserDialog dialog = new SFUserDialog(null, name.getText(), stage);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(myStage);
        stage.setScene(scene);
        stage.setTitle("Nuevo usuario");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            SFUser newUser = dialog.getUser();
            if (newUser != null){
                users.getItems().add(newUser);
            }
            
        });
    }
    
    public void openEditUser(int index){
        Stage stage = new Stage();
        SFUser sfu = users.getItems().get(index);
        SFUserDialog dialog = new SFUserDialog(sfu, name.getText(), stage);
        Scene scene = new Scene(dialog);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(myStage);
        stage.setScene(scene);
        stage.setTitle("Nuevo usuario");
        stage.show();
        
        stage.setOnCloseRequest(eh -> {
            SFUser newUser = dialog.getUser();
            if (newUser != null){
                users.getItems().set(index, newUser);
            }
            
        });
    }
    
    public boolean validarDatos(){
        test.setText("Comprobando datos...");
        test.setDisable(true);
        boolean isValid = true;
        if (users.getSelectionModel().getSelectedItem() == null){
            status.set("Seleccione un usuario para validar");
            isValid = false;
        }
        
        if (users.getItems().isEmpty()){
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
        
        return testConnection(login.getText());
        
    }
    
    public boolean testConnection(String urls){
        try {
            String hostName = new URL(urls).getHost();
            boolean reachable = InetAddress.getByName(hostName).isReachable(2000);
            if (reachable){
                return true;
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    public void validarOrg(){
        if (getValidUsers() > 0){
            valid.set(true);
        } else {
            valid.set(false);
        }
    }
    
    public void vistaNuevo(){
        newOrg = new SFOrg();
        valid.set(false);
        
        check.setVisible(false);
        consumerbox.setDisable(false);
    }
    
    public void vistaEdit(){
        cargarDatos();
        check.setVisible(true);
        consumerbox.setDisable(true);
    }
    
    public void cargarDatos(){
        newOrg = org;
        orgName.setText(org.getName());
        name.setText(org.getName());
        domain.setText(org.getDomain());
        login.setText(org.getLoginUrl());
        key.setText(org.getClientId());
        secret.setText(org.getClientSecret());
        valid.set(org.isValid());
        users.setItems(FXCollections.observableArrayList(org.getUsers()));
        validStatus.setText("Validar usuarios (" + getValidUsers() + "/" + getSize() + ")");
    }
    
    
    public SFOrg buscarPorNombre(String st){
        for (SFOrg o : orgs){
            if (o.getName().equals(st)){
                return o;
            }
        }
        return null;
    }
}
