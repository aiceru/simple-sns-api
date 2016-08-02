package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernateUserDao implements DaoInterface<User, Integer> {

  private Session session;
  private Transaction transaction;

  @Override
  public void getCurrentSession() {
    session = HibernateUtil.getSessionFactory().getCurrentSession();
  }

  @Override
  public void getCurrentSessionWithTransaction() {
    getCurrentSession();
    transaction = session.beginTransaction();
  }

  @Override
  public void closeCurrentSession() {
    session.close();
  }

  @Override
  public void closeCurrentSessionWithTransaction() {
    transaction.commit();
    closeCurrentSession();
  }

  @Override
  public Integer persist(User entity) {
    return (Integer) session.save(entity);
  }

  @Override
  public void update(User entity) {
    session.update(entity);
  }

  @Override
  public User findById(Integer id) {
    return session.get(User.class, id);
  }

  @Override
  public void delete(User entity) {
    session.delete(entity);
  }

  @Override
  public List<User> findAll() {
    return session.createQuery("from User").list();
  }

  @Override
  public void deleteAll() {
    List<User> users = session.createQuery("from User").list();
    for(User user : users) {
      session.delete(user);
    }
  }
}
