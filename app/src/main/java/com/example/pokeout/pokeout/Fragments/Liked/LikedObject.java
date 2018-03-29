package com.example.pokeout.pokeout.Fragments.Liked;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedObject {

    private String categoryId;
    private String name;
    private String categoryImageUrl;
    private String catDescryption;
    private String count;

    public LikedObject (String categoryId, String name, String categoryImageUrl,String catDescryption, String count){
        this.categoryId = categoryId;
        this.name = name;
        this.categoryImageUrl = categoryImageUrl;
        this.catDescryption = catDescryption;
        this.count = count;
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
    public String getCatDescryption() {
        return catDescryption;
    }
    public void setCatDescryption(String catDescryption) {
        catDescryption = catDescryption;
    }

    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
}
