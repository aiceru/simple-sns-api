package com.aiceru.lezhinapply.util.jpa;

import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.*;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 2..
 */
public class SqlExecuter {
  private static Session session;
  private static Transaction tx;

  public static void execute(InputStream inputStream) throws IOException {
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    tx = session.beginTransaction();

    //FileReader fileReader = new FileReader(filename);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//fileReader);

    String sql = bufferedReader.readLine();
    while(sql != null) {
      session.createNativeQuery(sql).executeUpdate();
      sql = bufferedReader.readLine();
    }

    tx.commit();
    session.close();
  }

  public static void pustPosts() {
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    tx = session.beginTransaction();
    List<User> allusers = session.createQuery("from User").list();
    for(User me : allusers) {
      List<Post> myPosts = me.getPosts();
      List<User> myFollowers = me.getFollowers();
      for(Post post : myPosts) {
        me.addFollowingPost(post);
        for(User follower : myFollowers) {
          follower.addFollowingPost(post);
        }
      }
    }
    tx.commit();
    session.close();
  }
}
