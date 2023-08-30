package com.example.sns.configuration;

import com.example.sns.model.User;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // application.yml 에서 spring.yml에 저장된 url 가져온다.
        RedisURI redisURI = RedisURI.create(redisProperties.getUrl());
        // URL로 레디스 설정 클래스 생성
        org.springframework.data.redis.connection.RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
        // 레디스 factory 생성
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        // 레디스 템플릿에 정의한 팩토리 주입받음
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key는 스트링이니 스트링 시리얼라이저 주입
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 임의로 만든 클래스이므로 jackson 시리얼라이저 주입
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class));
        return redisTemplate;
    }
}
