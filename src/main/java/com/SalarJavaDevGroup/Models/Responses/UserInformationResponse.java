package com.SalarJavaDevGroup.Models.Responses;

import com.SalarJavaDevGroup.Models.User;

import java.time.LocalDate;

public class UserInformationResponse {
    private String name, familyName, phone, email, bio, password;
    private LocalDate birthDate;
    private byte[] image;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInformationResponse(User user) {
        name = user.getName();
        familyName = user.getFamilyName();
        phone = user.getPhoneNumber();
        email = user.getEmail();
        bio = user.getBio();
        birthDate = user.getBirthDate();
        password = user.getPassword();
        image = user.getImage();
    }

    public UserInformationResponse(String name, String familyName, String phone, String email, String bio, LocalDate birthDate) {
        this.name = name;
        this.familyName = familyName;
        this.phone = phone;
        this.email = email;
        this.bio = bio;
        this.birthDate = birthDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
