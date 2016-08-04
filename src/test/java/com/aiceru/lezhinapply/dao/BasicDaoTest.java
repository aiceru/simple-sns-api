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
    userDao.getCurrentSessionWithTransaction();
    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));
    assertEquals(gasfard.getUserId()+1, loyd.getUserId());
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();
    helpme.setPostId(postDao.persist(helpme));
    killyou.setPostId(postDao.persist(killyou));
    please.setPostId(postDao.persist(please));
    nono.setPostId(postDao.persist(nono));
    assertEquals(helpme.getPostId()+1, killyou.getPostId());
    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testFindById() {
    userDao.getCurrentSessionWithTransaction();

    User foundUser = userDao.findById(gasfard.getUserId());
    assertNull(foundUser);

    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));

    foundUser = userDao.findById(gasfard.getUserId());
    assertEquals(gasfard, foundUser);

    foundUser = userDao.findById(loyd.getUserId());
    assertEquals(loyd, foundUser);

    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();

    Post foundPost = postDao.findById(helpme.getPostId());
    assertNull(foundPost);

    helpme.setPostId(postDao.persist(helpme));
    killyou.setPostId(postDao.persist(killyou));
    please.setPostId(postDao.persist(please));
    nono.setPostId(postDao.persist(nono));

    foundPost = postDao.findById(helpme.getPostId());
    assertEquals(helpme, foundPost);

    foundPost = postDao.findById(killyou.getPostId());
    assertEquals(killyou, foundPost);

    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testFindAll() {
    userDao.getCurrentSessionWithTransaction();

    List<User> users = userDao.findAll();
    assertEquals(0, users.size());

    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));

    users = userDao.findAll();
    assertEquals(users.size(), 2);
    assertEquals(gasfard, users.get(0));
    assertEquals(loyd, users.get(1));

    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();

    List<Post> posts = postDao.findAll();
    assertEquals(0, posts.size());

    helpme.setPostId(postDao.persist(helpme));
    killyou.setPostId(postDao.persist(killyou));
    please.setPostId(postDao.persist(please));
    nono.setPostId(postDao.persist(nono));

    posts = postDao.findAll();
    assertEquals(4, posts.size());
    assertEquals(helpme, posts.get(0));
    assertEquals(killyou, posts.get(1));
    assertEquals(please, posts.get(2));
    assertEquals(nono, posts.get(3));

    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testUpdate() {
    userDao.getCurrentSessionWithTransaction();

    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));

    gasfard.setName("gasfard revisited");
    userDao.update(gasfard);

    assertEquals("gasfard revisited", userDao.findById(gasfard.getUserId()).getName());
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();

    helpme.setPostId(postDao.persist(helpme));
    killyou.setPostId(postDao.persist(killyou));
    please.setPostId(postDao.persist(please));
    nono.setPostId(postDao.persist(nono));

    nono.setContent("no mercy");
    postDao.update(nono);

    assertEquals("no mercy", postDao.findById(nono.getPostId()).getContent());
    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testDelete() {
    userDao.getCurrentSessionWithTransaction();
    gasfard.setUserId(userDao.persist(gasfard));
    User user = userDao.findById(gasfard.getUserId());
    assertEquals(gasfard, user);
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();
    helpme.setPostId(postDao.persist(helpme));
    Post post = postDao.findById(helpme.getPostId());
    assertEquals(helpme, post);

    postDao.delete(post);
    assertNull(postDao.findById(helpme.getPostId()));
    postDao.closeCurrentSessionWithTransaction();

    userDao.getCurrentSessionWithTransaction();
    userDao.delete(user);
    assertNull(userDao.findById(gasfard.getUserId()));
    userDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testDeleteAll() {
    userDao.getCurrentSessionWithTransaction();
    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();
    helpme.setPostId(postDao.persist(helpme));
    killyou.setPostId(postDao.persist(killyou));
    please.setPostId(postDao.persist(please));
    nono.setPostId(postDao.persist(nono));

    assertEquals(helpme, postDao.findById(helpme.getPostId()));
    assertEquals(4, postDao.findAll().size());
    postDao.deleteAll();
    assertNull(postDao.findById(helpme.getPostId()));
    assertEquals(0, postDao.findAll().size());
    postDao.closeCurrentSessionWithTransaction();

    userDao.getCurrentSessionWithTransaction();
    assertEquals(gasfard, userDao.findById(gasfard.getUserId()));
    assertEquals(2, userDao.findAll().size());
    userDao.deleteAll();
    assertNull(userDao.findById(gasfard.getUserId()));
    assertEquals(0, userDao.findAll().size());
    userDao.closeCurrentSessionWithTransaction();
  }
}
