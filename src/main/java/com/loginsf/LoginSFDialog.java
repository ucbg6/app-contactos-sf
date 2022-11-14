/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

import com.appcontactossf.App;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author ucb40
 */
public class LoginSFDialog extends Parent{
    
    @FXML Button login;
    @FXML ComboBox pickProfile;
    @FXML PasswordField pass;
    @FXML Label nameLabel, status;
    
    ObservableList<SFProfile> profileList = FXCollections.emptyObservableList();

    public LoginSFDialog(){
    }
}
