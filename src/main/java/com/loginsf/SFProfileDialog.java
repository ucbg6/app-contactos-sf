/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ucb40
 */
public class SFProfileDialog implements Initializable {
    
    @FXML TextField name, login, user, pass, id, secret;
    @FXML ComboBox pickLogin;
    @FXML Button test, save;
    @FXML Label status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
