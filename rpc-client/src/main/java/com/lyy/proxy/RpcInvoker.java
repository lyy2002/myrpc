package com.lyy.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.lyy.Transporters;
import com.lyy.entity.RpcRequest;

/**
 * @author lyy
 */
public class RpcInvoker implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RpcRequest request = new RpcRequest();
		
		String requestId = UUID.randomUUID().toString();
		
		String className = method.getDeclaringClass().getName();
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		request.setRequestId(requestId);
		request.setClassName(className);
		request.setMethodName(methodName);
		request.setParameters(args);
		request.setParameterTypes(parameterTypes);
		
		return Transporters.send(request).getResult();
	}

}
