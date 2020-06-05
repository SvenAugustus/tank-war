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

package com.github.flysium.io.tank.remoting;

import com.github.flysium.io.tank.remoting.server.StatusUpdateMessage;

/**
 * <code>ProtocolMessage</code> Type for Tank.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public enum TankMessageType {
  /**
   * Client Login, body : {@link com.github.flysium.io.tank.remoting.client.ClientLoginMessage}
   */
  CLIENT_LOGIN,
  /**
   * Game Initialization, body : {@link com.github.flysium.io.tank.remoting.server.GameInitializationMessage}
   */
  INITIALIZATION,
  /**
   * New Game Object, body : {@link com.github.flysium.io.tank.remoting.server.NewObjectMessage}
   */
  NEW_OBJECT,
  /**
   * Game Object Die, body : {@link com.github.flysium.io.tank.remoting.server.ObjectDieMessage}
   */
  OBJECT_DIE,
  /**
   * Tank Move, body : {@link com.github.flysium.io.tank.remoting.client.TankMoveMessage}
   */
  CLIENT_MOVE,
  /**
   * Tank fire, body : {@link com.github.flysium.io.tank.remoting.client.TankMoveMessage}
   */
  CLIENT_FIRE,
  /**
   * Status Update, body : {@link StatusUpdateMessage}
   */
  STATUS_UPDATE;
}
