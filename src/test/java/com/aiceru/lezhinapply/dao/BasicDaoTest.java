package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by iceru on 2016. 8. 1..
 */
public class BasicDaoTest extends HibernateDaoTest {
  @Test
  public void testPersist() {
    dao.getCurrentSession();
    dao.beginTransaction();

    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));
    assertEquals(gasfard.getUserId()+1, loyd.getUserId());

    helpme.setPostId(dao.persist(helpme));
    killyou.setPostId(dao.persist(killyou));
    please.setPostId(dao.persist(please));
    nono.setPostId(dao.persist(nono));
    assertEquals(helpme.getPostId()+1, killyou.getPostId());

    dao.commit();
  }

  @Test
  public void testFindById() {
    dao.getCurrentSession();
    dao.beginTransaction();

    User foundUser = dao.findById(User.class, gasfard.getUserId());
    assertNull(foundUser);

    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));

    foundUser = dao.findById(User.class, gasfard.getUserId());
    assertEquals(gasfard, foundUser);

    foundUser = dao.findById(User.class, loyd.getUserId());
    assertEquals(loyd, foundUser);

    Post foundPost = dao.findById(Post.class, helpme.getPostId());
    assertNull(foundPost);

    helpme.setPostId(dao.persist(helpme));
    killyou.setPostId(dao.persist(killyou));
    please.setPostId(dao.persist(please));
    nono.setPostId(dao.persist(nono));

    foundPost = dao.findById(Post.class, helpme.getPostId());
    assertEquals(helpme, foundPost);

    foundPost = dao.findById(Post.class, killyou.getPostId());
    assertEquals(killyou, foundPost);

    dao.commit();
  }

  @Test
  public void testFindAll() {
    dao.getCurrentSession();
    dao.beginTransaction();

    List<User> users = dao.findAll(User.class);
    assertEquals(0, users.size());

    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));

    users = dao.findAll(User.class);
    assertEquals(users.size(), 2);
    assertEquals(gasfard, users.get(0));
    assertEquals(loyd, users.get(1));

    List<Post> posts = dao.findAll(Post.class);
    assertEquals(0, posts.size());

    helpme.setPostId(dao.persist(helpme));
    killyou.setPostId(dao.persist(killyou));
    please.setPostId(dao.persist(please));
    nono.setPostId(dao.persist(nono));

    posts = dao.findAll(Post.class);
    assertEquals(4, posts.size());
    assertEquals(helpme, posts.get(0));
    assertEquals(killyou, posts.get(1));
    assertEquals(please, posts.get(2));
    assertEquals(nono, posts.get(3));

    dao.commit();
  }

  @Test
  public void testUpdate() {
    dao.getCurrentSession();
    dao.beginTransaction();

    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));

    gasfard.setName("gasfard revisited");
    dao.update(gasfard);

    assertEquals("gasfard revisited", dao.findById(User.class, gasfard.getUserId()).getName());

    helpme.setPostId(dao.persist(helpme));
    killyou.setPostId(dao.persist(killyou));
    please.setPostId(dao.persist(please));
    nono.setPostId(dao.persist(nono));

    nono.setContent("no mercy");
    dao.update(nono);

    assertEquals("no mercy", dao.findById(Post.class, nono.getPostId()).getContent());

    dao.commit();
  }

  @Test
  public void testDelete() {
    dao.getCurrentSession();
    dao.beginTransaction();

    gasfard.setUserId(dao.persist(gasfard));
    User user = dao.findById(User.class, gasfard.getUserId());
    assertEquals(gasfard, user);

    helpme.setPostId(dao.persist(helpme));
    Post post = dao.findById(Post.class, helpme.getPostId());
    assertEquals(helpme, post);

    dao.delete(post);
    assertNull(dao.findById(Post.class, helpme.getPostId()));

    dao.delete(user);
    assertNull(dao.findById(User.class, gasfard.getUserId()));

    dao.commit();
  }

  @Test
  public void testDeleteAll() {
    dao.getCurrentSession();
    dao.beginTransaction();

    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));

    helpme.setPostId(dao.persist(helpme));
    killyou.setPostId(dao.persist(killyou));
    please.setPostId(dao.persist(please));
    nono.setPostId(dao.persist(nono));

    assertEquals(helpme, dao.findById(Post.class, helpme.getPostId()));
    assertEquals(4, dao.findAll(Post.class).size());
    dao.deleteAll(Post.class);
    dao.commit();
    dao.getCurrentSession();
    dao.beginTransaction();
    assertNull(dao.findById(Post.class, helpme.getPostId()));
    assertEquals(0, dao.findAll(Post.class).size());

    assertEquals(gasfard, dao.findById(User.class, gasfard.getUserId()));
    assertEquals(2, dao.findAll(User.class).size());
    dao.deleteAll(User.class);
    dao.commit();
    dao.getCurrentSession();
    dao.beginTransaction();
    assertNull(dao.findById(User.class, gasfard.getUserId()));
    assertEquals(0, dao.findAll(User.class).size());

    dao.commit();
  }
}
