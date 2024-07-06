package com.cq.oj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.net.UnknownHostException;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory, LettuceConnectionFactory redisConnectionFactory) throws UnknownHostException {
        // 创建Template对象
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // 创建连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置序列化工具
        GenericJackson2JsonRedisSerializer Jackson2JsonRedisSerializer
                = new GenericJackson2JsonRedisSerializer();
        // key和hashKey采用String序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // value和hashValue采用Jackson2JsonRedisSerializer序列化
        redisTemplate.setValueSerializer(Jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(Jackson2JsonRedisSerializer);
        return redisTemplate;
    }
}
