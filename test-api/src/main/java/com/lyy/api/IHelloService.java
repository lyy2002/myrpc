package com.lyy.api;

import com.lyy.RpcInterface;

@RpcInterface
public interface IHelloService {

    String sayHi(String name);

}
