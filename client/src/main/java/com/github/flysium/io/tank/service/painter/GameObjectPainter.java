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

package com.github.flysium.io.tank.service.painter;

import com.github.flysium.io.tank.model.GameObject;
import java.awt.Color;
import java.awt.Graphics;

/**
 * factory which is used to paint game objects.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public interface GameObjectPainter {

  /**
   * paint text message
   *
   * @param g       <code>Graphics</code>
   * @param color   <code>Color</code>
   * @param message a text message
   */
  void paint(Graphics g, Color color, String message);

  /**
   * paint GameObject
   *
   * @param g          <code>Graphics</code>
   * @param gameObject a <code>GameObject</code>
   */
  void paint(Graphics g, GameObject gameObject);

  /**
   * new <code>GameObjectPainter</code>.
   *
   * @param clazzName class name of <code>GameObjectPainter</code>.
   * @return <code>GameObjectPainter</code> instance.
   */
  @SuppressWarnings("unchecked")
  static GameObjectPainter newGameObjectPainter(String clazzName) {
    try {
      Class<GameObjectPainter> clazz = (Class<GameObjectPainter>) Class.forName(clazzName);
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return new GraphicalGameObjectPainter();
  }
}
