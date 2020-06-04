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
 * Movable <code>GameObject</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public interface Movable {

  /**
   * Get the moving speed.
   *
   * @return return zero if can not move, otherwise return the moving speed.
   */
  int getSpeed();

  /**
   * Get the current   <code>Direction</code>
   *
   * @return current <code>Direction</code>
   */
  Direction getDirection();

  /**
   * back off
   */
  void back();

  /**
   * Change <code>Direction</code> to prepare moving
   *
   * @param direction <code>Direction</code>
   */
  void changeDirection(Direction direction);

  /**
   * move this location of <code>DirectionRectangle</code> according by the current direction.
   */
  void moveOn();

}
