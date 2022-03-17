package com.silenteight.scb.ingest.adapter.incomming.common.metrics;

import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

@Slf4j
class BridgeMetrics implements MeterBinder {

  private static final String BASE_UNIT = "alerts";

  private Counter fetchedAlerts;

  void alertsFetched(int chunkSize) {
    if (fetchedAlerts == null)
      return;

    if (log.isDebugEnabled())
      log.debug("Meter fetched alerts, chunkSize={}", chunkSize);

    fetchedAlerts.increment(chunkSize);
  }

  double countFetchedAlerts() {
    return fetchedAlerts.count();
  }

  @Override
  public void bindTo(MeterRegistry registry) {
    fetchedAlerts = Counter
        .builder(MetricNames.ALERTS_FETCHED)
        .description("Count of alerts fetched by bridge")
        .baseUnit(BASE_UNIT)
        .register(registry);
  }

  private static class MetricNames {

    private static final String ALERTS_FETCHED = "serp.scb.bridge.alerts.fetched";
  }
}
