package com.lyy;

import com.lyy.entity.RpcResponse;

/**
 * @author lyy
 */
public class DefaultFuture {
	private RpcResponse rpcResponse;
	private volatile boolean isSucceed = false;
	private final Object object = new Object();
	
	public RpcResponse getResponse() {
		synchronized(object) {
			while(!isSucceed) {
				try {
					object.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return rpcResponse;
		}
	}
	
	public void setResponse(RpcResponse response) {
		if(isSucceed) {
			return ;
		}
		
		synchronized(object) {
			isSucceed = true;
			this.rpcResponse = response;
			object.notifyAll();
		}

	}
}
