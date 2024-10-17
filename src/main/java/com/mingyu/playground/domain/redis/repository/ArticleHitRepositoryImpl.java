package com.mingyu.playground.domain.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ArticleHitRepositoryImpl implements ArticleHitRepositoryCustom {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "article-hit";

    /**
     * hash table 특정 articleId에 저장된 hitCount를 증가시킨다.
     * @param articleId
     * @param hits
     * @return
     */
    @Override
    public int incrementPcHitCount(String articleId, Integer hits) {
        String key = KEY_PREFIX + ":" + articleId;
        Long updatedValue = redisTemplate.opsForHash().increment(key, "hitCount", hits);

        return updatedValue.intValue();
    }

    /**
     * hash table 특정 articleId에 저장된 mobileHitCount를 증가시킨다.
     * @param articleId
     * @param hits
     * @return
     */
    @Override
    public int incrementMobileHitCount(String articleId, Integer hits) {
        String key = KEY_PREFIX + ":" + articleId;
        Long updatedValue = redisTemplate.opsForHash().increment(key, "mobileHitCount", hits);

        return updatedValue.intValue();
    }

    /**
     * Sets 의 key에 저장된 모든 멤버를 가져온다.
     * @return
     */
    @Override
    public List<String> getMembers() {
        String key = KEY_PREFIX;

        Set<Object> members = redisTemplate.opsForSet().members(key);

        if (members != null) {
            List<String> returnMembers = new ArrayList<>();
            for (Object member : members) {
                returnMembers.add(member.toString());
            }
            return returnMembers;
        }

        return List.of();
    }


    /**
     * 특정 articleId에 저장된 모든 hitCount를 가져오고 삭제한다.
     * @param articleId Article ID
     * @return articleId에 저장된 모든 hitCount
     */
    @Override
    public Map<String, Object> getAndDeleteAllHitCount(String articleId) {
        String key = KEY_PREFIX + ":" + articleId;

        // Lua 스크립트 정의
        String luaScript =
                "local result = redis.call('HGETALL', KEYS[1]); " +
                        "redis.call('DEL', KEYS[1]); " +
                        "return result;";

        // RedisScript 생성
        RedisScript<List> script = RedisScript.of(luaScript, List.class);

        // Lua 스크립트 실행
        List<Object> result = redisTemplate.execute(script, Collections.singletonList(key));
        redisTemplate.opsForSet().remove(KEY_PREFIX, articleId);

        // 결과를 Map으로 변환
        Map<String, Object> hitCounts = new HashMap<>();

        if (result != null && !result.isEmpty()) {
            for (int i = 0; i < result.size(); i += 2) {
                String field = result.get(i).toString();
                String valueStr = result.get(i + 1).toString();
                hitCounts.put(field, valueStr);
            }
        }

        return hitCounts;
    }
}
