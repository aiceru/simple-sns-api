package com.aiceru.lezhinapply.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernateDao implements Dao {
  private SessionFactory sessionFactory;
  private static Session session;
  private static Transaction tx;

  public HibernateDao(SessionFactory sf) {
    this.setSessionFactory(sf);
  }

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

  public void rollbackIfTxActive() {
    if (tx != null && tx.isActive()) {
      tx.rollback();
    }
  }

  public void closeCurrentSession() {
    if (session != null) {
      session.close();
    }
  }

  @Override
  public <T> int persist(T entity) {
    assert tx.isActive();
    return (Integer) session.save(entity);
  }

  @Override
  public <T> void update(T entity) {
    assert tx.isActive();
    session.update(entity);
  }

  @Override
  public <T> T findById(final Class<T> type, int id) {
    assert tx.isActive();
    return session.get(type, (Serializable) id);
  }

  @Override
  public <T> void delete(T entity) {
    assert tx.isActive();
    session.delete(entity);
  }

  @Override
  public <T> List<T> findAll(final Class<T> type) {
    assert tx.isActive();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(type);
    Root<T> rootEntry = cq.from(type);
    CriteriaQuery<T> all = cq.select(rootEntry);
    TypedQuery<T> allQuery = session.createQuery(all);
    return allQuery.getResultList();
  }

  @Override
  public <T> void deleteAll(final Class<T> type) {
    assert tx.isActive();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaDelete<T> cd = cb.createCriteriaDelete(type);
    cd.from(type);
    session.createQuery(cd).executeUpdate();
  }
}
