package com.example.rfarm.constructors;

public class Userinformation {
    public String imgUrl;
    public String name;

    public String phoneno;

    public Userinformation(){
        //for datasnapshot
    }

    public Userinformation(String imgUrl, String name, String phoneno) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.phoneno = phoneno;
    }

    public Userinformation(String name, String phoneno){
        this.name = name;

        this.phoneno = phoneno;
    }
    public String getUserName() {
        return name;
    }

    public String getUserPhoneno() {
        return phoneno;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}