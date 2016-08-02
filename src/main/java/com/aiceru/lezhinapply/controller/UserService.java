package com.aiceru.lezhinapply.controller;

import com.aiceru.lezhinapply.dao.DaoInterface;
import com.aiceru.lezhinapply.dao.HibernatePostDao;
import com.aiceru.lezhinapply.dao.HibernateUserDao;
import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 2..
 */
@Path("/users")
public class UserService {
  private DaoInterface<User, Integer> userDao = new HibernateUserDao();
  private DaoInterface<Post, Integer> postDao = new HibernatePostDao();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getAllUsers() {
    userDao.getCurrentSessionWithTransaction();
    User user = new User("gasfard", "idiot@somemail.com");
    User usera = new User("loyd", "kill@somemail.com");
    Post post = new Post(user, new Date(), "postcontetn");

    user.addFollower(usera);
    usera.addFollowing(user);
    user.addFollowing(usera);
    usera.addFollower(user);
    user.addPost(post);
    userDao.persist(user);
    userDao.persist(usera);
    List<User> users = userDao.findAll();
    userDao.closeCurrentSessionWithTransaction();
    return users;

    /*postDao.getCurrentSessionWithTransaction();
    List<Post> posts = postDao.findAll();
    postDao.closeCurrentSessionWithTransaction();
    return posts;*/
  }
}
