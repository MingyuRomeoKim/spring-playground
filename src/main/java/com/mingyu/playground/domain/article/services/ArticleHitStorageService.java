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
    public void batchUpdateHitCounts(List<ArticleHit> hitCountsFromRedis) {
        List<ArticleHitStorage> articleHitStorages = new ArrayList<>();

        for (ArticleHit articleHit: hitCountsFromRedis) {
            String articleId = articleHit.getArticleId();
            int hitCount = articleHit.getHitCount();
            int mobileHitCount = articleHit.getMobileHitCount();

            ArticleHitStorage articleHitStorage = articleHitStorageRepository.findById(articleId)
                    .orElse(new ArticleHitStorage(articleId, 0, 0));

            articleHitStorage.setHitCount(articleHitStorage.getHitCount() + hitCount);
            articleHitStorage.setMobileHitCount(articleHitStorage.getMobileHitCount() + mobileHitCount);

            articleHitStorages.add(articleHitStorage);
        }

        articleHitStorageRepository.saveAll(articleHitStorages);
    }

    public void updateHitStorage() {
        // Redis에서 모든 히트 정보 조회
        Iterable<ArticleHit> articleHits = articleHitRepository.findAll();

        List<ArticleHit> articleHitList = new ArrayList<>();
        articleHits.forEach(articleHit -> {
            if (articleHit != null) {
                articleHitList.add(articleHit);
            }
        });

        if (articleHitList.isEmpty()) {
            return ;
        }

        // 배치 업데이트
        batchUpdateHitCounts(articleHitList);

        // 동시성을 고려한 조회된 정보에 한한 Redis 초기화
        articleHitRepository.deleteAll(articleHitList);
    }
}
