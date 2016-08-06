package com.aiceru.lezhinapply.dao;

import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public interface Dao {
  public void setSessionFactory(SessionFactory sf);
  public void getCurrentSession();
  public void beginTransaction();
  public void commit();
  public void closeCurrentSession();

  public <T> int persist(T entity);
  public <T> void update(T entity);
  public <T> T findById(final Class<T> type, int id);
  public <T> void delete(T entity);
  public <T> List<T> findAll(final Class<T> type);
  public <T> void deleteAll(final Class<T> type);
}
