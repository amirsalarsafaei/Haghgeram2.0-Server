package com.SalarJavaDevGroup.Models.Events;

import java.time.LocalDate;

public class AuthEvent {
    public String username, password, name, family_name, bio, email, phone;
    public LocalDate birthDate;
    public byte[] image;
}
