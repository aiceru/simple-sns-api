package com.aiceru.lezhinapply.resource;

import com.aiceru.lezhinapply.dao.Dao;
import com.aiceru.lezhinapply.dao.HibernateDao;
import com.aiceru.lezhinapply.dao.TransactionManager;
import com.aiceru.lezhinapply.dao.TransactionManager.TransactionCallable;
import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.model.Target;
import com.aiceru.lezhinapply.model.User;
import com.aiceru.lezhinapply.util.filter.TimeLineView;
import com.aiceru.lezhinapply.util.filter.UserDetailView;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.persistence.PersistenceException;
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
  public Response createUser(final User user, @Context UriInfo uriInfo) {
    User result;
    try {
      result = TransactionManager.doInTransaction(new TransactionCallable<User>() {
        @Override
        public User execute() {
          int createdId = dao.persist(user);
          User newUser = dao.findById(User.class, createdId);
          return newUser;
        }
      }, dao);
    } catch (IllegalStateException e) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(result.getUserId()));
    return Response.created(builder.build()).entity(
            new GenericEntity<User>(result, User.class)).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getUsers(@QueryParam("q") final String searchQuery,
                             @DefaultValue("0") @QueryParam("offset") final int offset,
                             @DefaultValue("-1") @QueryParam("limit") final int limit) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        String name = null;
        return dao.findAll(User.class, searchQuery, null, true, offset, limit);
      }
    }, dao);
    return result;
  }

  @GET
  @Path("/{userId}")
  public Response getUser(@PathParam("userId") final int userid) {
    User result = TransactionManager.doInTransaction(new TransactionCallable<User>() {
      @Override
      public User execute() {
        return dao.findById(User.class, userid);
      }
    }, dao);
    Response response;
    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @PUT
  @Path("/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateUser(final User updateuser, @PathParam("userId") final int userid, @Context UriInfo uriInfo) {
    User result;
    try {
      result = TransactionManager.doInTransaction(new TransactionCallable<User>() {
        @Override
        public User execute() {
          User user = dao.findById(User.class, userid);
          if (user == null) {
            return null;
          }
          if (updateuser.getName() != null) user.setName(updateuser.getName());
          if (updateuser.getEmail() != null) user.setEmail(updateuser.getEmail());
          // user id does not changed;
          return user;
        }
      }, dao);
    } catch (PersistenceException e) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    if ( result == null ) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @DELETE
  @Path("/{userId}")
  public Response deleteUser(@PathParam("userId") final int userid) {
    User result = TransactionManager.doInTransaction(new TransactionCallable<User>() {
      @Override
      public User execute() {
        User user = dao.findById(User.class, userid);
        if (user == null) {
          return null;
        }
        dao.delete(user);
        return user;
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @GET
  @Path("/{userId}/followers")
  public Response getFollowers(@PathParam("userId") final int userid,
                               @DefaultValue("0") @QueryParam("offset") final int offset,
                               @DefaultValue("-1") @QueryParam("limit") final int limit) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        int from = offset, to = offset + limit;
        User user = dao.findById(User.class, userid);
        if (user == null) {
          return null;
        }
        List<User> followers = user.getFollowers();
        if (to > followers.size() || limit <= 0) {
          to = followers.size();
        }
        if (from > followers.size()) {
          from = followers.size();
        }
        return followers.subList(from, to);
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }

  @PUT
  @Path("/{userId}/followers")
  public Response updateFollowers(final Target target, @PathParam("userId") final int userid) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        User user = dao.findById(User.class, userid);
        User follower = dao.findById(User.class, target.getTargetId());
        if (user == null || follower == null) {
          return null;
        }
        user.addFollower(follower);
        for(Post p : user.getPosts()) {
          follower.addFollowingPost(p);
        }
        return user.getFollowers();
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{userId}/followers/{targetId}")
  public Response DeleteFollowers(@PathParam("userId") final int userid, @PathParam("targetId") final int targetId) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        User user = dao.findById(User.class, userid);
        User follower = dao.findById(User.class, targetId);
        if (user == null || follower == null) {
          return null;
        }
        for(Post p : user.getPosts()) {
          follower.removeFollowingPost(p);
        }
        user.removeFollower(follower);
        return user.getFollowers();
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }

  @GET
  @Path("/{userId}/followings")
  public Response GetFollowings(@PathParam("userId") final int userid,
                                @DefaultValue("0") @QueryParam("offset") final int offset,
                                @DefaultValue("-1") @QueryParam("limit") final int limit) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        int from = offset, to = offset + limit;
        User user = dao.findById(User.class, userid);
        if (user == null) {
          return null;
        }
        List<User> followings = user.getFollowings();
        if (to > followings.size() || limit <= 0) {
          to = followings.size();
        }
        if (from > followings.size()) {
          from = followings.size();
        }
        return followings.subList(from, to);
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }

  @PUT
  @Path("/{userId}/followings")
  public Response updateFollowings(final Target target, @PathParam("userId") final int userid) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        User user = dao.findById(User.class, userid);
        User following = dao.findById(User.class, target.getTargetId());
        if (user == null || following == null) {
          return null;
        }
        user.addFollowing(following);
        for(Post p : following.getPosts()) {
          user.addFollowingPost(p);
        }
        return user.getFollowings();
      }
    }, dao);
    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }

  @DELETE
  @Path("/{userId}/followings/{targetId}")
  public Response DeleteFollowings(@PathParam("userId") final int userid, @PathParam("targetId") final int targetId) {
    List<User> result = TransactionManager.doInTransaction(new TransactionCallable<List<User>>() {
      @Override
      public List<User> execute() {
        User user = dao.findById(User.class, userid);
        User following = dao.findById(User.class, targetId);
        if (user == null || following == null) {
          return null;
        }
        for (Post p : following.getPosts()) {
          user.removeFollowingPost(p);
        }
        user.removeFollowing(following);
        return user.getFollowings();
      }
    }, dao);
    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<User>> entity =
            new GenericEntity<List<User>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }

  @GET
  @Path("/{userId}/timeline")
  @TimeLineView
  @Produces(MediaType.APPLICATION_JSON)
  public List<Post> getTimeLine(@PathParam("userId") final int userid,
                                @DefaultValue("0") @QueryParam("offset") final int offset,
                                @DefaultValue("-1") @QueryParam("limit") final int limit) {
    List<Post> result = TransactionManager.doInTransaction(new TransactionCallable<List<Post>>() {
      @Override
      public List<Post> execute() {
        int from = offset, to = offset + limit;
        User user = dao.findById(User.class, userid);
        if (user == null) {
          return null;
        }
        List<Post> timeline = user.getFollowingPosts();
        if (to > timeline.size() || limit <= 0) {
          to = timeline.size();
        }
        if (from > timeline.size()) {
          from = timeline.size();
        }
        return timeline.subList(from, to);
      }
    }, dao);
    return result;
  }

  @POST
  @Path("/{userId}/posts")
  @TimeLineView
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUsersPost(final Post post, @PathParam("userId") final int userid, @Context UriInfo uriInfo) {
    Post result = TransactionManager.doInTransaction(new TransactionCallable<Post>() {
      @Override
      public Post execute() {
        User user = dao.findById(User.class, userid);
        if (user == null) {
          return null;
        }

        int createdId = dao.persist(new Post(user, new Date(), post.getContent()));
        Post newPost = dao.findById(Post.class, createdId);

        for(User follower : user.getFollowers()) {
          follower.addFollowingPost(newPost);
        }
        user.addFollowingPost(newPost);
        return newPost;
      }
    }, dao);
    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Integer.toString(result.getPostId()));
    return Response.created(uriBuilder.build()).entity(
            new GenericEntity<Post>(result, Post.class)).build();
  }

  @GET
  @Path("/{userId}/posts/")
  @UserDetailView
  public Response getUsersPosts(@PathParam("userId") int userid,
                                @DefaultValue("0") @QueryParam("offset") final int offset,
                                @DefaultValue("-1") @QueryParam("limit") final int limit) {
    List<Post> result = TransactionManager.doInTransaction(new TransactionCallable<List<Post>>() {
      @Override
      public List<Post> execute() {
        int from = offset, to = offset + limit;
        User user = dao.findById(User.class, userid);
        if (user == null) {
          return null;
        }
        List<Post> posts = user.getPosts();
        if (to > posts.size() || limit <= 0) {
          to = posts.size();
        }
        if (from > posts.size()) {
          from = posts.size();
        }
        return posts.subList(from, to);
      }
    }, dao);
    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    GenericEntity<List<Post>> entity =
            new GenericEntity<List<Post>>(Lists.newArrayList(result)) {};
    return Response.ok(entity).build();
  }
}