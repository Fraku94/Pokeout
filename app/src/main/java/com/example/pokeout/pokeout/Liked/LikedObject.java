package com.example.pokeout.pokeout.Liked;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedObject {


    private String categoryId;
    private String name;
    private String categoryImageUrl;



    public LikedObject (String categoryId, String name, String categoryImageUrl){
        this.categoryId = categoryId;
        this.name = name;
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategoryId(){
        return categoryId;
    }
    public void setCategoryID(String categoryId){
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }
}
