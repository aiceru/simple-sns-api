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
    userDao.persist(santiago);

    List<Post> postList = postDao.findAll();
    assertTrue(postList.contains(santiagoPost));

    userDao.delete(santiago);

    postList = postDao.findAll();
    assertFalse(postList.contains(santiagoPost));
  }

  @Test
  public void testFollowingUsers() {
    gasfard.addPost(helpme);
    gasfard.addPost(please);
    loyd.addPost(killyou);
    loyd.addPost(nono);
    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));
    santiago.setUserId(userDao.persist(santiago));

    User gasfardFromDao = userDao.findById(gasfard.getUserId());
    User loydFromDao = userDao.findById(loyd.getUserId());
    User santiagoFromDao = userDao.findById(santiago.getUserId());

    /* gasfard <--follows--> santiago --follows--> loyd */
    santiagoFromDao.addFollowing(loydFromDao);
    loydFromDao.addFollower(santiagoFromDao);
    santiagoFromDao.addFollowing(gasfardFromDao);
    gasfardFromDao.addFollower(santiagoFromDao);
    santiagoFromDao.addFollower(gasfardFromDao);
    gasfardFromDao.addFollowing(santiagoFromDao);

    userDao.update(gasfardFromDao);
    userDao.update(loydFromDao);
    userDao.update(santiagoFromDao);

    User testUser = userDao.findById(santiago.getUserId());
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
  }

  @Test
  public void testPushPost() {
    gasfard.setUserId(userDao.persist(gasfard));
    loyd.setUserId(userDao.persist(loyd));
    santiago.setUserId(userDao.persist(santiago));

    User gasfardFromDao = userDao.findById(gasfard.getUserId());
    User loydFromDao = userDao.findById(loyd.getUserId());
    User santiagoFromDao = userDao.findById(santiago.getUserId());

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

    User testUser = userDao.findById(gasfardFromDao.getUserId());
    assertEquals(1, testUser.getFollowingPosts().size());
    assertTrue(testUser.getFollowingPosts().contains(santiagoFromDao.getPosts().get(0)));
  }
}