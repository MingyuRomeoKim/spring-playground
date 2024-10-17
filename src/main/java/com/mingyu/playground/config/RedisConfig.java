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
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
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
    @Value("${redis.host1}")
    private String host1;
    @Value("${redis.host2}")
    private String host2;
    @Value("${redis.host3}")
    private String host3;
    @Value("${redis.port1}")
    private int port1;
    @Value("${redis.port2}")
    private int port2;
    @Value("${redis.port3}")
    private int port3;
    @Value("${redis.timeout.socket}")
    private int timeoutSocket;
    @Value("${redis.timeout.client}")
    private int timeoutClient;

    /**
     * local 환경에서 사용할 redis connection factory - 단일 구성
     * @return
     */
    @Bean
    @Profile("local")
    public RedisConnectionFactory redisConnectionFactoryLocal() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host1);
        redisStandaloneConfiguration.setPort(port1);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * prod 환경에서 사용할 redis connection factory - 클러스터 구성
     * @return
     */
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

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory, BatchStrategies.scan(1000));
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisCacheWriter)
                .cacheDefaults(defaultCacheConfig())
                .withInitialCacheConfigurations(customCacheConfig())
                .build();
        redisCacheManager.setTransactionAware(false);
        return redisCacheManager;
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
        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); // hash key

        redisTemplate.setValueSerializer(new StringRedisSerializer());  // value
        redisTemplate.setHashValueSerializer(new StringRedisSerializer()); // hash value
        return redisTemplate;
    }

}
