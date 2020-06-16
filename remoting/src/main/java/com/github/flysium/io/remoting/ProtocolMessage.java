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

package com.github.flysium.io.remoting;

/**
 * Protocol Message.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class ProtocolMessage implements java.io.Serializable {

  private static final long serialVersionUID = -8070044213749552890L;

  private short type;

  private int length;

  private byte[] body;

  /**
   * new <code>ProtocolMessage</code>
   */
  public static ProtocolMessage newMessage(short type, byte[] body) {
    if (body == null) {
      body = new byte[0];
    }
    int length = body.length;
    return ProtocolMessage.builder()
        .type(type)
        .length(length)
        .body(body)
        .build();
  }

  public short getType() {
    return type;
  }

  public void setType(short type) {
    this.type = type;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public byte[] getBody() {
    return body;
  }

  public void setBody(byte[] body) {
    this.body = body;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private ProtocolMessage protocolMessage;

    private Builder() {
      protocolMessage = new ProtocolMessage();
    }

    public Builder type(short type) {
      protocolMessage.setType(type);
      return this;
    }

    public Builder length(int length) {
      protocolMessage.setLength(length);
      return this;
    }

    public Builder body(byte[] body) {
      protocolMessage.setBody(body);
      return this;
    }

    public ProtocolMessage build() {
      return protocolMessage;
    }
  }
}
