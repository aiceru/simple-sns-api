package com.aiceru.lezhinapply.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by iceru on 2016. 7. 29..
 */
@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @ManyToMany
  @JoinTable(name = "FOLLOWINGS",
          joinColumns = {@JoinColumn(name = "follower_id", referencedColumnName = "user_id", nullable = false)},
          inverseJoinColumns = {@JoinColumn(name = "following_id", referencedColumnName = "user_id", nullable = false)})
  private Collection<User> followings;


  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private Collection<Post> posts;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "FOLLOWINGPOSTS",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "post_id"))
  private Collection<Post> followingPosts;

  public User() {
  }

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Collection<User> getFollowings() {
    return followings;
  }

  public void setFollowings(Collection<User> followings) {
    this.followings = followings;
  }

  public Collection<Post> getPosts() {
    return posts;
  }

  public void setPosts(Collection<Post> posts) {
    this.posts = posts;
  }

  public Collection<Post> getFollowingPosts() {
    return followingPosts;
  }

  public void setFollowingPosts(Collection<Post> followingPosts) {
    this.followingPosts = followingPosts;
  }

  public boolean addFollowing(User u) {
    if (followings == null) {
      followings = new ArrayList<User>();
    }
    return followings.add(u);
  }

  public boolean addPost(Post p) {
    if (posts == null) {
      posts = new ArrayList<Post>();
    }
    return posts.add(p);
  }

  public boolean addFollowingPost(Post p) {
    if (followingPosts == null) {
      followingPosts = new ArrayList<Post>();
    }
    return followingPosts.add(p);
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    return ( id == user.getId() &&
            name.equals(user.getName()) &&
            email.equals(user.getEmail()) );
  }

  @Override
  public int hashCode() {
    return getId();
  }
}
