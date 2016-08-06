package com.aiceru.lezhinapply.resource;

import com.aiceru.lezhinapply.dao.Dao;
import com.aiceru.lezhinapply.dao.HibernateDao;
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
  public List<Post> getPosts() {
    dao.getCurrentSession();
    dao.beginTransaction();

    List<Post> posts = dao.findAll(Post.class);

    dao.commit();
    return posts;
  }

  @GET
  @Path("/{postId}")
  public Response getPost(@PathParam("postId") int postid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    Response response;
    Post post = dao.findById(Post.class, postid);
    if (post == null) {
      response = Response.status(Response.Status.NOT_FOUND).build();
    } else {
      response = Response.ok(post, MediaType.APPLICATION_JSON_TYPE).build();
    }

    dao.commit();
    return response;
  }

  @PUT
  @Path("/{postId}")
  @TimeLineView
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updatePost(Post updatepost, @PathParam("postId") int postid, @Context UriInfo uriInfo) {
    dao.getCurrentSession();
    dao.beginTransaction();

    Post post = dao.findById(Post.class, postid);
    if (post == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    post.setContent(updatepost.getContent());

    dao.commit();
    return Response.ok(post, MediaType.APPLICATION_JSON_TYPE).build();
  }

  @DELETE
  @Path("/{postId}")
  @TimeLineView
  public Response deletePost(@PathParam("postId") int postid) {
    dao.getCurrentSession();
    dao.beginTransaction();

    Post post = dao.findById(Post.class, postid);
    if (post == null) {
      dao.closeCurrentSession();
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    post.getCreatedBy().removePost(post);
    dao.delete(post);

    dao.commit();
    return Response.ok(post, MediaType.APPLICATION_JSON_TYPE).build();
  }
}
