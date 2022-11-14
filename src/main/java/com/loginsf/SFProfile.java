/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

/**
 *
 * @author ucb40
 */
public class SFProfile {
    String name;
    SFOrg org;
    String username;
    String securityToken;

    
    public SFProfile(String name, SFOrg userorg, String user, String token){
        this.name = name;
        org = userorg;
        username = user;
        securityToken = token;

    }
    
    public String getLoginURL(){
        return org.getLoginURL() + "&username=" + username + "&password=";
    }
    
}
