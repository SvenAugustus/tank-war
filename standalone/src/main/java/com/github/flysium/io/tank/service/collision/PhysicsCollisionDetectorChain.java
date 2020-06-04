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

import com.github.flysium.io.tank.model.GameObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <code>PhysicsCollisionDetector</code> Chain.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class PhysicsCollisionDetectorChain implements PhysicsCollisionDetector {

  private final List<PhysicsCollisionDetector> detectors = new CopyOnWriteArrayList<>();

  public PhysicsCollisionDetectorChain() {
    register(new BulletTankPhysicsCollisionDetector());
    register(new TankTankPhysicsCollisionDetector());
  }

  public boolean register(PhysicsCollisionDetector physicsCollisionDetector) {
    if (detectors.contains(physicsCollisionDetector)) {
      return false;
    }
    return detectors.add(physicsCollisionDetector);
  }

  @Override
  public boolean detect(GameObject a, GameObject b) {
    return detectors.stream().anyMatch(detector -> detector.detect(a, b));
  }

}
