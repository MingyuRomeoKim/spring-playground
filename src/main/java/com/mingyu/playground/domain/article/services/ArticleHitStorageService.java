package com.mingyu.playground.domain.article.services;

import com.mingyu.playground.domain.article.entities.ArticleHitStorage;
import com.mingyu.playground.domain.article.repositories.ArticleHitStorageRepository;
import com.mingyu.playground.domain.redis.entities.ArticleHit;
import com.mingyu.playground.domain.redis.repository.ArticleHitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleHitStorageService {

    private final ArticleHitStorageRepository articleHitStorageRepository;
    private final ArticleHitRepository articleHitRepository;

    @Transactional
    public void batchUpdateHitCounts(List<ArticleHit> hitCountsFromRedis) {
        List<ArticleHitStorage> articleHitStorages = new ArrayList<>();

        for (ArticleHit articleHit : hitCountsFromRedis) {

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

        //Redis에서 모든 히트 정보 조회
        List<ArticleHit> articleHits = articleHitRepository.findAllBy();
        //동시성을 고려한 조회된 정보에 한한 Redis 초기화
        articleHitRepository.deleteAll(articleHits);

        /**
         * Lua script를 이용한 Redis 초기화.. TODO:: 오류 해결 및 재구현
         */
        //List<ArticleHit> articleHits = articleHitRepository.getAndDeleteAllArticleHits();

        if (articleHits.isEmpty()) {
            return;
        }

        // 배치 업데이트
        batchUpdateHitCounts(articleHits);
    }
}
