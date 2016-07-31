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

  private void openSession() {
    session = HibernateUtil.getSessionFactory().openSession();
  }

  private void openSessionWithTransaction() {
    openSession();
    transaction = session.beginTransaction();
  }

  private void closeSession() {
    session.close();
  }

  private void closeSessionWithTransaction() {
    transaction.commit();
    closeSession();
  }

  @Override
  public Integer persist(User entity) {
    openSessionWithTransaction();
    Integer id = (Integer) session.save(entity);
    closeSessionWithTransaction();
    return id;
  }

  @Override
  public void update(User entity) {
    openSessionWithTransaction();
    session.update(entity);
    closeSessionWithTransaction();
  }

  @Override
  public User findById(Integer id) {
    openSession();
    User user = session.get(User.class, id);
    closeSession();
    return user;
  }

  @Override
  public void delete(User entity) {
    openSessionWithTransaction();
    session.delete(entity);
    closeSessionWithTransaction();
  }

  @Override
  public List<User> findAll() {
    openSession();
    List<User> users = session.createQuery("from User u").list();
    closeSession();
    return users;
  }

  @Override
  public void deleteAll() {
    openSessionWithTransaction();
    List<User> users = session.createQuery("from User u").list();
    for(User user : users) {
      session.delete(user);
    }
    closeSessionWithTransaction();
  }
}
