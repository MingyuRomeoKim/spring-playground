package com.mingyu.playground.config;

import com.mingyu.playground.common.error.RedisCacheErrorHandler;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableCaching
@EnableRedisRepositories
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${redis-cache.host1}")
    private String host1;
    @Value("${redis-cache.host2}")
    private String host2;
    @Value("${redis-cache.host3}")
    private String host3;
    @Value("${redis-cache.port1}")
    private int port1;
    @Value("${redis-cache.port2}")
    private int port2;
    @Value("${redis-cache.port3}")
    private int port3;
    @Value("${redis-cache.timeout.socket}")
    private int timeoutSocket;
    @Value("${redis-cache.timeout.client}")
    private int timeoutClient;

    @Bean
    @Profile("local")
    public RedisConnectionFactory redisConnectionFactoryLocal() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host1);
        redisStandaloneConfiguration.setPort(port1);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Profile("prod")
    public RedisConnectionFactory redisConnectionFactory() {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.clusterNode(host1, port1);
        redisClusterConfiguration.clusterNode(host2, port2);
        redisClusterConfiguration.clusterNode(host3, port3);

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofMillis(timeoutSocket)).build();
        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeoutClient))
                .clientOptions(clientOptions)
                .build();

        return new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
    }

    /**
     * 캐시 공통 설정
     * @return 레디스 캐시 설정
     */
    private RedisCacheConfiguration defaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(120)); // default
    }

    /**
     * 개별 캐시 설정
     * @return 캐시 설정 Map
     */
    private Map<String, RedisCacheConfiguration> customCacheConfig() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("mtnw.userTicket", defaultCacheConfig().entryTtl(Duration.ofSeconds(120)));
        cacheConfigurations.put("mtnw.youtubeStreamingList", defaultCacheConfig().entryTtl(Duration.ofSeconds(30)));
        cacheConfigurations.put("zeus-api.advisorUserInfos", defaultCacheConfig().entryTtl(Duration.ofSeconds(60)));
        cacheConfigurations.put("zeus-api.adminMessages", defaultCacheConfig().entryTtl(Duration.ofSeconds(300)));
        cacheConfigurations.put("zeus-api.advisorMessages", defaultCacheConfig().entryTtl(Duration.ofSeconds(300)));

        /* cacheConfigurations.put("keygroup.keyname", defaultCacheConfig().entryTtl(Duration.ofSeconds(600))); */

        return cacheConfigurations;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }

    // Redis template
    @Bean
    @Profile("prod")
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());   //connection
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key
        redisTemplate.setValueSerializer(new StringRedisSerializer());  // value
        return redisTemplate;
    }

    @Bean
    @Profile("local")
    public RedisTemplate<?, ?> redisTemplateLocal() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryLocal());   //connection
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key
        redisTemplate.setValueSerializer(new StringRedisSerializer());  // value
        return redisTemplate;
    }
}
