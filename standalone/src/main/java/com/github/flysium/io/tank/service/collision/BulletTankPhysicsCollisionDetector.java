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

package com.github.flysium.io.tank.service.collision;

import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Tank;

/**
 * <code>PhysicsCollisionDetector</code> between <code>Bullet</code> and <code>Tank</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class BulletTankPhysicsCollisionDetector implements PhysicsCollisionDetector {

  @Override
  public boolean detect(GameObject a, GameObject b) {
    if (a == b) {
      return false;
    }
    if (a instanceof Bullet && b instanceof Tank) {
      Bullet bullet = (Bullet) a;
      Tank tank = (Tank) b;
      if (bullet.isAlive() && tank.isAlive()
          // not my bullet
          && !tank.equals(bullet.getOwner())
          // not my allies's bullet
          && !tank.getGroup().equals(bullet.getOwner().getGroup())
          // physics collision occurs when they intersects is true
          && tank.getLocation().intersects(bullet.getLocation())
      ) {
        // handle physics collision
//        tank.die();
        tank.damage(bullet.getDamageValue());
        bullet.die();
      }
    } else if (a instanceof Tank && b instanceof Bullet) {
      return detect(b, a);
    }
    return false;
  }

}
