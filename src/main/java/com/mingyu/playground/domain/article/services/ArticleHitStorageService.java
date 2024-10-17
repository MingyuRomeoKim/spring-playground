package com.mingyu.playground.domain.article.services;

import com.mingyu.playground.domain.article.entities.ArticleHitStorage;
import com.mingyu.playground.domain.article.repositories.ArticleHitStorageRepository;
import com.mingyu.playground.domain.redis.entities.ArticleHit;
import com.mingyu.playground.domain.redis.repository.ArticleHitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleHitStorageService {

    private final ArticleHitStorageRepository articleHitStorageRepository;
    private final ArticleHitRepository articleHitRepository;

    @Transactional
    public void batchUpdateHitCounts(Map<String, Map<String, String>> hitCountsFromRedis) {
        List<ArticleHitStorage> articleHitStorages = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : hitCountsFromRedis.entrySet()) {
            String articleId = entry.getKey();
            Map<String, String> counts = entry.getValue();
            int hitCount = Integer.parseInt(counts.get("hitCount"));
            int mobileHitCount = Integer.parseInt(counts.get("mobileHitCount"));

            ArticleHitStorage articleHitStorage = articleHitStorageRepository.findById(articleId)
                    .orElse(new ArticleHitStorage(articleId, 0, 0));

            articleHitStorage.setHitCount(articleHitStorage.getHitCount() + hitCount);
            articleHitStorage.setMobileHitCount(articleHitStorage.getMobileHitCount() + mobileHitCount);

            articleHitStorages.add(articleHitStorage);
        }
        articleHitStorageRepository.saveAll(articleHitStorages);
    }

    public void updateHitStorage() {
        List<String> articleIds = articleHitRepository.getMembers();
        Map<String, Map<String, String>> hitCountsFromRedis = new HashMap<>();


        if (articleIds == null) {
            return;
        }

        for (String articleId : articleIds) {
            Map<String, Object> allHitCounts = articleHitRepository.getAndDeleteAllHitCount(articleId);

            if (allHitCounts.isEmpty()) {
                continue;
            }

            Map<String, String> counts = new HashMap<>();
            counts.put("hitCount", allHitCounts.get("hitCount").toString());
            counts.put("mobileHitCount", allHitCounts.get("mobileHitCount").toString());

            hitCountsFromRedis.put(articleId, counts);
        }



         // [방법1] 커스텀 쿼리 사용 업데이트
         // 이 방법이 맞나?? 일괄로 increment 시킬 방법이 있을까??
         // 아냐. 이 방법은 articleId 별로 hitCount를 증가시키는 방법이 맞지만 반복하여 수행해서 일괄처리 방식이 아니야.
        /**
         for (String articleId : hitCountsFromRedis.keySet()) {
         articleHitStorageRepository.incrementHitCount(articleId, Integer.parseInt(hitCountsFromRedis.get(articleId).get("hitCount")));
         articleHitStorageRepository.incrementMobileHitCount(articleId, Integer.parseInt(hitCountsFromRedis.get(articleId).get("mobileHitCount")));
         }
         */

        // [방법2] JDBC를 이용한 배치 업데이트
        if (!hitCountsFromRedis.isEmpty()) {
            batchUpdateHitCounts(hitCountsFromRedis);
        }
    }
}
