package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernateUserDaoTest {
  DaoInterface<User, Integer> dao = new HibernateUserDao();
  User gasfard = new User("gasfard", "idiots@somemail.com");
  User loyd = new User("loyd", "loyd@idiots.com");

  @Test
  public void testPersistAndFindById() {
    int id1 = dao.persist(gasfard);
    int id2 = dao.persist(loyd);

    assertEquals(id1+1, id2);

/*
    List<User> noUsers = dao.findAll();
    assertEquals(noUsers.size(), 0);

    User foundUser = dao.findById(savedId);

    assertEquals(gasfard.getName(), foundUser.getName());
    assertEquals(gasfard.getEmail(), foundUser.getEmail());

    User loyd = new User("loyd", "loyd@idiots.com");
    dao.persist(loyd);

    List<User> idiots = dao.findAll();
    assertEquals(idiots.size(), 2);

    assertEquals(idiots.get(0).getId(), 1);
    assertEquals(idiots.get(0).getName(), gasfard.getName());
    assertEquals(idiots.get(0).getEmail(), gasfard.getEmail());

    assertEquals(idiots.get(1).getId(), 2);
    assertEquals(idiots.get(1).getName(), loyd.getName());
    assertEquals(idiots.get(1).getEmail(), loyd.getEmail());*/
  }

  @Test
  public void testFindById() {
    User notfound = dao.findById(1);
    assertNull(notfound);

    int id1 = dao.persist(gasfard);
    int id2 = dao.persist(loyd);

    User foundUser = dao.findById(id1);
    assertEquals(foundUser.getId(), id1);
    assertEquals(foundUser.getName(), gasfard.getName());
    assertEquals(foundUser.getEmail(), gasfard.getEmail());

    foundUser = dao.findById(id2);
    assertEquals(foundUser.getId(), id2);
    assertEquals(foundUser.getName(), loyd.getName());
    assertEquals(foundUser.getEmail(), loyd.getEmail());
  }

}
