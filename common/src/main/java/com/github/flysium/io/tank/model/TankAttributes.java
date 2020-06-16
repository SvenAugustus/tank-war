/*
 * Copyright (c) 2020 SvenAugustus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.flysium.io.tank.model;

/**
 * Tank Attributes
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class TankAttributes {

  private String ownerUsername;
  private String ownerUserId;
  private int initialHealth;
  private Direction initialDirection;
  private DirectionRectangularShape shape;
  private FinalRectangle bounds;
  private int movingSpeed;

  public int getInitialHealth() {
    return initialHealth;
  }

  public void setInitialHealth(int initialHealth) {
    this.initialHealth = initialHealth;
  }

  public Direction getInitialDirection() {
    return initialDirection;
  }

  public void setInitialDirection(Direction initialDirection) {
    this.initialDirection = initialDirection;
  }

  public DirectionRectangularShape getShape() {
    return shape;
  }

  public void setShape(DirectionRectangularShape shape) {
    this.shape = shape;
  }

  public FinalRectangle getBounds() {
    return bounds;
  }

  public void setBounds(FinalRectangle bounds) {
    this.bounds = bounds;
  }

  public int getMovingSpeed() {
    return movingSpeed;
  }

  public void setMovingSpeed(int movingSpeed) {
    this.movingSpeed = movingSpeed;
  }

  public String getOwnerUsername() {
    return ownerUsername;
  }

  public void setOwnerUsername(String ownerUsername) {
    this.ownerUsername = ownerUsername;
  }

  public String getOwnerUserId() {
    return ownerUserId;
  }

  public void setOwnerUserId(String ownerUserId) {
    this.ownerUserId = ownerUserId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private final TankAttributes tankAttributes;

    private Builder() {
      tankAttributes = new TankAttributes();
    }

    public Builder ownerUsername(String ownerUsername) {
      tankAttributes.setOwnerUsername(ownerUsername);
      return this;
    }

    public Builder ownerUserId(String ownerUserId) {
      tankAttributes.setOwnerUserId(ownerUserId);
      return this;
    }

    public Builder initialHealth(int initialHealth) {
      tankAttributes.setInitialHealth(initialHealth);
      return this;
    }

    public Builder initialDirection(Direction initialDirection) {
      tankAttributes.setInitialDirection(initialDirection);
      return this;
    }

    public Builder shape(DirectionRectangularShape shape) {
      tankAttributes.setShape(shape);
      return this;
    }

    public Builder bounds(FinalRectangle bounds) {
      tankAttributes.setBounds(bounds);
      return this;
    }

    public Builder movingSpeed(int movingSpeed) {
      tankAttributes.setMovingSpeed(movingSpeed);
      return this;
    }

    public TankAttributes build() {
      return tankAttributes;
    }
  }
}
