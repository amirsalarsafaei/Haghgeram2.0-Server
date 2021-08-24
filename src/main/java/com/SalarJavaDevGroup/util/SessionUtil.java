package com.SalarJavaDevGroup.util;

import com.SalarJavaDevGroup.Models.Tweet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class SessionUtil {
    public static SessionFactory factory = null;
    public static Session getSession() {
        if (factory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                factory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Failed to create sessionFactory object." + ex);
                System.out.println("Couldn't Connect to database");
                System.exit(-1);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return factory.openSession();
    }
//    public static void saveObj(Object obj){
//        Session session = getSession();
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            session.saveOrUpdate(obj);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx!=null) tx.rollback();
//            e.printStackTrace();
//        }
//        session.close();
//
//    }
//
//    public static void saveObj(Object obj, Session session) {
//        Transaction tx = null;
//        try {
//            tx = session.beginTransaction();
//            session.saveOrUpdate(obj);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx!=null) tx.rollback();
//            e.printStackTrace();
//        }
//    }

    public static void deleteObj(Object obj, Session session) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
    }

    public static void checkConnection() {
        if (factory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                factory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Failed to create sessionFactory object." + ex);
                System.out.println("Couldn't Connect to database");
                System.exit(-1);
                throw new ExceptionInInitializerError(ex);
            }
        }
        factory.openSession();
    }
}
