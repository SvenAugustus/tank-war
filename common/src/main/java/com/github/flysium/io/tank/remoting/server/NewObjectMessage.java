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
import com.github.flysium.io.tank.remoting.ProtocolMessageConverter;
import com.github.flysium.io.tank.remoting.TankMessageType;

/**
 * New Object
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class NewObjectMessage implements java.io.Serializable, ProtocolMessageConverter {

  private static final long serialVersionUID = 2805089728853197611L;

  private String ownerUserId;

  private GameObject object;

  /**
   * convert to <code>ProtocolMessage</code>
   */
  @Override
  public ProtocolMessage convertToProtocolMessage() throws Exception {
    return ProtocolMessage.newMessage((short) TankMessageType.NEW_OBJECT.ordinal(),
        SerializerUtils.toBytes(this));
  }

  public String getOwnerUserId() {
    return ownerUserId;
  }

  public void setOwnerUserId(String ownerUserId) {
    this.ownerUserId = ownerUserId;
  }

  public GameObject getObject() {
    return object;
  }

  public void setObject(GameObject object) {
    this.object = object;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private final NewObjectMessage newObjectMessage;

    private Builder() {
      newObjectMessage = new NewObjectMessage();
    }

    public Builder ownerUserId(String ownerUserId) {
      newObjectMessage.setOwnerUserId(ownerUserId);
      return this;
    }

    public Builder object(GameObject object) {
      newObjectMessage.setObject(object);
      return this;
    }

    public NewObjectMessage build() {
      return newObjectMessage;
    }
  }
}