package com.SalarJavaDevGroup.ManageDB;

import com.SalarJavaDevGroup.Models.Conversation;
import com.SalarJavaDevGroup.Models.UserList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManageList {
    private static final Logger logger = LogManager.getLogger(ManageList.class);
    private Session session;
    public ManageList(Session session) {
        this.session = session;
    }


    public UserList getListById(int id) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM UserList t WHERE t.id = " + id;
        Query query = session.createQuery(hql);
        logger.info("fetched lists with id " + id);
        List list = query.list();
        if (list.size() == 0)
            return null;
        UserList userList = (UserList) list.get(0);
        return userList;
    }

    public UserList getListByName(String name) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM UserList t WHERE t.name = \'" + name + "\'";
        logger.info("fetched lists with name " + name);
        Query query = session.createQuery(hql);
        List list = query.list();
        if (list.size() == 0)
            return null;
        UserList userList = (UserList) list.get(0);
        return userList;
    }

    public void Save(UserList userList) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(userList);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }
}
