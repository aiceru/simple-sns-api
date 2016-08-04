package com.aiceru.lezhinapply.resource;

import com.aiceru.lezhinapply.dao.DaoInterface;
import com.aiceru.lezhinapply.dao.HibernateUserDao;
import com.aiceru.lezhinapply.model.Target;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.filter.UserDetailView;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.*;
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
  public List<User> getUsers() {
    userDao.getCurrentSessionWithTransaction();
    List<User> users = userDao.findAll();
    userDao.closeCurrentSessionWithTransaction();
    return users;
  }

  @GET
  @Path("/{userId}")
  @UserDetailView
  public Response getUser(@PathParam("userId") int userid) {
    userDao.getCurrentSessionWithTransaction();
    Response response;
    User user = userDao.findById(userid);
    if (user == null) {
      response = Response.status(Response.Status.NOT_FOUND).build();
    }
    else {
      response = Response.ok(user, MediaType.APPLICATION_JSON_TYPE).build();
    }
    userDao.closeCurrentSessionWithTransaction();
    return response;
  }

  @PUT
  @Path("/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateUser(User updateUser, @PathParam("userId") int userid, @Context UriInfo uriInfo) {
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
  @Path("/{userId}")
  public Response deleteUser(@PathParam("userId") int userid) {
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

  @GET
  @Path("/{userId}/followers")
  public Response getFollowers(@PathParam("userId") int userid) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    if (user == null) {
      userDao.closeCurrentSessionWithTransaction();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<User> followers = user.getFollowers();
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(followers)) {};
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(entity).build();
  }

  @PUT
  @Path("/{userId}/followers")
  public Response updateFollowers(Target target, @PathParam("userId") int userid) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    User follower = userDao.findById(target.getTargetId());
    if (user == null || follower == null) {
      userDao.closeCurrentSessionWithTransaction();;
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<User> followers = user.getFollowers();
    followers.add(follower);
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(followers)) {};
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{userId}/followers/{targetId}")
  public Response DeleteFollowers(@PathParam("userId") int userid, @PathParam("targetId") int targetId) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    User follower = userDao.findById(targetId);
    if (user == null || follower == null) {
      userDao.closeCurrentSessionWithTransaction();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<User> followers = user.getFollowers();
    followers.remove(follower);
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(followers)) {};
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(entity).build();
  }

  @GET
  @Path("/{userId}/followings")
  public Response GetFollowings(@PathParam("userId") int userid) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    if (user == null) {
      userDao.closeCurrentSessionWithTransaction();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<User> followings = user.getFollowings();
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(followings)) {};
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(entity).build();
  }

  @PUT
  @Path("/{userId}/followings")
  public Response updateFollowings(Target target, @PathParam("userId") int userid) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    User following = userDao.findById(target.getTargetId());
    if (user == null || following == null) {
      userDao.closeCurrentSessionWithTransaction();;
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<User> followings = user.getFollowings();
    followings.add(following);
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(followings)) {};
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{userId}/followings/{targetId}")
  public Response DeleteFollowings(@PathParam("userId") int userid, @PathParam("targetId") int targetId) {
    userDao.getCurrentSessionWithTransaction();
    User user = userDao.findById(userid);
    User following = userDao.findById(targetId);
    if (user == null || following == null) {
      userDao.closeCurrentSessionWithTransaction();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    List<User> followings = user.getFollowings();
    followings.remove(following);
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(followings)) {};
    userDao.closeCurrentSessionWithTransaction();
    return Response.ok(entity).build();
  }
}