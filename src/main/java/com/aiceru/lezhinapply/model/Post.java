package com.aiceru.lezhinapply.model;

import com.aiceru.lezhinapply.util.filter.TimeLineView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  @TimeLineView
  private User createdBy;

  @Column(name = "time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timeStamp;

  @Column(name = "content")
  private String content;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "FOLLOWINGPOSTS",
          joinColumns = {@JoinColumn(name = "post_id", nullable = false)},
          inverseJoinColumns = {@JoinColumn(name = "user_id", nullable = false)})
  @JsonIgnore
  private List<User> followers;

  public Post() {
  }

  public Post(User createdBy, Date timeStamp, String content) {
    this.createdBy = createdBy;
    this.timeStamp = timeStamp;
    this.content = content;
    this.followers = new ArrayList<User>();
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
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

  public List<User> getFollowers() {
    return followers;
  }

  public void setFollowers(List<User> followers) {
    this.followers = followers;
  }

  public boolean addFollower(User user) {
    return followers.add(user);
  }

  @Override
  public String toString() {
    return "Post{" +
            "postId=" + postId +
            ", createdBy=" + createdBy +
            ", timeStamp=" + timeStamp +
            ", content='" + content + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Post post = (Post) o;

    return ( postId == post.getPostId() &&
            createdBy.equals(post.getCreatedBy()) &&
            (timeStamp.compareTo(post.getTimeStamp()) == 0 || post.getTimeStamp().compareTo(timeStamp) == 0) &&
            content.equals(post.getContent()) );
  }

  @Override
  public int hashCode() {
    return getPostId();
  }
}
