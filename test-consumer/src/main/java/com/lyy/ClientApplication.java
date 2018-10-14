package com.lyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.lyy.api.IHelloService;

@SpringBootApplication()
public class ClientApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class);
	    IHelloService helloService = context.getBean(IHelloService.class);
        System.out.println(helloService.sayHi("lyy"));
    }

}
