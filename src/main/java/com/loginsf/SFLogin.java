/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class SFLogin {
    private SFProfile profile;
    private String password;
    private String loginUrl;
    
    
    
    public SFLogin(SFProfile sfp, String pass){
        profile = sfp;
        password = pass;
    }
    
    public void login(){
        if (!password.isEmpty()){
            HttpClient client = HttpClientBuilder.create().build();
            loginUrl = profile.getLoginURL() + password;
            HttpPost post = new HttpPost(loginUrl);
            HttpResponse response = null;
            try{
                response = client.execute(post);
            } catch (IOException e){
                e.printStackTrace();
            }
            
            int statusCode = response.getStatusLine().getStatusCode();
            
        }
    }
    
    public void oAuthSessionProvider(){
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost();
            
        } catch (IOException e){
            
        }
    }
}
