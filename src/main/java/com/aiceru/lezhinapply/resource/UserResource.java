package com.aiceru.lezhinapply.resource;

import com.aiceru.lezhinapply.dao.Dao;
import com.aiceru.lezhinapply.dao.HibernateDao;
import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.Target;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.filter.TimeLineView;
import com.aiceru.lezhinapply.util.filter.UserDetailView;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Date;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 2..
 */
@Path("/users")
public class UserResource {
  private Dao dao;

  // to be moved to bean container later...
  public UserResource() {
    dao = new HibernateDao(HibernateUtil.getSessionFactory());
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(User user, @Context UriInfo uriInfo) {
    dao.getCurrentSession();
    dao.beginTransaction();

    user.setUserId(dao.persist(user));

    dao.commit();

    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(user.getUserId()));
    return Response.created(builder.build()).entity(
            new GenericEntity<User>(user, user.getClass())).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getUsers() {
    dao.getCurrentSession();
    dao.beginTransaction();

    List<User> users = dao.findAll(User.class);

    dao.commit();
    return users;
  }

  @GET
  @Path("/{userId}")
  public Response getUser(@PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    Response response;
    User user = dao.findById(User.class, userid);
    if (user == null) {
      response = Response.status(Response.Status.NOT_FOUND).build();
    } else {
      response = Response.ok(user, MediaType.APPLICATION_JSON_TYPE).build();
    }

    dao.commit();
    return response;
  }

  @PUT
  @Path("/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateUser(User updateuser, @PathParam("userId") int userid, @Context UriInfo uriInfo) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    if (user == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    user.setName(updateuser.getName());
    user.setEmail(updateuser.getEmail());

    dao.commit();
    return Response.ok(user, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @DELETE
  @Path("/{userId}")
  public Response deleteUser(@PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    if (user == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    dao.delete(user);

    dao.commit();
    return Response.ok(user, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @GET
  @Path("/{userId}/followers")
  public Response getFollowers(@PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    if (user == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(user.getFollowers())) {};

    dao.commit();
    return Response.ok(entity).build();
  }

  @PUT
  @Path("/{userId}/followers")
  public Response updateFollowers(Target target, @PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    User follower = dao.findById(User.class, target.getTargetId());
    if (user == null || follower == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    user.addFollower(follower);
    for(Post p : user.getPosts()) {
      follower.addFollowingPost(p);
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(user.getFollowers())) {};

    dao.commit();
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{userId}/followers/{targetId}")
  public Response DeleteFollowers(@PathParam("userId") int userid, @PathParam("targetId") int targetId) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    User follower = dao.findById(User.class, targetId);
    if (user == null || follower == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    for(Post p : user.getPosts()) {
      follower.removeFollowingPost(p);
    }
    user.removeFollower(follower);
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(user.getFollowers())) {};

    dao.commit();
    return Response.ok(entity).build();
  }

  @GET
  @Path("/{userId}/followings")
  public Response GetFollowings(@PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    if (user == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(user.getFollowings())) {};

    dao.commit();
    return Response.ok(entity).build();
  }

  @PUT
  @Path("/{userId}/followings")
  public Response updateFollowings(Target target, @PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    User following = dao.findById(User.class, target.getTargetId());
    if (user == null || following == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    user.addFollowing(following);
    for(Post p : following.getPosts()) {
      user.addFollowingPost(p);
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(user.getFollowings())) {};

    dao.commit();
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{userId}/followings/{targetId}")
  public Response DeleteFollowings(@PathParam("userId") int userid, @PathParam("targetId") int targetId) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    User following = dao.findById(User.class, targetId);
    if (user == null || following == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    for(Post p : following.getPosts()) {
      user.removeFollowingPost(p);
    }
    user.removeFollowing(following);
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(user.getFollowings())) {};

    dao.commit();
    return Response.ok(entity).build();
  }

  @GET
  @Path("/{userId}/timeline")
  @TimeLineView
  @Produces(MediaType.APPLICATION_JSON)
  public List<Post> getTimeLine(@PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    List<Post> posts = (List<Post>) dao.findById(User.class, userid).getFollowingPosts();
    for(Post p : posts) {
      p.getCreatedBy().getUserId();
    }

    dao.commit();
    return posts;
  }

  @POST
  @Path("/{userId}/posts")
  @TimeLineView
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUsersPost(Post post, @PathParam("userId") int userid, @Context UriInfo uriInfo) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    if (user == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    Post newPost = new Post(user, new Date(), post.getContent());
    newPost.setPostId(dao.persist(newPost));

    for(User follower : user.getFollowers()) {
      follower.addFollowingPost(newPost);
    }
    user.addFollowingPost(newPost);

    dao.commit();
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(newPost.getPostId()));
    return Response.created(uriBuilder.build()).entity(
            new GenericEntity<Post>(newPost, newPost.getClass())).build();
  }

  @GET
  @Path("/{userId}/posts/")
  @UserDetailView
  public Response getUsersPosts(@PathParam("userId") int userid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    User user = dao.findById(User.class, userid);
    if (user == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    GenericEntity<List<Post>> entity =
            new GenericEntity<List<Post>>(Lists.newArrayList(user.getPosts())) {};

    dao.commit();
    return Response.ok(entity).build();
  }
}