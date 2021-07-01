package com.example.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：zhx
 * @date ：Created in 2020/4/9 15:09
 * @modified By：
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfigParam {

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 端口号
     */
    private String port;


    private Integer database;


    private String password;

}
