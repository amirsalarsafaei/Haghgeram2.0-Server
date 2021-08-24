package com.SalarJavaDevGroup.ManageDB;

import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Tweet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManageConversation {
    private static final Logger logger = LogManager.getLogger(ManageConversation.class);
    private Session session;
    public ManageConversation(Session session) {
        this.session = session;
    }


    public Conversation getConversation(int id) {
        String hql = "FROM Conversation t WHERE t.id = " + id;
        Query query = session.createQuery(hql);
        logger.info("fetched conversations with id " + id);
        List list = query.list();
        if (list.size() == 0)
            return null;
        Conversation tweet = (Conversation) list.get(0);
        return tweet;
    }

    public void Save(Conversation conversation) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(conversation);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }
}
