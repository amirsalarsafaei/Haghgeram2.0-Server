package com.SalarJavaDevGroup.Models;

import com.SalarJavaDevGroup.ManageDB.ManageTweet;
import com.SalarJavaDevGroup.Models.Events.SendMessageToConversationEvent;
import com.SalarJavaDevGroup.util.Filter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="message")
public class Message {

    @ManyToOne
    private User user;
    private String Content;
    private LocalDateTime timeSent;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    private Tweet tweet;
    private byte[] image;
    @ManyToMany()
    @JoinTable(name="MessageReadRel",
            joinColumns = {@JoinColumn(name="message_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")})
    private Set<User> userReads = new HashSet<>();

    @ManyToOne()
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Message(SendMessageToConversationEvent event, User user, Conversation conversation) {
        this.user = user;
        Content = event.getContent();
        if (event.getLocalDateTime() == null || event.getLocalDateTime().isBefore(LocalDateTime.now()))
            timeSent = LocalDateTime.now();
        else
            timeSent = event.getLocalDateTime();
        image = event.getImage();
        this.conversation = conversation;
        messageStatus = MessageStatus.UnDelivered;
    }

    public Message(User user, String content, byte[] image, Conversation conversation) {
        this.user = user;
        this.Content = content;
        this.image = image;
        this.timeSent = LocalDateTime.now();
        this.conversation = conversation;
        messageStatus = MessageStatus.UnDelivered;
    }

    public Message(Tweet tweet, User user, Conversation conversation) {
        this.conversation = conversation;
        this.tweet = tweet;
        this.user = user;
        this.timeSent = LocalDateTime.now();
        messageStatus = MessageStatus.UnDelivered;
    }
    public Message() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<User> getUserReads() {
        return userReads;
    }

    public void setUserReads(Set<User> userReads) {
        this.userReads = userReads;
    }
}

