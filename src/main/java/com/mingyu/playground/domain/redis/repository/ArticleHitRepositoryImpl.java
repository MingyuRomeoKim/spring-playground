package com.mingyu.playground.domain.redis.repository;

import com.mingyu.playground.domain.redis.entities.ArticleHit;
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
    private static final String KEY_PATTERN = KEY_PREFIX + ":*";

    /**
     * hash table 특정 articleId에 저장된 hitCount를 증가시킨다.
     *
     * @param articleId
     * @param hits
     * @return
     */
    @Override
    public int incrementPcHitCount(String articleId, Integer hits) {
        String key = KEY_PREFIX + ":" + articleId;
        Long updatedValue = redisTemplate.opsForHash().increment(key, "hitCount", hits);
        redisTemplate.opsForSet().add(KEY_PREFIX, articleId);

        return updatedValue.intValue();
    }

    /**
     * hash table 특정 articleId에 저장된 mobileHitCount를 증가시킨다.
     *
     * @param articleId
     * @param hits
     * @return
     */
    @Override
    public int incrementMobileHitCount(String articleId, Integer hits) {
        String key = KEY_PREFIX + ":" + articleId;
        Long updatedValue = redisTemplate.opsForHash().increment(key, "mobileHitCount", hits);
        redisTemplate.opsForSet().add(KEY_PREFIX, articleId);

        return updatedValue.intValue();
    }

    /**
     * Sets 의 key에 저장된 모든 멤버를 가져온다.
     *
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
     * 저장된 모든 articleHits를 가져오고 삭제한다.
     * spring data redis의 findAll(), deleteAll() 사용시 그 사이에 조회수가 들어오면 조회수가 누락될 수 있기 때문에 이와 같은 방법으로 해결함.
     *
     * @return
     */
    @Override
    public List<ArticleHit> getAndDeleteAllArticleHits() {
        // Lua 스크립트 정의
        String luaScript =
                "local keys = redis.call('keys', ARGV[1])\n" +
                        "local result = {}\n" +
                        "for i, key in ipairs(keys) do\n" +
                        "    local data = redis.call('hgetall', key)\n" +
                        "    table.insert(result, key)\n" +
                        "    for j, value in ipairs(data) do\n" +
                        "        table.insert(result, value)\n" +
                        "    end\n" +
                        "end\n" +
                        "if #keys > 0 then\n" +
                        "    redis.call('del', unpack(keys))\n" +
                        "end\n" +
                        "return result";

        // RedisScript 생성
        RedisScript<List> script = RedisScript.of(luaScript, List.class);

        // 스크립트 실행
        List<Object> result = redisTemplate.execute(script, Collections.emptyList(), KEY_PATTERN);

        // 결과 파싱
        List<ArticleHit> articleHits = new ArrayList<>();

        if (result != null && !result.isEmpty()) {
            int index = 0;

            while (index < result.size()) {
                String key = result.get(index++).toString();
                Map<String, String> dataMap = new HashMap<>();

                while (index < result.size() && !result.get(index).toString().startsWith(KEY_PREFIX + ":")) {
                    String field = result.get(index++).toString();
                    String value = result.get(index++).toString();
                    dataMap.put(field, value);
                }

                ArticleHit articleHit = mapToArticleHit(key, dataMap);
                articleHits.add(articleHit);
            }
        }

        return articleHits;
    }

    private ArticleHit mapToArticleHit(String key, Map<String, String> dataMap) {

        String articleId = key.replace(KEY_PREFIX + ":", "");
        int hitCount = Integer.parseInt(dataMap.getOrDefault("hitCount", "0"));
        int mobileHitCount = Integer.parseInt(dataMap.getOrDefault("mobileHitCount", "0"));

        return new ArticleHit(articleId, hitCount, mobileHitCount);
    }

    /**
     * 특정 articleId에 저장된 모든 hitCount를 가져오고 삭제한다.
     *
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
