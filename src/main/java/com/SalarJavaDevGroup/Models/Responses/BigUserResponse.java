package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.LastSeen;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.util.DateFormatter;
import com.SalarJavaDevGroup.util.Filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BigUserResponse {
    private String username, name, family_name, bio, date, birthDay;
    private ArrayList<String> followers, following;
    private byte[] image;
    private boolean isBlocked, isMuted, isFollowed, isPending;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public BigUserResponse(User from, User target) {
        username = target.getUsername();
        name = target.getName();
        family_name = target.getFamilyName();
        isBlocked = from.getBlocked().contains(target);
        isMuted = from.getMuted().contains(target);
        image = target.getImage();
        followers = new ArrayList<>();
        following = new ArrayList<>();
        for (User user : target.getFollowers()) {
            if (user.isActive())
                followers.add(user.getUsername());
        }
        for (User user : target.getFollowing())
            if (user.isActive())
            following.add(user.getUsername());
        if (from == target)
            date = "Online";
        else if (!from.getFollowing().contains(target) || from.getLastSeenSetting() == LastSeen.noOne)
            date = "Last seen recently";
        else if (from.getLastSeenSetting() == LastSeen.Followings && from.getFollowers().contains(target))
            date = "Last seen recently";
        else if (target.getLastOnline().plusSeconds(30).isBefore(LocalDateTime.now()))
            date = "Online";
        else
            date = DateFormatter.getDateBasic(target.getLastOnline());
        bio = target.getBio();
        LocalDate time = target.getBirthDate();
        if (time == null)
            birthDay = "";
        else
            birthDay =  time.getMonth().toString().toLowerCase() + " " + time.getDayOfMonth();
        isFollowed = from.getFollowing().contains(target);
        isPending = from.getPending().contains(target);
        id = target.getId();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
