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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Game View of Tank War
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class TankFrame extends JFrame {

  public static final int WINDOW_WIDTH = 800;
  public static final int WINDOW_HEIGHT = 600;
  private int x = 50;
  private int y = 50;

  public TankFrame() throws HeadlessException {
    this.setTitle("The War of Tank");
    this.setBounds(200, 200, WINDOW_WIDTH, WINDOW_HEIGHT);
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

  @Override
  public void paint(Graphics g) {
    Color c = g.getColor();
    g.setColor(Color.YELLOW);
    // draw the tank
    g.fillRect(x, y, 50, 50);
    // reset Graphics's color
    g.setColor(c);
  }

  private class MyKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
          // Key Left / Key A: Move Left
          x -= 10;
          break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
          // Key Right / Key D: Move Right
          x += 10;
          break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
          // Key Up / Key W: Move Up
          y -= 10;
          break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
          // Key Down / Key S: Move Down
          y += 10;
          break;
        default:
          break;
      }
    }

  }

}