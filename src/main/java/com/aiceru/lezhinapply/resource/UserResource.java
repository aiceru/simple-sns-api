package com.aiceru.lezhinapply.resource;

import com.aiceru.lezhinapply.dao.DaoInterface;
import com.aiceru.lezhinapply.dao.HibernatePostDao;
import com.aiceru.lezhinapply.dao.HibernateUserDao;
import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.filter.TimeLineView;
import com.aiceru.lezhinapply.util.filter.UserDetailView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 2..
 */
@Path("/users")
public class UserResource {
  private DaoInterface<User, Integer> userDao = new HibernateUserDao();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(User user, @Context UriInfo uriInfo) {
    userDao.getCurrentSessionWithTransaction();
    user.setUserId(userDao.persist(user));
    userDao.closeCurrentSessionWithTransaction();
    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(user.getUserId()));
    return Response.created(builder.build()).entity(
            new GenericEntity<User>(user, user.getClass())).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @TimeLineView
  public List<User> getUsers() {
    userDao.getCurrentSessionWithTransaction();
    List<User> users = userDao.findAll();
    userDao.closeCurrentSessionWithTransaction();
    return users;
  }

  @GET
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @UserDetailView
  public Response getUser(@PathParam("id") int userid) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    userDao.closeCurrentSessionWithTransaction();
    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(user, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateUser(User updateUser, @PathParam("id") int userid, @Context UriInfo uriInfo) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    if (user == null) {
      userDao.closeCurrentSessionWithTransaction();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    user.setName(updateUser.getName());
    user.setEmail(updateUser.getEmail());
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(user, MediaType.APPLICATION_JSON).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteUser(@PathParam("id") int userid) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    if (user == null) {
      userDao.closeCurrentSessionWithTransaction();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    userDao.delete(user);
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(user, MediaType.APPLICATION_JSON_TYPE).build();
  }
}