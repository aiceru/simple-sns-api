package com.aiceru.lezhinapply.dao;

import com.aiceru.lezhinapply.model.User;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by iceru on 2016. 7. 31..
 */
public class HibernateUserDaoTest {
  DaoInterface<User, Integer> dao = new HibernateUserDao();
  User gasfard;
  User loyd;

  @Before
  public void setup() {
    gasfard = new User("gasfard", "idiots@somemail.com");
    loyd = new User("loyd", "loyd@idiots.com");
  }

  @After
  public void teardown() {
    dao.deleteAll();
  }

  @Test
  public void testPersist() {
    int id1 = dao.persist(gasfard);
    int id2 = dao.persist(loyd);

    assertEquals(id1+1, id2);
  }

  @Test
  public void testFindById() {
    User foundUser = dao.findById(1);
    assertNull(foundUser);

    gasfard.setId(dao.persist(gasfard));
    loyd.setId(dao.persist(loyd));

    foundUser = dao.findById(gasfard.getId());
    assertEquals(gasfard, foundUser);

    foundUser = dao.findById(loyd.getId());
    assertEquals(loyd, foundUser);
  }

  @Test
  public void testFindAll() {
    List<User> users = dao.findAll();
    assertEquals(users.size(), 0);

    gasfard.setId(dao.persist(gasfard));
    loyd.setId(dao.persist(loyd));

    users = dao.findAll();
    assertEquals(users.size(), 2);
    assertEquals(users.get(0), gasfard);
    assertEquals(users.get(1), loyd);
  }

  @Test
  public void testUpdate() {
    int id = dao.persist(gasfard);
    gasfard.setId(id);
    assertEquals(gasfard, dao.findById(id));

    gasfard.setName("gasfard_revisited");
    dao.update(gasfard);
    assertEquals(gasfard, dao.findById(id));
  }

  @Test
  public void testDelete() {
    gasfard.setId(dao.persist(gasfard));
    assertEquals(gasfard, dao.findById(gasfard.getId()));

    dao.delete(gasfard);
    assertNull(dao.findById(gasfard.getId()));
  }

  @Test
  public void testDeleteAll() {
    gasfard.setId(dao.persist(gasfard));
    loyd.setId(dao.persist(loyd));

    assertEquals(gasfard, dao.findById(gasfard.getId()));
    assertEquals(loyd, dao.findById(loyd.getId()));

    dao.deleteAll();

    assertNull(dao.findById(gasfard.getId()));
    assertNull(dao.findById(loyd.getId()));
  }
}
