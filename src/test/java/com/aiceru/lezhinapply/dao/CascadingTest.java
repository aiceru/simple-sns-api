package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by iceru on 2016. 8. 1..
 */
public class CascadingTest extends HibernateDaoTest {
  private User santiago;
  private Post santiagoPost;

  @Before
  public void setup() throws SQLException {
    super.setup();
    santiago = new User("santiago", "muscle@idiots.com");
    santiagoPost = new Post(santiago, new Date(), "run run run");
    santiago.addPost(santiagoPost);
  }

  @After
  public void teardown() {
    super.teardown();
  }

  @Test
  public void testPersistAndDeleteUserWithPost() {
    dao.getCurrentSession();
    dao.beginTransaction();

    dao.persist(santiago);

    List<Post> postList = dao.findAll(Post.class);
    assertTrue(postList.contains(santiagoPost));

    dao.delete(santiago);

    postList = dao.findAll(Post.class);
    assertFalse(postList.contains(santiagoPost));

    dao.commit();
  }

  @Test
  public void testFollowingUsers() {
    dao.getCurrentSession();
    dao.beginTransaction();

    gasfard.addPost(helpme);
    gasfard.addPost(please);
    loyd.addPost(killyou);
    loyd.addPost(nono);
    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));
    santiago.setUserId(dao.persist(santiago));

    User gasfardFromDao = dao.findById(User.class, gasfard.getUserId());
    User loydFromDao = dao.findById(User.class, loyd.getUserId());
    User santiagoFromDao = dao.findById(User.class, santiago.getUserId());

    /* gasfard <--follows--> santiago --follows--> loyd */
    santiagoFromDao.addFollowing(loydFromDao);
    loydFromDao.addFollower(santiagoFromDao);
    santiagoFromDao.addFollowing(gasfardFromDao);
    gasfardFromDao.addFollower(santiagoFromDao);
    santiagoFromDao.addFollower(gasfardFromDao);
    gasfardFromDao.addFollowing(santiagoFromDao);

    dao.update(gasfardFromDao);
    dao.update(loydFromDao);
    dao.update(santiagoFromDao);

    User testUser = dao.findById(User.class, santiago.getUserId());
    assertEquals(santiago, testUser);
    assertEquals(2, testUser.getFollowings().size());
    assertEquals(1, testUser.getFollowers().size());
    assertTrue(testUser.getFollowers().contains(gasfardFromDao));
    assertTrue(testUser.getFollowings().contains(loydFromDao));
    assertTrue(testUser.getFollowings().contains(gasfardFromDao));

    for(User user : testUser.getFollowers()) {
      if(gasfardFromDao.equals(user)) {
        assertTrue(user.getPosts().contains(helpme));
        assertTrue(user.getPosts().contains(please));
        assertEquals(1, user.getFollowers().size());
        assertEquals(1, user.getFollowings().size());
        assertTrue(user.getFollowings().contains(santiagoFromDao));
        assertTrue(user.getFollowers().contains(santiagoFromDao));
      }
      else if(loyd.equals(user)) {
        assertTrue(user.getPosts().contains(killyou));
        assertTrue(user.getPosts().contains(nono));
        assertEquals(1, user.getFollowers().size());
        assertTrue(user.getFollowers().contains(santiagoFromDao));
        assertTrue(user.getFollowings().isEmpty());
      }
    }

    dao.commit();
  }

  @Test
  public void testPushPost() {
    dao.getCurrentSession();
    dao.beginTransaction();

    gasfard.setUserId(dao.persist(gasfard));
    loyd.setUserId(dao.persist(loyd));
    santiago.setUserId(dao.persist(santiago));

    User gasfardFromDao = dao.findById(User.class, gasfard.getUserId());
    User loydFromDao = dao.findById(User.class, loyd.getUserId());
    User santiagoFromDao = dao.findById(User.class, santiago.getUserId());

    gasfardFromDao.addFollowing(santiagoFromDao);
    santiagoFromDao.addFollower(gasfardFromDao);
    santiagoFromDao.addFollowing(gasfardFromDao);
    gasfardFromDao.addFollower(santiagoFromDao);
    santiagoFromDao.addFollowing(loydFromDao);
    loydFromDao.addFollower(santiagoFromDao);

    List<User> followers = santiagoFromDao.getFollowers();

    for(User f : followers) {
      f.addFollowingPost(santiagoFromDao.getPosts().get(0));
    }

    User testUser = dao.findById(User.class, gasfardFromDao.getUserId());
    assertEquals(1, testUser.getFollowingPosts().size());
    assertTrue(testUser.getFollowingPosts().contains(santiagoFromDao.getPosts().get(0)));

    dao.commit();
  }
}