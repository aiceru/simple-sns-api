package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernatePostDao extends Dao<Post,Integer> {

  public HibernatePostDao(SessionFactory sf) {
    this.setSessionFactory(sf);
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
