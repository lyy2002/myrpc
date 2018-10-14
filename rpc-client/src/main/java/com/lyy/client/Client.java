package com.lyy.client;

import java.net.InetSocketAddress;

import com.lyy.entity.RpcRequest;
import com.lyy.entity.RpcResponse;

public interface Client {

    RpcResponse send(RpcRequest request);

    void connect(InetSocketAddress inetSocketAddress);

    InetSocketAddress getInetSocketAddress();

    void close();

}
