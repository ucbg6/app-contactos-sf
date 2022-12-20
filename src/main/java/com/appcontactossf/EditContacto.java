/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.contact.Account;
import com.contact.Contact;
import java.io.IOException;
import java.net.URL;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.json.JSONObject;

/**
 *
 * @author ucb40
 */
public class EditContacto extends VBox{
    
    // Solo se guarda la cuenta con Id (insertada)
    private ObservableMap<String, Account> accounts;
    
    private Contact contact;
    private JSONObject jsonOutput;
    
    private int index;
    
    @FXML TextField firstName, lastName, 
            title, department, 
            phone, mobile, email,
            street, code, city, state, country,
            newacc;
    
    @FXML TextArea description;
    @FXML HBox accBox;
    @FXML ComboBox<Account> account;
    @FXML Button btnAcc, saveacc, cancelacc;
    
    // Utilizada en la ventana principal de la aplicación.
    BooleanProperty isValid = new SimpleBooleanProperty();
    StringProperty name = new SimpleStringProperty();
    
    public EditContacto(ObservableMap<String, Account> accounts){
        this.accounts = accounts;
        init();
    }
    
    public int getIndex(){
        return index;
    }
    
    public void setContact(Contact c, int n){
        // c es nulo -> Nuevo contacto
        contact = c;
        index = n;
        if (c != null){
            cargarDatos();
        }
    }
    
    public void newContact(){
        contact = null;
        clear();
    }
    
    public boolean isNew(){
        return (contact == null);
    }
    
    public JSONObject buildContact(){
        Contact c = new Contact(lastName.getText());
        if (!isNew()){
            c.setId(contact.getId());
        }
        c.setFirstName(firstName.getText());
        Account a = account.getSelectionModel().getSelectedItem();
        c.setAccountId(a == null ? "" : a.getId());
        c.setAccountName(a == null ? "" : a.getName());
        c.setTitle(title.getText());
        c.setDepartment(department.getText());
        c.setPhone(phone.getText());
        c.setMobile(mobile.getText());
        c.setEmail(email.getText());
        c.setStreet(street.getText());
        c.setPostalCode(code.getText());
        c.setCity(city.getText());
        c.setState(state.getText());
        c.setCountry(country.getText());
        c.setDescription(description.getText());
        
        return c.buildJsonOutput();
        
    }
    
    private void init(){
        // Carga de la vista FXML
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/EditContactoView.fxml");
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        charLimits();
        accounts.put("",null);
        account.setItems(FXCollections.observableArrayList(accounts.values()));
        
        accounts.addListener(new MapChangeListener<String,Account>(){
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Account> change) {
                account.getItems().add(change.getValueAdded());
            }
        });
        
        account.setConverter(new StringConverter<Account>(){
            @Override
            public String toString(Account a) {
                if (a != null){
                    return a.getName();
                }
                return "(sin cuenta)";
            }

            @Override
            public Account fromString(String string) {
                return new Account("", string);
                    
            }
        });
        
        firstName.textProperty().addListener((obv,oval,nval) ->{
            if (lastName.getText().isEmpty()){
                if (isNew()){
                    name.set("Nuevo contacto");
                } else {
                    name.set("<Necesario Apellido>");
                }
            }
            name.set(lastName.getText() + ", " + nval);
            
        });
        
        lastName.textProperty().addListener((obv,oval,nval) ->{
            if (nval.isEmpty()){
                isValid.set(false);
                name.set("<Necesario Apellido>");
                lastName.getStyleClass().add("tboxerr");
                
            } else {
                isValid.set(true);
                lastName.getStyleClass().remove("tboxerr");
                name.set(nval + ", " + firstName.getText());
                
            }
        });
        
        newacc.textProperty().addListener((obv,oval,nval) ->{
            if (nval.isEmpty()){
                saveacc.setDisable(true);
            } else {
                saveacc.setDisable(false);
            }
        });
        
        btnAcc.setOnAction(eh -> {
            accBox.setVisible(true);
            saveacc.setDisable(false);
        });
        
        cancelacc.setOnAction(eh -> {
            newacc.setText("");
            accBox.setVisible(false);
        });
    }

    public void charLimits(){
        setCharLimit(firstName, 40);
        setCharLimit(lastName, 80);
        setCharLimit(title, 128);
        setCharLimit(department, 80);
        setCharLimit(phone, 40);
        setCharLimit(mobile, 40);
        setCharLimit(email, 80);
        setCharLimit(street, 255);
        setCharLimit(code, 20);
        setCharLimit(city, 40);
        setCharLimit(state, 80);
        setCharLimit(country, 80);
        setCharLimit(newacc,255);
    }
    
    private void cargarDatos(){
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        title.setText(contact.getTitle());
        department.setText(contact.getDepartment());
        phone.setText(contact.getPhone());
        mobile.setText(contact.getMobile());
        email.setText(contact.getEmail());
        street.setText(contact.getStreet());
        code.setText(contact.getPostalCode());
        city.setText(contact.getCity());
        state.setText(contact.getState());
        country.setText(contact.getCountry());
        description.setText(contact.getDescription());
        account.getSelectionModel().select(accounts.get(contact.getAccountId()));
    }
    
    private void clear(){
        
        firstName.setText("");
        lastName.setText("");
        title.setText("");
        department.setText("");
        phone.setText("");
        mobile.setText("");
        email.setText("");
        street.setText("");
        code.setText("");
        city.setText("");
        state.setText("");
        country.setText("");
        description.setText("");
        account.getSelectionModel().select(null);
        name.set("Nuevo Contacto");
        lastName.getStyleClass().remove("tboxerr");
    }
    
    
    // Cada campo tiene un límite propio.
    private void setCharLimit(TextField tf, int limit){
        tf.lengthProperty().addListener((obv, oval, nval) -> {
            if ((int)nval > limit){
                tf.setText(tf.getText().substring(0,limit));
            }
        });
    }
    
}
