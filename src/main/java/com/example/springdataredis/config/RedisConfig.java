package com.example.springdataredis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() { //RedisConnectionFactory 빈을 생성하는 메서드
        //LettuceConnectionFactory : Spring data redis에서 redis와의 연결 설정
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() { //키,값 형태의 RedisTemplate 빈을 생성하는 메서드
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory()); //redis와 연결을 하고 관리하는 객체를 생성하고, 구성하기 위해 사용
        //키와 값을 직렬화, redis에서는 키와 값을 저장할 때 직렬화된 형태로 저장됨
        //StringRedisSerializer를 사용해 문자열로 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
