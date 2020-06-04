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

package com.github.flysium.io.tank.config.utils;

import java.util.Properties;
import java.util.function.Function;

/**
 * Properties Utils.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class PropertiesUtils {

  private static final Properties PROPERTIES = new Properties();

  static {
    try {
      PROPERTIES.load(PropertiesUtils.class.getClassLoader()
          .getResourceAsStream("config.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("--------->load properties: " + PROPERTIES);
  }

  public static String getProperty(String key) {
    return PROPERTIES.getProperty(key);
  }

  public static String getProperty(String key, String defaultValue) {
    return PROPERTIES.getProperty(key, defaultValue);
  }

  public static String getProperty(String key, Function<String, Boolean> valueCheck,
      String defaultValue) {
    try {
      String val = PROPERTIES.getProperty(key, defaultValue);
      if (valueCheck != null && !valueCheck.apply(val)) {
        return defaultValue;
      }
      return val;
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static boolean getBooleanProperty(String key, boolean defaultValue) {
    String value = getProperty(key);
    if ("".equals(value)) {
      return defaultValue;
    }
    return "true".equalsIgnoreCase(value);
  }

  public static int getIntegerProperty(String key, int defaultValue) {
    return getIntegerProperty(key, null, defaultValue);
  }

  public static int getIntegerProperty(String key, Function<Integer, Boolean> valueCheck,
      int defaultValue) {
    String value = getProperty(key);
    if ("".equals(value)) {
      return defaultValue;
    }
    try {
      int val = Integer.parseInt(value);
      if (valueCheck != null && !valueCheck.apply(val)) {
        return defaultValue;
      }
      return val;
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

}
