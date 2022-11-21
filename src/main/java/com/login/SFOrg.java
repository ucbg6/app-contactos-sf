/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ucb40
 */
public class SFOrg {
    private final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    private final String REVOKESERVICE = "/services/oauth2/token/revoke";
    
    int id;
    String name;
    String domain;
    String loginUrl;
    String clientId;
    String clientSecret;
    
    ObservableList<SFUser> users = FXCollections.observableArrayList();
    boolean valid = false;
    
    public SFOrg(){
    }
    
    public int getId(){
        return id;
    }
    
    public ObservableList<SFUser> getUsers(){
        return users;
    }
    
    public SFOrg(int orgid, String orgname, String login, String cid, String secret){
        id = orgid;
        name = orgname;
        loginUrl = login;
        clientId = cid;
        clientSecret = secret;
    }
    
    public String getLoginURL(){
        return loginUrl + GRANTSERVICE + "&client_id=" + clientId + "&client_secret=" + clientSecret;
    }
    
    public int getValidUsers(){
        int count = 0;
        for (SFUser user : users){
            if (user.valid){
                count++;
            }
        }
        return count;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    // Todos los usuarios deben de estar validados para guardar la org
    public boolean validate(){
        valid = getValidUsers() == users.size();
        return valid;
    }
}
