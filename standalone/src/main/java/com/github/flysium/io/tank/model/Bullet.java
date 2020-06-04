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

import java.awt.Rectangle;

/**
 * abstract Bullet
 *
 * @author Sven Augustus
 * @version 1.0
 */
public abstract class Bullet extends BaseFlyable implements Flyable, Lifecycle {

  private final Tank owner;

  public Bullet(final Tank owner, BulletAttributes attributes) {
    super(createDirectionRectangle(owner, attributes.getInitialDirection(), attributes.getShape()),
        attributes.getBulletFlyingSpeed());
    this.owner = owner;
  }

  /**
   * Get the owner
   *
   * @return tank
   */
  public Tank getOwner() {
    return owner;
  }

  /**
   * create a <code>DirectionRectangle</code> according by tank.
   *
   * @param owner tank
   * @param shape <code>DirectionRectangularShape</code>
   * @return <code>DirectionRectangle</code>
   */
  public static DirectionRectangle createDirectionRectangle(final Tank owner,
      Direction initialDirection,
      DirectionRectangularShape shape) {
    if (initialDirection == null) {
      initialDirection = owner.getDirection();
    }
    final Rectangle tankLocation = owner.getLocation();
    int x = tankLocation.x;
    int y = tankLocation.y;
    final int w = shape.getWidth(initialDirection);
    final int h = shape.getHeight(initialDirection);

    switch (initialDirection) {
      case UP:
        x = tankLocation.x + (tankLocation.width / 2) - (w / 2);
        y = tankLocation.y - (h / 2);
        break;
      case DOWN:
        x = tankLocation.x + (tankLocation.width / 2) - (w / 2);
        y = tankLocation.y + tankLocation.height - (h / 2);
        break;
      case LEFT:
        x = tankLocation.x - (w / 2);
        y = tankLocation.y + (tankLocation.height / 2) - (h / 2);
        break;
      case RIGHT:
        x = tankLocation.x + tankLocation.width - (w / 2);
        y = tankLocation.y + (tankLocation.height / 2) - (h / 2);
        break;
      default:
        break;
    }

    // enable move to outbound
    return new DirectionRectangle(x, y, initialDirection, shape, false, owner.getBounds());
  }

  @Override
  public boolean isAlive() {
    return super.isAlive() && !this.getDirectionRectangle().isOutbound();
  }

}
