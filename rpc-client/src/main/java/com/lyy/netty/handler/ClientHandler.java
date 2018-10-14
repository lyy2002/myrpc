package com.lyy.netty.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lyy.DefaultFuture;
import com.lyy.entity.RpcRequest;
import com.lyy.entity.RpcResponse;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * @author lyy
 */
public class ClientHandler extends ChannelDuplexHandler {

	 private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<String,DefaultFuture>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof RpcResponse) {
			RpcResponse response = (RpcResponse)msg;
			DefaultFuture future = futureMap.get(response.getRequestId());
			future.setResponse(response);
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg instanceof RpcRequest) {
			 RpcRequest request = (RpcRequest) msg;
			 futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
		}
		super.write(ctx, msg, promise);
	}
	
    public RpcResponse getRpcResponse(String requestId){

        try {
            DefaultFuture defaultFuture = futureMap.get(requestId);
            return defaultFuture.getResponse();
        }finally {
            futureMap.remove(requestId);
        }


    }

}
