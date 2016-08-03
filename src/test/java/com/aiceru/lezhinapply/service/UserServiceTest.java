package com.aiceru.lezhinapply.service;

import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.jpa.SqlExecuter;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by iceru on 16. 8. 3.
 */
public class UserServiceTest {
  UserService userService = new UserService();
  @Test
  public void aTest() {
    try {
      SqlExecuter.execute(
              this.getClass().getClassLoader()
                      .getResourceAsStream("create_test_data.txt"));
      SqlExecuter.pustPosts();
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<User> allusers = userService.getAllUsers();
    assertEquals(39, allusers.size());
  }

  @After
  public void teardown() {
  }
}
