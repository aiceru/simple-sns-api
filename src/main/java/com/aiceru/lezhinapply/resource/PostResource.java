package com.aiceru.lezhinapply.resource;

import com.aiceru.lezhinapply.dao.Dao;
import com.aiceru.lezhinapply.dao.HibernateDao;
import com.aiceru.lezhinapply.dao.TransactionManager;
import com.aiceru.lezhinapply.dao.TransactionManager.TransactionCallable;
import com.aiceru.lezhinapply.model.Post;
import com.aiceru.lezhinapply.util.filter.TimeLineView;
import com.aiceru.lezhinapply.util.jpa.HibernateUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by iceru on 2016. 8. 5..
 */
@Path("/posts")
public class PostResource {
  private Dao dao;

  public PostResource() {
    this.dao = new HibernateDao(HibernateUtil.getSessionFactory());
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Post> getPosts(@DefaultValue("0") @QueryParam("offset") final int offset,
                             @DefaultValue("-1") @QueryParam("limit") final int limit) {
    List<Post> result = TransactionManager.doInTransaction(new TransactionCallable<List<Post>>() {
      @Override
      public List<Post> execute() {
        return dao.findAll(Post.class, null, "timeStamp", false, offset, limit);
      }
    }, dao);
    return result;
  }

  @GET
  @Path("/{postId}")
  @TimeLineView
  public Response getPost(@PathParam("postId") final int postid) {
    Post result = TransactionManager.doInTransaction(new TransactionCallable<Post>() {
      @Override
      public Post execute() {
        return dao.findById(Post.class, postid);
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @PUT
  @Path("/{postId}")
  @TimeLineView
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updatePost(final Post updatepost, @PathParam("postId") final int postid, @Context UriInfo uriInfo) {
    Post result = TransactionManager.doInTransaction(new TransactionCallable<Post>() {
      @Override
      public Post execute() {
        Post post = dao.findById(Post.class, postid);
        if (post == null) {
          return null;
        }
        post.setContent(updatepost.getContent());
        return post;
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @DELETE
  @Path("/{postId}")
  @TimeLineView
  public Response deletePost(@PathParam("postId") final int postid) {
    Post result = TransactionManager.doInTransaction(new TransactionCallable<Post>() {
      @Override
      public Post execute() {
        Post post = dao.findById(Post.class, postid);
        if (post == null) {
          return null;
        }
        post.getCreatedBy().removePost(post);
        dao.delete(post);
        return post;
      }
    }, dao);

    if (result == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build();
  }
}
