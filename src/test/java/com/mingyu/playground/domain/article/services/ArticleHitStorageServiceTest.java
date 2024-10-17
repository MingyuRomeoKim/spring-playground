package com.mingyu.playground.domain.article.services;

import com.mingyu.playground.domain.redis.entities.ArticleHit;
import com.mingyu.playground.domain.redis.repository.ArticleHitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("local")
class ArticleHitStorageServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ArticleHitStorageServiceTest.class);
    @Autowired
    private ArticleHitStorageService articleHitStorageService;
    @Autowired
    private ArticleHitRepository articleHitRepository;

    @BeforeEach
    void cleanUp() {
//        articleHitRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 테스트 - PC, MObile 히트 카운트 증가")
    void incrementHitCountConcurrently() throws InterruptedException {

        Random random = new Random();

        for (int z = 0; z < 100; z++) {
            // Given
            int randomNum = 10000000 + random.nextInt(90000000);
            String articleId = String.valueOf(randomNum);

            int numberOfThreads = 100;
            int incrementPerThread = 1;

            // 스레드 풀 생성
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads / 10);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // When
            IntStream.range(0, numberOfThreads).forEach( i -> {
                executorService.execute(() -> {
                    try {
                        articleHitRepository.incrementPcHitCount(articleId, incrementPerThread);
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
            Assertions.assertThat(updateArticleHit.getHitCount()).isEqualTo(numberOfThreads * incrementPerThread);
        }

//        articleHitStorageService.updateHitStorage();
    }


    @Test
    @DisplayName("Spring Data Redis FindAll")
    public void test() {
        List<ArticleHit> articleHitList =  articleHitRepository.findAllBy();
        log.info("articleHitList: {}", articleHitList);
        Assertions.assertThat(articleHitList).size().isNotEqualTo(0);
    }

}