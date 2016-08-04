package com.aiceru.lezhinapply.model;

/**
 * Created by iceru on 2016. 8. 4..
 * Wrapper class, used for make jackson accept request's body
 * that only have one integer variable in JSON format.
 */
public class Target {
  private int targetId;

  public Target() {
  }

  public int getTargetId() {
    return targetId;
  }

  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
}
