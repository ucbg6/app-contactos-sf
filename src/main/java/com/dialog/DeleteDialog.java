/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.dialog;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DeleteDialog{
    Alert alert = new Alert(AlertType.CONFIRMATION);
    boolean delete = false;
    
    public DeleteDialog(String text){
        alert.setTitle("Eliminar");
        alert.setHeaderText(null);
        alert.setContentText(text);
    }
    
    public boolean showAndWait(){
        Optional<ButtonType> result = alert.showAndWait();
        delete = result.get() == ButtonType.OK;
        return delete;
    }
}
