package com.example.pokeout.pokeout.Connect;

/**
 * Created by Z710 on 2018-02-17.
 */

public class ConnectObject {

    private String Id;
    private String Name;
    private String ImageUrl;
    private String Descryption;
    private String Brith;
    private String Sex;
    private String Phone;
    private String Distance;
    private String City;




    public ConnectObject (String Id, String Name, String ImageUrl, String Descryption, String Brith, String Sex, String Phone, String Distance, String City ){
        this.Id = Id;
        this.Name = Name;
        this.ImageUrl = ImageUrl;
        this.Descryption = Descryption;
        this.Brith = Brith;
        this.Sex = Sex;
        this.Phone = Phone;
        this.City=City;
        this.Distance=Distance;


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

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

}