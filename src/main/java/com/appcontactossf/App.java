package com.appcontactossf;

import com.login.SFLoginDialog;
import com.login.SFLogin;
import com.login.SFOrg;
import com.login.SFUser;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    // Commit 2
    public void start(Stage stage) {
        
        SFLoginDialog root = new SFLoginDialog(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("AppContactosSF");
        Platform.setImplicitExit(false);
        
        stage.setOnCloseRequest(eh -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Salir");
            alert.setHeaderText(null);
            alert.setContentText("Seguro que quiere salir de la aplicación?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            } else {
                eh.consume();
            }
            
        });
        
        stage.show();
        
        SFOrg defOrg = new SFOrg(0,"Developer Default","https://login.salesforce.com",
            "3MVG9SOw8KERNN0.CJVU2QegHFFCu15a5CrqMyPE3vIDJGLtC73pnKn70GlpnV.3E4JmnPVqzBY17e9pQ72RY",
            "FB45F66BE5B16B26B647C02B49763A111BFBD42ED7D3FF5B5EAE0C70CE90A278");
        
        SFUser defUser = new SFUser(0,defOrg,"urielc@ieslmucb.com","P1Z5hsu5rpCC7vCrPtXjl5kB");
        
        defOrg.getUsers().add(defUser);
        root.getOrgs().add(defOrg);
          
    }

    public static void main(String[] args) {
        launch();
    }

}