package com.SalarJavaDevGroup.ManageDB;

import com.SalarJavaDevGroup.MiddleWare.Router;
import com.SalarJavaDevGroup.Models.User;
import com.SalarJavaDevGroup.Models.UserList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManageUser {
    private static final Logger logger = LogManager.getLogger(ManageUser.class);
    Session session;
    public ManageUser(Session session) {
        this.session = session;
    }


    public boolean usernameExists(String username) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM User E WHERE E.Username = \'" + username + "\'";
        logger.info("Fetched users with username " + username);
        Query query = session.createQuery(hql);
        boolean res = (query.list().size() > 0);
        return res;
    }
    public boolean emailExists(String email) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM User E WHERE E.Email = \'" + email + "\'";
        logger.info("Fetched users with email " + email);
        Query query = session.createQuery(hql);
        boolean res = (query.list().size() > 0);
        return res;
    }

    public boolean phoneExists(String phone) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM User E WHERE E.Email = \'" + phone + "\'";
        logger.info("Fetched users with username " + phone);
        Query query = session.createQuery(hql);
        boolean res = (query.list().size() > 0);
        session.close();
        return res;
    }

    public User getUser(String username) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM User E WHERE E.Username = \'" + username + "\'";
        logger.info("Fetched users with username " + username);
        Query query = session.createQuery(hql);
        List list = query.list();
        if (list.size() == 0)
            return null;
        User user = (User) list.get(0);
        return user;
    }

    public User getUserById(int id) {
        Transaction tx = null;
        Integer userID = null;
        String hql = "FROM User E WHERE E.id = \'" + id+ "\'";
        logger.info("Fetched users with id " + id);
        Query query = session.createQuery(hql);
        List list = query.list();
        if (list.size() == 0)
            return null;
        User user = (User) list.get(0);
        return user;
    }

    public void Save(User user) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }
}