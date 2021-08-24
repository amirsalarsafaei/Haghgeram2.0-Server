package com.SalarJavaDevGroup.Models;

import com.SalarJavaDevGroup.Models.Events.SendCommentEvent;
import com.SalarJavaDevGroup.Models.Events.SendRetweetEvent;
import com.SalarJavaDevGroup.Models.Events.SendTweetEvent;
import com.SalarJavaDevGroup.ManageDB.ManageTweet;
import com.SalarJavaDevGroup.util.SessionUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//
@Entity
@Table(name = "tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @ManyToMany()
    @JoinTable(name="reportsRel",
            joinColumns={@JoinColumn(name="tweet_id")},
            inverseJoinColumns={@JoinColumn(name="reporter_id")})
    private Set<User> reportedBy = new HashSet<>();
    @ManyToOne()
    private User from;
    private String Content;
    private boolean hasImage = false;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @ManyToMany()
    @JoinTable(name="likesRel",
            joinColumns = {@JoinColumn(name = "tweet_id")},
            inverseJoinColumns = {@JoinColumn(name="user_id")})
    private Set<User> Likes = new HashSet<>();
    @ManyToOne()
    private Tweet retweeted;
    private LocalDateTime createdDate;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private byte[] image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<User> getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Set<User> reportedBy) {
        this.reportedBy = reportedBy;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public Set<User> getLikes() {
        return Likes;
    }

    public void setLikes(Set<User> likes) {
        Likes = likes;
    }

    public Tweet getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Tweet retweeted) {
        this.retweeted = retweeted;
    }

    public Set<Tweet> getComments() {
        return Comments;
    }

    public void setComments(Set<Tweet> comments) {
        Comments = comments;
    }

    public Set<Tweet> getRetweets() {
        return Retweets;
    }

    public void setRetweets(Set<Tweet> retweets) {
        Retweets = retweets;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @ManyToMany()
    @JoinTable(name="tweetToCommentsRel",
        joinColumns = {@JoinColumn(name = "tweet_id")},
        inverseJoinColumns = {@JoinColumn(name="comment_id")})
    private Set<Tweet> Comments = new HashSet<>();


    @ManyToMany()
    @JoinTable(name="retweetsRel",
            joinColumns = {@JoinColumn(name = "tweet_id")},
            inverseJoinColumns = {@JoinColumn(name="retweet_id")})
    private Set<Tweet> Retweets = new HashSet<>();
    private Boolean deleted = false;


    public Tweet(User from, String content, byte[] image, Tweet retweeted) {
        this.from = from;
        setContent(content);
        setCreatedDate(LocalDateTime.now());
        setHasImage(true);
        setImage(image);
        setRetweeted(retweeted);
    }

    public Tweet(User from, String content, Tweet retweeted) {
        this.from = from;
        setContent(content);
        setCreatedDate(LocalDateTime.now());
        setHasImage(false);
        setImage(null);
        setRetweeted(retweeted);
    }

    public Tweet(User from, String content) {
        this.from = from;
        setContent(content);
        setCreatedDate(LocalDateTime.now());
        setHasImage(false);
        setImage(null);

    }

    public Tweet(SendTweetEvent sendTweetEvent, User from) {
        setFrom(from);
        setContent(sendTweetEvent.getContent());
        setImage(sendTweetEvent.getImage());
        setCreatedDate(LocalDateTime.now());
    }

    public Tweet(SendRetweetEvent sendRetweetEvent, User from) {
        Session session = SessionUtil.getSession();
        setFrom(from);
        setContent(sendRetweetEvent.getContent());
        setImage(null);
        ManageTweet manageTweet = new ManageTweet(session);
        Tweet retweet = manageTweet.getTweet(sendRetweetEvent.getRetweeted());
        session.close();
        setRetweeted(retweet);
        setCreatedDate(LocalDateTime.now());
    }

    public Tweet(SendCommentEvent sendCommentEvent, User from) {
        setFrom(from);
        setContent(sendCommentEvent.getContent());
        setImage(sendCommentEvent.getImage());
        setCreatedDate(LocalDateTime.now());
    }

    public Tweet(){

    }

}
