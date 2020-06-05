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

import com.github.flysium.io.remoting.ProtocolMessage;
import com.github.flysium.io.remoting.codec.ProtocolMessageDecoder;
import com.github.flysium.io.remoting.codec.ProtocolMessageEncoder;
import com.github.flysium.io.tank.config.GameConfig;
import com.github.flysium.io.tank.model.DefaultTank;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.DirectionRectangularShape;
import com.github.flysium.io.tank.model.TankAttributes;
import com.github.flysium.io.tank.remoting.server.GameInitializationMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sven Augustus
 * @version 1.0
 */
public class TankLoginResponseMessageTest {

  @Test
  public void testDecode() throws Exception {
    EmbeddedChannel channel = new EmbeddedChannel();
    channel.pipeline().addLast(new ProtocolMessageDecoder());

    ProtocolMessage m1 = GameInitializationMessage.builder()
        .success(true)
        .userId(UUID.randomUUID().toString())
        .tank(new DefaultTank(50, 50,
            TankAttributes.builder()
                .initialHealth(100)
                .initialDirection(Direction.DOWN)
                .shape(new DirectionRectangularShape(50))
                .bounds(GameConfig.getSingleton().getFinalRectangle())
                .movingSpeed(5)
                .build()))
        .build()
        .convertToProtocolMessage();

    channel.writeOutbound(m1);
    ProtocolMessage cm1 = channel.readOutbound();

    assertMessage(m1, cm1);
  }

  @Test
  public void testEncode() throws Exception {
    EmbeddedChannel channel = new EmbeddedChannel();
    channel.pipeline().addLast(new ProtocolMessageDecoder(),
        new ProtocolMessageEncoder());

    ProtocolMessage m1 = GameInitializationMessage.builder()
        .success(true)
        .userId(UUID.randomUUID().toString())
        .tank(new DefaultTank(50, 50,
            TankAttributes.builder()
                .initialHealth(100)
                .initialDirection(Direction.DOWN)
                .shape(new DirectionRectangularShape(50))
                .bounds(GameConfig.getSingleton().getFinalRectangle())
                .movingSpeed(5)
                .build()))
        .build()
        .convertToProtocolMessage();

    channel.writeInbound(m1);
    ProtocolMessage cm1 = channel.readInbound();

    assertMessage(m1, cm1);
  }

  private void assertMessage(ProtocolMessage m1, ProtocolMessage cm1) {
    Assert.assertNotNull(cm1);
    Assert.assertEquals(m1.getType(), cm1.getType());
    Assert.assertEquals(m1.getLength(), cm1.getLength());
    Assert.assertEquals(m1.getBody(), cm1.getBody());
    Assert.assertEquals(m1.getBody().length, cm1.getLength());
  }

}