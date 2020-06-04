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
import com.github.flysium.io.tank.service.fire.DefaultFireStrategy;
import com.github.flysium.io.tank.service.fire.FireStrategy;
import java.util.function.Function;

/**
 * The configuration of Game.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class GameConfig {

  // game initialization
  private final int mainTankInitialHealth;
  private final int mainTankMovingSpeed;

  private final int mainTankBulletFlyingSpeed;
  private final int mainTankBulletDamage;

  private final int initEnemyTanksCount;

  private final int enemyTankInitialHealth;
  private final int enemyTankMovingSpeed;

  private final int enemyTankBulletFlyingSpeed;
  private final int enemyTankBulletDamage;

  // fire system
  private final String mainTankFireStrategy;
  private final String enemyTankFireStrategy;

  private static final Function<String, Boolean> CHECK_FIRE_SYSTEM_FUNCTION = v -> {
    try {
      Class<?> clazz = Class.forName(v);
      return FireStrategy.class.isAssignableFrom(clazz);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return false;
  };

  private GameConfig() {
    // game initialization
    mainTankInitialHealth = PropertiesUtils
        .getIntegerProperty("game.mainTank.initialHealth", v -> v > 0, 100);
    mainTankMovingSpeed = PropertiesUtils
        .getIntegerProperty("game.mainTank.movingSpeed", v -> v > 0, 5);
    mainTankBulletFlyingSpeed = PropertiesUtils
        .getIntegerProperty("game.mainTank.bulletFlyingSpeed", v -> v > 0, 10);
    mainTankBulletDamage = PropertiesUtils
        .getIntegerProperty("game.mainTank.bulletDamage", v -> v > 0, 10);

    initEnemyTanksCount = PropertiesUtils
        .getIntegerProperty("game.initEnemyTanksCount", v -> v >= 0, 4);
    enemyTankInitialHealth = PropertiesUtils
        .getIntegerProperty("game.enemyTank.initialHealth", v -> v > 0, 100);
    enemyTankMovingSpeed = PropertiesUtils
        .getIntegerProperty("game.enemyTank.movingSpeed", v -> v > 0, 5);
    enemyTankBulletFlyingSpeed = PropertiesUtils
        .getIntegerProperty("game.enemyTank.bulletFlyingSpeed", v -> v > 0, 10);
    enemyTankBulletDamage = PropertiesUtils
        .getIntegerProperty("game.enemyTank.bulletDamage", v -> v > 0, 10);

    // fire system
    mainTankFireStrategy = PropertiesUtils.getProperty("game.mainTank.fireStrategy"
        , CHECK_FIRE_SYSTEM_FUNCTION, DefaultFireStrategy.class.getCanonicalName());
    enemyTankFireStrategy = PropertiesUtils.getProperty("game.enemyTank.fireStrategy"
        , CHECK_FIRE_SYSTEM_FUNCTION, DefaultFireStrategy.class.getCanonicalName());
  }

  public static GameConfig getSingleton() {
    return Holder.INSTANCE;
  }

  public int getMainTankInitialHealth() {
    return mainTankInitialHealth;
  }

  public int getMainTankMovingSpeed() {
    return mainTankMovingSpeed;
  }

  public int getMainTankBulletFlyingSpeed() {
    return mainTankBulletFlyingSpeed;
  }

  public int getMainTankBulletDamage() {
    return mainTankBulletDamage;
  }

  public int getInitEnemyTanksCount() {
    return initEnemyTanksCount;
  }

  public int getEnemyTankInitialHealth() {
    return enemyTankInitialHealth;
  }

  public int getEnemyTankMovingSpeed() {
    return enemyTankMovingSpeed;
  }

  public int getEnemyTankBulletFlyingSpeed() {
    return enemyTankBulletFlyingSpeed;
  }

  public int getEnemyTankBulletDamage() {
    return enemyTankBulletDamage;
  }

  public String getMainTankFireStrategy() {
    return mainTankFireStrategy;
  }

  public String getEnemyTankFireStrategy() {
    return enemyTankFireStrategy;
  }

  private static class Holder {

    // singleton instance.
    static final GameConfig INSTANCE = new GameConfig();
  }

}
