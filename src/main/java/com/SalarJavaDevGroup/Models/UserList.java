package com.SalarJavaDevGroup.Models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name="UserLists")
public class UserList {

    public UserList() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany()
    @JoinTable(name="UserListToUserRel",
            joinColumns = {@JoinColumn(name = "list_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")})
    private Set<User> users = new HashSet<>();
    private String name;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserList(String name) {
        this.name = name;
    }
}
