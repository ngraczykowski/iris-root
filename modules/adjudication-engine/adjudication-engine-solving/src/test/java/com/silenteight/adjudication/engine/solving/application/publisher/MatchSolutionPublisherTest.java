/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.engine.solving.data.MatchSolutionEntityGenerator;
import com.silenteight.adjudication.engine.solving.data.MatchSolutionStore;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class MatchSolutionPublisherTest {

  private ThreadPoolTaskScheduler taskExecutor;

  @BeforeEach
  void setUp() {
    taskExecutor = new ThreadPoolTaskScheduler();
    taskExecutor.setPoolSize(4);
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    taskExecutor.afterPropertiesSet();
  }

  @RepeatedTest(10)
  void testPublisherWithSingeExecutorAndMultiplePublishing() {
    var matchSolutionStore = Mockito.spy(new MockMatchSolutionStore());
    var matchSolutionPublisherPort = new MatchSolutionPublisher(taskExecutor, matchSolutionStore);

    int numbers = 10000;
    for (int i = 0; i < numbers; i++) {
      matchSolutionPublisherPort.resolve(MatchSolutionEntityGenerator.createMatchSolution());
    }

    awaitForExecutor();

    assertThat(matchSolutionStore.storedObjects()).isEqualTo(numbers);
  }

  @Test
  void publishSolutionAndThrowExceptionQueueShouldBeEmpty() {
    var matchSolutionStore = Mockito.spy(new MockMatchSolutionStoreThrowsEx());
    var matchSolutionPublisherPort = new MatchSolutionPublisher(taskExecutor, matchSolutionStore);

    int numbers = 2;
    for (int i = 0; i < numbers; i++) {
      matchSolutionPublisherPort.resolve(MatchSolutionEntityGenerator.createMatchSolution());
    }

    awaitForExecutor();

    assertThat(matchSolutionStore.storedObjects()).isEqualTo(numbers);
  }

  @Disabled // -> https://github.com/mockito/mockito/issues/2540
  @RepeatedTest(10)
  void testForMultipleExecutor() {
    Queue<MatchSolution> matchSolutionQueue = Mockito.spy(new ArrayBlockingQueue<>(100));

    var matchSolutionStore = Mockito.spy(new MockMatchSolutionStore());
    var matchSolutionPublisherPort =
        new MatchSolutionPublisher(new SimpleAsyncTaskExecutor(), matchSolutionStore);

    int numbers = 100_000;
    for (int i = 0; i < numbers; i++) {
      matchSolutionPublisherPort.resolve(MatchSolutionEntityGenerator.createMatchSolution());
    }

    verify(matchSolutionQueue, times(numbers)).add(any());
    verify(matchSolutionQueue, atLeastOnce()).poll();
    await().until(() -> matchSolutionStore.storedObjects() == numbers);
    assertThat(matchSolutionStore.storedObjects()).isEqualTo(numbers);
    assertThat(matchSolutionQueue).isEmpty();
  }

  private void awaitForExecutor() {
    await().atMost(Duration.ofSeconds(5)).until(() -> taskExecutor.getActiveCount() == 0);
  }

  private static class MockMatchSolutionStore implements MatchSolutionStore {

    final AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void store(MatchSolution matchSolution) {
      atomicInteger.incrementAndGet();
    }

    int storedObjects() {
      return atomicInteger.get();
    }
  }

  private static class MockMatchSolutionStoreThrowsEx extends MockMatchSolutionStore {

    @Override
    public void store(MatchSolution matchSolution) {
      super.store(matchSolution);
      throw new RuntimeException("Store exception");
    }
  }
}
