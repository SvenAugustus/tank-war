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

package com.github.flysium.io.tank.service;

import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.model.Tank;
import java.awt.Graphics;

/**
 * <code>GameService</code> implement
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GameServiceImpl implements GameService {

  private final GameModel gameModel;

  public GameServiceImpl(final FinalRectangle bounds) {
    gameModel = new GameModel(bounds);
  }

  @Override
  public void paint(Graphics g) {
    gameModel.paint(g);
  }

  @Override
  public void moveMainTankByDirection(Direction direction) {
    Tank mainTank = gameModel.getMainTank();
    mainTank.changeDirection(direction);
    mainTank.moveOn();
  }

  @Override
  public void fireMainTank() {
    gameModel.getMainTank().fire();
  }

}
