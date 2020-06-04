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

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Shape according by <code>Direction</code> .
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class DirectionRectangularShape {

  private final Map<Direction, Integer> widthShape = new HashMap<>(8);
  private final Map<Direction, Integer> heightShape = new HashMap<>(8);

  public DirectionRectangularShape(int cubeSize) {
    Arrays.stream(Direction.values()).forEach(direction -> {
      putWidthShapeIfAbsent(direction, cubeSize);
    });
    Arrays.stream(Direction.values()).forEach(direction -> {
      putHeightShapeIfAbsent(direction, cubeSize);
    });
  }

  public DirectionRectangularShape(Map<Direction, BufferedImage> shape) {
    if (shape != null) {
      Arrays.stream(Direction.values()).forEach(direction -> {
        putWidthShapeIfAbsent(direction, shape.get(direction).getWidth());
        putHeightShapeIfAbsent(direction, shape.get(direction).getHeight());
      });
    }
  }

  /**
   * init width shape
   *
   * @param direction <code>Direction</code>
   * @param width     width
   */
  public void putWidthShapeIfAbsent(Direction direction, Integer width) {
    widthShape.putIfAbsent(direction, width);
  }

  /**
   * init height shape
   *
   * @param direction <code>Direction</code>
   * @param height    height
   */
  public void putHeightShapeIfAbsent(Direction direction, Integer height) {
    heightShape.putIfAbsent(direction, height);
  }

  /**
   * Get the width according by direction shape.
   *
   * @param direction <code>Direction</code>
   * @return width
   */
  public int getWidth(Direction direction) {
    return widthShape.getOrDefault(direction, -1);
  }

  /**
   * Get the height according by direction shape.
   *
   * @param direction <code>Direction</code>
   * @return height
   */
  public int getHeight(Direction direction) {
    return heightShape.getOrDefault(direction, -1);
  }

}
