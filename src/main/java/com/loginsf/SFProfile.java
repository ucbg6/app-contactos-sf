/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.loginsf;

import javafx.collections.ObservableList;

/**
 *
 * @author ucb40
 */
public class SFProfile {
    int id;
    String name;
    SFOrg org;
    String username = "";
    String securityToken;

    public SFProfile(){
        id = 0;
        name = "Nuevo";
    }
    
    public SFProfile(int id, String name, SFOrg userorg, String user, String token){
        this.id = id;
        this.name = name;
        org = userorg;
        username = user;
        securityToken = token;

    }
    
    public String getLoginURL(){
        return org.getLoginURL() + "&username=" + username + "&password=";
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
