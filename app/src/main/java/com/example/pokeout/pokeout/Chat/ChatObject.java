package com.example.pokeout.pokeout.Chat;

/**
 * Created by Z710 on 2018-02-17.
 */

public class ChatObject {
    private String message;
    private String timeH;
    private int timeD;
    private int timeM;
    private int timeY;

    private Boolean currentUser;



    public ChatObject(String message, String timeH,int timeD, int timeM, int timeY, Boolean currentUser){
        this.message = message;
        this.timeH = timeH;
        this.timeD = timeD;
        this.timeM = timeM;
        this.timeY = timeY;
        this.currentUser = currentUser;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }

    public String getTimeH() {
        return timeH;
    }
    public void setTimeH(String timeH) {
        this.timeH = timeH;
    }

    public int getTimeD() {
        return timeD;
    }
    public void setTimeD(int timeD) {
        this.timeD = timeD;
    }

    public int getTimeM() {
        return timeM;
    }
    public void setTimeM(int timeM) {
        this.timeM = timeM;
    }

    public int getTimeY() {
        return timeY;
    }
    public void setTimeY(int timeY) {
        this.timeY = timeY;
    }

}