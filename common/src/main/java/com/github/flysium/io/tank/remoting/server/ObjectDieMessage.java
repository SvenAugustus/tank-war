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
import com.github.flysium.io.tank.remoting.ProtocolMessageConverter;
import com.github.flysium.io.tank.remoting.TankMessageType;

/**
 * Game Object Die
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class ObjectDieMessage implements java.io.Serializable, ProtocolMessageConverter {

  private static final long serialVersionUID = -9167831730797439102L;

  private String id;

  private String type;

  /**
   * convert to <code>ProtocolMessage</code>
   */
  @Override
  public ProtocolMessage convertToProtocolMessage() throws Exception {
    return ProtocolMessage.newMessage((short) TankMessageType.OBJECT_DIE.ordinal(),
        SerializerUtils.toBytes(this));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private final ObjectDieMessage objectDieMessage;

    private Builder() {
      objectDieMessage = new ObjectDieMessage();
    }

    public Builder id(String id) {
      objectDieMessage.setId(id);
      return this;
    }

    public Builder type(String type) {
      objectDieMessage.setType(type);
      return this;
    }

    public ObjectDieMessage build() {
      return objectDieMessage;
    }
  }
}
