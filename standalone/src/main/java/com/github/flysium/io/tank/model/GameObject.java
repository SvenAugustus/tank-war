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
import java.util.Objects;
import java.util.UUID;

/**
 * 2D Game Object
 *
 * @author Sven Augustus
 * @version 1.0
 */
public abstract class GameObject implements Lifecycle {

  // ID
  protected final String id = UUID.randomUUID().toString();

  // same group is allies
  private final Group group;

  // location
  protected final PositionRectangle location;

  // alive flag
  protected boolean alive = false;

  public GameObject(Group group, PositionRectangle location) {
    this.group = group;
    this.location = location;
  }

  /**
   * Get it's ID
   *
   * @return ID
   */
  public String getId() {
    return id;
  }

  /**
   * Get what Group it belongs to.
   *
   * @return <code>Group</code>
   */
  public Group getGroup() {
    return group;
  }

  /**
   * Get it's location snapshot on the moment.
   *
   * @return <code>Rectangle</code>
   */
  public Rectangle getLocation() {
    return location.getSnapshot();
  }

  /**
   * Get the bounds
   *
   * @return bounds
   */
  public FinalRectangle getBounds() {
    return location.getBounds();
  }

  @Override
  public void arise() {
    alive = true;
  }

  @Override
  public void die() {
    alive = false;
  }

  @Override
  public boolean isAlive() {
    return alive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameObject that = (GameObject) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
