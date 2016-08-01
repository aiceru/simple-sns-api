package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    for( Post p : postList ) {
      if( santiagoPost.equals(p) ) {
        return;
      }
    }
    fail("santiago's post not exists in DB!");

    userDao.delete(santiago);
    postList = postDao.findAll();
    for( Post p : postList ) {
      if( santiagoPost.equals(p) ) {
        fail("santiago's post exists in DB after delete!");
      }
    }
  }

  @Test
  public void testFollowingUsers() {
    gasfard.addPost(helpme);
    gasfard.addPost(please);
    loyd.addPost(killyou);
    loyd.addPost(nono);
    persistGasfardAndLoyd();  // all posts saved by cascading
    santiago.setId(userDao.persist(santiago));

    User gasfardFromDao = userDao.findById(gasfard.getId());
    User loydFromDao = userDao.findById(loyd.getId());
    User santiagoFromDao = userDao.findById(santiago.getId());

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

    User testUser = userDao.findById(santiago.getId());
    assertEquals(santiago, testUser);
    assertTrue(testUser.getFollowers().contains(gasfard));
    assertTrue(testUser.getFollowings().contains(loyd));
    Iterator<User> it = testUser.getFollowers().iterator();
    while(it.hasNext()) {
      User user = it.next();
      if(gasfard.equals(user)) {
        assertTrue(user.getPosts().contains(helpme));
        assertTrue(user.getPosts().contains(please));
        assertTrue(user.getFollowings().contains(santiago));
        assertTrue(user.getFollowers().contains(santiago));
      }
      else if(loyd.equals(user)) {
        assertTrue(user.getPosts().contains(killyou));
        assertTrue(user.getPosts().contains(nono));
        assertTrue(user.getFollowers().contains(santiago));
        assertTrue(user.getFollowings().isEmpty());
      }
    }
  }

  @Test
  public void testPushPost() {
    persistGasfardAndLoyd();
    santiago.setId(userDao.persist(santiago));

    User gasfardFromDao = userDao.findById(gasfard.getId());
    User loydFromDao = userDao.findById(loyd.getId());
    User santiagoFromDao = userDao.findById(santiago.getId());

    gasfardFromDao.addFollowing(santiagoFromDao);
    gasfardFromDao.addFollower(santiagoFromDao);
    santiagoFromDao.addFollower(gasfardFromDao);
    santiagoFromDao.addFollowing(gasfardFromDao);
    santiagoFromDao.addFollowing(loydFromDao);
    loydFromDao.addFollower(santiagoFromDao);

    Set<User> followSet = santiagoFromDao.getFollowers();
    Iterator<User> it = followSet.iterator();

    while(it.hasNext()) {
      it.next().addFollowingPost(santiagoFromDao.getPosts().get(0));
    }

    userDao.update(gasfardFromDao);
    userDao.update(santiagoFromDao);
    userDao.update(loydFromDao);

    List<User> users = userDao.findAll();
  }
}