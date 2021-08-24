package com.SalarJavaDevGroup.ManageDB;

import com.SalarJavaDevGroup.Models.Token;
import com.SalarJavaDevGroup.Models.Tweet;
import com.SalarJavaDevGroup.Models.UserList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManageTokens {
    private static final Logger logger = LogManager.getLogger(ManageTokens.class);
    private Session session;
    public ManageTokens(Session session) {
        this.session = session;
    }


    public List getAllTokens() {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM Token t";
        Query query = session.createQuery(hql);
        logger.info("fetched all tokens");
        List list = query.list();
        return list;
    }
    public void Save(Token token) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(token);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }

}
