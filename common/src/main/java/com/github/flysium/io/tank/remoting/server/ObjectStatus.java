package com.github.flysium.io.tank.remoting.server;

import com.github.flysium.io.tank.model.Direction;

/**
 * Object Status
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class ObjectStatus {

  private static final long serialVersionUID = -94819986374759692L;

  private String id;

  private Direction direction;

  private int x;

  private int y;

  private int health;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public static ObjectStatusBuilder builder() {
    return new ObjectStatusBuilder();
  }

  public static final class ObjectStatusBuilder {

    private final ObjectStatus objectStatus;

    private ObjectStatusBuilder() {
      objectStatus = new ObjectStatus();
    }

    public ObjectStatusBuilder id(String id) {
      objectStatus.setId(id);
      return this;
    }

    public ObjectStatusBuilder direction(Direction direction) {
      objectStatus.setDirection(direction);
      return this;
    }

    public ObjectStatusBuilder x(int x) {
      objectStatus.setX(x);
      return this;
    }

    public ObjectStatusBuilder y(int y) {
      objectStatus.setY(y);
      return this;
    }

    public ObjectStatusBuilder health(int health) {
      objectStatus.setHealth(health);
      return this;
    }

    public ObjectStatus build() {
      return objectStatus;
    }
  }
}
