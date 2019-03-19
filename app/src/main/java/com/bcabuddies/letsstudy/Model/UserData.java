package com.bcabuddies.letsstudy.Model;

public class UserData {
    public String name, profileURL;

    public UserData() {
    }

    public UserData(String name, String profileURL) {
        this.name = name;
        this.profileURL = profileURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
