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

package com.github.flysium.io.tank;

import com.github.flysium.io.tank.config.ResourceManager;
import com.github.flysium.io.tank.config.WindowConfig;
import com.github.flysium.io.tank.view.TankFrame;
import java.util.concurrent.TimeUnit;

/**
 * Main
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class Main {

  private static final WindowConfig WINDOW_CONFIG = WindowConfig.getSingleton();

  public static void main(String[] args) {
    TankFrame ui = new TankFrame(WINDOW_CONFIG);
    ui.setVisible(true);

    new Thread(() -> {
      while (true) {
        try {
          TimeUnit.MILLISECONDS.sleep(WINDOW_CONFIG.getRefreshMillis());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        // repaint
        ui.repaint();
      }
    }, "repaint").start();

    new Thread(() -> {
      while (true) {
        try {
          TimeUnit.MILLISECONDS.sleep(WINDOW_CONFIG.getAutomaticMillis());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        ui.automatic();
      }
    }, "automatic").start();

    new Thread(() -> {
      while (ResourceManager.getSingleton().isAudioOn()) {
        ResourceManager.getSingleton().playWarAudio();
        try {
          TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }, "music").start();
  }

}
