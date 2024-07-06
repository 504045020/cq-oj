package com.cq.oj.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String RedisPort;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redisClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+ redisHost + ":" + RedisPort)
                .setDatabase(0)
                .setPassword(password);
        return Redisson.create(config);
    }
}
