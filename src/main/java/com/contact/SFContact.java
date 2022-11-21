/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.contact;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Objeto base de la aplicación. Permite crear, importar, editar y publicar objetos de tipo Contacto tanto en local como en Salesforce.
 * @author ucb40
 */
public class SFContact {
    /*
    Campos que usará la aplicación:
     - PhotoURL
     - AccountID
     - Id (Id)
     - Name (Name)
        - First Name (FirstName)
        - Last Name (LastName)
     - Phone
        - Phone (Phone)
        - Mobile Phone (Mobile Phone)
    - Email (Email)
    - Address
        - Mailing Address
            - Street
            - Zip/Postal Code
            - Mailing City
            - Mailing State/Province
            - Mailing Country
    - Description
    */
    
    
    
    // ID del contacto, solo existe si el contacto se ha publicado o importado de Salesforce.
    private int id;
    // Respuesta JSON del servidor
    private String jsonInput;
    // Contenido a publicar en Salesforce
    private String jsonOutput;
    // Campos del Contacto (transformados)
    ObservableList<SFField> campos = FXCollections.observableArrayList();
}
