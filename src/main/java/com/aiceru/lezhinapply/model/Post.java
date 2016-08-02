package com.aiceru.lezhinapply.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
  private int id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User createUser;

  @Column(name = "time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date timeStamp;

  @Column(name = "content")
  private String content;

  public Post() {
  }

  public Post(User createUser, Date timeStamp, String content) {
    this.createUser = createUser;
    this.timeStamp = timeStamp;
    this.content = content;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getCreateUser() {
    return createUser;
  }

  public void setCreateUser(User createUser) {
    this.createUser = createUser;
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
            "id=" + id +
            ", createUser=" + createUser +
            ", timeStamp=" + timeStamp +
            ", content='" + content + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Post post = (Post) o;

    return ( id == post.getId() &&
            createUser.equals(post.getCreateUser()) &&
            (timeStamp.compareTo(post.getTimeStamp()) == 0 || post.getTimeStamp().compareTo(timeStamp) == 0) &&
            content.equals(post.getContent()) );
  }

  @Override
  public int hashCode() {
    return getId();
  }
}
