package com.example.pokeout.pokeout.UserDescryption;

/**
 * Created by Z710 on 2018-02-27.
 */

public class UserDescryptionObject {

    private String Id;
    private String Name;
    private String ImageUrl;





    public UserDescryptionObject(String Id, String Name, String ImageUrl){
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
