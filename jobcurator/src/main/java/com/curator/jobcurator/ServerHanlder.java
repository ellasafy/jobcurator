package com.curator.jobcurator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHanlder extends SimpleChannelInboundHandler<Object> {
	
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof ByteBuf) {
			
			ctx.channel().writeAndFlush("ret");
		} else {
			
		}
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
		   .addListener(ChannelFutureListener.CLOSE);
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}

}
