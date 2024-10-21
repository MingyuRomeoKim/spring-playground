package com.mingyu.playground.domain.article.services;

import com.mingyu.playground.domain.article.entities.ArticleHitStorage;
import com.mingyu.playground.domain.article.repositories.ArticleHitStorageRepository;
import com.mingyu.playground.domain.redis.entities.ArticleHit;
import com.mingyu.playground.domain.redis.repository.ArticleHitRepository;
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
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("local")
class ArticleHitStorageServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ArticleHitStorageServiceTest.class);
    @Autowired
    private ArticleHitStorageService articleHitStorageService;
    @Autowired
    private ArticleHitRepository articleHitRepository;
    @Autowired
    private ArticleHitStorageRepository articleHitStorageRepository;

    @BeforeEach
    void cleanUp() {
//        articleHitRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 테스트 - PC, MObile 히트 카운트 증가")
    void incrementHitCountConcurrently() throws InterruptedException {

        Random random = new Random();
        int loopCount = 100;

        for (int z = 0; z < loopCount; z++) {
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
        }
        // Then
        List<ArticleHit> articleHitList =  articleHitRepository.findAllBy();
        log.info("articleHitList: {}", articleHitList);
        Assertions.assertThat(articleHitList).size().isNotEqualTo(0);
        Assertions.assertThat(articleHitList).size().isGreaterThanOrEqualTo(loopCount);
    }


    @Test
    @DisplayName("동시성 테스트 - PC, Mobile 히트 카운트 증가 및 저장")
    void incrementHitCountConcurrentlyWithUpdateHitStorage() throws InterruptedException, ExecutionException {

        Random random = new Random();
        int loopCount = 100;

        // 스레드 풀 생성
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads / 10);

        // 전체 작업 수에 대한 CountDownLatch 설정
        CountDownLatch latch = new CountDownLatch(loopCount * numberOfThreads);

        // updateHitStorage 스레드 종료를 위한 플래그
        AtomicBoolean isRunning = new AtomicBoolean(true);

        // updateHitStorage를 실행하는 스레드 추가
        ExecutorService updateExecutorService = Executors.newSingleThreadExecutor();
        Future<?> updateFuture = updateExecutorService.submit(() -> {
            try {
                // 조회수가 몰리는 동안 주기적으로 updateHitStorage 실행
                while (isRunning.get()) {
                    articleHitStorageService.updateHitStorage();
                    Thread.sleep(100); // 100ms마다 실행
                }
                // 마지막으로 남은 데이터 처리
                articleHitStorageService.updateHitStorage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        for (int z = 0; z < loopCount; z++) {
            // Given
            int randomNum = 10000000 + random.nextInt(90000000);
            String articleId = String.valueOf(randomNum);

            int incrementPerThread = 1;

            // 조회수 증가 작업
            for (int i = 0; i < numberOfThreads; i++) {
                executorService.execute(() -> {
                    try {
                        articleHitRepository.incrementPcHitCount(articleId, incrementPerThread);
                        articleHitRepository.incrementMobileHitCount(articleId, incrementPerThread);
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        // 모든 조회수 증가 작업 완료 대기
        latch.await();
        executorService.shutdown();

        // 조회수 증가 작업이 모두 완료되었으므로 updateHitStorage 스레드 종료 플래그 설정
        isRunning.set(false);

        // updateHitStorage 스레드 종료 대기
        updateExecutorService.shutdown();
        updateFuture.get(); // 혹은 updateExecutorService.awaitTermination() 사용

        // Then
        // 최종적으로 Redis와 RDB에 데이터가 일관되게 저장되었는지 확인
        List<ArticleHit> articleHitList = articleHitRepository.findAllBy();
        log.info("articleHitList (Redis): {}", articleHitList);

        List<ArticleHitStorage> articleHitStorageList = articleHitStorageRepository.findAll();
        log.info("articleHitStorageList (RDB): {}", articleHitStorageList);

        // Assertions를 통해 데이터 검증
        // Redis에 남아 있는 데이터와 RDB에 저장된 데이터의 합이 기대한 값과 일치하는지 확인
        int totalHitCount = articleHitStorageList.stream()
                .mapToInt(ArticleHitStorage::getHitCount)
                .sum();
        int totalMobileHitCount = articleHitStorageList.stream()
                .mapToInt(ArticleHitStorage::getMobileHitCount)
                .sum();

        int expectedTotalCount = loopCount * numberOfThreads;

        Assertions.assertThat(totalHitCount).isEqualTo(expectedTotalCount);
        Assertions.assertThat(totalMobileHitCount).isEqualTo(expectedTotalCount);

        // Redis에 남아 있는 데이터가 없는지 확인 (모두 RDB로 이동되었으므로)
        Assertions.assertThat(articleHitList).isEmpty();
    }


    @Test
    @DisplayName("Spring Data Redis FindAll")
    public void test() {
        List<ArticleHit> articleHitList =  articleHitRepository.findAllBy();
        log.info("articleHitList: {}", articleHitList);
        Assertions.assertThat(articleHitList).size().isNotEqualTo(0);
    }

}