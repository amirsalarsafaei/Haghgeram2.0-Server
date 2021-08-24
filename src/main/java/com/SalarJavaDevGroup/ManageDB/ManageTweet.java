package com.SalarJavaDevGroup.ManageDB;

import com.SalarJavaDevGroup.Models.Tweet;
import com.SalarJavaDevGroup.Models.UserList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManageTweet {
    private static final Logger logger = LogManager.getLogger(ManageTweet.class);
    private Session session;
    public ManageTweet(Session session) {
        this.session = session;
    }


    public Tweet getTweet(int id) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM Tweet t WHERE t.id = " + id;
        Query query = session.createQuery(hql);
        logger.info("fetched tweets with id " + id);
        List list = query.list();
        if (list.size() == 0)
            return null;
        Tweet tweet = (Tweet)list.get(0);
        return tweet;
    }

    public List getAllTweets() {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM Tweet t";
        logger.info("fetched all tweets");
        Query query = session.createQuery(hql);
        List list = query.list();
        return list;
    }

    public void Save(Tweet tweet) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(tweet);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }
}
