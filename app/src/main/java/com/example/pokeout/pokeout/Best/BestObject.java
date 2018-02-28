package com.example.pokeout.pokeout.Best;

/**
 * Created by Z710 on 2018-02-27.
 */

public class BestObject {

    private String Id;
    private String Name;
    private String ImageUrl;
    private String catDescryption;
    private String Count;




    public BestObject (String Id, String Name, String ImageUrl, String catDescryption, String Count){
        this.Id = Id;
        this.Name = Name;
        this.ImageUrl = ImageUrl;
        this.catDescryption = catDescryption;
        this.Count = Count;

    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCatDescryption() {
        return catDescryption;
    }
    public void setCatDescryption(String catDescryption) {
        catDescryption = catDescryption;
    }

    public String getCount() {
        return Count;
    }
    public void setCount(String count) {
        Count = count;
    }

}
