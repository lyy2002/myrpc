package com.lyy;

import com.lyy.entity.RpcRequest;
import com.lyy.entity.RpcResponse;
import com.lyy.netty.NettyClient;

public class Transporters {

	private static NettyClient nettyClient = new NettyClient("127.0.0.1", 8080);
	
	static {
		nettyClient.connect(nettyClient.getInetSocketAddress());
	}
    public static RpcResponse send(RpcRequest request){
        RpcResponse send = nettyClient.send(request);
        return send;

    }

}
