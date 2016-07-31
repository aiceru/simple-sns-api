package com.aiceru.lezhinapply.model;

import com.aiceru.lezhinapply.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;

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
    session.save(testUser);
    session.getTransaction().commit();
    session.close();

    session = factory.openSession();
    User otherUser = session.get(User.class, 1);
    assertEquals(otherUser.getName(), testUser.getName());
    assertEquals(otherUser.getId(), 1);
    assertEquals(otherUser.getEmail(), testUser.getEmail());
    session.close();
    logger.trace(otherUser);
  }

  @Test
  public void testPostMapping() {
    Session session = factory.openSession();

    Post testPost = new Post(session.get(User.class, 1), new Date(), "this is test posting");

    session.beginTransaction();
    session.save(testPost);
    session.getTransaction().commit();
    session.close();

    session = factory.openSession();
    Post otherPost = session.get(Post.class, 1);
    assertEquals(otherPost.getPostId(), 1);
    assertEquals(otherPost.getCreateUser(), testPost.getCreateUser());
    assertEquals(otherPost.getContent(), testPost.getContent());
    session.close();
    logger.trace(otherPost);
  }

  @AfterClass
  public static void tearDown() {
    factory.close();
  }
}
