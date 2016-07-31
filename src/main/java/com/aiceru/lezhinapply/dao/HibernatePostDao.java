package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernatePostDao implements DaoInterface<Post, Integer> {
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
  public Integer persist(Post entity) {
    openSessionWithTransaction();
    Integer id = (Integer) session.save(entity);
    closeSessionWithTransaction();
    return id;
  }

  @Override
  public void update(Post entity) {
    openSessionWithTransaction();
    session.update(entity);
    closeSessionWithTransaction();
  }

  @Override
  public Post findById(Integer id) {
    openSession();
    Post post = session.get(Post.class, id);
    closeSession();
    return post;
  }

  @Override
  public void delete(Post entity) {
    openSessionWithTransaction();
    session.delete(entity);
    closeSessionWithTransaction();
  }

  @Override
  public List<Post> findAll() {
    openSession();
    List<Post> posts = session.createQuery("from Post p").list();
    closeSession();
    return posts;
  }

  @Override
  public void deleteAll() {
    openSessionWithTransaction();
    List<Post> posts = session.createQuery("from Post p").list();
    for(Post post : posts) {
      session.delete(post);
    }
    closeSessionWithTransaction();
  }
}
