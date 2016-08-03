package com.aiceru.lezhinapply.service;

import com.aiceru.lezhinapply.dao.DaoInterface;
import com.aiceru.lezhinapply.dao.HibernateUserDao;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.filter.TimeLineView;
import com.aiceru.lezhinapply.util.filter.UserDetailView;
import com.aiceru.lezhinapply.util.jpa.SqlExecuter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 2..
 */
@Path("/users")
public class UserService {
  private DaoInterface<User, Integer> userDao = new HibernateUserDao();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @TimeLineView
  public List<User> getAllUsers() {
    try {
      SqlExecuter.execute(
      this.getClass().getClassLoader().getResourceAsStream("/create_test_data.txt"));
      SqlExecuter.pustPosts();
    } catch (IOException e) {
      e.printStackTrace();
    }

    userDao.getCurrentSessionWithTransaction();
    List<User> users = userDao.findAll();
    userDao.closeCurrentSessionWithTransaction();
    return users;
  }
}
