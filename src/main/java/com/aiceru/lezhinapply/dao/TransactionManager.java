package com.aiceru.lezhinapply.dao;

/**
 * Created by iceru on 2016. 8. 6..
 */
public class TransactionManager {
  public interface TransactionCallable<T> {
    public T execute();
  }

  public static <T> T doInTransaction(TransactionCallable<T> c, Dao dao) {
    T result = null;
    try {
      dao.getCurrentSession();
      dao.beginTransaction();

      result = (T) c.execute();

    } catch (RuntimeException e) {
      dao.rollbackIfTxActive();
      throw e;
    } finally {
      dao.commit();
      dao.closeCurrentSession();
    }
    return result;
  }
}
