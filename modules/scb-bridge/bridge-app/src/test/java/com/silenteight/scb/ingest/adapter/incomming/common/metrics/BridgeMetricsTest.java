package com.silenteight.scb.ingest.adapter.incomming.common.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BridgeMetricsTest {

  private static BridgeMetrics bridgeMetrics;

  static {
    bridgeMetrics = new BridgeMetrics();
    bridgeMetrics.bindTo(new SimpleMeterRegistry());
  }

  @Test
  void meterFetchedAlerts() {
    // when
    bridgeMetrics.alertsFetched(250);

    // then
    assertThat(bridgeMetrics.countFetchedAlerts()).isEqualTo(250);
  }

  @Test
  void unboundMetricsDoNotFail() {
    assertThatNoException()
        .isThrownBy(() -> new BridgeMetrics().alertsFetched(123));
  }
}
