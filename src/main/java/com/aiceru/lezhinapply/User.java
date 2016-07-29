package com.aiceru.lezhinapply;

import javax.persistence.*;

/**
 * Created by iceru on 2016. 7. 29..
 */
@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private int userId;

  @Column(name = "name")
  private String name;

  @Column(name = "passwd")
  private String password;

  public User() {
  }

  public User(String name, String password) {
    this.name = name;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User info : name [" + this.name + "], password [" + this.password + "]";
  }
}
