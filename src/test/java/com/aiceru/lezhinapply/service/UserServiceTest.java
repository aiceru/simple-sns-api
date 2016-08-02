package com.aiceru.lezhinapply.service;

import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.SqlExecuter;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by iceru on 16. 8. 3.
 */
public class UserServiceTest {
  UserService userService = new UserService();
  @Test
  public void aTest() {
    try {
      SqlExecuter.execute("src/test/resources/create_test_data.txt");
      SqlExecuter.pustPosts();
    } catch (IOException e) {
      fail(e.getMessage());
    }

    List<User> allusers = userService.getAllUsers();
    assertEquals(39, allusers.size());
  }

  @After
  public void teardown() {
  }
}
