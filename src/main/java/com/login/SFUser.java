/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.login;

public class SFUser {
    private String username = "";
    private String securityToken;
    private boolean valid = false;
    
    public SFUser(){
        
    }
    
    public String getLogin(){
        return "&username=" + username + "&password=";
    }
    
    public void setUsername(String name){
        username = name;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setToken(String token){
        securityToken = token;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    
     
    @Override
    public String toString(){
        return username;
    }
    
    
}
