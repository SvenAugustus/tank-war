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
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.view.shaper.GameObjectShaper;
import com.github.flysium.io.tank.view.shaper.GraphicalGameObjectShaper;
import java.awt.Dimension;

/**
 * The configuration of Game.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class GameConfig {

  private final Dimension dimension;
  private final FinalRectangle finalRectangle;

  // game initialization
  private final String shaper;

  private static class Holder {

    // singleton instance.
    static final GameConfig INSTANCE = new GameConfig();
  }

  public static GameConfig getSingleton() {
    return Holder.INSTANCE;
  }

  private GameConfig() {
    int windowWidth = PropertiesUtils.getIntegerProperty("window.width", v -> v >= 800, 800);
    int windowHeight = PropertiesUtils.getIntegerProperty("window.height", v -> v >= 600, 600);
    this.dimension = new Dimension(windowWidth, windowHeight);
    this.finalRectangle = new FinalRectangle(2, 25, windowWidth - 4, windowHeight - 29);

    this.shaper = PropertiesUtils.getProperty("game.shaper", v -> {
      try {
        Class<?> clazz = Class.forName(v);
        return GameObjectShaper.class.isAssignableFrom(clazz);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      return false;
    }, GraphicalGameObjectShaper.class.getCanonicalName());

  }

  public Dimension getDimension() {
    return dimension;
  }

  public FinalRectangle getFinalRectangle() {
    return finalRectangle;
  }

  public String getShaper() {
    return shaper;
  }
}
