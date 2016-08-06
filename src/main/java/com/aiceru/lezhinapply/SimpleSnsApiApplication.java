package com.aiceru.lezhinapply;

import com.aiceru.lezhinapply.util.jpa.SqlExecuter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.io.IOException;

/**
 * Created by iceru on 2016. 8. 3..
 */
@ApplicationPath("/")
public class SimpleSnsApiApplication extends ResourceConfig {
  public SimpleSnsApiApplication() {
    packages("com.aiceru.lezhinapply");
    register(EntityFilteringFeature.class);
    register(JacksonFeature.class);

    /*
     * For test only...
     */
    /*try {
      SqlExecuter.execute(
              this.getClass().getClassLoader().getResourceAsStream("/create_test_data.txt"));
      SqlExecuter.pustPosts();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
  }
}
