package com.example.pokeout.pokeout.UsersInCategory;

/**
 * Created by Z710 on 2018-02-26.
 */

public class UsersInCategoryObject {

    private String Id;
    private String Name;
    private String ImageUrl;
    private String Descryption;
    private String Brith;
    private String Sex;
    private String Phone;




    public UsersInCategoryObject (String Id, String Name, String ImageUrl, String Descryption, String Brith, String Sex, String Phone){
        this.Id = Id;
        this.Name = Name;
        this.ImageUrl = ImageUrl;
        this.Descryption = Descryption;
        this.Brith = Brith;
        this.Sex = Sex;
        this.Phone = Phone;
    }

    public String getId(){
        return Id;
    }
    public void setId(String Id){
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getDescryption() {
        return Descryption;
    }
    public void setDescryption(String descryption) {
        Descryption = descryption;
    }

    public String getBrith() {
        return Brith;
    }
    public void setBrith(String brith) {
        Brith = brith;
    }

    public String getSex() {
        return Sex;
    }
    public void setSex(String sex) {
        Sex = sex;
    }

    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }
}