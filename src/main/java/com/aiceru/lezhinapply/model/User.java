package com.aiceru.lezhinapply.model;

import com.aiceru.lezhinapply.util.filter.UserDetailView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by iceru on 2016. 7. 29..
 */
@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private int userId;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @ManyToMany
  @JoinTable(name = "FOLLOWINGS",
          joinColumns = {@JoinColumn(name = "follower_id", nullable = false)},
          inverseJoinColumns = {@JoinColumn(name = "following_id", nullable = false)})
  @JsonIgnore
  private List<User> followings;

  @ManyToMany
  @JoinTable(name = "FOLLOWINGS",
          joinColumns = {@JoinColumn(name = "following_id", nullable = false)},
          inverseJoinColumns = {@JoinColumn(name = "follower_id", nullable = false)})
  @JsonIgnore
  private List<User> followers;

  @OneToMany(targetEntity = Post.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "createdBy")
  @UserDetailView
  private List<Post> posts;

  // TODO : Confirm this!!
  @ManyToMany
  @JoinTable(name = "FOLLOWINGPOSTS",
          joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
          inverseJoinColumns = {@JoinColumn(name = "post_id", nullable = false)})
  @JsonIgnore
  private List<Post> followingPosts;

  public User() {
    posts = new ArrayList<Post>();
    followers = new ArrayList<User>();
    followings = new ArrayList<User>();
    followingPosts = new ArrayList<Post>();
  }

  public User(String name, String email) {
    this();
    this.name = name;
    this.email = email;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
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

  public List<User> getFollowings() {
    return followings;
  }

  public void setFollowings(List<User> followings) {
    this.followings = followings;
  }

  public List<User> getFollowers() {
    return followers;
  }

  public void setFollowers(List<User> followers) {
    this.followers = followers;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public Collection<Post> getFollowingPosts() {
    return followingPosts;
  }

  public void setFollowingPosts(List<Post> followingPosts) {
    this.followingPosts = followingPosts;
  }

  public boolean addFollowing(User u) {
    return followings.add(u);
  }

  public boolean addFollower(User u) {
    return followers.add(u);
  }

  public boolean addPost(Post p) {
    return posts.add(p);
  }

  public boolean addFollowingPost(Post p) {
    return followingPosts.add(p);
  }

  @Override
  public String toString() {
    return "User{" +
            "userId=" + userId +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    return ( userId == user.getUserId() &&
            name.equals(user.getName()) &&
            email.equals(user.getEmail()) );
  }

  @Override
  public int hashCode() {
    return getUserId();
  }
}
