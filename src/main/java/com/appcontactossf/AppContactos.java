/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.contact.Account;
import com.contact.Contact;
import com.contact.RestOperation;
import com.login.SFLogin;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author ucb40
 */
public class AppContactos extends BorderPane{
    
    Stage appStage;
    SFLogin login;
    @FXML TableView contactView;
    @FXML ListView<Account> accountList;
    @FXML Label onlineStatus, name, id, status;
    @FXML Hyperlink account, idLink;
    @FXML Button refresh, logout, newContact, editSave, deleteCancel;
    @FXML Button showField, hideField, fieldUp, fieldDown;
    @FXML StackPane scroll;
    @FXML GridPane contactBox;
    
    @FXML ProgressBar progress;
    
    
    @FXML Label noSelect;
    @FXML VBox contactCard;
    
    @FXML ListView<TableColumn> listaValidos, listaVisibles;
    
    BooleanProperty isEdit = new SimpleBooleanProperty(false);
    // Cuando se está editando un contacto no cambia la selección
    
    InfoContacto info = new InfoContacto();
    
    ObservableList<Contact> contacts = FXCollections.observableArrayList();
    ObservableMap<String,Account> accounts = FXCollections.observableHashMap();
    ObservableList<TableColumn> columns = FXCollections.observableArrayList();
    
    EditContacto edit = new EditContacto(accounts);
    
    public AppContactos(Stage stage){
        appStage = stage;
        initialize();
    }
    
    public AppContactos(SFLogin sfl, Stage stage){
        login = sfl;
        appStage = stage;
        initialize();
    }
    
    
    private void initialize(){
        // Carga de la vista FXML
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/ContactosView.fxml");
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        scroll.getChildren().add(info);
        scroll.getChildren().add(edit);
        edit.setManaged(false);
        hide(edit);
        hide(info);
        
        onlineStatus.setText("Conectado: " + login.getUsername() + " - " + login.getInstanceURL());
        
        
        
        eventos();
        
        
        setTableView();
        /* 
        Contact nuevo = new Contact("Nuevo");
        nuevo.setFirstName("Martin");
        nuevo.setTitle("Director de Operaciones");
        nuevo.setDepartment("Operaciones");
        nuevo.setEmail("nuevo.martin@salesforce.com");
        nuevo.setPhone("667294232");
        nuevo.setMobile("723317891");
        nuevo.setStreet("AVD Reina Sofía, 108");
        nuevo.setPostalCode("29100");
        nuevo.setCity("Coin");
        nuevo.setState("Málaga");
        nuevo.setCountry("ES");
        contacts.add(nuevo);
        */
        
        /* Obtiene las cuentas y contactos de Salesforce */
        // accountList.setItems(FXCollections.observableArrayList(accounts.values()));
        refresh();
        
        
        
        
        
        
    }
    
    public void startBar(String text){
        progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        status.setText(text);
    }
    
    public void stopBar(String text){
        progress.setProgress(0);
        status.setText(text);
    }
    
    public void hide(Region region){
        region.setVisible(false);
        region.setMaxHeight(0);
    }
    
    public void show(Region region){
        region.setMaxHeight(region.getPrefHeight());
        region.setVisible(true);
    }
    
    private void eventos(){
        isEdit.addListener((obv, oval, nval) -> {
            if (nval){
                hide(info);
                show(edit);
                name.textProperty().bind(edit.name);
                edit.requestFocus();
                edit.setManaged(true);
                newContact.setDisable(true);
                editSave.setText("Guardar");
                deleteCancel.setDisable(false);
                deleteCancel.setText("Cancelar");
            } else {
                hide(edit);
                show(info);
                edit.setManaged(false);
                name.textProperty().unbind();
                contactView.getSelectionModel().select(info.getContact());
                info.requestFocus();
                newContact.setDisable(false);
                editSave.setText("Editar");
                deleteCancel.setText("Eliminar");
            }
        });
        
        accounts.addListener(new MapChangeListener<String,Account>(){
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Account> change) {
                accountList.getItems().add(change.getValueAdded());
            }
        });
        
