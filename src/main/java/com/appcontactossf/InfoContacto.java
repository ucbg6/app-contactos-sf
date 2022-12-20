/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import com.contact.Account;
import com.contact.Contact;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author ucb40
 */
public class InfoContacto extends VBox{
    @FXML Label title, department, phone, mobile, address, description, account, email;
    
    private Contact contact;
    private String accName;
    private int index;
    
    public InfoContacto(){
        init();
    }
    
    public int getIndex(){
        return index;
    }
    
    private void init(){
        // Carga de la vista FXML
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/InfoContactoView.fxml");
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public void setContact(Contact c, int num){
        contact = c;
        index = num;
        cargaDatos();
    }
    
    public Contact getContact(){
        return contact;
    }
    
    public void cargaDatos(){
        if (contact != null){
            setText(account,contact.getAccountName(),"Sin cuenta");
            setText(title,contact.getTitle(),"-");
            setText(department,contact.getDepartment(),"-");
            setText(email,contact.getEmail(),"-");
            setText(phone,contact.getPhone(),"-");
            setText(mobile,contact.getMobile(),"-");
            setText(address,contact.getStreet() + "\n" + contact.getPostalCode() + " " + contact.getCity() + " " + contact.getState() + "\n" + contact.getCountry(),"-");
            setText(description,contact.getDescription(),"-");
        }
    }
    
    private void setText(Label lb, String text, String ifEmpty){
        if (text.isEmpty()){
            lb.setText(ifEmpty);
        } else {
            lb.setText(text);
        }
    }
    
    
    
}
