/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class IngestServiceMetrics implements MeterBinder {

  private static final String INGESTED_LEARNING_ALERTS = "serp.scb.bridge.ingested.learning.alerts";
  private final IngestedLearningAlertsCounter learningAlertsCounter;

  @Override
  public void bindTo(MeterRegistry registry) {
    log.info("Binding Ingest Service metrics");

    FunctionCounter
        .builder(
            INGESTED_LEARNING_ALERTS, learningAlertsCounter,
            IngestedLearningAlertsCounter::getCount)
        .description("The number of ingested learning alerts")
        .register(registry);
  }
}