        accountList.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>(){
            @Override
            public ListCell<Account> call(ListView<Account> p) {
                return new ListCell<>(){
                @Override
                public void updateItem(Account account, boolean empty){
                    super.updateItem(account,empty);
                    if (!empty){
                        this.setTooltip(new Tooltip("Id: " + account.getId()));
                        setText(account.getName());
                    }
                }
                    
                
                };
                
            } 
            
        });
        
        contactBox.prefWidthProperty().bind(scroll.prefWidthProperty());
        contactCard.prefWidthProperty().bind(scroll.widthProperty());
        info.prefWidthProperty().bind(scroll.widthProperty());
        edit.prefWidthProperty().bind(scroll.widthProperty());
        
        listaValidos.getSelectionModel().selectedItemProperty().addListener((obv, oval, nval) ->{
            
            if (nval != null){
                listaVisibles.getSelectionModel().clearSelection();
                showField.setDisable(false);
            } else {
                showField.setDisable(true);
            }
        });
        
        listaVisibles.getSelectionModel().selectedItemProperty().addListener((obv, oval, nval) ->{
            if (nval != null){
                listaValidos.getSelectionModel().clearSelection();
                hideField.setDisable(false);
                // No es el último. Puede bajar
                if (!nval.equals(listaVisibles.getItems().get(listaVisibles.getItems().size()-1))){
                    fieldDown.setDisable(false);
                } else {
                    fieldDown.setDisable(true);
                }
                if (!nval.equals(listaVisibles.getItems().get(0))){
                    fieldUp.setDisable(false);
                } else {
                    fieldUp.setDisable(true);
                }
            } else {
                fieldUp.setDisable(true);
                fieldDown.setDisable(true);
            }
        });
        
        listaValidos.setCellFactory(clbk -> {
            return new ListCell<TableColumn>(){
                @Override
                public void updateItem(TableColumn tc, boolean empty){
                    super.updateItem(tc,empty);
                    if (!empty){
                        setText(tc.getText());
                    } else {
                        setText("");
                    }
                }
            };     
        });
        
        listaVisibles.setCellFactory(clbk -> {
            return new ListCell<TableColumn>(){
                @Override
                public void updateItem(TableColumn tc, boolean empty){
                    super.updateItem(tc,empty);
                    if (!empty){
                        setText(tc.getText());
                    } else {
                        setText("");
                    }
                }
            };     
        });
            
        
        
        
        
        showField.setOnAction(eh -> {
            TableColumn selected = listaValidos.getSelectionModel().getSelectedItem();
            if (selected != null){
                listaValidos.getItems().remove(selected);
                contactView.getColumns().add(selected);
                listaVisibles.getSelectionModel().select(selected);
                
            }
        });
        
        hideField.setOnAction(eh -> {
            TableColumn selected = listaVisibles.getSelectionModel().getSelectedItem();
            if (selected != null){
                contactView.getColumns().remove(selected);
                listaValidos.getItems().add(selected);
                listaValidos.getSelectionModel().select(selected); 
            }
        });
        
        
        fieldUp.setOnAction(eh -> {
            int selected = listaVisibles.getSelectionModel().getSelectedIndex();
            swap(contactView.getColumns(), selected, selected-1);
            listaVisibles.getSelectionModel().select(selected-1);
        });
        
        fieldDown.setOnAction(eh -> {
            int selected = listaVisibles.getSelectionModel().getSelectedIndex();
            swap(contactView.getColumns(), selected, selected+1);
            listaVisibles.getSelectionModel().select(selected+1);
        });
        
        contactView.getColumns().addListener(new ListChangeListener<>(){

            @Override
            public void onChanged(ListChangeListener.Change<? extends Object> change) {
                while (change.next()){
                    listaVisibles.setItems(contactView.getColumns());
                }
            }
        });
        
        edit.isValid.addListener((obv,oval,nval) -> {
            editSave.setDisable(!nval);
        });
        
        refresh.setOnAction(eh -> {
            refresh();
        });
        
        newContact.setOnAction(eh -> {
            nuevoContacto();
        });
        
        editSave.setOnAction(eh -> {
            if (isEdit.get()){
                // save
                if (edit.isNew()){
                    insertContact();
                } else {
                    updateContact();
                }
                
            } else {
                Contact c = info.getContact();
                int index = info.getIndex();
                if (c != null){
                    editContacto(c, index);
                }
            }
        });
        
        deleteCancel.setOnAction(eh -> {
            if (isEdit.get()){
                isEdit.set(false);
                Contact c = (Contact) contactView.getSelectionModel().getSelectedItem();
                int index = contactView.getSelectionModel().getSelectedIndex();
                if (!edit.isNew()){
                    infoContacto(c, index);
                } else {
                    contactView.getSelectionModel().clearSelection();
                }
                
            } else {
                // delete
                
            }
        });
        
        edit.saveacc.setOnAction(eh -> {
            // insert account
            insertAccount(edit.newacc.getText());
        });
        
        logout.setOnAction(eh -> {
            appStage.fireEvent(new WindowEvent(appStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        
    }
   
    
    private void insertContact(){
        JSONObject json = edit.buildContact();
        RestOperation newContact = new RestOperation("insert contact",json,login);
        startBar("Creando contacto: " + json.getString("LastName") + ", " + json.getString("FirstName"));
        newContact.setOnSuccess(() -> {
            JSONObject newJson = newContact.getObjects().get(0);
            Contact c = new Contact(json);
            c.setId(newJson.getString("id"));
            Account a = accounts.get(c.getAccountId());
            c.setAccountName(a == null ? "" : a.getName());
            contacts.add(c);
            stopBar("Contacto creado. " + json.getString("LastName") + ", " + json.getString("FirstName") + ". Id: "  + c.getId());
            contacts.sort(Comparator.comparing(Contact::getLastName));
            contactView.setItems(contacts);
            isEdit.set(false);
            contactView.getSelectionModel().select(c);
            contactView.scrollTo(c);
            
        });
        newContact.setOnError(() -> {
            stopBar("Error al crear contacto.");
        });
        
        newContact.run();
    }
    
    private void insertAccount(String name){
        edit.accBox.setVisible(false);
        JSONObject json = new JSONObject();
        json.put("Name", name);
        RestOperation accInsert = new RestOperation("insert account",json,login);
        startBar("Creando cuenta: " + name);
        accInsert.setOnSuccess(() -> {
            JSONObject newJson = accInsert.getObjects().get(0);
            newJson.put("Id",newJson.get("id"));
            Account newAcc = new Account(newJson.getString("id"),name);
            accounts.put(newAcc.getId(),newAcc);
            stopBar("Cuenta creada correctamente. Id: " + newAcc.getId());
            edit.account.getSelectionModel().select(newAcc);
        });
        accInsert.setOnError(() -> {
            stopBar("Error al crear cuenta.");
        });
        
        accInsert.run();
    }
    
    private void updateContact(){
        JSONObject json = edit.buildContact();
        RestOperation update = new RestOperation("update contact",json,login);
        startBar("Guardando cambios...");
        update.setOnSuccess(() -> {
            Contact c = new Contact(json);
            System.out.println("Id: " + c.getId());
            Account a = accounts.get(c.getAccountId());
            c.setAccountName(a == null ? "" : a.getName());
            stopBar("Cambios guardados. " + json.getString("LastName") + ", " + json.getString("FirstName") + ". Id: "  + c.getId());
            contacts.set(edit.getIndex(),c);
            contacts.sort(Comparator.comparing(Contact::getLastName));
            contactView.setItems(contacts);
            isEdit.set(false);
            contactView.getSelectionModel().select(c);
            contactView.scrollTo(c);
            
            
        });
        update.setOnError(() -> {
            stopBar("Error al guardar cambios.");
        });
        
        update.run();
        
    }
                
 
    public TableColumn buscarColumna(String col){
        for (TableColumn tc : columns){
            if (tc.getText().equals(col)){
                return tc;
            }
        }
        return null;
    }
    
    private void refresh(){
        contactView.getItems().clear();
        accountList.getItems().clear();
        // accounts.clear();
        contacts.removeAll();

        queryAccounts();
    }
    
    private void queryAccounts(){
        // System.out.println("Intentando obtener la lista de cuentas");
        startBar("Cargando cuentas de la Org.");
        RestOperation accountQuery = new RestOperation("query account",null,login);
        accountQuery.setOnSuccess(() -> {
            for (JSONObject j : accountQuery.getObjects()){
                String accountId = j.getString("Id");
                Account a = new Account(j);
                accounts.put(accountId, new Account(j));

            }
            stopBar("Cuentas cargadas correctamente");
            queryContacts();
        });

        accountQuery.setOnError(() -> {
            stopBar("Error al importar cuentas");
        });

        accountQuery.run();
    }
    
    private void queryContacts(){
        if (login != null){
            startBar("Cargando contactos de la Org.");
            // System.out.println("Intentando obtener la lista de contactos");
            RestOperation contactQuery = new RestOperation("query contact",null,login);
            contactQuery.setOnSuccess(() -> {
                for (JSONObject j : contactQuery.getObjects()){
                    Contact c = new Contact(j);
                    Account a = accounts.get(c.getAccountId());
                    String accName = (a == null) ? "" : a.getName();
                    c.setAccountName(accName);
                    contacts.add(c);
                    // System.out.println("Lista obtenida");
                }
                contacts.sort(Comparator.comparing(Contact::getLastName));
                contactView.setItems(contacts);
                stopBar("Contactos cargados correctamente");
                // contactView.getSelectionModel().select(20);
                // contactView.scrollTo(20);
            });
            contactQuery.run();
        }
        
    }
    
    public void setTableView(){
        // Columna Id
        TableColumn idc = new TableColumn("Id");
        idc.setCellValueFactory(new PropertyValueFactory<Contact,String>("id"));
        // Columna Apellidos
        TableColumn lastName = new TableColumn("Apellidos");
        lastName.setCellValueFactory(new PropertyValueFactory<Contact,String>("lastName"));
        // Nombre
        TableColumn firstName = new TableColumn("Nombre");
        firstName.setCellValueFactory(new PropertyValueFactory<Contact,String>("firstName"));
        // Email
        TableColumn emailt = new TableColumn("Email");
        emailt.setCellValueFactory(new PropertyValueFactory<Contact,String>("email"));
        // Teléfono
        TableColumn phonet = new TableColumn("Teléfono");
        phonet.setCellValueFactory(new PropertyValueFactory<Contact,String>("phone"));
        // Dirección
        TableColumn address = new TableColumn("Dirección");
        address.setCellValueFactory(new PropertyValueFactory<Contact,String>("address"));
        // Titulo
        TableColumn title = new TableColumn("Título");
        title.setCellValueFactory(new PropertyValueFactory<Contact,String>("title"));
        // Departamento
        TableColumn dep = new TableColumn("Departamento");
        dep.setCellValueFactory(new PropertyValueFactory<Contact,String>("department"));
        // Id Cuenta
        TableColumn accId = new TableColumn("Id Cuenta");
        accId.setCellValueFactory(new PropertyValueFactory<Contact,String>("accountId"));
        // Nombre Cuenta
        TableColumn accName = new TableColumn("Nombre Cuenta");
        accName.setCellValueFactory(new PropertyValueFactory<Contact,String>("accountName"));
        // Móvil
        TableColumn mobile = new TableColumn("Móvil");
        mobile.setCellValueFactory(new PropertyValueFactory<Contact,String>("mobile"));
        // Descripción
        TableColumn desc = new TableColumn("Descripción");
        desc.setCellValueFactory(new PropertyValueFactory<Contact,String>("description"));
        
        // columns.addAll(lastName, firstName, emailt, phonet, idc, address, title, dep, accId, accName, mobile, desc);
        
        listaVisibles.getItems().addAll(lastName, firstName, emailt, phonet);
        listaValidos.getItems().addAll(idc, accId, accName, title, dep, mobile, address, desc);
        
        contactView.getColumns().addAll(listaVisibles.getItems());
        
        contactView.setItems(contacts);
        
        contactView.getSelectionModel().selectedItemProperty().addListener((obv, oval, nval) -> {
            if (nval != null){
                contactCard.setVisible(true);
                noSelect.setVisible(false);
                if (!isEdit.get()){
                    infoContacto((Contact) nval, contactView.getSelectionModel().getSelectedIndex());
                }
                editSave.setDisable(false);
                deleteCancel.setDisable(false);
                
                
            } else {
                if (!isEdit.get()){
                    contactCard.setVisible(false);
                    noSelect.setVisible(true);
                    info.setVisible(false);
                }
                
                editSave.setDisable(true);
                deleteCancel.setDisable(true);
            }
        });
    }
    
    private void swap(ObservableList<TableColumn> list, int i, int j){
        TableColumn tmpi = list.get(i);
        TableColumn tmpj = list.get(j);
        list.removeAll(tmpi,tmpj);
        
        if (i < j){
            list.add(i,tmpj);
            list.add(j,tmpi);
        } else {
            list.add(j,tmpi);
            list.add(i,tmpj);
        }
        
    }
    
    private void infoContacto(Contact c, int index){
        name.setText(c.getLastName() + ", " + c.getFirstName());
        id.setText("Id: " + c.getId());
        Account a = accounts.get(c.getAccountId());

        info.setContact(c, index);
        show(info);
        
    }
    
    private void editContacto(Contact c, int index){
        name.setText(c.getLastName() + ", " + c.getFirstName());
        id.setText("Id: " + c.getId());
        edit.setContact(c,index);
        isEdit.set(true);
    }
    
    private void nuevoContacto(){
        name.setText("Nuevo contacto");
        contactCard.setVisible(true);
        editSave.setDisable(true);
        id.setText("");
        edit.newContact();
        isEdit.set(true);
    }
    
}
