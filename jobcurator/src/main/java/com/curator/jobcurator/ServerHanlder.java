package com.curator.jobcurator;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHanlder extends SimpleChannelInboundHandler<Object> {
	
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof ByteBuf) {
			ByteBuf ret = process((ByteBuf)msg);
			ctx.channel().writeAndFlush(ret);
		} else {
			
		}
	}
	
	public ByteBuf process(ByteBuf buf) {
		  byte[] bytes = new byte[buf.readableBytes()];
	        buf.readBytes(bytes);
//	        LOG.debug("Received message : " + new String(bytes));
	        
	        Map<String, Object> result = new HashMap<String, Object>();
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode node = null;
	        try {
	            node = mapper.readTree(bytes);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        String type = node.findValue("type").asText();
	        if (type.equals("add")) {
	        	
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
