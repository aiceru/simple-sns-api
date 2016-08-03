package com.aiceru.lezhinapply;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by iceru on 2016. 8. 3..
 */
@ApplicationPath("/")
public class SimpleSnsApiApplication extends ResourceConfig {
  public SimpleSnsApiApplication() {
    packages("com.aiceru.lezhinapply");
    register(EntityFilteringFeature.class);
    register(JacksonFeature.class);
  }
}
