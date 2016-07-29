package com.aiceru.lezhinapply;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by iceru on 2016. 7. 29..
 */
@Path("/test")
public class RestTest {
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getHello() {
    return "Hello, Rest!";
  }
}
