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
import java.io.Serializable;

/**
 * Rectangle according by <code>Direction</code> .
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class DirectionRectangle implements PositionRectangle, Serializable {

  private static final long serialVersionUID = -6824568360919586510L;

  // location
  private final Rectangle location;

  // current direction.
  private Direction direction;

  // shape
  private final DirectionRectangularShape shape;

  // disable the object move to outbound
  private final boolean disableOutbound;

  // bounds
  private final FinalRectangle bounds;

  // outbound flag
  private boolean outbound;

  // snapshot
  private Rectangle snapshot;

  // previous position
  private Rectangle previousSnapshot;

  public DirectionRectangle(final int x, final int y, final Direction initialDirection,
      DirectionRectangularShape shape, FinalRectangle bounds) {
    this(x, y, initialDirection, shape, true, bounds);
  }

  public DirectionRectangle(final int x, final int y, final Direction initialDirection,
      DirectionRectangularShape shape, boolean disableOutbound, FinalRectangle bounds) {
    this.location = new Rectangle(x, y, shape.getWidth(initialDirection),
        shape.getHeight(initialDirection));
    this.direction = initialDirection;
    this.shape = shape;
    this.disableOutbound = disableOutbound;
    this.bounds = bounds;
    this.snapshot = updateSnapshot();
    this.previousSnapshot = snapshot;
  }

  /**
   * Get the <code>DirectionRectangularShape</code>
   *
   * @return <code>DirectionRectangularShape</code>
   */
  public DirectionRectangularShape getShape() {
    return shape;
  }

  /**
   * Get the snapshot on the moment.
   *
   * @return snapshot <code>Rectangle</code>
   */
  @Override
  public Rectangle getSnapshot() {
    return snapshot;
  }

  /**
   * weather the object is outbound or not.
   *
   * @return return true if it is outbound, otherwise return false.
   */
  public boolean isOutbound() {
    return outbound;
  }

  /**
   * Get the bounds
   *
   * @return bounds
   */
  @Override
  public FinalRectangle getBounds() {
    return bounds;
  }


  /**
   * update snapshot
   */
  private Rectangle updateSnapshot() {
    return new Rectangle(location.x, location.y, location.width, location.height);
  }

  /**
   * Get the current   <code>Direction</code>
   *
   * @return current <code>Direction</code>
   */
  public Direction getDirection() {
    return this.direction;
  }

  /**
   * Moves this <code>DirectionRectangle</code> by the specified <code>Direction</code>>.
   *
   * @param direction   <code>Direction</code>
   * @param increment2D the X or Y coordinate increment of the new location
   */
  public void moveByDirection(Direction direction, int increment2D) {
    changeDirection(direction);
    refresh(increment2D);
  }

  /**
   * back off
   */
  public void back() {
    location.x = previousSnapshot.x;
    location.y = previousSnapshot.y;
    location.width = previousSnapshot.width;
    location.height = previousSnapshot.height;
    snapshot = previousSnapshot;
  }

  /**
   * Change <code>Direction</code> to prepare moving
   *
   * @param direction <code>Direction</code>
   */
  public void changeDirection(Direction direction) {
    this.direction = direction;
  }

  /**
   * refresh this location of <code>DirectionRectangle</code> according by the direction.
   *
   * @param increment2D the X or Y coordinate increment of the new location
   */
  public void refresh(int increment2D) {
    if (increment2D < 0) {
      refresh(-increment2D);
      return;
    }
    switch (direction) {
      case UP:
        setLocation(direction, location.x, location.y - increment2D);
        break;
      case DOWN:
        setLocation(direction, location.x, location.y + increment2D);
        break;
      case LEFT:
        setLocation(direction, location.x - increment2D, location.y);
        break;
      case RIGHT:
        setLocation(direction, location.x + increment2D, location.y);
        break;
      default:
        break;
    }
  }

  /**
   * Moves this <code>DirectionRectangle</code> to the specified location.
   *
   * @param direction <code>Direction</code>
   * @param x         the X coordinate of the new location
   * @param y         the Y coordinate of the new location
   */
  protected void setLocation(Direction direction, int x, int y) {
    // bounds check
    if (x < bounds.x) {
      if (disableOutbound) {
        x = bounds.x;
      }
      outbound = true;
    }
    final int shapeWidth = shape.getWidth(direction);
    if ((x + shapeWidth) >= (bounds.x + bounds.width)) {
      if (disableOutbound) {
        x = (bounds.x + bounds.width) - shapeWidth;
      }
      outbound = true;
    }
    if (y < bounds.y) {
      if (disableOutbound) {
        y = bounds.y;
      }
      outbound = true;
    }
    final int shapeHeight = shape.getHeight(direction);
    if ((y + shapeHeight) >= (bounds.y + bounds.height)) {
      if (disableOutbound) {
        y = (bounds.y + bounds.height) - shapeHeight;
      }
      outbound = true;
    }

    // Moves this Rectangle to the specified location.
    this.location.setLocation(x, y);

    // update snapshot
    this.previousSnapshot = snapshot;
    this.snapshot = updateSnapshot();
  }

}
