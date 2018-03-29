package com.example.pokeout.pokeout.CategoryDescryption;

/**
 * Created by Z710 on 2018-02-27.
 */

public class CategoryDescryptionObject {

    private String Id;
    private String Name;
    private String ImageUrl;





    public CategoryDescryptionObject(String Id, String Name, String ImageUrl){
        this.Id = Id;
        this.Name = Name;
        this.ImageUrl = ImageUrl;
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


}
