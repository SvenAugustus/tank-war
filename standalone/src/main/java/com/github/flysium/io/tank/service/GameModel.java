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

package com.github.flysium.io.tank.service;

import com.github.flysium.io.tank.model.DefaultTank;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.DirectionRectangularShape;
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.model.Tank;
import com.github.flysium.io.tank.model.TankAttributes;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Game Model with manage all <code>GameObject</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GameModel {

  private final FinalRectangle bounds;
  private final Tank mainTank;

  public GameModel(final FinalRectangle bounds) {
    this.bounds = bounds;
    this.mainTank = new DefaultTank(50, 50,
        TankAttributes.builder()
            .initialDirection(Direction.RIGHT)
            .shape(new DirectionRectangularShape(50))
            .bounds(this.bounds)
            .movingSpeed(15).build());
    this.mainTank.arise();
  }

  /**
   * paint <code>Graphics</code>
   *
   * @param g Graphics
   */
  public void paint(Graphics g) {
    drawTank(g, this.mainTank);
  }

  private void drawTank(Graphics g, Tank tank) {
    if (!tank.isAlive()) {
      return;
    }
    Color c = g.getColor();
    g.setColor(Color.YELLOW);
    // draw the tank
    Rectangle location = tank.getLocation();
    g.fillRect(location.x, location.y, location.width, location.height);
    // reset Graphics's color
    g.setColor(c);
  }

  /**
   * Get the main <code>Tank</code>
   *
   * @return the main Tank.
   */
  public Tank getMainTank() {
    return mainTank;
  }

}
