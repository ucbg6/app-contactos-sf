/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

public class SFLogin {
    // Org y usuario
    private final SFOrg org;
    private final SFUser user;
    private final String password;
    
    Button loginBtn;
    Alert loginMessage;
    
    // Estado y token
    IntegerProperty statusCode = new SimpleIntegerProperty(0);
    private String getResult;
    private String loginUrl;
    private String loginAccessToken;
    private String loginInstanceUrl;
    
    HttpClient client = HttpClientBuilder.create().build();
    
    public SFLogin(SFOrg org, SFUser user, String pass, Button loginButton){
        this.org = org;
        this.user = user;
        password = pass;
        loginBtn = loginButton;
    }
    
    public HttpResponse connect(){
        HttpResponse response = null;
        if (!password.isEmpty()) {
            loginUrl = org.getLoginURL() + user.getLogin() + password + user.securityToken;
            System.out.println("Login URL: " + loginUrl);
            HttpPost post = new HttpPost(loginUrl);
            try {
                response = client.execute(post);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        return response;
    }
    
    public void login(){
        HttpResponse response = connect();
        if (response == null){
            System.out.println("A response was not received");
            return;
        }
        try {
            getResult = EntityUtils.toString(response.getEntity());
        } catch (IOException ioException) {
            // Handle system IO exception
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(getResult, JsonObject.class);
        System.out.println(jsonObject);
        
        statusCode.set(response.getStatusLine().getStatusCode());
        
        if (statusCode.get() == HttpStatus.SC_OK){
            try {
            loginAccessToken = jsonObject.get("access_token").getAsString();
            loginInstanceUrl = jsonObject.get("instance_url").getAsString();
        } catch (JSONException jsonException) {}
            System.out.println("Successful login");
            System.out.println("  instance URL: " + loginInstanceUrl);
            System.out.println("  access token/session ID: " + loginAccessToken);
        } else {
            System.out.println("Error authenticating to Force.com: " + statusCode);
        }
        
        
        
            
        
    }
    
    public void setOnResponse(Runnable toRun){
        
    }
}
