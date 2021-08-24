package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;

public class SmallUserResponse {
    private String username;
    private int id;
    private byte[] image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public SmallUserResponse(User user) {
        this.id = user.getId();
        this.image = user.getImage();
        this.username = user.getUsername();
    }
}
