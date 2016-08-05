package com.aiceru.lezhinapply.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public abstract class Dao<T, Id extends Serializable> {
  static SessionFactory sessionFactory;
  static Session session;
  static Transaction tx;

  public void setSessionFactory(SessionFactory sf) {
    sessionFactory = sf;
  }

  public void getCurrentSession() {
    session = sessionFactory.getCurrentSession();
  }

  public void beginTransaction() {
    tx = session.beginTransaction();
  }

  public void commit() {
    tx.commit();
  }

  public void closeCurrentSession() {
    session.close();
  }

  public abstract Id persist(T entity);
  public abstract void update(T entity);
  public abstract T findById(Id id);
  public abstract void delete(T entity);
  public abstract List<T> findAll();
  public abstract void deleteAll();
}
