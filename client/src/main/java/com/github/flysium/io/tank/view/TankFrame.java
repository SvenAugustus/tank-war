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

package com.github.flysium.io.tank.view;

import com.github.flysium.io.tank.config.GameConfig;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.service.TankClientService;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 * Game View of Tank War
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class TankFrame extends JFrame {

  private final Dimension dimension = GameConfig.getSingleton().getDimension();
  private final TankClientService clientService;

  public TankFrame(String username) throws HeadlessException, InterruptedException {
    this.clientService = new TankClientService(username);
    this.setTitle("The War of Tank (" + username + ")");
    this.setBounds(200, 200, dimension.width, dimension.height);
    this.setResizable(false);

    // Exit on window button x.
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    // add Key listener
    this.addKeyListener(new MyKeyListener());
  }

  public void start() throws InterruptedException {
    this.clientService.start();
  }

  private BufferedImage image;
  private Graphics imageGraphics;

  @Override
  public void paint(Graphics g) {
    // buffer for swing, fix the twinkle.
    if (image == null) {
      image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
      imageGraphics = image.createGraphics();
    }
    imageGraphics.fillRect(0, 0, dimension.width, dimension.height);
    imageGraphics.setColor(Color.WHITE);

    // draw to buffer
    clientService.paint(imageGraphics);

    // output buffer to screen
    g.drawImage(image, 0, 0, null);
  }

  private class MyKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
          // Key Left / Key A: Move Left
          clientService.moveMainTankByDirection(Direction.LEFT);
          break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
          // Key Right / Key D: Move Right
          clientService.moveMainTankByDirection(Direction.RIGHT);
          break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
          // Key Up / Key W: Move Up
          clientService.moveMainTankByDirection(Direction.UP);
          break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
          // Key Down / Key S: Move Down
          clientService.moveMainTankByDirection(Direction.DOWN);
          break;
        default:
          break;
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_CONTROL:
          // Key Down / Key S: Move Down
          clientService.fireMainTank();
          break;
        default:
          break;
      }
    }
  }

}
