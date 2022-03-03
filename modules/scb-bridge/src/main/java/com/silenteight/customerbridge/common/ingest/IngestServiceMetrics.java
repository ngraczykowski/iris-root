package com.silenteight.customerbridge.common.ingest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@RequiredArgsConstructor
@Slf4j
class IngestServiceMetrics implements MeterBinder {

  private static final String INGESTED_LEARNING_ALERTS = "serp.scb.bridge.ingested.learning.alerts";

  private final IngestService ingestService;

  @Override
  public void bindTo(MeterRegistry registry) {
    log.info("Binding Ingest Service metrics");

    FunctionCounter
        .builder(
            INGESTED_LEARNING_ALERTS, ingestService,
            IngestService::getIngestedLearningAlertsCounter)
        .description("The number of ingested learning alerts")
        .register(registry);
  }
}
