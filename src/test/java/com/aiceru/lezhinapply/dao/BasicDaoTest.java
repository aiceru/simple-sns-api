package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.SqlExecuter;
import org.junit.Test;

import java.sql.SQLException;
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
    gasfard.setId(userDao.persist(gasfard));
    loyd.setId(userDao.persist(loyd));
    assertEquals(gasfard.getId()+1, loyd.getId());
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();
    helpme.setId(postDao.persist(helpme));
    killyou.setId(postDao.persist(killyou));
    please.setId(postDao.persist(please));
    nono.setId(postDao.persist(nono));
    assertEquals(helpme.getId()+1, killyou.getId());
    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testFindById() {
    userDao.getCurrentSessionWithTransaction();

    User foundUser = userDao.findById(gasfard.getId());
    assertNull(foundUser);

    gasfard.setId(userDao.persist(gasfard));
    loyd.setId(userDao.persist(loyd));

    foundUser = userDao.findById(gasfard.getId());
    assertEquals(gasfard, foundUser);

    foundUser = userDao.findById(loyd.getId());
    assertEquals(loyd, foundUser);

    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();

    Post foundPost = postDao.findById(helpme.getId());
    assertNull(foundPost);

    helpme.setId(postDao.persist(helpme));
    killyou.setId(postDao.persist(killyou));
    please.setId(postDao.persist(please));
    nono.setId(postDao.persist(nono));

    foundPost = postDao.findById(helpme.getId());
    assertEquals(helpme, foundPost);

    foundPost = postDao.findById(killyou.getId());
    assertEquals(killyou, foundPost);

    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testFindAll() {
    userDao.getCurrentSessionWithTransaction();

    List<User> users = userDao.findAll();
    assertEquals(users.size(), 0);

    gasfard.setId(userDao.persist(gasfard));
    loyd.setId(userDao.persist(loyd));

    users = userDao.findAll();
    assertEquals(users.size(), 2);
    assertEquals(gasfard, users.get(0));
    assertEquals(loyd, users.get(1));

    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();

    List<Post> posts = postDao.findAll();
    assertEquals(0, posts.size());

    helpme.setId(postDao.persist(helpme));
    killyou.setId(postDao.persist(killyou));
    please.setId(postDao.persist(please));
    nono.setId(postDao.persist(nono));

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

    gasfard.setId(userDao.persist(gasfard));
    loyd.setId(userDao.persist(loyd));

    gasfard.setName("gasfard revisited");
    userDao.update(gasfard);

    assertEquals("gasfard revisited", userDao.findById(gasfard.getId()).getName());
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();

    helpme.setId(postDao.persist(helpme));
    killyou.setId(postDao.persist(killyou));
    please.setId(postDao.persist(please));
    nono.setId(postDao.persist(nono));

    nono.setContent("no mercy");
    postDao.update(nono);

    assertEquals("no mercy", postDao.findById(nono.getId()).getContent());
    postDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testDelete() {
    userDao.getCurrentSessionWithTransaction();
    gasfard.setId(userDao.persist(gasfard));
    User user = userDao.findById(gasfard.getId());
    assertEquals(gasfard, user);
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();
    helpme.setId(postDao.persist(helpme));
    Post post = postDao.findById(helpme.getId());
    assertEquals(helpme, post);

    postDao.delete(post);
    assertNull(postDao.findById(helpme.getId()));
    postDao.closeCurrentSessionWithTransaction();

    userDao.getCurrentSessionWithTransaction();
    userDao.delete(user);
    assertNull(userDao.findById(gasfard.getId()));
    userDao.closeCurrentSessionWithTransaction();
  }

  @Test
  public void testDeleteAll() {
    userDao.getCurrentSessionWithTransaction();
    gasfard.setId(userDao.persist(gasfard));
    loyd.setId(userDao.persist(loyd));
    userDao.closeCurrentSessionWithTransaction();

    postDao.getCurrentSessionWithTransaction();
    helpme.setId(postDao.persist(helpme));
    killyou.setId(postDao.persist(killyou));
    please.setId(postDao.persist(please));
    nono.setId(postDao.persist(nono));

    assertEquals(helpme, postDao.findById(helpme.getId()));
    assertEquals(4, postDao.findAll().size());
    postDao.deleteAll();
    assertNull(postDao.findById(helpme.getId()));
    assertEquals(0, postDao.findAll().size());
    postDao.closeCurrentSessionWithTransaction();

    userDao.getCurrentSessionWithTransaction();
    assertEquals(gasfard, userDao.findById(gasfard.getId()));
    assertEquals(2, userDao.findAll().size());
    userDao.deleteAll();
    assertNull(userDao.findById(gasfard.getId()));
    assertEquals(0, userDao.findAll().size());
    userDao.closeCurrentSessionWithTransaction();
  }
}
