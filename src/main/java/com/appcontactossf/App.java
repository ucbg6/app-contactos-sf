package com.appcontactossf;

import com.loginsf.LoginSFDialog;
import com.loginsf.SFLogin;
import com.loginsf.SFOrg;
import com.loginsf.SFProfile;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    // Commit 2
    public void start(Stage stage) {
        
        LoginSFDialog root = new LoginSFDialog(stage);

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("AppContactosSF");
        stage.show();
        
        SFOrg defOrg = new SFOrg("Developer Default","login.salesforce.com",
            "3MVG9SOw8KERNN0.CJVU2QegHFFCu15a5CrqMyPE3vIDJGLtC73pnKn70GlpnV.3E4JmnPVqzBY17e9pQ72RY",
            "FB45F66BE5B16B26B647C02B49763A111BFBD42ED7D3FF5B5EAE0C70CE90A278");
        
        SFProfile defProfile = new SFProfile(1,"Developer Default - urielc@ieslmucb.com",defOrg,
                "urielc@ieslmucb.com","kbunpqN7m9EWV0XbiW7bqWcH4");
        
        root.getOrgs().add(defOrg);
        root.getProfileList().add(defProfile);  
    }

    public static void main(String[] args) {
        launch();
    }

}