package com.tinybullet.game.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	public TimeClient() throws Exception {
		String host = "localhost";
		int port = 45678;

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {
					ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
				}
			});

			ChannelFuture f = b.connect(host, port).sync();

			f.channel().closeFuture().sync();

		}
		finally {
			workerGroup.shutdownGracefully();
		}
	}
}
