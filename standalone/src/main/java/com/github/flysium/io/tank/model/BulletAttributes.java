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
 * Bullet Attributes
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class BulletAttributes {

  private Direction initialDirection;
  private DirectionRectangularShape shape;
  private Integer bulletFlyingSpeed;
  private Integer damageValue;

  /**
   * load default <code>BulletAttributes</code>
   *
   * @param attributes <code>BulletAttributes</code>
   * @return fresh this <code>BulletAttributes</code>
   */
  public BulletAttributes defaultOf(BulletAttributes attributes) {
    if (attributes == null) {
      return this;
    }
    if (initialDirection == null) {
      initialDirection = attributes.getInitialDirection();
    }
    if (shape == null) {
      shape = attributes.getShape();
    }
    if (bulletFlyingSpeed == null) {
      bulletFlyingSpeed = attributes.getBulletFlyingSpeed();
    }
    if (damageValue == null) {
      damageValue = attributes.getDamageValue();
    }
    return this;
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

  public Integer getBulletFlyingSpeed() {
    return bulletFlyingSpeed;
  }

  public void setBulletFlyingSpeed(Integer bulletFlyingSpeed) {
    this.bulletFlyingSpeed = bulletFlyingSpeed;
  }

  public Integer getDamageValue() {
    return damageValue;
  }

  public void setDamageValue(Integer damageValue) {
    this.damageValue = damageValue;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private final BulletAttributes bulletAttributes;

    private Builder() {
      bulletAttributes = new BulletAttributes();
    }

    public Builder initialDirection(Direction initialDirection) {
      bulletAttributes.setInitialDirection(initialDirection);
      return this;
    }

    public Builder shape(DirectionRectangularShape shape) {
      bulletAttributes.setShape(shape);
      return this;
    }

    public Builder bulletFlyingSpeed(Integer bulletFlyingSpeed) {
      bulletAttributes.setBulletFlyingSpeed(bulletFlyingSpeed);
      return this;
    }

    public Builder damageValue(Integer damageValue) {
      bulletAttributes.setDamageValue(damageValue);
      return this;
    }

    public BulletAttributes build() {
      return bulletAttributes;
    }
  }
}
