/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.contact;

import com.login.SFLogin;
import com.mashape.unirest.http.HttpClientHelper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import java.util.ArrayList;
import java.util.concurrent.Future;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ucb40
 */
public class RestOperation {
    final String REST_ENDPOINT = "/services/data/";
    final String API_VERSION = "v53.0";
    private SFLogin login;
    private String baseUri;
    private String operation;
    private String getResult;
    private ArrayList<JSONObject> objects = new ArrayList<>();
    private JSONObject json = null;
    private Future<HttpResponse<String>> response = null;
    private Map<String,String> headers = new HashMap<>();
    
    private Runnable onSuccess;
    private Runnable onError;
    
    Timer timer;
    TimerTask task;
    
    public RestOperation(String name, JSONObject jo, SFLogin sfl){
        operation = name;
        login = sfl;
        json = jo;
        if (json != null){
            // json == null -> Es una query
            // objects = json;
        }
        
        
        baseUri = login.getInstanceURL() + REST_ENDPOINT + API_VERSION;
        // System.out.println(baseUri);
        headers.put("Authorization", "OAuth " + login.getAccessToken());
        headers.put("X-PrettyPrint", "1");
        headers.put("Content-Type","application/json");
    }
    
    public void run(){
        if (operation.equals("query contact")){
            // System.out.println("Query contact");
            query("Contact");
        }
        if (operation.equals("query account")){
            query("Account");
        }
        if (operation.equals("insert contact")){
            insert("Contact");
        }
        if (operation.equals("insert account")){
            insert("Account");
        }
        if (operation.equals("update contact")){
            update();
        }
        if (operation.equals("delete contact")){
            delete();
        }
    }
    
    private void query(String sobject){
        String uri = "";
        if (sobject.equals("Contact")) {
            uri = baseUri + "/query?q=Select+Id+,+FirstName+,+LastName+,+AccountId+,+Title+,+Department+,+Phone+,+MobilePhone+,+Email+,+" +
                    "MailingStreet+,+MailingPostalCode+,+MailingCity+,+MailingState+,+MailingCountry+,+Description+From+Contact";
        }
        else {
            uri = baseUri + "/query?q=Select+Id+,+Name+From+Account";
        }

        HttpRequest get = Unirest.get(uri);
        get.headers(headers);

        response = HttpClientHelper.requestAsync(get, String.class, new Callback<String>() {
        @Override
            public void completed(HttpResponse<String> res) {
                getResult = res.getBody();
                // System.out.println(getResult);

                JSONObject json = new JSONObject(getResult);
                JSONArray records = json.getJSONArray("records");
                System.out.println(records.length());

                for (int i = 0; i < records.length(); i++){
                    // System.out.println(records.getJSONObject(i));
                    // System.out.println(records.getJSONObject(i));
                    objects.add(records.getJSONObject(i));
                }

                Platform.runLater(onSuccess);

            }

            @Override
            public void failed(UnirestException ue) {
                Platform.runLater(onError);

            }

            @Override
            public void cancelled() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    private void insert(String sobject){
        String uri = baseUri + "/sobjects/" + sobject + "/";
        
        HttpRequestWithBody post = Unirest.post(uri);
        post.body(json);
        post.headers(headers);
        
        response = HttpClientHelper.requestAsync(post, String.class, new Callback<String>() {
        @Override
        public void completed(HttpResponse<String> res) {
            getResult = res.getBody();
            if (res.getStatus() == 201){
                JSONObject json = new JSONObject(getResult);
                objects.add(json);
                Platform.runLater(onSuccess);
            } else {
                System.out.println(getResult);
                Platform.runLater(onError);
            }
            

        }

        @Override
        public void failed(UnirestException ue) {
            Platform.runLater(onError);

        }

        @Override
        public void cancelled() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        });
        
        
    }
    
    private void update(){
        String uri = baseUri + "/sobjects/Contact/" + json.getString("Id");
        System.out.println(uri);
        
        JSONObject body = new JSONObject(json);
        body.remove("Id");

        HttpRequestWithBody patch = Unirest.patch(uri);
        patch.body(body);
        patch.headers(headers);

        response = HttpClientHelper.requestAsync(patch, String.class, new Callback<String>() {
            @Override
            public void completed(HttpResponse<String> res) {
                System.out.println(res.getStatus());
                if (res.getStatus() == 204) {
                    Platform.runLater(onSuccess);
                } else {
                    
                    Platform.runLater(onError);
                }

            }

            @Override
            public void failed(UnirestException ue) {
                Platform.runLater(onError);

            }

            @Override
            public void cancelled() {}
        });
    }
    
    private void delete(){
        
    }
    
    public ArrayList<JSONObject> getObjects(){
        return objects;
    }
    
    public JSONObject getJSONObject(){
        return json;
    }
    
    public void setOnSuccess(Runnable toRun){
        onSuccess = toRun;
    }
    
    public void setOnError(Runnable toRun){
        onError = toRun;
    }
    
    public void runOnSuccess(){
        if (onSuccess != null){
            onSuccess.run();
        }
    }
    
    public void runOnError(){
        if (onError != null){
            onError.run();
        }
    }
    
}
