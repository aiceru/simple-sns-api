package com.aiceru.lezhinapply.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iceru on 2016. 7. 31..
 */
public interface DaoInterface<T, Id extends Serializable> {
  public Id persist(T entity);
  public void update(T entity);
  public T findById(Id id);
  public void delete(T entity);
  public List<T> findAll();
  public void deleteAll();
}
