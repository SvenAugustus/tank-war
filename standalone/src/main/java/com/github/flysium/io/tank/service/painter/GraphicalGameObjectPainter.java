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

import com.github.flysium.io.tank.config.ResourceManager;
import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.DirectionRectangularShape;
import com.github.flysium.io.tank.model.Group;
import com.github.flysium.io.tank.model.Tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Graphical <code>GameObjectPainter</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GraphicalGameObjectPainter extends SimpleGameObjectPainter
    implements GameObjectPainter {

  private final ResourceManager resourceManager = ResourceManager.getSingleton();

  @Override
  public void paint(Graphics g, Tank tank) {
    if (!tank.isAlive()) {
      return;
    }
    BufferedImage image = Group.MAIN_GROUP.equals(tank.getGroup()) ?
        resourceManager.getMainTankImage().get(tank.getDirection()) :
        resourceManager.getEnemyTankImage().get(tank.getDirection());
    if (image == null) {
      super.paint(g, tank);
      return;
    }
    Color c = g.getColor();
    // draw the tank
    Rectangle location = tank.getLocation();
    g.drawImage(image, location.x, location.y, null);

    g.setColor(Color.gray);
    g.drawString("" + tank.getHealthValue(), location.x + location.width * 3 / 10, location.y);

    // reset Graphics's color
    g.setColor(c);
  }

  @Override
  public void paint(Graphics g, Bullet bullet) {
    if (!bullet.isAlive()) {
      return;
    }
    BufferedImage image = resourceManager.getBulletImage().get(bullet.getDirection());
    if (image == null) {
      super.paint(g, bullet);
      return;
    }
    bullet.fly();

    Color c = g.getColor();
    g.setColor(Color.RED);
    // draw the tank
    Rectangle location = bullet.getLocation();
    g.drawImage(image, location.x, location.y, null);
    // reset Graphics's color
    g.setColor(c);
  }

  @Override
  public DirectionRectangularShape getTankShape(Group group) {
    return new DirectionRectangularShape(Group.MAIN_GROUP.equals(group) ?
        resourceManager.getMainTankImage()
        : resourceManager.getEnemyTankImage());
  }

  @Override
  public DirectionRectangularShape getBulletShape(Group group) {
    return new DirectionRectangularShape(resourceManager.getBulletImage());
  }

}
