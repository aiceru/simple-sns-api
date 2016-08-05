package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by iceru on 2016. 8. 1..
 */
@Ignore
class HibernateDaoTest {
  Dao<User, Integer> userDao = new HibernateUserDao(HibernateUtil.getSessionFactory());
  Dao<Post, Integer> postDao = new HibernatePostDao(HibernateUtil.getSessionFactory());
  User gasfard;
  User loyd;
  Post helpme, please;
  Post killyou, nono;

  private static final long HOUR = 3600 * 1000;
  private static final String DB_URL = "jdbc:h2:mem:simple-sns-db;";

  @Before
  public void setup() throws SQLException {
    gasfard = new User("gasfard", "idiot@somemail.com");
    loyd = new User("loyd", "killerfish@idiots.com");
    helpme = new Post(gasfard, new Date(), "help me!!!!");
    killyou = new Post(loyd, new Date(new Date().getTime() + (HOUR)), "no, I'll kill you :D");
    please = new Post(gasfard, new Date(new Date().getTime() + (2 * HOUR)), "please dontdothat");
    nono = new Post(loyd, new Date(new Date().getTime() + (3 * HOUR)), "that's nono");

    userDao.getCurrentSession();
    userDao.beginTransaction();
  }

  @After
  public void teardown() {
    /* all post records will be delete by cascading */
    userDao.deleteAll();

    assertEquals(0, postDao.findAll().size());
    assertEquals(0, userDao.findAll().size());

    userDao.commit();
  }
}
