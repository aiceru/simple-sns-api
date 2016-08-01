package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
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
    persistGasfardAndLoyd();
    assertEquals(gasfard.getId()+1, loyd.getId());

    persistGasfardAndLoydPosts();
    assertEquals(helpme.getId()+1, killyou.getId());
  }

  @Test
  public void testFindById() {
    User foundUser = userDao.findById(gasfard.getId());
    assertNull(foundUser);

    persistGasfardAndLoyd();

    foundUser = userDao.findById(gasfard.getId());
    assertEquals(gasfard, foundUser);

    foundUser = userDao.findById(loyd.getId());
    assertEquals(loyd, foundUser);

    Post foundPost = postDao.findById(helpme.getId());
    assertNull(foundPost);

    persistGasfardAndLoydPosts();

    foundPost = postDao.findById(helpme.getId());
    assertEquals(helpme, foundPost);

    foundPost = postDao.findById(killyou.getId());
    assertEquals(killyou, foundPost);
  }

  @Test
  public void testFindAll() {
    List<User> users = userDao.findAll();
    assertEquals(users.size(), 0);

    persistGasfardAndLoyd();

    users = userDao.findAll();
    assertEquals(users.size(), 2);
    assertEquals(gasfard, users.get(0));
    assertEquals(loyd, users.get(1));

    List<Post> posts = postDao.findAll();
    assertEquals(0, posts.size());

    persistGasfardAndLoydPosts();

    posts = postDao.findAll();
    assertEquals(4, posts.size());
    assertEquals(helpme, posts.get(0));
    assertEquals(killyou, posts.get(1));
    assertEquals(please, posts.get(2));
    assertEquals(nono, posts.get(3));
  }

  @Test
  public void testUpdate() {
    persistGasfardAndLoyd();
    gasfard.setName("gasfard revisited");
    userDao.update(gasfard);

    assertEquals("gasfard revisited", userDao.findById(gasfard.getId()).getName());

    persistGasfardAndLoydPosts();
    nono.setContent("no mercy");
    postDao.update(nono);

    assertEquals("no mercy", postDao.findById(nono.getId()).getContent());
  }

  @Test
  public void testDelete() {
    gasfard.setId(userDao.persist(gasfard));
    assertEquals(gasfard, userDao.findById(gasfard.getId()));

    userDao.delete(gasfard);
    assertNull(userDao.findById(gasfard.getId()));
  }

  @Test
  public void testDeleteAll() {
    persistGasfardAndLoyd();
    persistGasfardAndLoydPosts();

    assertEquals(gasfard, userDao.findById(gasfard.getId()));
    assertEquals(helpme, postDao.findById(helpme.getId()));

    assertEquals(2, userDao.findAll().size());
    assertEquals(4, postDao.findAll().size());

    userDao.deleteAll();

    assertNull(userDao.findById(gasfard.getId()));
    assertNull(postDao.findById(helpme.getId()));

    assertEquals(0, userDao.findAll().size());
    assertEquals(0, postDao.findAll().size());
  }
}
