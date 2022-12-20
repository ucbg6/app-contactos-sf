/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.dialog;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author ucb40
 */
public class NetDialog {
    public Alert alert = new Alert(AlertType.ERROR);
    boolean online = true;
    
    public NetDialog(String operation){
        alert.setTitle("Error de conexión");
        alert.setHeaderText(null);
        alert.setContentText("La siguiente operación no se ha podido realizar: \n - " + operation + "\n\nNo está conectado a internet");
        test();
        
    }
    
    public void show(){
        if (!online){
            alert.show();
        }
    }
    
    public boolean getOnline(){
        return online;
    }
    
    private void test(){
        online = true;
        try {
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection();

            if (connection.getContentLength() == -1) {
                // fail("Failed to verify connection");
                // online = false;
            } else {
                System.out.println("Conectado a la red.");
            }
        } catch (IOException e) {
            online = false;
            show();
            e.printStackTrace();
        }
    }
}
