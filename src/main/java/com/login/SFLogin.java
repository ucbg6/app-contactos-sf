/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import com.dialog.NetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpClientHelper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import org.apache.http.HttpStatus;

public class SFLogin {
    // Org y usuario
    private final SFOrg org;
    private final SFUser user;
    private String password;
    
    Alert loginMessage;
    
    // Estado y token
    IntegerProperty statusCode = new SimpleIntegerProperty(0);
    private String getResult;
    private String loginUrl;
    private String loginAccessToken;
    private String loginInstanceUrl;
    
    Runnable onSuccess;
    Runnable onError;
    
    private Timer timer = new Timer();
    private TimerTask task;
    
    private boolean doLogin = false;

    private Future<HttpResponse<String>> response = null;
    
    public SFLogin(SFOrg org, SFUser user, String pass){
        this.org = org;
        this.user = user;
        password = pass;
    }

    public String getInstanceURL(){
        return loginInstanceUrl;
    }
    
    public String getAccessToken(){
        return loginAccessToken;
    }
    
    public String getUsername(){
        return user.getUsername();
    }
    
    public String getResult(){
        return getResult;
    }
    
    public void connect(boolean login){
        doLogin = login;
        NetDialog net = new NetDialog("Login Salesforce");
        if (!net.getOnline()){
            return;
        }
        
        if (!password.isEmpty()) {
            loginUrl = org.getFullLoginURL() + user.getLogin() + password + user.getSecurityToken();
            clear();
            HttpRequest post = Unirest.post(loginUrl);
            response = HttpClientHelper.requestAsync(post, String.class, new Callback<String>(){
                @Override
                public void completed(HttpResponse<String> res) {
                    getResult = res.getBody();
                    statusCode.set(res.getStatus());
                    
                    if (statusCode.get() == HttpStatus.SC_OK){
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(getResult, JsonObject.class);

                        loginAccessToken = jsonObject.get("access_token").getAsString();
                        loginInstanceUrl = jsonObject.get("instance_url").getAsString();
                        org.setDomain(loginInstanceUrl);

                        Platform.runLater(onSuccess);
                        
                    } else {
                        Platform.runLater(onError);
                        
                    }
                    timer.cancel();
                }

                @Override
                public void failed(UnirestException ue) {
                    Platform.runLater(onError);
                    timer.cancel();
                    
                }

                @Override
                public void cancelled() {
                    // runOnError();
                    System.out.println("Login cancelled");
                }
            });
            setTimer();
        }
    }
    
    public void revoke(){
        loginUrl = org.getDomain() + org.getLogoutURL() + loginAccessToken;
        System.out.println(loginUrl);
        
        HttpRequest post = Unirest.post(loginUrl);
        response = HttpClientHelper.requestAsync(post, String.class, new Callback<String>() {
            @Override
            public void completed(HttpResponse<String> res) {
                statusCode.set(res.getStatus());
                if (statusCode.get() == 200) {
                    Platform.runLater(onSuccess);

                } else {
                    Platform.runLater(onError);
                    System.out.println("Status: " + statusCode.get());

                }
                timer.cancel();
            }

            @Override
            public void failed(UnirestException ue) {
                Platform.runLater(onError);
                timer.cancel();

            }

            @Override
            public void cancelled() {}
        });
    }
    
    public void setTimer(){
        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Time's up!");
                try {
                    response.get();
                } catch (InterruptedException | ExecutionException ex) {
                    Platform.runLater(onError);
                }
            }
        };
        timer.schedule(task, 5000);
        System.out.println("Timer set.");
    }
    
    public void clear(){
        password = "";
    }
    
    public void setOnSuccess(Runnable toRun){
        onSuccess = toRun;
    }
    
    public void setOnError(Runnable toRun){
        onError = toRun;
    }
}
