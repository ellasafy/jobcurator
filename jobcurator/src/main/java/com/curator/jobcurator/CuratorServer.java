package com.curator.jobcurator;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class CuratorServer {
	private int port;
	
	public void initServer() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group)
			.channel(NioServerSocketChannel.class)
			.localAddress(new InetSocketAddress(port))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ServerHanlder());
				}
			});
			
			ChannelFuture f = boot.bind().sync();
			
			
		}finally {
			group.shutdownGracefully().sync();
		}
	}

}
