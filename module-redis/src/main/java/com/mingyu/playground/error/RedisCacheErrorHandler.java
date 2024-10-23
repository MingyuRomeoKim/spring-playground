package com.mingyu.playground.error;

import io.lettuce.core.RedisCommandTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * redis cache 가 오류를 내더라도 500 에러를 내지 않고 우회하게 하는 에러 핸들러
 */
public class RedisCacheErrorHandler implements CacheErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {        handleTimeOutException(exception);
        log.warn("Unable to get from cache " + cache.getName() + " : " + exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        handleTimeOutException(exception);
        log.warn("Unable to put into cache " + cache.getName() + " : " + exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        handleTimeOutException(exception);
        log.warn("Unable to evict from cache " + cache.getName() + " : " + exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        handleTimeOutException(exception);
        log.warn("Unable to clean cache " + cache.getName() + " : " + exception.getMessage());
    }

    /**
     * We handle redis connection timeout exception , if the exception is handled then it is treated as a cache miss and
     * gets the data from actual storage
     *
     * @param exception
     */
    private void handleTimeOutException(RuntimeException exception) {
        if (exception instanceof RedisCommandTimeoutException) {
            return;
        }
    }
}

