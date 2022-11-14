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
        
        SFOrg defOrg = new SFOrg("Developer Default","login.salesforce.com",
            "3MVG9SOw8KERNN0.CJVU2QegHFFCu15a5CrqMyPE3vIDJGLtC73pnKn70GlpnV.3E4JmnPVqzBY17e9pQ72RY",
            "FB45F66BE5B16B26B647C02B49763A111BFBD42ED7D3FF5B5EAE0C70CE90A278");
        
        SFProfile defProfile = new SFProfile("Default",defOrg,
                "urielc@ieslmucb.com","kbunpqN7m9EWV0XbiW7bqWcH4");
        
        SFLogin login = new SFLogin(defProfile,"password");
        
        Parent root;

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/LoginSF.fxml");
        System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        // fxmlLoader.setRoot(this);
        // fxmlLoader.setController(this);

        try {
            root = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        var scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}