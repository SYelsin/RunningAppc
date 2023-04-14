package com.example.runningapp.clases;


public class DataClass {
    private String imageURL, caption, userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DataClass(){

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public DataClass(String imageURL, String caption, String userName) {
        this.imageURL = imageURL;
        this.caption = caption;
        this.userName = userName;
    }


}