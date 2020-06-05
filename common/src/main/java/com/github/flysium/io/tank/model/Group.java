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

package com.github.flysium.io.tank.model;

import java.util.Objects;

/**
 * Group, same group is allies
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class Group implements java.io.Serializable {

  private static final long serialVersionUID = 8977251046757661244L;

  // Group Code
  private final String groupCode;

  public Group(String groupCode) {
    this.groupCode = groupCode;
  }

  public String getGroupCode() {
    return groupCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Group group = (Group) o;
    return Objects.equals(groupCode, group.groupCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupCode);
  }

  /**
   * System Group
   */
  public static final Group SYSTEM_GROUP = new Group("system");
  /**
   * Main Group
   */
  public static final Group MY_GROUP = new Group("my");
  /**
   * Other Group
   */
  public static final Group OTHER_GROUP = new Group("other");

}
