package com.example.pokeout.pokeout.Search;

/**
 * Created by simco on 1/24/2018.
 */

public class SearchObject {
    private String email;
    private String uid;

    public SearchObject(String email, String uid){
        this.email = email;
        this.uid = uid;
    }

    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
}
