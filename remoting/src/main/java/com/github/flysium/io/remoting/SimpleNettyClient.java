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

import com.github.flysium.io.remoting.codec.ProtocolMessageDecoder;
import com.github.flysium.io.remoting.codec.ProtocolMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Netty Client.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class SimpleNettyClient {

  private static final Logger logger = LoggerFactory.getLogger(SimpleNettyClient.class);

  private final String host;
  private final int port;
  private final ClientHandler clientHandler;
  private Channel channel = null;

  public SimpleNettyClient(String host, int port, ClientHandler clientHandler) {
    this.host = host;
    this.port = port;
    this.clientHandler = clientHandler;
  }

  /**
   * start the client.
   */
  public void start() throws InterruptedException {
    EventLoopGroup workGroup = new NioEventLoopGroup(1);
    try {
      new Bootstrap()
          .group(workGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .option(ChannelOption.SO_LINGER, 100)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline()
                  .addLast(new ProtocolMessageDecoder())
                  .addLast(new ProtocolMessageEncoder())
                  .addLast(new ClientChannelHandler());
            }
          })
          .connect(host, port)
          .addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
              // offer to ready message queue
              logger.warn("Channel is not connected !");
            } else {
              logger.debug("Channel is connected !");
              // initialize the channel
              channel = future.channel();
            }
          }).sync()
          .channel().closeFuture()
          .addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
              logger.warn("channel close success");
            } else {
              logger.warn("channel close fail");
            }
          })
          .sync();

      logger.warn("Connection closed !");
    } finally {
      workGroup.shutdownGracefully();
    }
  }

  /**
   * stop the client.
   */
  public void stop() {
    if (channel != null) {
      channel.close();
    }
  }

  /**
   * write to server.
   *
   * @param protocolMessage <code>ProtocolMessage</code> content
   */
  public void sendMessage(ProtocolMessage protocolMessage) {
    channel.writeAndFlush(protocolMessage);
  }

  class ClientChannelHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
      if (clientHandler != null) {
        clientHandler.channelActive();
      }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg) {
      if (clientHandler != null) {
        clientHandler.channelRead(msg);
      }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      logger.error(cause.getMessage(), cause);
      ctx.close();
    }
  }

}
