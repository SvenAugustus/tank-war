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
import com.github.flysium.io.tank.service.automatic.AutomaticStrategy;
import com.github.flysium.io.tank.service.automatic.RandomAutomaticStrategy;

/**
 * The configuration of automatic.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class AutomaticConfig {

  private final String enemyTankStrategy;

  // random
  private final int enemyTankRandomFireRatio;
  private final int enemyTankRandomChangeDirectionRatio;
  private final int enemyTankRandomMoveOnRatio;

  private AutomaticConfig() {
    enemyTankStrategy = PropertiesUtils
        .getProperty("automatic.enemyTank.strategy", v -> {
          try {
            Class<?> clazz = Class.forName(v);
            return AutomaticStrategy.class.isAssignableFrom(clazz);
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
          return false;
        }, RandomAutomaticStrategy.class.getCanonicalName());

    // random
    int enemyTankRandomFireRatio = PropertiesUtils
        .getIntegerProperty("automatic.enemyTank.random.fire.ratio", v -> v > 0, 5);
    int enemyTankRandomChangeDirectionRatio = PropertiesUtils
        .getIntegerProperty("automatic.enemyTank.random.changeDirection.ratio", v -> v > 0, 5);
    int enemyTankRandomMoveOnRatio = PropertiesUtils
        .getIntegerProperty("automatic.enemyTank.random.moveOn.ratio", v -> v > 0, 40);
    int enemyTankRandomIdleRatio = PropertiesUtils
        .getIntegerProperty("automatic.enemyTank.random.idle.ratio", v -> v > 0, 40);
    int enemyTankRandomTotalRatio = 0;
    enemyTankRandomTotalRatio += enemyTankRandomFireRatio;
    enemyTankRandomTotalRatio += enemyTankRandomChangeDirectionRatio;
    enemyTankRandomTotalRatio += enemyTankRandomMoveOnRatio;
    enemyTankRandomTotalRatio += enemyTankRandomIdleRatio;

    this.enemyTankRandomFireRatio = enemyTankRandomFireRatio * 100
        / enemyTankRandomTotalRatio;
    this.enemyTankRandomChangeDirectionRatio = enemyTankRandomChangeDirectionRatio * 100
        / enemyTankRandomTotalRatio;
    this.enemyTankRandomMoveOnRatio = enemyTankRandomMoveOnRatio * 100
        / enemyTankRandomTotalRatio;
  }

  public static AutomaticConfig getSingleton() {
    return Holder.INSTANCE;
  }

  public String getEnemyTankStrategy() {
    return enemyTankStrategy;
  }

  public int getEnemyTankRandomFireRatio() {
    return enemyTankRandomFireRatio;
  }

  public int getEnemyTankRandomChangeDirectionRatio() {
    return enemyTankRandomChangeDirectionRatio;
  }

  public int getEnemyTankRandomMoveOnRatio() {
    return enemyTankRandomMoveOnRatio;
  }

  private static class Holder {

    // singleton instance.
    static final AutomaticConfig INSTANCE = new AutomaticConfig();
  }

}
