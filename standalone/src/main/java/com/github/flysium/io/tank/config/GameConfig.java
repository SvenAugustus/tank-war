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
 * The configuration of Game.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class GameConfig {

  // game initialization

  private final int mainTankMovingSpeed;

  private final int mainTankBulletFlyingSpeed;

  private final int initEnemyTanksCount;

  private final int enemyTankMovingSpeed;

  private final int enemyTankBulletFlyingSpeed;

  private GameConfig() {
    mainTankMovingSpeed = PropertiesUtils
        .getIntegerProperty("game.mainTank.movingSpeed", v -> v > 0, 5);
    mainTankBulletFlyingSpeed = PropertiesUtils
        .getIntegerProperty("game.mainTank.bulletFlyingSpeed", v -> v > 0, 10);
    initEnemyTanksCount = PropertiesUtils
        .getIntegerProperty("game.initEnemyTanksCount", v -> v >= 0, 4);
    enemyTankMovingSpeed = PropertiesUtils
        .getIntegerProperty("game.enemyTank.movingSpeed", v -> v > 0, 5);
    enemyTankBulletFlyingSpeed = PropertiesUtils
        .getIntegerProperty("game.enemyTank.bulletFlyingSpeed", v -> v > 0, 10);
  }

  public static GameConfig getSingleton() {
    return Holder.INSTANCE;
  }

  public int getInitEnemyTanksCount() {
    return initEnemyTanksCount;
  }

  public int getMainTankMovingSpeed() {
    return mainTankMovingSpeed;
  }

  public int getMainTankBulletFlyingSpeed() {
    return mainTankBulletFlyingSpeed;
  }

  public int getEnemyTankMovingSpeed() {
    return enemyTankMovingSpeed;
  }

  public int getEnemyTankBulletFlyingSpeed() {
    return enemyTankBulletFlyingSpeed;
  }

  private static class Holder {

    // singleton instance.
    static final GameConfig INSTANCE = new GameConfig();
  }

}
