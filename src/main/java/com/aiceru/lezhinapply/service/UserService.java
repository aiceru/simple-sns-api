package com.aiceru.lezhinapply.service;

import com.aiceru.lezhinapply.dao.DaoInterface;
import com.aiceru.lezhinapply.dao.HibernateUserDao;
import com.aiceru.lezhinapply.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 2..
 */
@Path("/users")
public class UserService {
  private DaoInterface<User, Integer> userDao = new HibernateUserDao();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getAllUsers() {
    /*try {
      SqlExecuter.execute("/create_test_data.txt");
      SqlExecuter.pustPosts();
    } catch (IOException e) {
      e.printStackTrace();
    }*/

    userDao.getCurrentSessionWithTransaction();
    List<User> users = userDao.findAll();
    userDao.closeCurrentSessionWithTransaction();
    return users;
  }
}