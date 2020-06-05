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

import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.view.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Resource Manager.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class ResourceManager {

  private final Map<Direction, BufferedImage> mainTankImage;
  private final Map<Direction, BufferedImage> otherTankImage;
  private final Map<Direction, BufferedImage> bulletImage;
  private final List<BufferedImage> explodeImage = new ArrayList<>(20);

  private static class Holder {

    // singleton instance.
    static final ResourceManager INSTANCE = new ResourceManager();
  }

  public static ResourceManager getSingleton() {
    return Holder.INSTANCE;
  }

  private ResourceManager() {
    mainTankImage = readBufferedImageToDirectionMap("images/mainTank.png");
    otherTankImage = readBufferedImageToDirectionMap("images/otherTank.png");
    bulletImage = readBufferedImageToDirectionMap("images/bullet.png");

    for (int i = 1; i <= 16; i++) {
      BufferedImage image = readBufferedImage("images/explode/e" + i + ".gif");
      explodeImage.add(image);
    }
  }

  private Map<Direction, BufferedImage> readBufferedImageToDirectionMap(String pathName) {
    Map<Direction, BufferedImage> map = new HashMap<>(16);
    BufferedImage image = readBufferedImage(pathName);
    if (image != null) {
      map.put(Direction.UP, image);
      map.put(Direction.LEFT, ImageUtils.rotateImage(image, -90));
      map.put(Direction.RIGHT, ImageUtils.rotateImage(image, 90));
      map.put(Direction.DOWN, ImageUtils.rotateImage(image, 180));
    }
    return map;
  }

  private BufferedImage readBufferedImage(String pathName) {
    try {
      return ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader()
          .getResourceAsStream(pathName)));
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }


  public Map<Direction, BufferedImage> getMainTankImage() {
    return Collections.unmodifiableMap(mainTankImage);
  }

  public Map<Direction, BufferedImage> getOtherTankImage() {
    return Collections.unmodifiableMap(otherTankImage);
  }

  public Map<Direction, BufferedImage> getBulletImage() {
    return Collections.unmodifiableMap(bulletImage);
  }

  public List<BufferedImage> getExplodeImage() {
    return Collections.unmodifiableList(explodeImage);
  }

}
