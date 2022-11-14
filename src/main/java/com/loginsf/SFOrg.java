/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

/**
 *
 * @author ucb40
 */
public class SFOrg {
    private final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    
    String name;
    String loginUrl;
    String clientId;
    String clientSecret;
    
    public SFOrg(String orgname, String login, String id, String secret){
        name = orgname;
        loginUrl = login;
        clientId = id;
        clientSecret = secret;
    }
    
    public String getLoginURL(){
        return loginUrl + GRANTSERVICE + "&client_id=" + clientId + "&client_secret=" + clientSecret;
    }
}
