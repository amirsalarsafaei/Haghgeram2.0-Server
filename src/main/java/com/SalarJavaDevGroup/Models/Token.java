package com.SalarJavaDevGroup.Models;

import javax.persistence.*;

@Entity
@Table(name = "Tokens")
public class Token {
    private String token;
    @ManyToOne()
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    public Token() {

    }

    public Token(String token, User user){
        this.token = token;
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
