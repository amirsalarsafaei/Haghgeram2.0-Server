package com.SalarJavaDevGroup.util;

import com.SalarJavaDevGroup.Models.*;

import java.util.Comparator;

public abstract class Compare {

    public static Comparator<Tweet> compare_tweet_by_date = (Tweet t1, Tweet t2) -> t1.getCreatedDate().compareTo(t2.getCreatedDate());

    public static Comparator<Message> compare_message_by_date = (Message m1, Message m2) -> {
        if (m1.getTimeSent().isEqual(m2.getTimeSent()))
            return Integer.compare(m1.getId(), m2.getId());
        return m1.getTimeSent().compareTo(m2.getTimeSent());
    };

    public static Comparator<UserList> compare_lists_by_name = (UserList l1, UserList l2)-> l1.getName().compareTo(l2.getName());

    public static class compare_conversations implements Comparator<Conversation> {
        private User user;
        public compare_conversations(User user) {
            this.user = user;
        }
        @Override
        public int compare(Conversation o1, Conversation o2) {
            if (o1.getMessages().size() == 0 && o2.getMessages().size() == 0)
                return 0;
            if (o1.getMessages().size() == 0)
                return -1;
            if (o2.getMessages().size() == 0)
                return 1;
            if (o1.unread(user) > 0 && o2.unread(user) > 0)
                return o1.getLatestMessage().getTimeSent().compareTo(o2.getLatestMessage().getTimeSent());
            if (o1.unread(user) > 0)
                return 1;
            if (o2.unread(user) > 0)
                return -1;
            if (o1.getLatestMessage().getTimeSent().isEqual(o2.getLatestMessage().getTimeSent())) {
                return Integer.compare(o1.getId(), o2.getId());
            }
            return o1.getLatestMessage().getTimeSent().compareTo(o2.getLatestMessage().getTimeSent());
        }
    }
}
