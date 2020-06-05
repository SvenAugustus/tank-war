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

package com.github.flysium.io.tank.config;

import com.github.flysium.io.tank.config.utils.PropertiesUtils;

/**
 * The configuration of Server.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class ServerConfig {

  private final int port;

  // server initialization
  private final int refreshMillis;

  private final int tankInitialHealth;
  private final int tankMovingSpeed;

  private final int tankBulletFlyingSpeed;
  private final int tankBulletDamage;

  private static class Holder {

    // singleton instance.
    static final ServerConfig INSTANCE = new ServerConfig();
  }

  public static ServerConfig getSingleton() {
    return Holder.INSTANCE;
  }

  private ServerConfig() {
    port = PropertiesUtils.getIntegerProperty("server.port", 80);
    // game initialization
    refreshMillis = PropertiesUtils.getIntegerProperty("server.refreshMillis", v -> v >= 25, 50);
    tankInitialHealth = PropertiesUtils
        .getIntegerProperty("server.tank.initialHealth", v -> v > 0, 100);
    tankMovingSpeed = PropertiesUtils
        .getIntegerProperty("server.tank.movingSpeed", v -> v > 0, 5);
    tankBulletFlyingSpeed = PropertiesUtils
        .getIntegerProperty("server.tank.bulletFlyingSpeed", v -> v > 0, 10);
    tankBulletDamage = PropertiesUtils
        .getIntegerProperty("server.tank.bulletDamage", v -> v > 0, 10);
  }

  public int getPort() {
    return port;
  }

  public int getRefreshMillis() {
    return refreshMillis;
  }

  public int getTankInitialHealth() {
    return tankInitialHealth;
  }

  public int getTankMovingSpeed() {
    return tankMovingSpeed;
  }

  public int getTankBulletFlyingSpeed() {
    return tankBulletFlyingSpeed;
  }

  public int getTankBulletDamage() {
    return tankBulletDamage;
  }

}
