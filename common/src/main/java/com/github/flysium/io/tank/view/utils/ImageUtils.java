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

package com.github.flysium.io.tank.view.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Image Utils.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class ImageUtils {

  private ImageUtils() {
  }

  /**
   * route the Image with degree
   *
   * @param bufferedImage Image
   * @param degree        degree
   * @return new Image.
   */
  public static BufferedImage rotateImage(final BufferedImage bufferedImage,
      final int degree) {
    int w = bufferedImage.getWidth();
    int h = bufferedImage.getHeight();
    int type = bufferedImage.getColorModel().getTransparency();
    BufferedImage img;
    Graphics2D graphics2d;
    (graphics2d = (img = new BufferedImage(w, h, type))
        .createGraphics()).setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
    graphics2d.drawImage(bufferedImage, 0, 0, null);
    graphics2d.dispose();
    return img;
  }

}
