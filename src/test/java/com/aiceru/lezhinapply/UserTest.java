package com.aiceru.lezhinapply;

import com.aiceru.lezhinapply.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

/**
 * Created by iceru on 2016. 7. 30..
 */
public class UserTest {

  SessionFactory factory = HibernateUtil.getSessionFactory();

  @Test
  public void test() {
    User testUser = new User("arahansa", "passwd123");
    Session session = factory.openSession();
    session.beginTransaction();
    session.save(testUser);
    session.getTransaction().commit();

    User newUser = session.get(User.class, 1);
    System.out.println(newUser);
  }
}
