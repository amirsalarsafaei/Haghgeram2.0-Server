package com.SalarJavaDevGroup.Models;

import com.SalarJavaDevGroup.ManageDB.ManageConversation;
import com.SalarJavaDevGroup.Models.Events.AuthEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "USERS")
public class User {
    private static final Logger logger = LogManager.getLogger(User.class);
    @Column(nullable = false)
    private String Password;

    public Set<User> getFollowRequests() {
        return FollowRequests;
    }

    public void setFollowRequests(Set<User> followRequests) {
        FollowRequests = followRequests;
    }

    public Set<UserList> getLists() {
        return lists;
    }

    public void setLists(Set<UserList> lists) {
        this.lists = lists;
    }

    @Column(nullable = false)
    private String Name;
    @Column(nullable = false)
    private String FamilyName;
    @Column(nullable = false, unique = true)
    private String Username;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false, unique = true)
    private String Email;
    private String PhoneNumber;
    private LocalDate BirthDate;
    private String Bio;
    private Boolean Active;
    private LocalDateTime LastOnline;
    private Boolean isOnline;
    private boolean deleted = false;


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @ElementCollection
    List<String> events = new LinkedList<>();


    public Boolean getActive() {
        return Active;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    @ManyToMany()
    @JoinTable(name="UserlikesRel",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name="tweet_id")})
    private Set<Tweet> Likes = new HashSet<>();
    @ManyToMany( )
    @JoinTable(name="userToTweetsRel",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name="tweet_id")})
    private Set<Tweet> tweets = new HashSet<>();

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private byte[] image;

    public Set<Tweet> getLikes() {
        return Likes;
    }

    public void setLikes(Set<Tweet> likes) {
        Likes = likes;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
        this.tweets = tweets;
    }

    public Set<Tweet> getComments() {
        return comments;
    }

    public void setComments(Set<Tweet> comments) {
        this.comments = comments;
    }

    @ManyToMany()
    @JoinTable(name="userToCommentsRel",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name="tweet_id")})
    private Set<Tweet> comments = new HashSet<>();
    @ManyToMany()
    @JoinTable(name="Following",
        joinColumns={@JoinColumn(name="follower_id")},
        inverseJoinColumns={@JoinColumn(name="following_id")})
    private Set<User> Following = new HashSet<>();

    public Set<User> getBlocked() {
        return Blocked;
    }

    public void setBlocked(Set<User> blocked) {
        Blocked = blocked;
    }

    public Set<User> getMuted() {
        return Muted;
    }

    public void setMuted(Set<User> muted) {
        Muted = muted;
    }

    public Set<User> getBlockedBy() {
        return BlockedBy;
    }

    public void setBlockedBy(Set<User> blockedBy) {
        BlockedBy = blockedBy;
    }

    @ManyToMany()
    @JoinTable(name="Followers",
            joinColumns={@JoinColumn(name="following_id")},
            inverseJoinColumns={@JoinColumn(name="follower_id")})
    private Set<User> Followers = new HashSet<>();

    public Set<User> getFollowing() {
        return Following;
    }

    public void setFollowing(Set<User> following) {
        Following = following;
    }

    public Set<User> getFollowers() {
        return Followers;
    }

    public void setFollowers(Set<User> followers) {
        Followers = followers;
    }

    @ManyToMany()
    @JoinTable(name="BlockedRel",
            joinColumns={@JoinColumn(name="blocker_id")},
            inverseJoinColumns={@JoinColumn(name="blocked_id")})
    private Set<User> Blocked = new HashSet<>();

    @ManyToMany()
    @JoinTable(name="MuteRel",
            joinColumns={@JoinColumn(name="muter_id")},
            inverseJoinColumns={@JoinColumn(name="muted_id")})
    private Set<User> Muted = new HashSet<>();

    @ManyToMany()
    @JoinTable(name="BlockedByRel",
            joinColumns={@JoinColumn(name="blocked_id")},
            inverseJoinColumns={@JoinColumn(name="blocker_id")})
    private Set<User> BlockedBy = new HashSet<>();

    @ManyToMany()
    @JoinTable(name="FollowReqs",
            joinColumns={@JoinColumn(name="requested_id")},
            inverseJoinColumns={@JoinColumn(name="requester_id")})
    private Set<User> FollowRequests = new HashSet<>();


    @ManyToMany()
    @JoinTable(name="UserToListRel",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="list_id")})
    private Set<UserList> lists = new HashSet<>();


    @ManyToMany()
    @JoinTable(name="PendingReqs",
            joinColumns={@JoinColumn(name="receiver")},
            inverseJoinColumns={@JoinColumn(name="sender")})
    private Set<User> Pending = new HashSet<>();
    @ManyToMany()
    @JoinTable(name="DeniedReqs",
            joinColumns={@JoinColumn(name="receiver")},
            inverseJoinColumns={@JoinColumn(name="sender")})
    private Set<User> Denied = new HashSet<>();


    @ManyToMany()
    @JoinTable(name="AcceptedReqs",
            joinColumns={@JoinColumn(name="receiver")},
            inverseJoinColumns={@JoinColumn(name="sender")})
    private Set<User> Accepted = new HashSet<>();

    @ManyToMany()
    @JoinTable(name="UserToConversations",
            joinColumns={@JoinColumn(name="user_id")},
            inverseJoinColumns={@JoinColumn(name="conversation_id")})
    private Set<Conversation> conversations = new HashSet<>();

    private Boolean privateAccount;
    @Enumerated(EnumType.STRING)
    private LastSeen lastSeenSetting;
    //Creates new User



    public User(AuthEvent authEvent) {
        setName(authEvent.name);
        setEmail(authEvent.email);
        setFamilyName(authEvent.family_name);
        setUsername(authEvent.username);
        setPassword(authEvent.password);
        setActive(true);
        setLastOnline(LocalDateTime.now());
        setBio(authEvent.bio);
        setBirthDate(authEvent.birthDate);
        setLastSeenSetting(LastSeen.Everyone);
        setPhoneNumber(authEvent.phone);
        setImage(authEvent.image);
        setPrivateAccount(false);
    }



    public User(){}

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFamilyName() {
        return FamilyName;
    }

    public void setFamilyName(String familyName) {
        FamilyName = familyName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        BirthDate = birthDate;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public Boolean isActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }

    public LocalDateTime getLastOnline() {
        return LastOnline;
    }

    public void setLastOnline(LocalDateTime lastOnline) {
        LastOnline = lastOnline;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Boolean getPrivateAccount() {
        return privateAccount;
    }

    public void setPrivateAccount(Boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    public LastSeen getLastSeenSetting() {
        return lastSeenSetting;
    }

    public void setLastSeenSetting(LastSeen lastSeenSetting) {
        this.lastSeenSetting = lastSeenSetting;
    }

    public Set<User> getPending() {
        return Pending;
    }

    public void setPending(Set<User> pending) {
        Pending = pending;
    }

    public Set<User> getDenied() {
        return Denied;
    }

    public void setDenied(Set<User> denied) {
        Denied = denied;
    }

    public Set<User> getAccepted() {
        return Accepted;
    }

    public void setAccepted(Set<User> accepted) {
        Accepted = accepted;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public User(
            String UserName,
            String Password,
            String Name,
            String FamilyName,
            LocalDate BirthDate,
            String Email,
            String PhoneNumber,
            String Bio) {
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
        this.BirthDate = BirthDate;
        this.Name = Name;
        this.Password = Password;
        this.FamilyName = FamilyName;
        this.Username = UserName;
        this.Bio = Bio;
        this.LastOnline = LocalDateTime.now();
        this.Active = true;
        privateAccount = false;
        lastSeenSetting = LastSeen.Everyone;
        logger.info("User " + id + " Created");
    }
}
