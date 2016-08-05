package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernateUserDao extends Dao<User,Integer> {

  public HibernateUserDao(SessionFactory sf) {
    this.setSessionFactory(sf);
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
