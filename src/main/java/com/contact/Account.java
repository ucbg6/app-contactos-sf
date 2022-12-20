/*
    Uriel Caracuel Barrera - 2º DAM

 */
package com.contact;

import org.json.JSONObject;

/**
 *
 * @author ucb40
 */
public class Account {
    private String id;
    private String name;
    
    public Account(String id, String name){
        this.id = id;
        this.name = name;
    }
    
    public Account(JSONObject j){
        this.id = j.getString("Id");
        this.name = j.getString("Name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
}
