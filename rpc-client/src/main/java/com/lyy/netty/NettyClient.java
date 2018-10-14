package com.lyy.netty;

import java.net.InetSocketAddress;

import com.lyy.api.JsonSerialization;
import com.lyy.client.Client;
import com.lyy.coder.RpcDecoder;
import com.lyy.coder.RpcEncoder;
import com.lyy.entity.RpcRequest;
import com.lyy.entity.RpcResponse;
import com.lyy.netty.handler.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author lyy
 */
public class NettyClient implements Client {
	
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private ClientHandler clientHandler;
	
	private final String host;
	private final int port;
	
	public NettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	

	@Override
	public RpcResponse send(RpcRequest request) {
		try {
			channel.writeAndFlush(request).await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientHandler.getRpcResponse(request.getRequestId());
	}

	@Override
	public void connect(final InetSocketAddress inetSocketAddress) {
        clientHandler = new ClientHandler();

        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4));
                        pipeline.addLast(new RpcEncoder(RpcRequest.class,new JsonSerialization()));
                        pipeline.addLast(new RpcDecoder(RpcResponse.class,new JsonSerialization()));
                        pipeline.addLast(clientHandler);
                    }
                });

        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	@Override
	public InetSocketAddress getInetSocketAddress() {
		return new InetSocketAddress(host,port);
	}

	@Override
	public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
	}

}
