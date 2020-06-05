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
import com.github.flysium.io.tank.view.utils.AudioUtils;

/**
 * Audio Manager.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class AudioManager {

  private final boolean audioOn;

  private AudioManager() {
    audioOn = PropertiesUtils.getBooleanProperty("client.audio.on", false);
  }

  public static AudioManager getSingleton() {
    return Holder.INSTANCE;
  }

  public boolean isAudioOn() {
    return audioOn;
  }

  public void playWarAudio() {
    if (!audioOn) {
      return;
    }
    AudioUtils.play("audio/war1.wav");
  }

  public void asyncPlayFireAudio() {
    if (!audioOn) {
      return;
    }
    AudioUtils.asyncPlayChannel("audio/tank_fire.wav");
  }

  public void asyncPlayMoveAudio() {
    if (!audioOn) {
      return;
    }
    AudioUtils.asyncPlayChannel("audio/tank_move.wav");
  }

  public void asyncPlayExplodeAudio() {
    if (!audioOn) {
      return;
    }
    AudioUtils.asyncPlayChannel("audio/explode.wav");
  }

  private static class Holder {

    // singleton instance.
    static final AudioManager INSTANCE = new AudioManager();
  }
}
