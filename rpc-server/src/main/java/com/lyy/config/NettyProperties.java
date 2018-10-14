package com.lyy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author lyy
 */

@Data
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {

    private int tcpPort = 8080;

    private int bossCount = 4;

    private int workerCount = 16;

    private boolean keepAlive = true;

    private int backlog;
}
