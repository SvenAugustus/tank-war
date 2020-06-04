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

import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.BulletAttributes;
import com.github.flysium.io.tank.model.DefaultBullet;
import com.github.flysium.io.tank.model.DefaultTank;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.DirectionRectangularShape;
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Group;
import com.github.flysium.io.tank.model.Tank;
import com.github.flysium.io.tank.model.TankAttributes;
import com.github.flysium.io.tank.service.collision.PhysicsCollisionDetectorChain;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Game Model with manage all <code>GameObject</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GameModel {

  private final FinalRectangle bounds;
  private final Tank mainTank;
  private final Map<String, GameObject> gameObjects = new ConcurrentHashMap<>();

  private final PhysicsCollisionDetectorChain physicsCollisionDetectorChain = new PhysicsCollisionDetectorChain();

  public GameModel(final FinalRectangle bounds) {
    this.bounds = bounds;

    // init main tank
    this.mainTank = createTank(Group.MAIN_GROUP, 50, 50);
    this.mainTank.arise();

    // init enemy tanks
    createTank(Group.ENEMY_GROUP, 150, 100).arise();
    createTank(Group.ENEMY_GROUP, 250, 100).arise();
    createTank(Group.ENEMY_GROUP, 350, 100).arise();
    createTank(Group.ENEMY_GROUP, 450, 100).arise();
  }

  /**
   * paint <code>Graphics</code>
   *
   * @param g Graphics
   */
  public void paint(Graphics g) {
    // detect and handle any physics collisions
    physicsCollisionDetect(gameObjects.values(), gameObjects.values());

    // paint
    gameObjects.forEach((id, gameObject) -> {
      if (gameObject instanceof Tank) {
        draw(g, (Tank) gameObject);
      }
      if (gameObject instanceof Bullet) {
        draw(g, (Bullet) gameObject);
      }
    });

    // messages.
    Color c = g.getColor();
    g.setColor(Color.BLACK);
    long tanksCount = gameObjects.values().stream().filter(o -> o instanceof Tank).count();
    long bulletsCount = gameObjects.values().stream().filter(o -> o instanceof Bullet).count();
    g.drawString("Tanks: " + tanksCount + ", Bullets: " + bulletsCount, 10, 40);
    // reset Graphics's color
    g.setColor(c);
  }

  private void draw(Graphics g, Tank tank) {
    if (!tank.isAlive()) {
      gameObjects.remove(tank.getId());
      return;
    }
    Color c = g.getColor();
    g.setColor(Group.MAIN_GROUP.equals(tank.getGroup()) ? Color.YELLOW : Color.GRAY);
    // draw the tank
    Rectangle location = tank.getLocation();
    g.fillRect(location.x, location.y, location.width, location.height);
    // reset Graphics's color
    g.setColor(c);
  }

  private void draw(Graphics g, Bullet bullet) {
    if (!bullet.isAlive()) {
      gameObjects.remove(bullet.getId());
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

  private void draw(Graphics g, Bullet bullet) {
    if (!bullet.isAlive()) {
      gameObjects.remove(bullet.getId());
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

  /**
   * Get the main <code>Tank</code>
   *
   * @return the main Tank.
   */
  public Tank getMainTank() {
    return mainTank;
  }

  /**
   * detect and handle any physics collisions
   */
  private void physicsCollisionDetect(Collection<? extends GameObject> as,
      Collection<? extends GameObject> bs) {
    for (GameObject a : as) {
      for (GameObject b : bs) {
        if (a != b) {
          if (physicsCollisionDetectorChain.detect(a, b)) {
            break;
          }
        }
      }
    }
  }

  /**
   * create a tank
   */
  private Tank createTank(Group group, final int x, final int y) {
    return createTank(group, x, y,
        TankAttributes.builder()
            .initialDirection(Direction.RIGHT)
            .shape(new DirectionRectangularShape(50))
            .bounds(this.bounds)
            .movingSpeed(15).build());
  }

  /**
   * create a tank
   */
  private Tank createTank(Group group, final int x, final int y, TankAttributes attributes) {
    Tank tank = new DefaultTank(group, x, y, attributes, this);
    gameObjects.putIfAbsent(tank.getId(), tank);
    return tank;
  }

  /**
   * create a bullet and make it fly
   */
  public Bullet createBullet(Tank tank) {
    return createBullet(tank, BulletAttributes.builder()
        .shape(new DirectionRectangularShape(20))
        .bulletFlyingSpeed(20)
        .build());
  }

  /**
   * create a bullet and make it fly
   */
  public Bullet createBullet(Tank tank, BulletAttributes attributes) {
    Bullet bullet = new DefaultBullet(tank, attributes);
    gameObjects.putIfAbsent(bullet.getId(), bullet);
    return bullet;
  }

}
