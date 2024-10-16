package com.mingyu.playground.domain.redis.repository;

import com.mingyu.playground.domain.article.entities.Article;
import com.mingyu.playground.domain.article.entities.ArticleHit;
import com.mingyu.playground.domain.article.repositories.ArticleHitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HitRedisRepository {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final ArticleHitRepository articleHitRepository;

    private static final int SCAN_MATCH_LIMIT_COUNT = 10;
    private static final String SCAN_MATCH_PATTERN = "*";

    public void increaseHits(String keyName, Long id) {
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();

        StringBuilder key = new StringBuilder(keyName + ":");
        key.append(id);
        key.append("hits");
        ;

        operations.increment(key.toString());
    }

    public Integer getHits(String keyName, Long id) {
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();

        StringBuilder key = new StringBuilder(keyName + ":");
        key.append(id);
        key.append("hits");

        return operations.get(key.toString());
    }

    public Integer getAndDelete(String keyName, Long id) {
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();

        StringBuilder key = new StringBuilder(keyName + ":");
        key.append(id);
        key.append("hits");

        return operations.getAndDelete(key.toString());
    }

    @Transactional
    public void updateArticleRDB() {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(SCAN_MATCH_PATTERN).count(SCAN_MATCH_LIMIT_COUNT).build();
        Cursor<byte[]> keys = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);

        while (keys.hasNext()) {
            Long articleId = extractId(keys);
            ArticleHit articleHit = articleHitRepository.findById(articleId).get();
            Integer hits = getHits("article",articleId);

            // RDB 반영 도중 조회수 증가 요청
            Integer andDelete = getAndDelete("article",articleId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO - 개발 속행
            articleHit.increasePcHits(hits);
            articleHit.increaseMobileHits(hits);
        }
        flushAll();
    }

    public void flushAll() {
        RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        DefaultStringRedisConnection defaultStringRedisConnection = new DefaultStringRedisConnection(redisConnection, redisSerializer);
        defaultStringRedisConnection.flushAll();
    }

    private Long extractId(Cursor<byte[]> keys) {
        String key = new String(keys.next());
        String[] split = key.split(":");
        int index = key.indexOf(":");
        int index1 = key.indexOf("hits");

        return Long.valueOf(key.substring(index + 1, index1));
    }
}
