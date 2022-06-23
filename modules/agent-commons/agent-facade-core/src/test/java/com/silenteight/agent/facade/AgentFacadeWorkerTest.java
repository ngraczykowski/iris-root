package com.silenteight.agent.facade;

import com.silenteight.agents.v1.api.exchange.AgentOutput;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class AgentFacadeWorkerTest {

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 })
  void shouldUseExactNumberOfThreads(int parallelism) {

    var testStartTime = Instant.now();
    Set<String> observedThreads = Collections.synchronizedSet(new HashSet<>());

    BiFunction<Object, Set<String>, AgentOutput> mapper = (o, s) -> {
      observedThreads.add(Thread.currentThread().getName());
      await().until(() -> Duration.between(testStartTime, Instant.now()).toMillis() > 100);
      return AgentOutput.getDefaultInstance();
    };
    var underTest = new AgentFacadeWorker<>(mapper);
    underTest.setParallelism(parallelism);

    var data = IntStream
        .range(0, parallelism * 2)
        .mapToObj(i -> new Object())
        .collect(Collectors.toList());

    underTest.process(data, Set.of("configName"));

    assertEquals(parallelism, observedThreads.size(),
        () -> "Registered thread names: " + observedThreads);
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 })
  void shouldPropagateException(int parallelism) {

    RuntimeException exception = new RuntimeException();
    var testStartTime = Instant.now();

    BiFunction<Object, Set<String>, AgentOutput> mapper = (o, s) -> {
      await().until(() -> Duration.between(testStartTime, Instant.now()).toMillis() > 100);
      throw exception;
    };
    var underTest = new AgentFacadeWorker<>(mapper);
    underTest.setParallelism(parallelism);

    var data = IntStream
        .range(0, parallelism * 2)
        .mapToObj(i -> new Object())
        .collect(Collectors.toList());

    var res =
        assertThrows(RuntimeException.class, () -> underTest.process(data, Set.of("configName")));

    assertEquals(exception, res);
  }
}