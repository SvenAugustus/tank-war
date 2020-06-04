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

import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.DirectionRectangularShape;
import com.github.flysium.io.tank.model.Explode;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Group;
import com.github.flysium.io.tank.model.Tank;
import com.github.flysium.io.tank.model.Wall;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Simple <code>GameObjectPainter</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class SimpleGameObjectPainter implements GameObjectPainter {

  @Override
  public void paint(Graphics g, Color color, String message) {
    Color c = g.getColor();
    g.setColor(color);
    g.drawString(message, 10, 40);
    // reset Graphics's color
    g.setColor(c);
  }

  @Override
  public void paint(Graphics g, GameObject gameObject) {
    if (gameObject instanceof Tank) {
      paint(g, (Tank) gameObject);
    }
    if (gameObject instanceof Explode) {
      paint(g, (Explode) gameObject);
    }
    if (gameObject instanceof Bullet) {
      paint(g, (Bullet) gameObject);
    }
    if (gameObject instanceof Wall) {
      paint(g, (Wall) gameObject);
    }
  }

  protected void paint(Graphics g, Tank tank) {
    if (!tank.isAlive()) {
      return;
    }
    Color c = g.getColor();
    g.setColor(Group.MAIN_GROUP.equals(tank.getGroup()) ? Color.YELLOW : Color.GRAY);
    // draw the tank
    Rectangle location = tank.getLocation();
    g.fillRect(location.x, location.y, location.width, location.height);

    g.setColor(Color.GREEN);
    g.drawString("" + tank.getHealthValue(), location.x + location.width * 4 / 10,
        location.y + location.height / 2);

    // reset Graphics's color
    g.setColor(c);
  }

  protected void paint(Graphics g, Explode explode) {
    // TODO
  }

  protected void paint(Graphics g, Bullet bullet) {
    if (!bullet.isAlive()) {
      return;
    }
    bullet.fly();

    Color c = g.getColor();
    g.setColor(Color.RED);
    // draw the tank
    Rectangle location = bullet.getLocation();
    g.fillOval(location.x, location.y, location.width, location.height);
    // reset Graphics's color
    g.setColor(c);
  }

  protected void paint(Graphics g, Wall wall) {
    if (!wall.isAlive()) {
      return;
    }
    Color c = g.getColor();
    g.setColor(Color.DARK_GRAY);
    // draw the tank
    Rectangle location = wall.getLocation();
    g.fillRect(location.x, location.y, location.width, location.height);
    // reset Graphics's color
    g.setColor(c);
  }

  @Override
  public DirectionRectangularShape getTankShape(Group group) {
    return new DirectionRectangularShape(50);
  }

  @Override
  public DirectionRectangularShape getBulletShape(Group group) {
    return new DirectionRectangularShape(20);
  }

}
