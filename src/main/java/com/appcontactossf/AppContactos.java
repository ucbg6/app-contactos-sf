/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.appcontactossf;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author ucb40
 */
public class AppContactos extends BorderPane{
    
    Stage appStage;
    
    public AppContactos(Stage stage){
        appStage = stage;
        initialize();
    }
    
    private void initialize(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/fxml/ContactosView.fxml");
        // System.out.println("Location: " + url.toString());
        fxmlLoader.setLocation(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
}
