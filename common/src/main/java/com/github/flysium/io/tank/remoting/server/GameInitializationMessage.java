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

package com.github.flysium.io.tank.remoting.server;

import com.github.flysium.io.remoting.ProtocolMessage;
import com.github.flysium.io.serialization.SerializerUtils;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Tank;
import com.github.flysium.io.tank.remoting.TankMessageType;
import java.util.List;

/**
 * Game Initialization
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GameInitializationMessage implements java.io.Serializable {

  private static final long serialVersionUID = 4803384143357221964L;

  private boolean success;

  private String error;

  private String userId;

  private Tank tank;

  private List<GameObject> objects;

  /**
   * convert to <code>ProtocolMessage</code>
   */
  public ProtocolMessage convertToProtocolMessage() throws Exception {
    return ProtocolMessage.newMessage((short) TankMessageType.INITIALIZATION.ordinal(),
        SerializerUtils.toBytes(this));
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Tank getTank() {
    return tank;
  }

  public void setTank(Tank tank) {
    this.tank = tank;
  }

  public List<GameObject> getObjects() {
    return objects;
  }

  public void setObjects(List<GameObject> objects) {
    this.objects = objects;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private final GameInitializationMessage gameInitializationMessage;

    private Builder() {
      gameInitializationMessage = new GameInitializationMessage();
    }

    public Builder success(boolean success) {
      gameInitializationMessage.setSuccess(success);
      return this;
    }

    public Builder error(String error) {
      gameInitializationMessage.setError(error);
      return this;
    }

    public Builder userId(String userId) {
      gameInitializationMessage.setUserId(userId);
      return this;
    }

    public Builder tank(Tank tank) {
      gameInitializationMessage.setTank(tank);
      return this;
    }

    public Builder objects(List<GameObject> objects) {
      gameInitializationMessage.setObjects(objects);
      return this;
    }

    public GameInitializationMessage build() {
      return gameInitializationMessage;
    }
  }
}
