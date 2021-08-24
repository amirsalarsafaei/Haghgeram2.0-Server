package com.SalarJavaDevGroup.ManageDB;

import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.Message;
import com.SalarJavaDevGroup.Models.UserList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManageMessages {
    private static final Logger logger = LogManager.getLogger(ManageMessages.class);
    private Session session;
    public ManageMessages(Session session) {
        this.session = session;
    }


    public Message getMessage(int id) {
        String hql = "FROM Message t WHERE t.id = " + id;
        Query query = session.createQuery(hql);
        logger.info("fetched message with id " + id);
        List list = query.list();
        if (list.size() == 0)
            return null;
        Message message = (Message) list.get(0);
        return message;
    }

    public void Save(Message message) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(message);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }
}
