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

import com.github.flysium.io.tank.config.AudioManager;
import com.github.flysium.io.tank.config.ClientConfig;
import com.github.flysium.io.tank.view.TankFrame;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Main
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class TankClientBootstrap {

  public static void main(String[] args) throws InterruptedException {
    String username = randomString(4, 10);
    TankFrame ui = new TankFrame(username);

    new Thread(() -> {
      while (true) {
        try {
          TimeUnit.MILLISECONDS.sleep(ClientConfig.getSingleton().getRefreshMillis());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        // repaint
        ui.repaint();
      }
    }, "repaint").start();

    new Thread(() -> {
      while (AudioManager.getSingleton().isAudioOn()) {
        AudioManager.getSingleton().playWarAudio();
        try {
          TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }, "music").start();

    ui.setVisible(true);
    ui.start();
  }

  private static final String DICT_STRING = "abcdefghjkmnopqrstuvwxyz023456789";

  private static String randomString(int min, int max) {
    Random random = new Random();
    int length = min + random.nextInt(max - min);
    StringBuilder nameString = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      nameString.append(DICT_STRING.charAt(random.nextInt(DICT_STRING.length())));
    }
    return nameString.toString();
  }

}
