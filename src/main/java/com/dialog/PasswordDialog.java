/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.dialog;

import com.login.SFUser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author ucb40
 */
public class PasswordDialog extends Dialog<String>{
    private final SFUser user;
    private PasswordField pass;
    
    public PasswordDialog(SFUser u){
        user = u;
        setTitle("Contraseña de " + user.getUsername());
        
        ButtonType passwordButtonType = new ButtonType("Aceptar", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar",ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(passwordButtonType, cancelButtonType);

        pass = new PasswordField();
        pass.setPromptText("Contraseña");

        HBox hBox = new HBox();
        hBox.getChildren().add(pass);
        hBox.setPadding(new Insets(20));

        HBox.setHgrow(pass, Priority.ALWAYS);

        getDialogPane().setContent(hBox);

        Platform.runLater(() -> pass.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton == passwordButtonType) {
                return pass.getText();
            } else{
                return "";
            }
        });
    }

    public PasswordField getPasswordField() {
        return pass;
    }
}
