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
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Netty Server
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class SimpleNettyServer {

  private static final Logger logger = LoggerFactory.getLogger(SimpleNettyServer.class);

  private final int port;
  private final ServerChildHandler serverChildHandler;
  private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  public SimpleNettyServer(int port, ServerChildHandler serverChildHandler) {
    this.port = port;
    this.serverChildHandler = serverChildHandler;
  }

  /**
   * Start the server.
   */
  public void start() throws InterruptedException {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup(12);
    try {
      new ServerBootstrap()
          .group(bossGroup, workGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1000)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.TCP_NODELAY, true)
          .childOption(ChannelOption.SO_LINGER, 100)
          .childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline()
                  .addLast(new ProtocolMessageDecoder())
                  .addLast(new ProtocolMessageEncoder())
                  .addLast(new ServerChannelChildHandler());
            }
          })
          .bind(port)
          .sync()
          .channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }

  /**
   * write to all active channels
   */
  public void sendMessageToAllChannels(ProtocolMessage message) {
    channels.forEach(channel -> channel.writeAndFlush(message));
  }

  /**
   * write to the channel
   */
  public void sendMessageToChannel(String channelId, ProtocolMessage message) {
    channels.stream()
        .filter(channel -> channelId != null && channelId.equals(getChannelId(channel)))
        .findFirst().ifPresent(channel -> channel.writeAndFlush(message));
  }

  /**
   * write to other channels except this channel
   */
  public void sendMessageToChannelsExcept(String channelId, ProtocolMessage message) {
    channels.stream()
        .filter(channel -> channelId != null && !channelId.equals(getChannelId(channel)))
        .forEach(channel -> channel.writeAndFlush(message));
  }

  private String getChannelId(Channel channel) {
    return channel.id().asLongText();
  }

  class ServerChannelChildHandler extends SimpleChannelInboundHandler<ProtocolMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage msg) {
      if (serverChildHandler != null) {
        serverChildHandler.channelRead(getChannelId(ctx.channel()), msg);
      }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
      if (serverChildHandler != null) {
        serverChildHandler.channelInactive(getChannelId(ctx.channel()));
      }
      channels.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
      channels.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      channels.remove(ctx.channel());
      logger.error(cause.getMessage(), cause);
      ctx.close();
    }
  }

}
