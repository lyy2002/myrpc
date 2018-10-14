package com.lyy.config;

import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.lyy.RpcInterface;
import com.lyy.proxy.ProxyFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lyy
 */
@Configuration
@Slf4j
public class RpcConfig implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Reflections reflection = new Reflections("com.lyy");
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		Set<Class<?>> clazzs = reflection.getTypesAnnotatedWith(RpcInterface.class);
		for(Class<?> clz : clazzs) {
			beanFactory.registerSingleton(clz.getSimpleName(), ProxyFactory.create(clz));
		}
		log.info("afterPropertiesSet is {}",clazzs);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
