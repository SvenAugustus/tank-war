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

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Audio Utils.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public final class AudioUtils {

  private AudioUtils() {
  }

  private static final Set<String> AUDIOS = new ConcurrentSkipListSet<>();

  private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(4, 4, 60,
      TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
      Executors.defaultThreadFactory(), new CallerRunsPolicy());

  /**
   * async play audio on a channel.
   *
   * @param fileName audio
   */
  public static void asyncPlayChannel(String fileName) {
    System.out.println(fileName + "--->" + AUDIOS);
    if (!AUDIOS.add(fileName)) {
      return;
    }
    EXECUTOR.submit(() -> {
      try {
        play(fileName);
      } finally {
        AUDIOS.remove(fileName);
      }
    });
  }

  /**
   * async play audio
   *
   * @param fileName audio
   */
  public static void asyncPlay(String fileName) {
    EXECUTOR.submit(() -> {
      play(fileName);
    });
  }

  /**
   * play audio.
   *
   * @param fileName audio
   */
  public static void play(String fileName) {
    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioUtils.class
        .getClassLoader().getResource(fileName));
    ) {
      AudioFormat audioFormat = audioInputStream.getFormat();
      SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem
          .getLine(new DataLine.Info(SourceDataLine.class, audioFormat));

      byte[] b = new byte[1024 * 5];
      int len = 0;
      sourceDataLine.open(audioFormat, 1024 * 5);
      sourceDataLine.start();

      audioInputStream.mark(12358946);
      while ((len = audioInputStream.read(b)) > 0) {
        sourceDataLine.write(b, 0, len);
      }
      sourceDataLine.drain();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

}
