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
import com.github.flysium.io.tank.service.painter.GameObjectPainter;
import com.github.flysium.io.tank.service.painter.GraphicalGameObjectPainter;

/**
 * The configuration of Client.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class ClientConfig {

  private final String host;
  private final int port;

  // game initialization
  private final String painter;
  private final int refreshMillis;

  private static class Holder {

    // singleton instance.
    static final ClientConfig INSTANCE = new ClientConfig();
  }

  public static ClientConfig getSingleton() {
    return Holder.INSTANCE;
  }

  private ClientConfig() {
    host = PropertiesUtils.getProperty("server.host", "127.0.0.1");
    port = PropertiesUtils.getIntegerProperty("server.port", 80);
    // game initialization
    painter = PropertiesUtils.getProperty("client.painter", v -> {
      try {
        Class<?> clazz = Class.forName(v);
        return GameObjectPainter.class.isAssignableFrom(clazz);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      return false;
    }, GraphicalGameObjectPainter.class.getCanonicalName());
    refreshMillis = PropertiesUtils.getIntegerProperty("client.refreshMillis", v -> v >= 25, 50);
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getPainter() {
    return painter;
  }

  public int getRefreshMillis() {
    return refreshMillis;
  }

}
