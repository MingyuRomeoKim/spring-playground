package com.mingyu.playground.domain.redis.repository;

import com.mingyu.playground.domain.redis.entities.ArticleHit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@ActiveProfiles("local")
@SpringBootTest
class ArticleHitRepositoryTest {

    private static final String ARTICLE_ID = "20240102";
    private static final Logger log = LoggerFactory.getLogger(ArticleHitRepositoryTest.class);

    @Autowired
    private ArticleHitRepository articleHitRepository;

    @BeforeEach
    void cleanUp() {
        articleHitRepository.deleteAll();
    }

    @Test
    @DisplayName("히트 카운트 증가 - 동시성 이슈 미고려")
    void incrementHitCount() {
        // Given
        String articleId = ARTICLE_ID;
        Integer hits = 5;

        // When
        Optional<ArticleHit> articleHit = articleHitRepository.findById(articleId);
        if (articleHit.isEmpty()) {
            ArticleHit newArticleHit = ArticleHit.builder()
                    .articleId(articleId)
                    .build();
            articleHitRepository.save(newArticleHit);
        }

        int updateHitCount = articleHitRepository.incrementPcHitCount(articleId, hits);

        // Then
        ArticleHit updateArticleHit = articleHitRepository.findById(articleId).orElseThrow();
        Assertions.assertThat(hits).isEqualTo(updateArticleHit.getHitCount());

    }

    @Test
    @DisplayName("모바일 히트 카운트 증가 - 동시성 이슈 미고려")
    void incrementMobileHitCount() {
        // Given
        String articleId = ARTICLE_ID;
        Integer hits = 5;

        // When
        Optional<ArticleHit> articleHit = articleHitRepository.findById(articleId);
        if (articleHit.isEmpty()) {
            ArticleHit newArticleHit = ArticleHit.builder()
                    .articleId(articleId)
                    .build();
            articleHitRepository.save(newArticleHit);
        }

        // Then
        int updateHitCount = articleHitRepository.incrementMobileHitCount(articleId, hits);
        Assertions.assertThat(hits).isEqualTo(updateHitCount);
    }

    @Test
    @DisplayName("동시성 테스트 - 히트 카운트 증가")
    void incrementHitCountConcurrently() throws InterruptedException {
        // Given
        String articleId = ARTICLE_ID;
        int numberOfThreads = 10000;
        int incrementPerThread = 1;

        // 초기값 저장
        Optional<ArticleHit> articleHit = articleHitRepository.findById(articleId);
        if (articleHit.isEmpty()) {
            ArticleHit newArticleHit = ArticleHit.builder()
                    .articleId(articleId)
                    .build();
            articleHitRepository.save(newArticleHit);
        }

        // 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads / 10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // When
        IntStream.range(0, numberOfThreads).forEach(i -> {
            executorService.execute(() -> {
                try {
                    articleHitRepository.incrementPcHitCount(articleId, incrementPerThread);
                } finally {
                    latch.countDown();
                }
            });
        });

        // 모든 스레드 작업 완료 대기
        latch.await();
        executorService.shutdown();

        // Then
        ArticleHit updateArticleHit = articleHitRepository.findById(articleId).orElseThrow();
        Assertions.assertThat(updateArticleHit.getHitCount()).isEqualTo(numberOfThreads * incrementPerThread);
    }

    @Test
    @DisplayName("동시성 테스트 - 모바일 히트 카운트 증가")
    void incrementMobileHitCountConcurrently() throws InterruptedException {
        // Given
        String articleId = ARTICLE_ID;
        int numberOfThreads = 10000;
        int incrementPerThread = 1;

        // 초기값 저장
        Optional<ArticleHit> articleHit = articleHitRepository.findById(articleId);
        if (articleHit.isEmpty()) {
            ArticleHit newArticleHit = ArticleHit.builder()
                    .articleId(articleId)
                    .build();
            articleHitRepository.save(newArticleHit);
        }

        // 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads / 10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // When
        IntStream.range(0, numberOfThreads).forEach(i -> {
            executorService.execute(() -> {
                try {
                    articleHitRepository.incrementMobileHitCount(articleId, incrementPerThread);
                } finally {
                    latch.countDown();
                }
            });
        });

        // 모든 스레드 작업 완료 대기
        latch.await();
        executorService.shutdown();

        // Then
        ArticleHit updateArticleHit = articleHitRepository.findById(articleId).orElseThrow();
        Assertions.assertThat(updateArticleHit.getMobileHitCount()).isEqualTo(numberOfThreads * incrementPerThread);
    }

    @Test
    @DisplayName("Redis Sets 조회")
    void getMembers() {
        // When
        List<String> members = articleHitRepository.getMembers();

        // Then
        log.info("members: {}", members);
        Assertions.assertThat(members).isNotNull();
    }

    @Test
    @DisplayName("pc & mobile view count 조회 및 삭제")
    void getAndDeleteAllHitCount() {
        // Given
        String articleId = ARTICLE_ID;

        // When
        Map<String,Object> hitCounts = articleHitRepository.getAndDeleteAllHitCount(articleId);

        // Then
        if (hitCounts.isEmpty()) {
            Assertions.assertThatComparable(hitCounts.size()).isEqualTo(0);
        } else {
            String hitCount = hitCounts.get("hitCount").toString();
            String mobileHitCount = hitCounts.get("mobileHitCount").toString();

            log.info("hitCount: {}", hitCount);
            log.info("mobileHitCount: {}", mobileHitCount);

            Assertions.assertThat(hitCount).isNotNull();
        }
    }
}