package com.aiceru.lezhinapply.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by iceru on 2016. 7. 30..
 */
@Entity
@Table(name = "POSTS")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private int postId;

  @Column(name = "user_id")
  private int userId;

  @Column(name = "time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timeStamp;

  @Column(name = "content")
  private String content;

  public Post() {
  }

  public Post(User createUser, Date timeStamp, String content) {
    this.userId = createUser.getUserId();
    this.timeStamp = timeStamp;
    this.content = content;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Post{" +
            "postId=" + postId +
            ", userId=" + userId +
            ", timeStamp=" + timeStamp +
            ", content='" + content + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Post post = (Post) o;

    return getPostId() == post.getPostId();

  }

  @Override
  public int hashCode() {
    return getPostId();
  }
}
