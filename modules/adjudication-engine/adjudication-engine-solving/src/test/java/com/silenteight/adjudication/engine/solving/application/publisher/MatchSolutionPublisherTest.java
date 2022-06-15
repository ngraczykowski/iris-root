/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.engine.solving.data.MatchSolutionEntityGenerator;
import com.silenteight.adjudication.engine.solving.data.MatchSolutionStore;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class MatchSolutionPublisherTest {

  @Test
  void testPublisherWithSingeExecutorAndMultiplePublishing() {
    Queue<MatchSolution> matchSolutionQueue = Mockito.spy(new LinkedList<>());

    var matchSolutionStore = Mockito.spy(new MockMatchSolutionStore());
    var matchSolutionPublisherPort =
        new MatchSolutionPublisher(
            matchSolutionStore, Executors.newSingleThreadScheduledExecutor(), matchSolutionQueue);

    int numbers = 100;
    for (int i = 0; i < numbers; i++) {
      matchSolutionPublisherPort.resolve(MatchSolutionEntityGenerator.createMatchSolution());
    }

    verify(matchSolutionQueue, times(numbers)).add(any());
    await().until(() -> matchSolutionStore.storedObjects() == numbers);

    verify(matchSolutionQueue, atLeast(numbers)).poll();
    assertThat(matchSolutionQueue).isEmpty();
  }

  @Test
  void publishSolutionAndThrowExceptionQueueShouldBeEmpty() {
    Queue<MatchSolution> matchSolutionQueue = Mockito.spy(new LinkedList<>());

    var matchSolutionStore = Mockito.spy(new MockMatchSolutionStoreThrowsEx());
    var matchSolutionPublisherPort =
        new MatchSolutionPublisher(
            matchSolutionStore, Executors.newSingleThreadScheduledExecutor(), matchSolutionQueue);

    int numbers = 2;
    for (int i = 0; i < numbers; i++) {
      matchSolutionPublisherPort.resolve(MatchSolutionEntityGenerator.createMatchSolution());
    }

    verify(matchSolutionQueue, times(numbers)).add(any());

    await().until(() -> matchSolutionStore.storedObjects() == numbers);

    verify(matchSolutionQueue, atLeast(numbers)).poll();
    assertThat(matchSolutionQueue).isEmpty();
  }

  @Disabled // -> https://github.com/mockito/mockito/issues/2540
  @Test
  void testForMultipleExecutor() {
    Queue<MatchSolution> matchSolutionQueue = Mockito.spy(new ArrayBlockingQueue<>(100));

    var matchSolutionStore = Mockito.spy(new MockMatchSolutionStore());
    var matchSolutionPublisherPort =
        new MatchSolutionPublisher(
            matchSolutionStore, Executors.newScheduledThreadPool(10), matchSolutionQueue);

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
