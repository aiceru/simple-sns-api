package com.aiceru.lezhinapply.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

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
  @JoinTable(name = "FOLLOWS",
          joinColumns = {@JoinColumn(name = "following_id", referencedColumnName = "user_id", nullable = false)},
          inverseJoinColumns = {@JoinColumn(name = "follower_id", referencedColumnName = "user_id", nullable = false)})
  private Collection<User> following;

  public User() {
  }

  public User(String name, String email) {
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

  public boolean addFollowing(User u) {
    if(this.following == null) {
      following = new ArrayList<User>();
    }
    return following.add(u);
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

    return getUserId() == user.getUserId();

  }

  @Override
  public int hashCode() {
    return getUserId();
  }
}
