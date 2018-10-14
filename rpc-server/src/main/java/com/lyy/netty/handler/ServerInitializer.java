package com.lyy.netty.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lyy.api.JsonSerialization;
import com.lyy.coder.RpcDecoder;
import com.lyy.coder.RpcEncoder;
import com.lyy.entity.RpcRequest;
import com.lyy.entity.RpcResponse;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author lyy
 */
@Component
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	
	@Autowired
	private ServerHandler serverHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4));
        pipeline.addLast(new RpcEncoder(RpcResponse.class,new JsonSerialization()));
        pipeline.addLast(new RpcDecoder(RpcRequest.class,new JsonSerialization()));
        pipeline.addLast(serverHandler);
	}

}
