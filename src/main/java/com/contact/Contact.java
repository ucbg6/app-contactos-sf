/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.contact;

import java.time.ZonedDateTime;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

/**
 * Objeto base de la aplicación. Permite crear, importar, editar y publicar objetos de tipo Contacto tanto en local como en Salesforce.
 * @author ucb40
 */
public class Contact {
    // ID del contacto, solo existe si el contacto se ha publicado o importado de Salesforce.
    private String id = "";  // Id
    // Respuesta JSON del servidor
    private JSONObject jsonInput;
    // Contenido a publicar en Salesforce
    // private JSONObject jsonOutput;
    // Campos del Contacto
    private String accountId;            // Cuenta asociada al contacto
    private String firstName = "";       // FirstName (40)
    private String lastName = "";        // LastName (80)
    private String title = "";           // Title (128)
    private String department = "";      // Department (80)
    private String phone = "";           // Phone (40)
    private String mobile = "";          // MobilePhone (40)
    private String email = "";           // Email (80)
    private String street = "";          // MailingStreet (255)
    private String postalCode = "";      // MailingPostalCode (20)
    private String city = "";            // MailingCity (40)
    private String state = "";           // MailingState (80)
    private String country = "";         // MailingCountry (80)
    private String description = "";     // Description (32000)
    
    private String accountName;          // Nombre cuenta
    private String address = "";
    
    public Contact(String name){
        lastName = name;
        id = "";
    }
    
    public Contact(JSONObject json){
        jsonInput = json;
        buildFromJsonInput();
    }
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getJsonInput() {
        return jsonInput;
    }

    public void setJsonInput(JSONObject jsonInput) {
        this.jsonInput = jsonInput;
    }
    
    private void buildFromJsonInput(){
        
        for (String key : jsonInput.keySet()){
            if (jsonInput.get(key) == null){
                jsonInput.put(key,"");
            }
        }
        
        if (jsonInput.has("Id")){
            id = getString("Id");
        }
        
        accountId = getString("AccountId");
        firstName = getString("FirstName");
        lastName = getString("LastName");
        title = getString("Title");
        department = getString("Department");
        phone = getString("Phone");
        mobile = getString("MobilePhone");
        email = getString("Email");
        street = getString("MailingStreet");
        postalCode = getString("MailingPostalCode");
        city = getString("MailingCity");
        state = getString("MailingState");
        country = getString("MailingCountry");
        address = street + "\n" + postalCode + " " + city + " " + state + "\n" + country;
        description = getString("Description");
        // System.out.println("Contacto(JSON): " + lastName + ", " + firstName);
    }
    
    public String getString(String key){
        // System.out.println(jsonInput.get(key));
        if (jsonInput.get(key).toString().equals("null")){
            return "";
        }
        return jsonInput.getString(key);
    }
    /* 
    public JSONObject getJsonOutput() {
        return jsonOutput;
    }

    public void setJsonOutput(JSONObject jsonOutput) {
        this.jsonOutput = jsonOutput;
    }
    */
    
    public JSONObject buildJsonOutput(){
        JSONObject jsonOutput = new JSONObject();
        if (!id.isEmpty()){
            jsonOutput.put("Id",id);
        }
        jsonOutput.put("AccountId",accountId);
        jsonOutput.put("FirstName",firstName);
        jsonOutput.put("LastName",lastName);
        jsonOutput.put("Title",title);
        jsonOutput.put("Department",department);
        jsonOutput.put("Phone",phone);
        jsonOutput.put("MobilePhone",mobile);
        jsonOutput.put("Email",email);
        jsonOutput.put("MailingStreet",street);
        jsonOutput.put("MailingPostalCode",postalCode);
        jsonOutput.put("MailingCity",city);
        jsonOutput.put("MailingState",state);
        jsonOutput.put("MailingCountry",country);
        jsonOutput.put("Description",description);
        
        return jsonOutput;
    }
    

    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountName(String name){
        accountName = name;
    }
    
    public String getAccountName(){
        return accountName;
    }

    public void setAccountId(String aid) {
        accountId = aid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getAddress(){
        return address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
