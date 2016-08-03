package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernatePostDao implements DaoInterface<Post, Integer> {
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
  public Integer persist(Post entity) {
    return (Integer) session.save(entity);
  }

  @Override
  public void update(Post entity) {
    session.update(entity);
  }

  @Override
  public Post findById(Integer id) {
    return session.get(Post.class, id);
  }

  @Override
  public void delete(Post entity) {
    session.delete(entity);
  }

  @Override
  public List<Post> findAll() {
    return session.createQuery("from Post p").list();
  }

  @Override
  public void deleteAll() {
    List<Post> posts = session.createQuery("from Post p").list();
    for(Post post : posts) {
      session.delete(post);
    }
  }
}
