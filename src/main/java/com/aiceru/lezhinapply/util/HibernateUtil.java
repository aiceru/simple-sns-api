package com.aiceru.lezhinapply.util;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by iceru on 2016. 7. 30..
 */
public class HibernateUtil {
  private static SessionFactory sessionFactory;
  private static String configFile = "hibernate.cfg.xml";

  static {
    try {
      Configuration cfg = new Configuration().configure(configFile)
              .addAnnotatedClass(User.class)
              .addAnnotatedClass(Post.class);
      StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
      sb.applySettings(cfg.getProperties());
      StandardServiceRegistry standardServiceRegistry = sb.build();
      sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
    } catch (Throwable th) {
      th.printStackTrace();
      throw new ExceptionInInitializerError(th);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public void shutdown() {
    sessionFactory.close();
  }
}
