package com.SalarJavaDevGroup.Models.Events;

import java.time.LocalDate;

public class EditProfileEvent {
    private String name, familyName, password, bio, email, phoneNumber;
    private LocalDate birthDate;
    private byte[] image;
    public EditProfileEvent(String name, String familyName, String password, String bio, String email, LocalDate birthDate,
                            byte[] image) {
        this.name = name;
        this.familyName = familyName;
        this.password = password;
        this.bio = bio;
        this.email = email;
        this.birthDate = birthDate;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
