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
 * The configuration of window.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class WindowConfig {

  private final int windowWidth;

  private final int windowHeight;

  private final int refreshMillis;

  private final int automaticMillis;

  private WindowConfig() {
    windowWidth = PropertiesUtils.getIntegerProperty("window.width", v -> v >= 800, 800);
    windowHeight = PropertiesUtils.getIntegerProperty("window.height", v -> v >= 600, 600);
    refreshMillis = PropertiesUtils.getIntegerProperty("window.refreshMillis", v -> v >= 25, 50);
    automaticMillis = PropertiesUtils.getIntegerProperty("window.automaticMillis", v -> v >= 25,
        100);
  }

  public static WindowConfig getSingleton() {
    return Holder.INSTANCE;
  }

  public int getWindowWidth() {
    return windowWidth;
  }

  public int getWindowHeight() {
    return windowHeight;
  }

  public int getRefreshMillis() {
    return refreshMillis;
  }

  public int getAutomaticMillis() {
    return automaticMillis;
  }

  private static class Holder {

    // singleton instance.
    static final WindowConfig INSTANCE = new WindowConfig();
  }

}
