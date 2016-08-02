package com.aiceru.lezhinapply.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by iceru on 2016. 8. 2..
 */
public class SqlExecuter {
  private static Session session;
  private static Transaction tx;

  public static void execute(String sql) {
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    tx = session.beginTransaction();

    int result = session.createNativeQuery(sql).executeUpdate();

    tx.commit();
    session.close();
  }
}
