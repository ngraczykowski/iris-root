/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.aspectj.AnnotationAsyncExecutionAspect;

import static org.assertj.core.api.Assertions.*;

class MeterFetchedAlertsListenerTest {

  private BridgeMetrics bridgeMetrics = new BridgeMetrics();
  private MeterFetchedAlertsListener listenerUnderTests =
      new MeterFetchedAlertsListener(bridgeMetrics);

  @BeforeEach
  void setUp() {
    bridgeMetrics.bindTo(new SimpleMeterRegistry());

    // NOTE(ahaczewski): The listener uses @Async aspect, which needs an executor.
    AnnotationAsyncExecutionAspect.aspectOf().setExecutor(new SyncTaskExecutor());
  }

  @Test
  void handleEventAndMeterAlertsFetchedAction() {
    // given
    var alertsFetchedEvent = new AlertsFetchedEvent(250);

    // when
    listenerUnderTests.onEvent(alertsFetchedEvent);

    // then
    assertThat(bridgeMetrics.countFetchedAlerts()).isEqualTo(250);
  }
}
