/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

public class SFUser {
    int id;
    SFOrg org;
    String username = "";
    String securityToken;
    boolean valid = false;

    public SFUser(){
    }
    
    public SFUser(int id, SFOrg org, String user, String token){
        this.id = id;
        this.org = org;
        username = user;
        securityToken = token;

    }
    
    public String getLogin(){
        return "&username=" + username + "&password=";
    }
     
    @Override
    public String toString(){
        return username;
    }
    
    
}
