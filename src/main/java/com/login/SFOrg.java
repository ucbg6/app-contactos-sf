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
    final transient String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    final transient String REVOKESERVICE = "/services/oauth2/revoke?token=";
    
    private String name;
    private String domain;
    private String loginUrl;
    private String clientId;
    private String clientSecret;
    
    private ObservableList<SFUser> users = FXCollections.observableArrayList();
    private boolean valid = false;
    
    public SFOrg(){
    }
    
    public void setUsers(ObservableList<SFUser> list){
        users = FXCollections.observableArrayList(list);
    }
    
    public ObservableList<SFUser> getUsers(){
        return users;
    }
    
    public SFOrg(String orgname, String login, String cid, String secret){
        name = orgname;
        loginUrl = login;
        clientId = cid;
        clientSecret = secret;
    }
    
    public String getFullLoginURL(){
        return loginUrl + GRANTSERVICE + "&client_id=" + clientId + "&client_secret=" + clientSecret;
    }
    
    public String getLogoutURL(){
        return REVOKESERVICE;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public int getValidUsers(){
        int count = 0;
        for (SFUser user : users){
            if (user.isValid()){
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
    
    public int size(){
        return users.size();
    }
    
}
