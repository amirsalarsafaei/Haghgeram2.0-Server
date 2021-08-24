package com.SalarJavaDevGroup.Models;

import com.SalarJavaDevGroup.ManageDB.ManageUser;
import com.SalarJavaDevGroup.Models.Events.AddMembersToGroupEvent;
import com.SalarJavaDevGroup.util.Compare;
import com.SalarJavaDevGroup.util.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

//Conversations which holds messages
@Entity
@Table(name="Conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToMany()
    @JoinTable(name="ConversationToUsers",
            joinColumns = {@JoinColumn(name="conversation_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")})
    private Set<User> users = new HashSet<>();
    @ManyToMany()
    @JoinTable(name="ConversationToMessages",
            joinColumns = {@JoinColumn(name="conversation_id")},
            inverseJoinColumns = {@JoinColumn(name="message_id")})
    private Set<Message> messages = new HashSet<>();
    private String name;
    private boolean isGroup;

    public Conversation(ArrayList<User> user) {
        users.addAll(user);
        isGroup = true;
    }

    public Conversation(User user) {
        users.add(user);
        isGroup = false;
        name="Saved Messages";
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public String getName(User self) {
        if (name == null) {
            if (!isGroup) {
                for (User user: getUsers())
                    if (user != self)
                        return user.getName();
            }
            String res = "";
            ArrayList<User> users = new ArrayList<>(getUsers());
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User user, User t1) {
                    return user.getName().compareTo(t1.getName());
                }
            });
            res = users.get(0).getName();
            for (int i = 1; i < users.size(); i++)
                res += ", " + users.get(i).getName();
            return res;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getLatestMessage() {
        if (getMessages().size() == 0)
            return null;
        return Collections.max(messages, Compare.compare_message_by_date);
    }

    public int unread(User user) {
        int unread = 0;
        for (Message message: getMessages())
            if (!message.getUserReads().contains(user) && message.getUser() != user)
                unread++;
        return unread;
    }


    public Conversation(User p1, User p2) {
        users.add(p1);
        users.add(p2);
        isGroup = false;
    }


    public Conversation() {

    }
}
