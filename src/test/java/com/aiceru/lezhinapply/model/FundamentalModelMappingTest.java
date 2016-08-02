package com.aiceru.lezhinapply.model;

import com.aiceru.lezhinapply.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by iceru on 2016. 7. 30..
 */
public class FundamentalModelMappingTest {
  static SessionFactory factory;
  static Logger logger;

  @BeforeClass
  public static void setup() {
    factory = HibernateUtil.getSessionFactory();
    logger = LogManager.getLogger(FundamentalModelMappingTest.class);
  }

  @Test
  public void testUserMapping() {
    User testUser = new User("gasfard", "idiots@somemail.com");
    Session session = factory.openSession();
    session.beginTransaction();
    Integer id = (Integer) session.save(testUser);
    testUser.setId(id);
    session.getTransaction().commit();
    session.close();

    session = factory.openSession();
    User otherUser = session.get(User.class, id);
    assertEquals(otherUser, testUser);
    session.close();
    logger.trace(otherUser);
  }

  @Test
  public void testPostMapping() {
    Session session = factory.openSession();

    User testUser = (User) session.createQuery("from User u").list().get(0);
    Post testPost = new Post(testUser, new Date(), "this is test posting");

    session.beginTransaction();
    Integer id = (Integer) session.save(testPost);
    testPost.setId(id);
    session.getTransaction().commit();
    session.close();

    session = factory.openSession();
    Post otherPost = session.get(Post.class, id);
    assertEquals(otherPost, testPost);
    session.close();
    logger.trace(otherPost);
  }
}
