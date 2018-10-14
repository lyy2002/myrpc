package com.lyy.netty.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.lyy.entity.RpcRequest;
import com.lyy.entity.RpcResponse;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * @author lyy
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
		RpcResponse response = new RpcResponse();
		response.setRequestId(msg.getRequestId());
		try {
			Object result = handler(msg);
			response.setResult(result);
		} catch (Throwable e) {
			response.setThrowable(e);
			e.printStackTrace();
		}
		ctx.writeAndFlush(response);
		
	}
	
	private Object handler(RpcRequest request) throws Throwable {
		Class<?> clz = Class.forName(request.getClassName());
		
		Object serviceBean = applicationContext.getBean(clz);
		
		String methodName = request.getMethodName();
		
		Class<?>[] parameterTypes = request.getParameterTypes();
		
		Object[] parameters = request.getParameters();
		
		FastClass fastClass = FastClass.create(serviceBean.getClass());
		
		FastMethod fastMethod = fastClass.getMethod(methodName,parameterTypes);
		
		return fastMethod.invoke(serviceBean, parameters);
	}

}
